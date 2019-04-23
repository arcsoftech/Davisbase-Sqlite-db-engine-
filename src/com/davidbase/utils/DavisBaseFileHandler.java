package com.davidbase.utils;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.Condition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.*;

/**
 * File utility to read/write data from .tbl files
 */
public class DavisBaseFileHandler {

    public static boolean databaseExists(String databaseName) {
        File databaseDir = new File(getDatabasePath(databaseName));
        return databaseDir.exists();
    }

    public static String getDatabasePath(String databaseName) {
        return DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + databaseName;
    }

    public static boolean createFile(String tableFileName) {
        /* Code to create a .tbl file to contain table data */
        try {
            /*
             * Create RandomAccessFile tableFile in read-write mode. Note that this doesn't
             * create the table file in the correct directory structure
             */
            RandomAccessFile tableFile = new RandomAccessFile(FILE_DIR + tableFileName + FILE_EXT, "rw");
            tableFile.setLength(PAGE_SIZE);
            tableFile.seek(0);
            tableFile.writeInt(63);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean deleteFile(String tableFileName) {
        return true;
    }

    public boolean readFromFile(String tableFileName) {
        return true;
    }

    public boolean writeLeafCell(String databaseName, String tableName, LeafCell leafCell) {
        try {
            RandomAccessFile tablefile = new RandomAccessFile(
                    new File(getDatabasePath(databaseName) + "/" + tableName + FILE_EXT), "rw");
            int pageNumber = 0;
            // iterate over each record to be inserted
            int pageCount = (int) (tablefile.length() / PAGE_SIZE);
            Page page = findPage(tablefile, leafCell.getHeader().getRow_id(), pageNumber);

            switch (pageCount) {
            case 1: // this is the first page to be inserted
                // insert leaf node with data

                // Prepare the leaf node
                Page<LeafCell> dataNode = new Page<LeafCell>();
                PageHeader header = new PageHeader(0);
                List<LeafCell> dataCells = new ArrayList<>();

                if (page.getPageheader().getNum_cells() <= 0) {
                    header.setPage_number(0);

                    header.setNum_cells((byte) 1);
                    System.out.println("Test data"+CellHeader.getSize());
                    System.out.println(leafCell.getPayload().getData().length);
                    int offset = ((short) PAGE_SIZE) - (leafCell.getPayload().getData().length + CellHeader.getSize());
                    header.setData_offset((short) offset);
                    header.setData_cell_offset((new short[] { (short) offset }));

                } else {
                    header.setNum_cells((byte) (page.getPageheader().getNum_cells() + 1));
                    int offset = page.getPageheader().getData_offset() - leafCell.getPayload().getData().length
                            + CellHeader.getSize();
                    header.setData_offset((short) offset);
                    int length = page.getPageheader().getData_cell_offset().length + 1;
                    short[] newOffsets = Arrays.copyOf(page.getPageheader().getData_cell_offset(), length);
                    newOffsets[length - 1] = (short) offset;
                    header.setData_cell_offset(newOffsets);
                }

                header.setPage_type(PageType.table_leaf);
                header.setNext_page_pointer(RIGHT_MOST_LEAF);

                dataNode.setPageheader(header);
                dataCells.add(leafCell);
                dataNode.setCells(dataCells);

                writeLeafCell(tablefile, dataCells, header.getData_offset());
                writePageHeader(tablefile, dataNode, pageNumber);
                break;
            default: // for all other cases.
                // cases:
                // 1. insert to existing leaf
                // 2. if leaf is full, split the node and add one internal plus 2 leaf nodes.
                break;
            }

            // check for page overflow

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static Page findPage(RandomAccessFile tableFile, int rowID, int pageNumber) {
        Page nextPage = readSinglePage(tableFile, pageNumber);
        if (nextPage.getPageheader().getPage_type() == PageType.table_leaf)
            return nextPage;
        pageNumber = pageNumber + 1; // FIXME
        findPage(tableFile, rowID, pageNumber);
        return null;
    }

    public static boolean deleteFromFile(String tableFileName) {
        return true;
    }

    private static Page readSinglePage(RandomAccessFile randomAccessFile, int pageNum) {
        try {
            Page page = new Page();
            PageHeader header = new PageHeader(0);
            List cells;
            randomAccessFile.seek(PAGE_SIZE * pageNum);
            byte pageType = randomAccessFile.readByte();
            if (pageType == PageType.table_node.getVal()) {
                cells = new ArrayList<NonLeafCell>();
            } else {
                cells = new ArrayList<LeafCell>();
            }
            header.setPage_number(pageNum);
            header.setPage_type(PageType.getType(pageType));
            header.setNum_cells(randomAccessFile.readByte());
            header.setData_offset(randomAccessFile.readShort());
            header.setNext_page_pointer(randomAccessFile.readInt());
            short[] recordsOffset = new short[header.getNum_cells()];
            for (int i = 0; i < recordsOffset.length; i++) {
                recordsOffset[i] = randomAccessFile.readShort();
            }
            header.setData_cell_offset(recordsOffset);
            page.setPageheader(header);
            page.setCells(cells);
            return page;
        } catch (Exception e) {
            throw new DavidBaseError("Error while reading page from file.");
        }
    }

    private static boolean writePageHeader(RandomAccessFile tableFile, Page page, int pageNumber) {
        try {

            // writing the page header first
            PageHeader header = page.getPageheader();
            long pageLocation = pageNumber * PAGE_SIZE;
            tableFile.seek(pageLocation);
            tableFile.writeByte(header.getPage_type().getVal());
            tableFile.writeByte(header.getNum_cells());
            tableFile.writeShort(header.getData_offset());
            tableFile.writeInt(header.getNext_page_pointer());
            for (Object offset : header.getData_cell_offset()) {
                tableFile.writeShort((short) offset);
            }
            return true;
        } catch (Exception e) {
            throw new DavidBaseError("Error while writing page header to file.");
        }
    }

    private static void writeLeafCell(RandomAccessFile tableFile, List<LeafCell> cells, short offset)
            throws IOException {
        tableFile.seek(offset);
        for (LeafCell cell : cells) {
            // write header
            tableFile.writeShort(cell.getHeader().getPayload_size());
            tableFile.writeInt(cell.getHeader().getRow_id());

            // write data @Deprecated
            // tableFile.writeByte(cell.getPayload().getNum_columns());

            // for (byte colType : cell.getPayload().getData_type())
            //     tableFile.writeByte(colType);

            for (byte colData : cell.getPayload().getData())
                tableFile.writeByte(colData);
        }
    }

    private static void writeNonLeafCell(RandomAccessFile tableFile, List<NonLeafCell> cells, short offset)
            throws IOException {
        tableFile.seek(offset);
        for (NonLeafCell cell : cells) {
            tableFile.writeInt(cell.getPage_number());
            tableFile.writeInt(cell.getKey_delim());
        }
    }

    public List<LeafCell> findRecord(String databaseName, String tableName, Condition condition, boolean getOne) {
        return findRecord(databaseName, tableName, condition, null, getOne);
    }

    public List<LeafCell> findRecord(String databaseName, String tableName, Condition condition,
            List<Byte> selectionColumnIndexList, boolean getOne) {
        List<Condition> conditionList = new ArrayList<>();
        if (condition != null)
            conditionList.add(condition);
        return findRecord(databaseName, tableName, conditionList, selectionColumnIndexList, getOne);
    }

    public List<LeafCell> findRecord(String databaseName, String tableName, List<Condition> conditionList,
            boolean getOne) {
        return findRecord(databaseName, tableName, conditionList, null, getOne);
    }

    public List<LeafCell> findRecord(String databaseName, String tableName, List<Condition> conditionList,
            List<Byte> selectionColumnIndexList, boolean getOne) {
        try {
            File file = new File(DEFAULT_DATA_DIRNAME + "/" + databaseName + "/" + tableName + FILE_EXT);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                if (conditionList != null) {
                    Page page = getPage(file);
                    LeafCell leafCell;
                    List<LeafCell> matchingLeafCells = new ArrayList<>();
                    boolean isMatch = false;
                    byte columnIndex;
                    short condition;
                    Object conditionValue;
                    DataType conditionValueType;
                    while (page != null) {
                        for (Object offset : page.getPageheader().getData_cell_offset()) {
                            isMatch = true;
                            leafCell = readLeaf(randomAccessFile, page.getPageheader().getPage_number(),
                                    (short) offset);
                            for (int i = 0; i < conditionList.size(); i++) {
                                isMatch = false;
                                columnIndex = conditionList.get(i).getIndex();
                                conditionValue = conditionList.get(i).getValue();
                                condition = conditionList.get(i).getConditionType();
                                conditionValueType = conditionList.get(i).getValType();
                                if (leafCell != null && leafCell.getPayload().getColValues().size() > columnIndex) {
                                    Object colValue = leafCell.getPayload().getColValues().get(columnIndex);
                                    DataType colType = DataType
                                            .getTypeFromSerialCode(leafCell.getPayload().getData_type()[columnIndex]);
                                    try {
                                        isMatch = compare(colValue, conditionValue, condition, colType,
                                                conditionValueType);
                                    } catch (Exception e) {
                                        randomAccessFile.close();
                                        throw new DavidBaseError("Error while executing query.");
                                    }
                                    if (!isMatch)
                                        break;
                                }
                            }

                            if (isMatch) {
                                LeafCell matchedLeaf = leafCell;
                                if (selectionColumnIndexList != null) {
                                    matchedLeaf = new LeafCell();
                                    matchedLeaf.getHeader().setRow_id(leafCell.getHeader().getRow_id());
                                    matchedLeaf.setPageNumber(leafCell.getPageNumber());
                                    matchedLeaf.setOffset(leafCell.getOffset());
                                    for (Byte index : selectionColumnIndexList) {
                                        matchedLeaf.getPayload().getColValues()
                                                .add(leafCell.getPayload().getColValues().get(index));
                                    }
                                }
                                matchingLeafCells.add(matchedLeaf);
                                if (getOne) {
                                    randomAccessFile.close();
                                    return matchingLeafCells;
                                }
                            }
                        }
                        if (page.getPageheader().getNext_page_pointer() == RIGHT_MOST_LEAF)
                            break;
                        page = readSinglePage(randomAccessFile, page.getPageheader().getNext_page_pointer());
                    }
                    randomAccessFile.close();
                    return matchingLeafCells;
                }
            } else {
                throw new DavidBaseError("Table does not exist, " + databaseName + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError("Error while executing query.");
        }
        return null;
    }

    public LeafCell readLeaf(RandomAccessFile randomAccessFile, int pageNumber, short offset) {
        {
            try {
                if (pageNumber >= 0 && offset >= 0) {
                    randomAccessFile.seek((PAGE_SIZE * pageNumber) + offset);
                    short payloadSize = randomAccessFile.readShort();
                    int rowId = randomAccessFile.readInt();
                    CellHeader cellheader = new CellHeader(payloadSize, rowId);
                    byte numberOfColumns = randomAccessFile.readByte();
                    byte[] serialTypeCodes = new byte[numberOfColumns];
                    for (byte i = 0; i < numberOfColumns; i++) {
                        serialTypeCodes[i] = randomAccessFile.readByte();
                    }
                    List<Object> values = new ArrayList<>();
                    Object object;
                    for (byte i = 0; i < numberOfColumns; i++) {
                        switch (DataType.getTypeFromSerialCode(serialTypeCodes[i])) {
                        // case DataType_TinyInt.nullSerialCode is overridden with DataType_Text

                        case NULL_TINYINT:
                            object = null;
                            break;

                        case NULL_SMALLINT:
                            randomAccessFile.readShort();
                            object = null;
                            break;

                        case NULL_INT:
                            randomAccessFile.readFloat();
                            object = null;
                            break;

                        case NULL_DOUBLE_DATE:
                            randomAccessFile.readDouble();
                            object = null;
                            break;

                        case TINYINT:
                            object = (Byte) randomAccessFile.readByte();
                            break;

                        case SMALLINT:
                            object = randomAccessFile.readShort();
                            break;

                        case INT:
                            object = randomAccessFile.readInt();
                            break;

                        case BIGINT:
                            object = randomAccessFile.readLong();
                            break;

                        case REAL:
                            object = randomAccessFile.readFloat();
                            break;

                        case DOUBLE:
                            object = randomAccessFile.readDouble();
                            break;

                        case DATETIME:
                            object = randomAccessFile.readLong();
                            break;

                        case DATE:
                            object = randomAccessFile.readLong();
                            break;

                        case TEXT:
                            object = "";
                            break;

                        default:
                            if (serialTypeCodes[i] > DataType.TEXT.getSerialCode()) {
                                byte length = (byte) (serialTypeCodes[i] - DataType.TEXT.getSerialCode());
                                char[] text = new char[length];
                                for (byte k = 0; k < length; k++) {
                                    text[k] = (char) randomAccessFile.readByte();
                                }
                                object = new String(text);
                            } else
                                object = null;
                            break;
                        }
                        values.add(object);
                    }
                    CellPayload payload = new CellPayload(numberOfColumns, serialTypeCodes);
                    payload.setColValues(values);
                    LeafCell leafCell = new LeafCell(cellheader, payload);
                    leafCell.setPageNumber(pageNumber);
                    leafCell.setOffset(offset);
                    return leafCell;
                }
            } catch (Exception e) {
                throw new DavidBaseError("Error while executing query.");
            }
            return null;
        }
    }

    private Page getPage(RandomAccessFile randomAccessFile, LeafCell dataCell, int pageNumber) {
        {
            try {
                Page page = readSinglePage(randomAccessFile, 0);
                while (page.getPageheader().getPage_type() == PageType.table_node) {
                    if (page.getPageheader().getNum_cells() == 0)
                        return null;
                    randomAccessFile.seek((PAGE_SIZE * page.getPageheader().getPage_number())
                            + ((short) page.getPageheader().getData_offset()));
                    page = readSinglePage(randomAccessFile, randomAccessFile.readInt());
                }
                randomAccessFile.close();
                return page;
            } catch (Exception e) {
                throw new DavidBaseError("Error while executing query.");
            }
        }
    }

    private Page getPage(File file) {
        {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                Page page = readSinglePage(randomAccessFile, 0);
                while (page.getPageheader().getPage_type() == PageType.table_node) {
                    if (page.getPageheader().getNum_cells() == 0)
                        return null;
                    randomAccessFile.seek((PAGE_SIZE * page.getPageheader().getPage_number())
                            + ((short) page.getPageheader().getData_offset()));
                    page = readSinglePage(randomAccessFile, randomAccessFile.readInt());
                }
                randomAccessFile.close();
                return page;
            } catch (Exception e) {
                throw new DavidBaseError("Error while executing query.");
            }
        }
    }

    private boolean compare(Object value1, Object value2, short condition, DataType colType, DataType conditionType) {
        if (value1 == null)
            return false;
        else
            switch (colType) {
            case TINYINT:
                return compare((byte) value1, value2, condition, conditionType);
            case SMALLINT:
                return compare((short) value1, value2, condition, conditionType);
            case INT:
                return compare((int) value1, value2, condition, conditionType);
            case BIGINT:
                return compare((long) value1, value2, condition, conditionType);
            case REAL:
                return compare((float) value1, value2, condition, conditionType);
            case DOUBLE:
                return compare((double) value1, value2, condition, conditionType);
            case DATE:
                break;
            case DATETIME:
                break;
            case TEXT:
                break;
            }
        return false;
    }

    private boolean compare(Byte value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case TINYINT:
            return DavisBaseUtil.conditionCompare(value1, (byte) value2, condition);
        case SMALLINT:
            return DavisBaseUtil.conditionCompare(value1, (short) value2, condition);
        case INT:
            return DavisBaseUtil.conditionCompare(value1, (int) value2, condition);
        case BIGINT:
            return DavisBaseUtil.conditionCompare(value1, (long) value2, condition);
        }
        return false;
    }

    private boolean compare(Short value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case TINYINT:
            return DavisBaseUtil.conditionCompare(value1, (byte) value2, condition);
        case SMALLINT:
            return DavisBaseUtil.conditionCompare(value1, (short) value2, condition);
        case INT:
            return DavisBaseUtil.conditionCompare(value1, (int) value2, condition);
        case BIGINT:
            return DavisBaseUtil.conditionCompare(value1, (long) value2, condition);
        }
        return false;
    }

    private boolean compare(Integer value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case TINYINT:
            return DavisBaseUtil.conditionCompare(value1, (byte) value2, condition);
        case SMALLINT:
            return DavisBaseUtil.conditionCompare(value1, (short) value2, condition);
        case INT:
            return DavisBaseUtil.conditionCompare(value1, (int) value2, condition);
        case BIGINT:
            return DavisBaseUtil.conditionCompare(value1, (long) value2, condition);
        }
        return false;
    }

    private boolean compare(Long value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case TINYINT:
            return DavisBaseUtil.conditionCompare(value1, (byte) value2, condition);
        case SMALLINT:
            return DavisBaseUtil.conditionCompare(value1, (short) value2, condition);
        case INT:
            return DavisBaseUtil.conditionCompare(value1, (int) value2, condition);
        case BIGINT:
            return DavisBaseUtil.conditionCompare(value1, (long) value2, condition);
        }
        return false;
    }

