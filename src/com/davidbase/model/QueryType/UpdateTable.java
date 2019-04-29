package com.davidbase.model.QueryType;

import java.util.*;

import com.davidbase.model.PageComponent.*;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.utils.DavisBaseFileHandler;
import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.utils.DataType;

public class UpdateTable implements QueryBase {
	private String columns;
	private Condition condition;
	private String tableName;
	private ArrayList<String> clause_column;
	private List clause_value;

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
		DavisBaseFileHandler filehandler = new DavisBaseFileHandler();
		DavisBaseCatalogHandler catalog = new DavisBaseCatalogHandler();
		List<Object> recordValue = new ArrayList<>();
		List<LeafCell> dataRecords = filehandler.findRecord(DavisBaseConstants.DEFAULT_DATA_DIRNAME, tableName,
				condition, null, false);
		int clause_index_primary_key = 0;
		List<String> colNames = catalog.fetchAllTableColumns("", tableName);
		String primaryKey = catalog.getTablePrimaryKey(DavisBaseConstants.DEFAULT_DATA_DIRNAME, tableName);
		for (String col : clause_column) {
			if (col.equals(primaryKey)) {
				clause_index_primary_key = clause_column.indexOf(col);
			}
		}
		if(clause_index_primary_key !=0)
		{
			for (LeafCell record : dataRecords) {
				
				String existingPrimaryKeyvalue = String
						.valueOf(record.getPayload().getColValues().get(colNames.indexOf(primaryKey) + 1));
				if (clause_value.get(clause_index_primary_key).equals(existingPrimaryKeyvalue))
					{
						System.out.println("Primary key value should be unique");
						return new QueryResult(0);
					}
			}
		}
		
	
		for (LeafCell record : dataRecords) {
			recordValue= record.getPayload().getColValues();
			byte[] dataType= record.getPayload().getData_type();
			List<DataType> colValTypes = new ArrayList<>();
			for (int i = 0;i<dataType.length;i++)
			{
				colValTypes.add(DataType.getTypeFromSerialCode(dataType[i]));
			}
			record.getPayload().setColTypes(colValTypes);
			for (String clause : clause_column) {
				recordValue.set(colNames.indexOf(clause),clause_value.get(clause_column.indexOf(clause))); 
			}
			record.getPayload().setColValues(recordValue);
			record.initializeLeafForWrite();
			filehandler.updateLeafCell(DavisBaseConstants.DEFAULT_DATA_DIRNAME,tableName,record);
		}
		
			return new QueryResult(1);
	}
}
