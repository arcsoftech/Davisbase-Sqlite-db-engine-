package com.davidbase.model.QueryType;

import com.davidbase.utils.DavisBaseFileHandler;

import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.DEFAULT_DATA_DIRNAME;

public class DeleteFrom implements QueryBase {

    public String databaseName;
    public String tableName;
    public List<Condition> conditions;

    public DavisBaseFileHandler fileHandler;

    public DeleteFrom(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.fileHandler = new DavisBaseFileHandler();
    }

    @Override
    public QueryResult execute() {
        int rowsEffected = fileHandler.deleteFromFile(databaseName,tableName,conditions);
        return new QueryResult(rowsEffected);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
