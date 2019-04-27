package com.davidbase.model.QueryType;

import java.util.*;
import com.davidbase.model.PageComponent.*;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.utils.DavisBaseFileHandler;
import com.davidbase.utils.DavisBaseConstants;

public class UpdateTable implements QueryBase {
	private String columns;
	private Condition condition;
	private String tableName;
	private ArrayList<String> clause_column;
	private List clause_value;
	DavisBaseFileHandler filehandler;
	DavisBaseCatalogHandler catalog;

	/**
	 * @return the catalog
	 */
	public DavisBaseCatalogHandler getCatalog() {
		return catalog;
	}

	/**
	 * @return the clause_column
	 */
	public ArrayList<String> getClause_column() {
		return clause_column;
	}

	/**
	 * @return the clause_value
	 */
	public List getClause_value() {
		return clause_value;
	}

	/**
	 * @return the columns
	 */
	public String getColumns() {
		return columns;
	}

	/**
	 * @param clause_column the clause_column to set
	 */
	public void setClause_column(ArrayList<String> clause_column) {
		this.clause_column = clause_column;
	}

	/**
	 * @param clause_value the clause_value to set
	 */
	public void setClause_value(List clause_value) {
		this.clause_value = clause_value;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(String columns) {
		this.columns = columns;
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
		List<Condition> conditions = new ArrayList<>();
		conditions.add(condition);
		List<LeafCell> dataRecords = filehandler.findRecord(DavisBaseConstants.DEFAULT_DATA_DIRNAME, tableName, condition,null, false);
		String primaryKey = catalog.getTablePrimaryKey(DavisBaseConstants.DEFAULT_DATA_DIRNAME, tableName);
		for (LeafCell record : dataRecords) {
			List<Object> recordValue = record.getPayload().getColValues();
		}
		return null;
	}
}
