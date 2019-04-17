package com.davidbase.model.impl;

import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.davidbase.DavidBaseConstants.pageSize;

/**
 * Class represents a Create Table query
 */
public class CreateTable implements QueryBase {

    private String tableName;
    private List<String> columns;
    private String primaryKey;
    private List<String> indexes;

    @Override
    public QueryResult execute() {
        //Run any pre-req for the create table

        String createTableString="";
        ArrayList<String> createTableTokens = new ArrayList<String>(Arrays.asList(createTableString.split(" ")));

        /* Define table file name */
        String tableFileName = createTableTokens.get(2) ;

        /*  Code to insert a row in the davisbase_tables table
         *  i.e. database catalog meta-data
         */

        /*  Code to insert rows in the davisbase_columns table
         *  for each column in the new table
         *  i.e. database catalog meta-data
         */

        return new QueryResult(1);
    }
}
