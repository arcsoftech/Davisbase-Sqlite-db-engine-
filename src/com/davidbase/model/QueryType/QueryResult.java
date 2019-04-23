package com.davidbase.model.QueryType;

import java.util.List;

public class QueryResult {
    int rowsAffected;
    private List<String> columns;
    String errorMessage;
    //Map<Integer,>

    public QueryResult(int rowsInRes){
        this.rowsAffected=rowsInRes;
    }
    public List<String> getColumns() {
        return columns;
    }
    public int getRowsAffected() {
        return rowsAffected;
    }
    
    public void setRowsAffected(int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
