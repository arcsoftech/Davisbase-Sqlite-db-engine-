package com.davidbase.model.QueryType;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.LeafCell;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.utils.DavisBaseFileHandler;

import static com.davidbase.utils.DavisBaseConstants.DEFAULT_DATA_DIRNAME;
import static com.davidbase.utils.DavisBaseConstants.FILE_EXT;

import java.io.File;
import java.util.List;

/**
 * This class represents a Select Query
 * supports a single table.
 */
public class SelectFrom implements QueryBase {

    private String tableName;
    private String columns;
    private Condition condition;

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
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
        	
//        	System.out.print(tableName);
        	 filehandler = new DavisBaseFileHandler();
            List<LeafCell> records = filehandler.findRecord("data", tableName, condition,null, false);

            for (LeafCell record : records) {
            	System.out.print(record.getPayload().getColValues());	
            }
        	
            new DavisBaseFileHandler().readFromFile(tableName);
        }catch(Exception e){
            e.printStackTrace();
            throw new DavidBaseError("Error while creating new table");
        }
        return null;
    }
}
