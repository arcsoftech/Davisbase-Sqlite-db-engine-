package com.davidbase.model;

public class QueryResult {
    int rowsAffected;
    String errorMessage;
    //Map<Integer,>

    public QueryResult(int rowsInRes){
        this.rowsAffected=rowsInRes;
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
