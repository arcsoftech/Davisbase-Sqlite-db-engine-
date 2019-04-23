package com.davidbase.model.QueryType;

import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;

import java.io.File;
import java.util.ArrayList;

/**
 * Class represents a Drop Table query
 */
public class DropTable implements QueryBase {

    public String databaseName;
    public String tableName;

    public DropTable(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    @Override
    public QueryResult execute() {
        return new QueryResult(1);
    }
}
