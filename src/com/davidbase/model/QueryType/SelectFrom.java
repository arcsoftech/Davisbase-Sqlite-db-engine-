package com.davidbase.model.QueryType;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.LeafCell;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.utils.DavisBaseFileHandler;
import java.util.List;

/**
 * This class represents a Select Query
 * supports a single table.
 */
public class SelectFrom implements QueryBase {

    private String tableName;
    private String columns;
	private Condition condition;
	private String databaseName;

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getColumns() {
		return this.columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public Condition getCondition() {
		return this.condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	 private DavisBaseFileHandler filehandler;

    @Override
    public QueryResult execute() {
        try {
        	
  
        	
        	DavisBaseCatalogHandler ctlg = new DavisBaseCatalogHandler();
//        	System.out.print(tableName);
        	 filehandler = new DavisBaseFileHandler();
            List<LeafCell> records = filehandler.findRecord(databaseName, tableName, condition,null, false);
            
            List<String> colNames = ctlg.fetchAllTableColumns("", tableName);
           
            QueryResult queryObject = new QueryResult(records.size());
            
          
         
           
            for (LeafCell record : records) {
            	
            	int colIndex = 0;
            	
            	int valueIndex = 0;
            	
            	List<Object> colValues = record.getPayload().getColValues();
            	
            	
           
            	for (Object colValue : colValues) {
            		
            		if (valueIndex > 0) {
						
            			queryObject.getColumns().add(colNames.get(colIndex));
            			
            			queryObject.getValues().add(String.valueOf(colValue));
            			colIndex = colIndex + 1;
            			
					}
					
            		valueIndex = valueIndex + 1;
	
            	}
           
 	
            }
            
//            System.out.print(queryObject.getColumns() + " " + queryObject.getValues());
            
//         
//        	
//            new DavisBaseFileHandler().readFromFile(tableName);
            
            return queryObject;
        }catch(Exception e){
            e.printStackTrace();
            throw new DavidBaseError("Error while creating new table");
        }
    }
}
