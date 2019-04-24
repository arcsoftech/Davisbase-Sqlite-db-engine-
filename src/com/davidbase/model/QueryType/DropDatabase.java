package com.davidbase.model.QueryType;

import com.davidbase.utils.DavisBaseCatalogHandler;

import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;

import java.io.File;

@Deprecated
public class DropDatabase implements QueryBase {
    public String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public QueryResult execute() {
        File database = new File(DavisBaseCatalogHandler.getDatabasePath(databaseName));

        QueryResult result = new QueryResult(1);
        return result;
    }
}
