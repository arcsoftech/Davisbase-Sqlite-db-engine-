package com.davidbase.model.QueryType;

import java.util.*;

public class UpdateTable implements QueryBase {
    private String columns;
    private List values;
    private Condition condition;
    private String tableName;


	public String getColumns() {
		return this.columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public List getValues() {
		return this.values;
	}

	public void setValues(List values) {
		this.values = values;
	}

	public Condition getCondition() {
		return this.condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



    @Override
    public QueryResult execute() {
        return null;
    }
}
