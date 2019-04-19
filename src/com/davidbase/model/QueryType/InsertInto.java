package com.davidbase.model.QueryType;

import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;
import com.davidbase.model.RawRecord;
import com.davidbase.utils.DavidBaseFileHandler;

import java.util.ArrayList;
import java.util.List;

public class InsertInto implements QueryBase {
    private String tableName;
    private List<String> columns;
    private List<Object> columnValues;

    @Override
    public QueryResult Execute() {
        // map passed values to records ready to be inserted into file
        List<RawRecord> recordsToFile = prepareRecord();
        DavidBaseFileHandler.writeToFile(tableName, recordsToFile );
        return new QueryResult(recordsToFile.size());
    }
    @Override
    public boolean Validate()
    {
        return true;
    }
    private List<RawRecord> prepareRecord(){
        List<RawRecord> recordsToFile = new ArrayList<>();
        return recordsToFile;
    }

    private int getNextRowId(){
        // call Catalog utility to fetch rowid( row count in table + 1)
        return 1;

    }
}
