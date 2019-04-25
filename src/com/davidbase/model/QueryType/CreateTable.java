package com.davidbase.model.QueryType;

import com.davidbase.model.PageComponent.InternalColumn;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseCatalogHandler;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.*;

/**
 * Class represents a Create Table query
 */
public class CreateTable implements QueryBase {

    private String tableName;
    private List<InternalColumn> columns;
    private String primaryKey;
    private List<String> indexes;
    private int rows;

    DavisBaseCatalogHandler catalog;

    public CreateTable(){
        this.catalog = new DavisBaseCatalogHandler();
    }

    @Override
    public QueryResult execute() {
        //Run any pre-req for the create

        try {
            catalog.createTable(DEFAULT_DATA_DIRNAME,tableName);
        }catch(Exception e){
            e.printStackTrace();
            //throw new DavidBaseError("Error while creating new table");
        }

        /*  Code to insert a row in the davisbase_tables table
         *  i.e. database catalog meta-data
         */

        catalog.updateSystemTablesTable(
                DEFAULT_CATALOG_DATABASENAME,
                tableName,
                2);


        /*  Code to insert rows in the davisbase_columns table
         *  for each column in the new table
         *  i.e. database catalog meta-data
         */

        int lastRowId = catalog.getLastRowId(
                DEFAULT_CATALOG_DATABASENAME,
                SYSTEM_COLUMNS_TABLENAME);

        catalog.updateSystemColumnsTable(
                DEFAULT_CATALOG_DATABASENAME,
                tableName,
                lastRowId,
                columns);


        return new QueryResult(1);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<InternalColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<InternalColumn> columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<String> indexes) {
        this.indexes = indexes;
    }
    public int getRows() {
        return rows;
    }
    
}
