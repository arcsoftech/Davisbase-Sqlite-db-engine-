package com.davidbase.service;

import com.davidbase.model.QueryBase;
import com.davidbase.model.QueryResult;
import com.davidbase.model.QueryType.CreateTable;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

import static com.davidbase.utils.DavidBaseConstants.pageSize;

/**
 * This class executes the user commands, deals with reading/writing to catalog and table files.
 */
public class DavidBaseCommandExecutor {

    /**
     *  Method for creating new tables
     *  @param query
     */
    public static QueryResult executeQuery(QueryBase query) {
        return query.Execute();
    }

}
