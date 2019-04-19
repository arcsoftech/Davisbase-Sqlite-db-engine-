package com.davidbase.utils;

import com.davidbase.utils.DataType;
import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.model.RawRecord;

import java.util.HashMap;
import java.util.List;

/**
 * Class to read/write the Database catalog files.
 * Since the Catalog files are also treated as system tables they will be read/wrtten using DavisBaseFileHandler *
 */
public class DavisBaseCatalogHandler {

    public boolean databaseExists(String databaseName){
        return true;
    }

    public static String getDatabasePath(String databaseName) {
        return DavisBaseConstants.defaultDataDirectory + "/" + databaseName;
    }

    public boolean tableExists(String databaseName, String tableName) {
        return true;
    }

    public List<String> fetchAllTableColumns(String databaseName, String tableName){
        return null;
    }

    public boolean checkNullConstraint(String databaseName, String tableName, HashMap<String, Integer> columnMap){
        return true;
    }

    public HashMap<String, DataType> fetchAllTableColumnDataTypes(String databaseName, String tableName){
        return null;
    }

    public String getTablePrimaryKey(String databaseName, String tableName){
        return null;
    }

    public int getTableRecordCount(String databaseName, String tableName){
        return 0;
    }

    public boolean checkIfValueForPrimaryKeyExists(String databaseName, String tableName, int value){
        return true;
    }

    /**
     * can create multiple copies of the this function as you need (overloading)
     * delegate call to DavisBaseFileHandler.findRecord
     * @param databaseName
     * @param tableName
     * @param conditionList
     * @param getOne
     * @return
     */
    public List findRecord(String databaseName, String tableName, List conditionList, boolean getOne)  {
        return null;
    }

    /**
     * can create multiple copies of the this function as you need (overloading)
     * delegate call to DavisBaseFileHandler.findRecord
     * @param databaseName
     * @param tableName
     * @param record
     * @return
     */
    public boolean writeRecord(String databaseName, String tableName, RawRecord record){
        return true;
    }
}
