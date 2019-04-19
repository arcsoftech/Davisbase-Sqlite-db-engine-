package com.davidbase.model.QueryType;

import com.davidbase.utils.DavidBaseCatalogHandler;

import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by parag on 4/12/2017.
 */
public class DropDatabase implements QueryBase {
    public String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public QueryResult Execute() {
        File database = new File(DavidBaseCatalogHandler.getDatabasePath(databaseName));

        QueryResult result = new QueryResult(1);
        return result;
    }
}
