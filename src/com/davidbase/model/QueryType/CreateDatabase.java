package com.davidbase.model.QueryType;

import com.davidbase.utils.DavidBaseCatalogHandler;
import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;

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
    public QueryResult Execute() {
        File database = new File(DavidBaseCatalogHandler.getDatabasePath(this.databaseName));
        boolean isCreated = database.mkdir();

        if(!isCreated){
            System.out.println(String.format("ERROR(200): Unable to create database '%s'", this.databaseName));
            return null;
        }

        QueryResult result = new QueryResult(1);
        return result;
    }
}
