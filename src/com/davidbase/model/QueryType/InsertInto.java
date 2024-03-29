package com.davidbase.model.QueryType;

import com.davidbase.model.PageComponent.LeafCell;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseFileHandler;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.utils.DataType;

import static com.davidbase.utils.DavisBaseConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InsertInto implements QueryBase {
    private String databaseName;
    private String tableName;
    private List<String> columns;
    private List<String> columnValues;

    DavisBaseCatalogHandler catalog =  new DavisBaseCatalogHandler();

    public InsertInto(String databaseName, String tableName, List<String> columns, List<String> columnsValues) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columns = columns;
        this.columnValues = columnsValues;

    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the columnValues
     */
    public List<String> getColumnValues() {
        return columnValues;
    }

    /**
     * @param columnValues the columnValues to set
     */
    public void setColumnValues(List<String> columnValues) {
        this.columnValues = columnValues;
    }

    /**
     * @return the columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
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

    @Override
    public QueryResult execute() {
        // Run any pre-req for the create

        try {
            // map passed values to records ready to be inserted into file
        LeafCell record = prepareRecord();
        int lastRowId = catalog.getLastRowId(DEFAULT_DATA_DIRNAME, tableName);
        record.getHeader().setRow_id(++lastRowId);
        List<Object> colVal = new ArrayList<>();
        List<DataType> colValTypes = new ArrayList<>();
        HashMap<String,DataType> columnDataTypeMap = catalog.fetchAllTableColumnDataTypes(DEFAULT_DATA_DIRNAME,tableName);
        colValTypes.add(DataType.INT);
        colVal.add(record.getHeader().getRow_id());
        for (int i = 0;i<columnValues.size();i++)
        {
            colValTypes.add(columnDataTypeMap.get(columns.get(i)));
            colVal.add(columnValues.get(i));
        }
        record.getPayload().setColTypes(colValTypes);
        record.getPayload().setColValues(colVal);
        record.initializeLeafForWrite();

        new DavisBaseFileHandler().writeLeafCell(DEFAULT_DATA_DIRNAME, tableName, record);

        } catch (Exception e) {
            e.printStackTrace();
            // throw new DavidBaseError("Error while creating new table");
        }

        /*
         * Code to insert a row in the davisbase_tables table i.e. database catalog
         * meta-data
         */
     /*
         * Code to insert rows in the davisbase_columns table for each column in the new
         * table i.e. database catalog meta-data
         */

        return new QueryResult(1);

        
    }

    private LeafCell prepareRecord() {
        LeafCell recordsToFile = new LeafCell();
        return recordsToFile;
    }
}
