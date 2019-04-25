package com.davidbase.model.QueryType;

import java.util.ArrayList;
import java.util.List;

import com.davidbase.model.PageComponent.LeafCell;
import com.davidbase.utils.DataType;
import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.utils.DavisBaseFileHandler;


public class ShowTable implements QueryBase {
    @Override
    public QueryResult execute() {
    	
    	 DavisBaseFileHandler filehandler = new DavisBaseFileHandler();
    	
    	  ArrayList<String> columns = new ArrayList<>();

      	List<Condition> conditions = new ArrayList<>();
//          conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new DataType_Text(databaseName)));
          conditions.add(Condition.CreateCondition(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_TABLE_NAME, Condition.EQUALS, null,null));

    	  List<LeafCell> records = filehandler.findRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME, conditions, null, false);
    	  
    	  System.out.print(records);
    	
    	
        return null;
    }
}
