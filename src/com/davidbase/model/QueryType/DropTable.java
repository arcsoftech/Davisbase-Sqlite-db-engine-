package com.davidbase.model.QueryType;

import com.davidbase.utils.DavidBaseConstants;
import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;

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
    public QueryResult Execute() {
        return new QueryResult(1);
    }
}
