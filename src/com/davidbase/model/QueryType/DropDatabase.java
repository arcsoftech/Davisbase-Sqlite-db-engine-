package com.davidbase.model.QueryType;


import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;


@Deprecated
public class DropDatabase implements QueryBase {
    public String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public QueryResult execute() {
        QueryResult result = new QueryResult(1);
        return result;
    }
}
