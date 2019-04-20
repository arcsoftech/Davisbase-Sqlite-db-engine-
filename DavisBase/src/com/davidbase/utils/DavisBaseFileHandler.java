package com.davidbase.utils;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.*;

/**
 * File utility to read/write data from .tbl files
 */
public class DavisBaseFileHandler {

    public static boolean createFile(String tableFileName){
        /*  Code to create a .tbl file to contain table data */
        try {
            /*  Create RandomAccessFile tableFile in read-write mode.
             *  Note that this doesn't create the table file in the correct directory structure
             */
            RandomAccessFile tableFile = new RandomAccessFile(fileDir+tableFileName+fileExt, "rw");
            tableFile.setLength(pageSize);
            tableFile.seek(0);
            tableFile.writeInt(63);
        }
        catch(Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String tableFileName){
        return true;
    }

    public static boolean readFromFile(String tableFileName){
        return true;
    }

    public static boolean writeToFile(String tableFileName, List<RawRecord> records){
        try {
            RandomAccessFile tablefile = new RandomAccessFile(fileDir+tableFileName+fileExt, "rw");
            int pageNumber =0;
            //iterate over each record to be inserted
            for(RawRecord record: records){
                int pageCount = (int) (tablefile.length() / pageSize);
                Page page;
                if(pageCount>0)
                    page = findPage(tablefile,record.getRowID(), pageNumber);

                switch(pageCount){
                    case 0: // this is the first page to be inserted
                            // insert leaf node with data

                            // Prepare the leaf node
                            Page<LeafCell> dataNode = new Page();
                            PageHeader header = new PageHeader();
                            List<LeafCell> dataCells = new ArrayList<>();
                            header.setPage_number(0);
                            header.setPage_type(PageType.table_leaf);
                            header.setNum_cells((byte)1);
                            header.setData_cell_offset((new short[]{0}));
                            header.setData_offset((short)0);
                            header.setNext_page_pointer(rightMostLeaf);
                            dataNode.setPageheader(header);

                            //prepare the data cells
                            CellHeader cellHeader = new CellHeader(record.getTotSize(),record.getRowID());
                            CellPayload payload = new CellPayload((byte)record.getColumns().size(),record.getSizeOfCol(),record.getColeVal());
                            dataCells.add(new LeafCell(cellHeader, payload));
                            dataNode.setCells(dataCells);
                            write(tablefile,dataNode,pageNumber);

                            break;
                    default: // for all other cases.
                            //cases:
                            // 1. insert to existing leaf
                            // 2. if leaf is full, split the node and add one internal plus 2 leaf nodes.
                            break;
                }

                //check for page overflow
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static Page findPage(RandomAccessFile tableFile, int rowID, int pageNumber) {
        Page nextPage = readSinglePage(tableFile,pageNumber);
        if(nextPage.getPageheader().getPage_type()==PageType.table_leaf)
            return nextPage;
        pageNumber = pageNumber+1; //FIXME
        findPage(tableFile,rowID,pageNumber);
        return null;
    }

    public static boolean deleteFromFile(String tableFileName){
        return true;
    }

    private static Page readSinglePage(RandomAccessFile randomAccessFile, int pageNum) {
        try {
            Page page = new Page();
            PageHeader header = new PageHeader();
            List cells;
            randomAccessFile.seek(pageSize * pageNum);
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

    private static boolean write(RandomAccessFile tableFile, Page page, int pageNumber) {
        try {

            // writing the page header first
            PageHeader header = page.getPageheader();
            long pageLocation = pageNumber* pageSize;
            tableFile.seek(pageLocation);
            tableFile.writeByte(header.getPage_type().getVal());
            tableFile.writeByte(header.getNum_cells());
            tableFile.writeShort(header.getData_offset());
            tableFile.writeInt(header.getNext_page_pointer());
            for (Object offset : header.getData_cell_offset()) {
                tableFile.writeShort((short) offset);
            }

            //writing the page cells
            tableFile.seek(pageLocation);
            switch(header.getPage_type()){
                case table_leaf: writeLeafCell(tableFile,page.getCells());
                                  break;
                case table_node: writeNonLeafCell(tableFile,page.getCells());
                                  break;
            }
            return true;
        } catch (Exception e) {
            throw new DavidBaseError("Error while writing page header to file.");
        }
    }

    private static void writeLeafCell(RandomAccessFile tableFile, List<LeafCell> cells) throws IOException {
        for(LeafCell cell : cells){
            // write header
            tableFile.writeShort(cell.getHeader().getPayload_size());
            tableFile.writeInt(cell.getHeader().getRow_id());

            //write data
            tableFile.writeByte(cell.getPayload().getNum_columns());

            for(byte colType: cell.getPayload().getData_type())
                tableFile.writeByte(colType);

            for(byte colData : cell.getPayload().getData())
                tableFile.writeByte(colData);
        }
    }

    private static void writeNonLeafCell(RandomAccessFile tableFile, List<NonLeafCell> cells) throws IOException {
        for(NonLeafCell cell : cells){
            tableFile.writeInt(cell.getPage_number());
            tableFile.writeInt(cell.getKey_delim());
        }
    }

    public List<RawRecord> findRecord(String databaseName, String tableName, Condition condition, boolean getOne) {
        return findRecord(databaseName, tableName, condition,null, getOne);
    }

    public List<RawRecord> findRecord(String databaseName, String tableName, Condition condition, List<Byte> selectionColumnIndexList, boolean getOne) {
        List<Condition> conditionList = new ArrayList<>();
        if(condition != null)
            conditionList.add(condition);
        return findRecord(databaseName, tableName, conditionList, selectionColumnIndexList, getOne);
    }

    public List<RawRecord> findRecord(String databaseName, String tableName, List<Condition> conditionList, boolean getOne)  {
        return findRecord(databaseName, tableName, conditionList, null, getOne);
    }

    public List<RawRecord> findRecord(String databaseName, String tableName, List<Condition> conditionList, List<Byte> selectionColumnIndexList, boolean getOne) {
        try {
            File file = new File(fileDir+databaseName + "/" + tableName + fileExt);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                if (conditionList != null) {
                    Page page = getPage(file);
                    RawRecord record;
                    List<RawRecord> matchRecords = new ArrayList<>();
                    boolean isMatch = false;
                    byte columnIndex;
                    short condition;
                    Object value;
                    while (page != null) {
                        for (Object offset : page.getPageheader().getData_cell_offset()) {
                            isMatch = true;
                            record = readDataRecord(randomAccessFile, page.getPageheader().getPage_number(), (short) offset);
                            for(int i = 0; i < conditionList.size(); i++) {
                                isMatch = false;
                                columnIndex = conditionList.get(i).getIndex();
                                value = conditionList.get(i).getValue();
                                condition = conditionList.get(i).getConditionType();
                                if (record != null && record.getColumnValues().size() > columnIndex) {
                                    Object object = record.getColumnValues().get(columnIndex);
                                    try {
                                        isMatch = compare(object, value, condition);
                                    }
                                    catch (Exception e) {
                                        randomAccessFile.close();
                                        throw new DavidBaseError("Error while executing query.");
                                    }
                                    if(!isMatch) break;
                                }
                            }

                            if(isMatch) {
                                RawRecord matchedRecord = record;
                                if(selectionColumnIndexList != null) {
                                    matchedRecord = new RawRecord();
                                    matchedRecord.setRowID(record.getRowID());
                                    matchedRecord.setPage(record.getPage());
                                    matchedRecord.setOffset(record.getOffset());
                                    for (Byte index : selectionColumnIndexList) {
                                        matchedRecord.getColumnValues().add(record.getColumnValues().get(index));
                                    }
                                }
                                matchRecords.add(matchedRecord);
                                if(getOne) {
                                    randomAccessFile.close();
                                    return matchRecords;
                                }
                            }
                        }
                        if (page.getPageheader().getNext_page_pointer() == rightMostLeaf)
                            break;
                        page = readSinglePage(randomAccessFile, page.getPageheader().getNext_page_pointer());
                    }
                    randomAccessFile.close();
                    return matchRecords;
                }
            } else {
                throw new DavidBaseError("Table doesn't exist, " +databaseName+tableName);
           }
        }
        catch (Exception e) {
            throw new DavidBaseError("Error while executing query.");
        }
        return null;
    }

    private RawRecord readDataRecord(RandomAccessFile randomAccessFile, int pageNumber, short offset) {
        {
            try {
                if (pageNumber >= 0 && offset >= 0) {
                    RawRecord record = new RawRecord();
                    record.setPage(pageNumber);
                    record.setOffset(offset);
                    randomAccessFile.seek((pageSize * pageNumber) + offset);
                    record.setTotSize(randomAccessFile.readShort());
                    record.setRowID(randomAccessFile.readInt());
                    byte numberOfColumns = randomAccessFile.readByte();
                    byte[] serialTypeCodes = new byte[numberOfColumns];
                    for (byte i = 0; i < numberOfColumns; i++) {
                        serialTypeCodes[i] = randomAccessFile.readByte();
                    }
                    Object object;
                    for (byte i = 0; i < numberOfColumns; i++) {
                        switch (DataType.getTypeFromSerialCode(serialTypeCodes[i])) {
                            //case DataType_TinyInt.nullSerialCode is overridden with DataType_Text

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
                                object = randomAccessFile.readByte();
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
                        record.getColumnValues().add(object);
                    }
                    return record;
                }
            }
            catch (Exception e) {
                throw new DavidBaseError("Error while executing query.");
            }
            return null;
        }
    }

    private Page getPage(File file) {{
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            Page page = readSinglePage(randomAccessFile, 0);
            while (page.getPageheader().getPage_type() == PageType.table_node) {
                if (page.getPageheader().getNum_cells() == 0)
                    return null;
                randomAccessFile.seek((pageSize* page.getPageheader().getPage_number()) + ((short) page.getPageheader().getData_offset()));
                page = readSinglePage(randomAccessFile, randomAccessFile.readInt());
            }
            randomAccessFile.close();
            return page;
        } catch (Exception e) {
            throw new DavidBaseError("Error while executing query.");
        }
    }
    }

    private boolean compare(Object object1, Object object2, short condition) {
        boolean isMatch = false;
        if(object1 == null)
            isMatch = false;
        else
            switch (object1.getClass().toString()) {
            }
        return isMatch;
    }
}
