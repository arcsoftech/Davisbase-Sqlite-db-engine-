package com.davidbase.model.impl;

import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;

import java.util.List;

/**
 * Class represents a Create Table query
 */
public class CreateTable implements QueryBase {

    private String tableName;
    private List<String> columns;
    private String primaryKey;
    private List<String> indexes;

    @Override
    public QueryResult execute() {
        return null;
    }
}
