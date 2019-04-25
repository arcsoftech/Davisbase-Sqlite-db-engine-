package com.davidbase.model.QueryType;

import java.util.*;

public class UpdateTable implements QueryBase {
    private String columns;
    private List values;
    private Condition condition;
    private String tableName;
    private String clause_column;
    private String clause_value;

	public String getClause_column() {
		return this.clause_column;
	}

	public void setClause_column(String clause_column) {
		this.clause_column = clause_column;
	}

	public String getClause_value() {
		return this.clause_value;
	}

	public void setClause_value(String clause_value) {
		this.clause_value = clause_value;
	}



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
