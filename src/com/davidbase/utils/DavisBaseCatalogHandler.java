package com.davidbase.utils;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.RawRecord;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.*;

import exceptions.InternalException;
import java.util.HashMap;
import com.davidbase.model.PageComponent.InternalColumn;
import java.util.List;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import static com.davidbase.utils.DavisBaseConstants.FILE_EXT;


/**
 * Class to read/write the Database catalog files.
 * Since the Catalog files are also treated as system tables they will be read/written using DavisBaseFileHandler *
 */
public class DavisBaseCatalogHandler {

    private DavisBaseFileHandler filehandler;

	public DavisBaseCatalogHandler(){
	    filehandler = new DavisBaseFileHandler();
    }
	  
	public static void InitializeDatabase() {
	        File baseDir = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME);
	        if(!baseDir.exists()) {
	            File catalogDir = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME);
	            if(!catalogDir.exists()) {
	                if(catalogDir.mkdirs()) {
	                    new DavisBaseCatalogHandler().createCatalogDatabase();
	                }
	            }
	        }

	    }


    public boolean createCatalogDatabase() {
        try {
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME);
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME);
            int startingRowId = this.updateSystemTablesTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME, 6);
            startingRowId *= this.updateSystemTablesTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, 8);
            if (startingRowId >= 0) {
                List<InternalColumn> columns = new ArrayList<>();
                columns.add(new InternalColumn("rowid", DataType.INT, false, false));
                columns.add(new InternalColumn("database_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("record_count", DataType.INT, false, false));
                columns.add(new InternalColumn("col_tbl_st_rowid", DataType.INT, false, false));
                columns.add(new InternalColumn("nxt_avl_col_tbl_rowid", DataType.INT, false, false));
                this.updateSystemColumnsTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME, 1, columns);
                columns.clear();
                columns.add(new InternalColumn("rowid", DataType.INT, false, false));
                columns.add(new InternalColumn("database_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("column_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("data_type", DataType.TEXT, false, false));
                columns.add(new InternalColumn("column_key", DataType.TEXT, false, false));
                columns.add(new InternalColumn("ordinal_position", DataType.TINYINT, false, false));
                columns.add(new InternalColumn("is_nullable", DataType.TEXT, false, false));
                this.updateSystemColumnsTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, 7, columns);
            }
            return true;
        }
        catch (Exception e) {
            throw new DavidBaseError("Error");
        }
        //return false;
    }
    

    public boolean updateSystemColumnsTable(String databaseName, String tableName, int startingRowId, List<InternalColumn> columns) {
        try {
        /*
         * System Tables Table Schema:
         * Column_no    Name                                    Data_type
         *      1       rowid                                   INT
         *      2       database_name                           TEXT
         *      3       table_name                              TEXT
         *      4       column_name                             TEXT
         *      5       data_type                               TEXT
         *      6       column_key                              TEXT
         *      7       ordinal_position                        TINYINT
         *      8       is_nullable                             TEXT
         */
//            IOManager manager = new IOManager();
            if (columns != null && columns.size() == 0) return false;
            int i = 0;
            for (; i < columns.size(); i++) {
                RawRecord record = new RawRecord();
                record.setRowID(startingRowId++);
                
               record.getColumnType().add(DataType.INT);
               record.getColumnValues().add(record.getRowID());
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(databaseName);
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(tableName);
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(columns.get(i).getName());
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(columns.get(i).getDataType());
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(columns.get(i).getStringIsPrimary());
               
               record.getColumnType().add(DataType.INT);
               record.getColumnValues().add(i + 1);
               
               record.getColumnType().add(DataType.TEXT);
               record.getColumnValues().add(columns.get(i).getStringIsNullable());
               
                record.setSize();
                if (!this.writeRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, record)) {
                    break;
                }
            }
            return true;
        }
        catch (Exception e) {
            throw new DavidBaseError("Error");
        }
       // return false;
    }
    
    
    public int updateSystemTablesTable(String databaseName, String tableName, int columnCount) {
        try {
        /*
         * System Tables Table Schema:
         * Column_no    Name                                    Data_type
         *      1       rowid                                   INT
         *      2       database_name                           TEXT
         *      3       table_name                              TEXT
         *      4       record_count                            INT
         *      5       col_tbl_st_rowid                        INT
         *      6       nxt_avl_col_tbl_rowid                   INT
         */
//            IOManager manager = new IOManager();
            List<Condition> conditions = new ArrayList<>();
            conditions.add(Condition.CreateCondition(DavisBaseConstants.TABLES_TABLE_SCHEMA_TABLE_NAME, Condition.EQUALS, DataType.TEXT, tableName));
            conditions.add(Condition.CreateCondition(DavisBaseConstants.TABLES_TABLE_SCHEMA_DATABASE_NAME, Condition.EQUALS, DataType.TEXT,databaseName));
            List<RawRecord> result = this.findRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME, conditions, true);
            if (result != null && result.size() == 0) {
                int returnValue = 1;
                Page<RawRecord> page = this.getLastRecordAndPage(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME);
                //Check if record exists
               RawRecord lastRecord = null;
                if (page.getCells().size() > 0) {
                    lastRecord = page.getCells().get(0);
                }
                RawRecord record = new RawRecord();
                if (lastRecord == null) {
                    record.setRowID(1);
                } else {
                    record.setRowID(lastRecord.getRowID() + 1);
                }
                
                
                record.getColumnType().add(DataType.INT);
                record.getColumnValues().add(record.getRowID());
                
                record.getColumnType().add(DataType.TEXT);
                record.getColumnValues().add(databaseName);
                
                record.getColumnType().add(DataType.TEXT);
                record.getColumnValues().add(tableName);
                
                record.getColumnType().add(DataType.INT);
                record.getColumnValues().add(0);
;
                if (lastRecord == null) {
                	
                	record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(1);
                    
                    record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(columnCount + 1);

                } else {
                   
                	int startingColumnIndex = (Integer)lastRecord.getColumnValues().get(DavisBaseConstants.TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID);
                   
                	returnValue = startingColumnIndex;
                    
                    record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(returnValue);
                    
                    record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(returnValue + columnCount);
                    
//                    record.getColumnValueList().add(new DataType_Int(returnValue));
//                    record.getColumnValueList().add(new DataType_Int(returnValue + columnCount));
                }
                
                record.setSize();
                this.writeRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME, record);
                return returnValue;
            } else {
//                Utils.printMessage(String.format("Table '%s.%s' already exists.", databaseName, tableName));
                throw new DavidBaseError("Error");
                //return -1;
            }
        }
        catch (InternalException e) {
//            Utils.printMessage(e.getMessage());
            throw new DavidBaseError("Error");
           // return -1;
        }
    }
    

    public Page<RawRecord> getLastRecordAndPage(String databaseName, String tableName) throws InternalException {
        
    return null;
    }
    
    
    private Page getRightmostLeafPage(File file) throws Exception {
       	
        return null;
    }


   
    
    public boolean createTable(String databaseName, String tableName){
        try {
            File dirFile = new File(this.getDatabasePath(databaseName));
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File file = new File(this.getDatabasePath(databaseName) + "/" + tableName+FILE_EXT);
            if (file.exists()) {
                return false;
            }
            if (file.createNewFile()) {
                RandomAccessFile randomAccessFile;
                Page<LeafCell> page = Page.createNewEmptyPage();
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.setLength(DavisBaseConstants.PAGE_SIZE);
                boolean isTableCreated = filehandler.writeFirstPageHeader(randomAccessFile, page);
                randomAccessFile.close();
                return isTableCreated;
            }
            return false;
        }
        catch (Exception e) {
            throw new DavidBaseError(e);
        }
    }
    
    
    public boolean databaseExists(String databaseName){ 
    	File databaseDir = new File(this.getDatabasePath(databaseName));
         return  databaseDir.exists();     
    }
    
    public static String getDatabasePath(String databaseName) {
        return DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + databaseName;
    }

    public boolean tableExists(String databaseName, String tableName) {
        return false;
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
    public List<RawRecord> findRecord(String databaseName, String tableName, List<Condition> conditionList, boolean getOne)  {
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

    public static void main(String[] args){
        DavisBaseCatalogHandler ctlg = new DavisBaseCatalogHandler();
        ctlg.createTable("db1","test2");
    }
}
