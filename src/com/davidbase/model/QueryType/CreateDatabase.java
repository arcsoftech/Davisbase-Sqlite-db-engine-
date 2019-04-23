package com.davidbase.model.QueryType;

import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;

import java.io.File;


/**
 * Class represents a Create database query
 */

public class CreateDatabase implements QueryBase {
    public String databaseName;

    public CreateDatabase(String databaseName){
        this.databaseName = databaseName;
    }

    @Override
    public QueryResult execute() {
        File database = new File(DavisBaseCatalogHandler.getDatabasePath(this.databaseName));
        boolean isCreated = database.mkdir();

        if(!isCreated){
            System.out.println(String.format("ERROR(200): Unable to create database '%s'", this.databaseName));
            return null;
        }

        QueryResult result = new QueryResult(1);
        return result;
    }
}
