package com.davidbase.utils;

import java.io.RandomAccessFile;

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

    public static boolean writeToFile(String tableFileName){
        return true;
    }

    public static boolean deleteFromFile(String tableFileName){
        return true;
    }
}
