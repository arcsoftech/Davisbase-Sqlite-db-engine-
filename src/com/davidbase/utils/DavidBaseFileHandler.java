package com.davidbase.utils;

import com.davidbase.model.*;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static com.davidbase.DavidBaseConstants.*;

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
                Page page = findPage(tablefile,record.getRowID(), pageNumber);

                //check for page overflow
            }
        } catch (FileNotFoundException e) {
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
}