    private boolean compare(Float value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case REAL:
            return DavisBaseUtil.conditionCompare(value1, (float) value2, condition);
        case DOUBLE:
            return DavisBaseUtil.conditionCompare(value1, (double) value2, condition);
        }
        return false;
    }

    private boolean compare(Double value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case REAL:
            return DavisBaseUtil.conditionCompare(value1, (float) value2, condition);
        case DOUBLE:
            return DavisBaseUtil.conditionCompare(value1, (double) value2, condition);
        }
        return false;
    }

    public boolean writeFirstPageHeader(RandomAccessFile randomAccessFile, Page page) {
        try {
            randomAccessFile.seek(page.getPageheader().getPage_number() * PAGE_SIZE);
            randomAccessFile.writeByte(page.getPageheader().getPage_type().getVal());
            randomAccessFile.writeByte(page.getPageheader().getNum_cells());
            randomAccessFile.writeShort(page.getPageheader().getData_offset());
            randomAccessFile.writeInt(page.getPageheader().getNext_page_pointer());
            for (Object offset : page.getPageheader().getData_cell_offset()) {
                randomAccessFile.writeShort((short) offset);
            }
            return true;
        } catch (Exception e) {
            throw new DavidBaseError(e);
        }
    }

    private boolean checkSpaceRequirements(Page page, LeafCell leafCell) {
        if (page != null && leafCell != null) {
            short endingAddress = page.getPageheader().getData_offset();
            short startingAddress = (short) (Page.getHeaderFixedLength()
                    + (page.getPageheader().getData_cell_offset().length * Short.BYTES));
            return (leafCell.getHeader().getPayload_size() + CellHeader.getSize() + Short.BYTES) <= (endingAddress
                    - startingAddress);
        }
        return false;
    }

    public Page getRightmostLeafPage(File file) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            Page page = readSinglePage(randomAccessFile, 0);
            while (page.getPageheader().getPage_type() == PageType.table_node
                    && page.getPageheader().getNext_page_pointer() != RIGHT_MOST_LEAF) {
                page = readSinglePage(randomAccessFile, page.getPageheader().getNext_page_pointer());
            }
            randomAccessFile.close();
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError(e.getMessage());
        }
    }
}
