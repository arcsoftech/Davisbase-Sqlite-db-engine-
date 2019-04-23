package com.davidbase.service;

import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;

/**
 * This class executes the user commands, deals with reading/writing to catalog and table files.
 */
public class DavidBaseCommandExecutor {

    /**
     *  Method for creating new tables
     *  @param query
     */
    public QueryResult executeQuery(QueryBase query) {
        return query.execute();
    }

}
