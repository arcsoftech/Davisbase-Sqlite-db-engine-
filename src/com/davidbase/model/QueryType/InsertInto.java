package com.davidbase.model.QueryType;

import com.davidbase.model.PageComponent.LeafCell;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.model.PageComponent.RawRecord;
import com.davidbase.utils.DavisBaseFileHandler;

import java.util.ArrayList;
import java.util.List;

public class InsertInto implements QueryBase {
    private String databaseName;
    private String tableName;
    private List<String> columns;
    private List<String> columnValues;


    public InsertInto(String databaseName,String tableName, List<String> columns, List<String> columnsValues){
        this.databaseName=databaseName;
        this.tableName=tableName;
        this.columns=columns;
        this.columnValues=columnsValues;

    }

    @Override
    public QueryResult Execute() {
        // map passed values to records ready to be inserted into file
        LeafCell recordsToFile = prepareRecord();
        new DavisBaseFileHandler().writeLeafCell(databaseName,tableName, recordsToFile );
        return new QueryResult(1);
    }
 
    private LeafCell prepareRecord(){
        LeafCell recordsToFile = new LeafCell();
        return recordsToFile;
    }

    private int getNextRowId(){
        // call Catalog utility to fetch rowid( row count in table + 1)
        return 1;

    }
}
