package com.davidbase.model.QueryType;

import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.model.PageComponent.RawRecord;
import com.davidbase.utils.DavisBaseFileHandler;

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
        DavisBaseFileHandler.writeToFile(tableName, recordsToFile );
        return new QueryResult(recordsToFile.size());
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
