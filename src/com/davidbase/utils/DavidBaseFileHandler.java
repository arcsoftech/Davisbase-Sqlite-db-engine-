package com.davidbase.utils;

import com.davidbase.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.davidbase.DavidBaseConstants.*;

/**
 * File utility to read/write data from .tbl files
 */
public class DavidBaseFileHandler {

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


}
