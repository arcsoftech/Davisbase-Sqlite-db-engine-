package com.davidbase.utils;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.Condition;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.FileInputStream;
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
    public static Map<String, String> metadata = new HashMap<>();
    public static boolean databaseExists(String databaseName) {
        File databaseDir = new File(getDatabasePath(databaseName));
        return databaseDir.exists();
    }

    public static String getDatabasePath(String databaseName) {
        return DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + databaseName;
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
            // iterate over each record to be inserted
            int pageCount = (int) (tablefile.length() / PAGE_SIZE);
            Page page = findPage(tablefile, leafCell.getHeader().getRow_id(), 0);

            switch (pageCount) {
                case 1: // this is the first page to be inserted
                    // insert leaf node with data
                    if(!checkSpaceRequirements(page,leafCell)){
                        System.out.println("Splitting page");

                        //3.  create root page (non leaf)
                        Page<NonLeafCell> rootPage = new Page();
                        rootPage.setPage_number(1);
                        rootPage.setPage_type(PageType.table_node);
                        rootPage.setNum_cells((byte) 1);
                        int offset = ((short)((long)(rootPage.getPage_number()+1) * PAGE_SIZE)) - (NonLeafCell.getLinkRecordSize());
                        rootPage.setData_offset((short) offset);
                        System.out.println("offset " + offset);
                        rootPage.setData_cell_offset((new short[]{(short) offset}));

                        splitPage(tablefile,page, rootPage,leafCell,1);

                        storeRootInformation(rootPage,tableName);


                    }else {
                        // Prepare the leaf node
                        Page<LeafCell> dataNode = new Page<LeafCell>();
                        PageHeader header;
                        List<LeafCell> dataCells = new ArrayList<>();

                        if (page.getPageheader().getNum_cells() <= 0) {
                            header = new PageHeader(0);;
                            header.setPage_number(0);
                            header.setNum_cells((byte) 1);
                            int offset = ((short) PAGE_SIZE) - (leafCell.getPayload().getPayloadSize() + CellHeader.getSize());
                            header.setData_offset((short) offset);
                            System.out.println("offset " + offset);
                            header.setData_cell_offset((new short[]{(short) offset}));
                            header.setPage_type(PageType.table_leaf);
                            header.setNext_page_pointer(RIGHT_MOST_LEAF);

                        } else {
                            header = page.getPageheader();
                            header.setNum_cells((byte) (page.getPageheader().getNum_cells() + 1));
                            int offset = page.getPageheader().getData_offset() - (leafCell.getPayload().getPayloadSize()
                                    + CellHeader.getSize());
                            header.setData_offset((short) offset);
                            int length = page.getPageheader().getData_cell_offset().length + 1;
                            short[] newOffsets = Arrays.copyOf(page.getPageheader().getData_cell_offset(), length);
                            newOffsets[length - 1] = (short) offset;
                            header.setData_cell_offset(newOffsets);
                            System.out.println("offset " + offset);
                        }

                        dataNode.setPageheader(header);
                        dataCells.add(leafCell);
                        dataNode.setCells(dataCells);
                        storeRootInformation(page,tableName);
                        writeLeafCell(tablefile, dataCells, header.getData_offset());
                        writePageHeader(tablefile, dataNode, 0);
                    }
                    break;
                default: // for all other cases.

                    // page already has a root page at pagenumber ;
//                int rootPageIndex = 1;
                	System.out.print(tableName);
//                    int rootPageIndex = Integer.valueOf(metadata.get(tableName));
                	 int rootPageIndex = 1;
                    if(!checkSpaceRequirements(page,leafCell)) {

                        System.out.println("Splitting page");
                        Page<NonLeafCell> currentRoot = readSinglePage(tablefile,rootPageIndex);
                        currentRoot.setNum_cells((byte) (currentRoot.getNum_cells() + 1));
                        splitPage(tablefile,page, currentRoot,leafCell,page.getPage_number());

                    }else{
                        // add the leaf cell to the current leaf
                        // Prepare the leaf node

                        List<LeafCell> dataCells = new ArrayList<>();
                        PageHeader header;
                        if (page.getPageheader().getNum_cells() <= 0) {
                            header = new PageHeader(0);
                            header.setPage_number(0);

                            header.setNum_cells((byte) 1);
                            int offset = ((short) PAGE_SIZE) - (leafCell.getPayload().getPayloadSize() + CellHeader.getSize());
                            header.setData_offset((short) offset);
                            System.out.println("offset " + offset);
                            header.setData_cell_offset((new short[]{(short) offset}));
                            header.setPage_type(PageType.table_leaf);
                            header.setNext_page_pointer(RIGHT_MOST_LEAF);

                        } else {
                            header = page.getPageheader();
                            header.setNum_cells((byte) (page.getPageheader().getNum_cells() + 1));
                            int offset = page.getPageheader().getData_offset() - (leafCell.getPayload().getPayloadSize()
                                    + CellHeader.getSize());
                            header.setData_offset((short) offset);
                            int length = page.getPageheader().getData_cell_offset().length + 1;
                            short[] newOffsets = Arrays.copyOf(page.getPageheader().getData_cell_offset(), length);
                            newOffsets[length - 1] = (short) offset;
                            header.setData_cell_offset(newOffsets);
                            System.out.println("offset " + offset);
                        }

                        page.setPageheader(header);
                        dataCells.add(leafCell);
                        page.setCells(dataCells);

                        writeLeafCell(tablefile, dataCells, header.getData_offset());
                        writePageHeader(tablefile, page, page.getPage_number());

                    }
                    break;
            }

            // check for page overflow

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void storeRootInformation(Page rootPage, String tableName) {
        /*Code to store table root page meta data */
        Map<String, String> metadata = new HashMap<>();
        Properties properties = new Properties();

        try {
            File f = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME + "/"+"MetaData.properties");
            if(f.exists())
            {
                properties.load(new FileInputStream(DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME + "/"+"MetaData.properties"));
                for (String key : properties.stringPropertyNames()) {
                    DavisBaseFileHandler.metadata.put(key, properties.get(key).toString());
                }
            }
            DavisBaseFileHandler.metadata.put(tableName,String.valueOf(rootPage.getPage_number()));
            properties.putAll(DavisBaseFileHandler.metadata);
            properties.store(new FileOutputStream(DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME + "/"+"MetaData.properties"), null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void splitPage(RandomAccessFile tableFile,Page page, Page<NonLeafCell> rootPage, LeafCell leafCell, int pageNumber) throws IOException {

        Page<LeafCell> rightLeafPage = new Page();
        rightLeafPage.setPage_number(pageNumber+1);
        rightLeafPage.setNum_cells((byte) 1);
        int offset = ((short) ((long)(rightLeafPage.getPage_number()+1) * PAGE_SIZE)) - (leafCell.getPayload().getPayloadSize() + CellHeader.getSize());
        rightLeafPage.setData_offset((short) offset);
        System.out.println("offset " + offset);
        rightLeafPage.setData_cell_offset((new short[]{(short) offset}));
        rightLeafPage.setNext_page_pointer(RIGHT_MOST_LEAF);
        rightLeafPage.setPage_type(PageType.table_leaf);
        List<LeafCell> dataCells = new ArrayList<>();
        dataCells.add(leafCell);
        rightLeafPage.setCells(dataCells);

        writeLeafCell(tableFile, dataCells, rightLeafPage.getData_offset());
        writePageHeader(tableFile, rightLeafPage, rightLeafPage.getPage_number());

        rootPage.setNext_page_pointer(rightLeafPage.getPage_number());
        List<NonLeafCell> nonLeafCells = new ArrayList<>();
        NonLeafCell nonLeafCell = new NonLeafCell(page.getPage_number(), leafCell.getHeader().getRow_id());

        if(rootPage.getNum_cells()>1) {
            PageHeader header = rootPage.getPageheader();
            offset = rootPage.getPageheader().getData_offset() - (NonLeafCell.getLinkRecordSize());
            header.setData_offset((short) offset);
            int length = rootPage.getPageheader().getData_cell_offset().length + 1;
            short[] newOffsets = Arrays.copyOf(rootPage.getPageheader().getData_cell_offset(), length);
            newOffsets[length - 1] = (short) offset;
            header.setData_cell_offset(newOffsets);
            System.out.println("offset " + offset);
            rootPage.setPageheader(header);
        }
        nonLeafCells.add(nonLeafCell);

        writeNonLeafCell(tableFile,nonLeafCells,rootPage.getData_offset());
        writePageHeader(tableFile,rootPage,rootPage.getPage_number());

        //update left leaf right point
        page.setNext_page_pointer(rightLeafPage.getPage_number());
        writePageHeader(tableFile,page,page.getPage_number());
    }

    private static Page findPage(RandomAccessFile tableFile, int rowID, int pageNumber) {
        Page nextPage = readSinglePage(tableFile, pageNumber);
        if (nextPage.getPageheader().getPage_type() == PageType.table_leaf
                && nextPage.getNext_page_pointer()==RIGHT_MOST_LEAF)
            return nextPage;
        pageNumber = pageNumber + 1; // FIXME
        return findPage(tableFile, rowID, pageNumber);
    }

    public int deleteFromFile(String databaseName, String tableName, List<Condition> delConditions) {
        int deletedRecordCount = 0;
        try {
            File file = new File(getDatabasePath(databaseName) + "/" + tableName + FILE_EXT);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                if(delConditions != null) {

                    //iterate here over all leaf pages
                    Page page = getPage(file);
                    LeafCell leafCell;
                    boolean isMatch;
                    byte columnIndex;
                    short condition;
                    Object conditionValue;
                    DataType conditionValueType;
                    while (page != null) {
                        int offSetIndex = 0;
                        for (Short offset : page.getData_cell_offset()) {
                            isMatch = true;
                            leafCell = readLeaf(randomAccessFile, page.getPage_number(), offset);
                            for(int i = 0; i < delConditions.size(); i++) {
                                isMatch = false;
                                columnIndex = delConditions.get(i).getIndex();
                                conditionValue = delConditions.get(i).getValue();
                                condition = delConditions.get(i).getConditionType();
                                conditionValueType = delConditions.get(i).getValType();
                                if (leafCell != null && leafCell.getPayload().getColValues().size() > columnIndex) {
                                    Object object = leafCell.getPayload().getColValues().get(columnIndex);
                                    try {
                                        isMatch = compare(object, conditionValue, condition,conditionValueType,conditionValueType);
                                    }
                                    catch (Exception e) {
                                        randomAccessFile.close();
                                        throw e;
                                    }
                                    if(!isMatch) break;
                                }
                            }

                            if(isMatch) {
                                page.setNumberOfCells((byte) (page.getNumberOfCells() - 1));
                                if(page.getNumberOfCells() == 0) {
                                    page.setData_offset((short) ((page.getPage_number()*PAGE_SIZE) + PAGE_SIZE - 1));
                                }else{
                                    page.setData_cell_offset(removeOffset(page.getData_cell_offset(),offSetIndex));
                                    int lengthOffsets = page.getData_cell_offset().length;
                                    page.setData_offset(page.getData_cell_offset()[lengthOffsets-1]);
                                }
                                writePageHeader(randomAccessFile, page, page.getPage_number());
                                deletedRecordCount++;
                                offSetIndex=0;
                            }else {
                                offSetIndex++;
                            }
                        }
                        if(page.getNext_page_pointer() == RIGHT_MOST_LEAF)
                            break;
                        page = readSinglePage(randomAccessFile, page.getNext_page_pointer());
                    }
                    randomAccessFile.close();
                    return deletedRecordCount;
                }
            }
            else {
                System.out.println("Table doesn't exist." + tableName);
                return deletedRecordCount;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError(e.getMessage());
        }
        return deletedRecordCount;
    }

    private short[] removeOffset(short[] data_cell_offset, int offSetIndex) {
        short[] newoffset = new short[data_cell_offset.length-1];
        for(int i=0,j=0;i<data_cell_offset.length;i++){
            if(i==offSetIndex)
                continue;
            newoffset[j++] = data_cell_offset[i];
        }
        return  newoffset;
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
            // For first time initialization
            short offset = randomAccessFile.readShort();
            header.setData_offset(offset<=0?(short)PAGE_SIZE-1:offset);
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
            tableFile.writeByte(cell.getPayload().getNum_columns());

            for (byte colType : cell.getPayload().getData_type())
                tableFile.writeByte(colType);

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
//                    
//                    int recordCount = page.getNum_cells();
                    
//                    System.out.print(recordCount);
                    
                    while (page != null) {
                        for (Object offset : page.getPageheader().getData_cell_offset()) {
                            isMatch = true;
                            leafCell = readLeaf(randomAccessFile, page.getPageheader().getPage_number(),
                                    (short) offset, page.getNum_cells());
                            
//                            System.out.print(conditionList.size());
                            for (int i = 0; i < conditionList.size(); i++) {
                                isMatch = false;
                                columnIndex = conditionList.get(i).getIndex();
                                conditionValue = conditionList.get(i).getValue();
//                                System.out.print(conditionList.get(i).getIndex());
                                condition = conditionList.get(i).getConditionType();
                                conditionValueType = conditionList.get(i).getValType();
                                if (leafCell != null && leafCell.getPayload().getColValues().size() > columnIndex) {
//                                
                                    Object colValue = leafCell.getPayload().getColValues().get(columnIndex);
//                      
                                    DataType colType = DataType
                                            .getTypeFromSerialCode(leafCell.getPayload().getData_type()[columnIndex]);
                                    try {
                                    	
//                                      System.out.print(colValue + " " + conditionValue + " " + condition + " " + colType + " " +conditionValueType +"\n" );

                                        isMatch = compare(colValue, conditionValue, condition, colType,
                                                conditionValueType);
                                        
//                                        System.out.print(isMatch);
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
    
    public LeafCell readLeaf(RandomAccessFile randomAccessFile, int pageNumber, short offset, int records) {
        {
            try {
                if (pageNumber >= 0 && offset >= 0 && pageNumber <= records) {
                	
//<<<<<<< HEAD
//                    randomAccessFile.seek((PAGE_SIZE * pageNumber) + offset);
//||||||| merged common ancestors
//               
//                    randomAccessFile.seek((PAGE_SIZE * pageNumber) + offset);
//=======
               
                    randomAccessFile.seek( offset);

                    short payloadSize = randomAccessFile.readShort();
                    int rowId = randomAccessFile.readInt();
                    CellHeader cellheader = new CellHeader(payloadSize, rowId);
                    byte numberOfColumns = randomAccessFile.readByte();
                    byte[] serialTypeCodes = new byte[numberOfColumns];
                    for (byte i = 0; i < numberOfColumns; i++) {
                        serialTypeCodes[i] = randomAccessFile.readByte();
                    }
                    List<Object> values = new ArrayList<>();
                    Object object=null;
                    for (byte i = 0; i < numberOfColumns; i++) {
                        switch (DataType.getTypeFromSerialCode(serialTypeCodes[i])) {
                            // case DataType_TinyInt.nullSerialCode is overridden with DataType_Text

                            case NULL:
                                object = null;
                                break;

                            case TINYINT:
                                object = (Byte) randomAccessFile.readByte();
                                break;
                            case YEAR:
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

                            case TIME:
                                object = randomAccessFile.readInt();
                                break;

                            case DATETIME:
                                object = randomAccessFile.readLong();
                                break;

                            case DATE:
                                object = randomAccessFile.readLong();
                                break;

                            case TEXT:
                            	object = "";
                                char[] text = new char[serialTypeCodes[i] - 12 ];
                                for (byte k = 0; k < (serialTypeCodes[i] - 12); k++) {
                                    text[k] = (char) randomAccessFile.readByte();
                                }
                                object = new String(text);

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
                e.printStackTrace();
                throw new DavidBaseError("Error while executing query.");
            }
            return null;
        }
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
                    Object object=null;
                    for (byte i = 0; i < numberOfColumns; i++) {
                        switch (DataType.getTypeFromSerialCode(serialTypeCodes[i])) {
                            // case DataType_TinyInt.nullSerialCode is overridden with DataType_Text
                            case NULL:
                            object = null;
                            break;

                        case TINYINT:
                            object = (Byte) randomAccessFile.readByte();
                            break;
                        case YEAR:
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

                        case TIME:
                            object = randomAccessFile.readInt();
                            break;

                        case DATETIME:
                            object = randomAccessFile.readLong();
                            break;

                        case DATE:
                            object = randomAccessFile.readLong();
                            break;

                        case TEXT:
                            object = "";
                            char[] text = new char[serialTypeCodes[i] - 12 ];
                            for (byte k = 0; k < (serialTypeCodes[i] - 12); k++) {
                                text[k] = (char) randomAccessFile.readByte();
                            }
                            object = new String(text);

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
                e.printStackTrace();
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
                case DATE:
                    break;
                case DATETIME:
                    break;
                case TIME:
                    break;
                case YEAR:
                    break;
                case TEXT:
               	 return compare((String)value1,(String) value2, condition, conditionType);
            }
        return false;
    }

    private boolean compare(Byte value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case TINYINT:
                return DavisBaseUtil.conditionCompare(value1, Byte.valueOf(String.valueOf(value2)), condition);
            case SMALLINT:
                return DavisBaseUtil.conditionCompare(value1, Short.valueOf(String.valueOf(value2)), condition);
            case INT:
                return DavisBaseUtil.conditionCompare(value1, Integer.valueOf(String.valueOf(value2)), condition);
            case BIGINT:
                return DavisBaseUtil.conditionCompare(value1, Long.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }

    private boolean compare(Short value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case TINYINT:
                return DavisBaseUtil.conditionCompare(value1, Byte.valueOf(String.valueOf(value2)), condition);
            case SMALLINT:
                return DavisBaseUtil.conditionCompare(value1, Short.valueOf(String.valueOf(value2)), condition);
            case INT:
                return DavisBaseUtil.conditionCompare(value1, Integer.valueOf(String.valueOf(value2)), condition);
            case BIGINT:
                return DavisBaseUtil.conditionCompare(value1, Long.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }

    private boolean compare(Integer value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case TINYINT:
                return DavisBaseUtil.conditionCompare(value1, Byte.valueOf(String.valueOf(value2)), condition);
            case SMALLINT:
                return DavisBaseUtil.conditionCompare(value1, Short.valueOf(String.valueOf(value2)), condition);
            case INT:
                return DavisBaseUtil.conditionCompare(value1, Integer.valueOf(String.valueOf(value2)), condition);
            case BIGINT:
                return DavisBaseUtil.conditionCompare(value1, Long.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }

    private boolean compare(Long value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case TINYINT:
                return DavisBaseUtil.conditionCompare(value1, Byte.valueOf(String.valueOf(value2)), condition);
            case SMALLINT:
                return DavisBaseUtil.conditionCompare(value1, Short.valueOf(String.valueOf(value2)), condition);
            case INT:
                return DavisBaseUtil.conditionCompare(value1, Integer.valueOf(String.valueOf(value2)), condition);
            case BIGINT:
                return DavisBaseUtil.conditionCompare(value1, Long.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }

    private boolean compare(Float value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case REAL:
                return DavisBaseUtil.conditionCompare(value1, Float.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }

    private boolean compare(Double value1, Object value2, short condition, DataType conditionType) {
        switch (conditionType) {
            case REAL:
                return DavisBaseUtil.conditionCompare(value1, Float.valueOf(String.valueOf(value2)), condition);
        }
        return false;
    }
    
    
    private boolean compare(String value1, String value2, short condition, DataType conditionType) {
        switch (conditionType) {
        case TEXT:
            return DavisBaseUtil.conditionCompare(value1,  value2, condition);
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
            short startingAddress = (short)((page.getPage_number()*PAGE_SIZE)+ (Page.getHeaderFixedLength()
                    + (page.getPageheader().getData_cell_offset().length * Short.BYTES)));
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
