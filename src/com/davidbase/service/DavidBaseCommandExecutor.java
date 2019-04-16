package com.davidbase.service;

import com.davidbase.model.QueryResult;
import com.davidbase.model.impl.CreateTable;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import static com.davidbase.DavidBaseConstants.pageSize;

/**
 * This class executes the user commands, deals with reading/writing to catalog and table files.
 */
public class DavidBaseCommandExecutor {

    /**
     *  Method for creating new tables
     *  @param query
     */
    public static QueryResult createTable(CreateTable query) {

        String createTableString="";
        ArrayList<String> createTableTokens = new ArrayList<String>(Arrays.asList(createTableString.split(" ")));

        /* Define table file name */
        String tableFileName = createTableTokens.get(2) + ".tbl";

        /* YOUR CODE GOES HERE */

        /*  Code to create a .tbl file to contain table data */
        try {
            /*  Create RandomAccessFile tableFile in read-write mode.
             *  Note that this doesn't create the table file in the correct directory structure
             */
            RandomAccessFile tableFile = new RandomAccessFile(tableFileName, "rw");
            tableFile.setLength(pageSize);
            tableFile.seek(0);
            tableFile.writeInt(63);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        /*  Code to insert a row in the davisbase_tables table
         *  i.e. database catalog meta-data
         */

        /*  Code to insert rows in the davisbase_columns table
         *  for each column in the new table
         *  i.e. database catalog meta-data
         */
        return new QueryResult();
    }

}
