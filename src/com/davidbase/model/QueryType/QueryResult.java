package com.davidbase.model.QueryType;

import java.util.ArrayList;
import java.util.List;

public class QueryResult {
    int rowsAffected;
    private List<String> columns;
    private List<String> values;
    String errorMessage;
    //Map<Integer,>

    public QueryResult(int rowsInRes){
        this.rowsAffected=rowsInRes;
        columns = new ArrayList<String>();
        values = new ArrayList<String>();
    }
    public List<String> getColumns() {
        return columns;
    }
    public int getRowsAffected() {
        return rowsAffected;
    }
    public int getColumnCount()
    {
    	return columns.size();    	
    }
    public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
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
