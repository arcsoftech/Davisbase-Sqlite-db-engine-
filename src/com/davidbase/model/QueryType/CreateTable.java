package com.davidbase.model.QueryType;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavidBaseFileHandler;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.davidbase.utils.DavidBaseConstants.pageSize;

/**
 * Class represents a Create Table query
 */
public class CreateTable implements QueryBase {

    private String tableName;
    private List<String> columns;
    private String primaryKey;
    private List<String> indexes;

    @Override
    public QueryResult Execute() {
        //Run any pre-req for the create

        try {
            DavidBaseFileHandler.createFile(tableName);
        }catch(Exception e){
            e.printStackTrace();
            throw new DavidBaseError("Error while creating new table");
        }

        /*  Code to insert a row in the davisbase_tables table
         *  i.e. database catalog meta-data
         */

        /*  Code to insert rows in the davisbase_columns table
         *  for each column in the new table
         *  i.e. database catalog meta-data
         */

        return new QueryResult(1);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<String> indexes) {
        this.indexes = indexes;
    }
}
