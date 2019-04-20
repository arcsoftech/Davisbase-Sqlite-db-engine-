package com.davidbase.utils;

import com.davidbase.model.PageComponent.RawRecord;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.*;

import exceptions.InternalException;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;


/**
 * Class to read/write the Database catalog files.
 * Since the Catalog files are also treated as system tables they will be read/wrtten using DavisBaseFileHandler *
 */
public class DavisBaseCatalogHandler {
	
	public static final byte TABLES_TABLE_SCHEMA_ROWID = 0;
    public static final byte TABLES_TABLE_SCHEMA_DATABASE_NAME = 1;
    public static final byte TABLES_TABLE_SCHEMA_TABLE_NAME = 2;
    public static final byte TABLES_TABLE_SCHEMA_RECORD_COUNT = 3;
    public static final byte TABLES_TABLE_SCHEMA_COL_TBL_ST_ROWID = 4;
    public static final byte TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID = 5;


<<<<<<< HEAD
  
    
//    public static void InitializeDatabase() {
//        File baseDir = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME);
//        if(!baseDir.exists()) {
//            File catalogDir = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME);
//            if(!catalogDir.exists()) {
//                if(catalogDir.mkdirs()) {
//                    new DavisBaseCatalogHandler().createCatalogDatabase();
//                }
//            }
//        }
//    }
||||||| merged common ancestors
    public static final byte COLUMNS_TABLE_SCHEMA_ROWID = 0;
    public static final byte COLUMNS_TABLE_SCHEMA_DATABASE_NAME = 1;
    public static final byte COLUMNS_TABLE_SCHEMA_TABLE_NAME = 2;
    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_NAME = 3;
    public static final byte COLUMNS_TABLE_SCHEMA_DATA_TYPE = 4;
    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_KEY = 5;
    public static final byte COLUMNS_TABLE_SCHEMA_ORDINAL_POSITION = 6;
    public static final byte COLUMNS_TABLE_SCHEMA_IS_NULLABLE = 7;

    public static final String PRIMARY_KEY_IDENTIFIER = "PRI";
    
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
=======
    public boolean databaseExists(String databaseName){
        return false;
    }
>>>>>>> 995f522f010afee4c1b34b26479978f3a8a8f96a
    
    

    public boolean createCatalogDatabase() {
        try {
//            IOManager manager = new IOManager();
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_TABLES_TABLENAME + DavisBaseConstants.DEFAULT_FILE_EXTENSION);
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME + DavisBaseConstants.DEFAULT_FILE_EXTENSION);
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
        catch (InternalException e) {
            Utils.printMessage(e.getMessage());
        }
        return false;
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
               
               
            
                
//                record.getColumnValues().add(new DataType_Int(record.getRowID()));
//                record.getColumnValues().add(new DataType_Text(databaseName));
//                record.getColumnValues().add(new DataType_Text(tableName));
//                record.getColumnValues().add(new DataType_Text(columns.get(i).getName()));
//                record.getColumnValues().add(new DataType_Text(columns.get(i).getDataType()));
//                record.getColumnValues().add(new DataType_Text(columns.get(i).getStringIsPrimary()));
//                record.getColumnValues().add(new DataType_Int(i + 1));
//                record.getColumnValues().add(new DataType_Text(columns.get(i).getStringIsNullable()));
                record.setSize();
                if (!this.writeRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, record)) {
                    break;
                }
            }
            return true;
        }
        catch (InternalException e) {
            Utils.printMessage(e.getMessage());
        }
        return false;
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
            conditions.add(Condition.CreateCondition(DavisBaseCatalogHandler.TABLES_TABLE_SCHEMA_TABLE_NAME, Condition.EQUALS, DataType.TEXT, tableName));
            conditions.add(Condition.CreateCondition(DavisBaseCatalogHandler.TABLES_TABLE_SCHEMA_DATABASE_NAME, Condition.EQUALS, DataType.TEXT,databaseName));
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
                
                
                
//                record.getColumnValueList().add(new DataType_Int(record.getRowId()));
//                record.getColumnValueList().add(new DataType_Text(databaseName));
//                record.getColumnValueList().add(new DataType_Text(tableName));
//                record.getColumnValueList().add(new DataType_Int(0));
                if (lastRecord == null) {
                	
                	record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(1);
                    
                    record.getColumnType().add(DataType.INT);
                    record.getColumnValues().add(columnCount + 1);
                    
//                    record.getColumnValueList().add(new DataType_Int(1));
//                    record.getColumnValueList().add(new DataType_Int(columnCount + 1));
                } else {
                   
                	DataType_Int startingColumnIndex = (DataType_Int) lastRecord.getColumnValueList().get(CatalogDatabaseHelper.TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID);
                    returnValue = startingColumnIndex.getValue();
                    
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
                Utils.printMessage(String.format("Table '%s.%s' already exists.", databaseName, tableName));
                return -1;
            }
        }
        catch (InternalException e) {
            Utils.printMessage(e.getMessage());
            return -1;
        }
    }
    

    public Page<RawRecord> getLastRecordAndPage(String databaseName, String tableName) throws InternalException {
        try {
            File file = new File(this.getDatabasePath(databaseName) + "/" + tableName + DavisBaseConstants.DEFAULT_FILE_EXTENSION);
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
                Page<RawRecord> page = getRightmostLeafPage(file);
                if (page.getNumberOfCells() > 0) {
                    randomAccessFile.seek((DavisBaseConstants.PAGE_SIZE * page.getPageNumber()) + Page.getHeaderFixedLength() + ((page.getNumberOfCells() - 1) * Short.BYTES));
                    short address = randomAccessFile.readShort();
                    DataRecord record = readDataRecord(randomAccessFile, page.getPageNumber(), address);
                    if (record != null)
                        page.getPageRecords().add(record);
                }
                randomAccessFile.close();
                return page;
            } else {
                Utils.printMessage(String.format("Table '%s.%s' doesn't exist.", databaseName, tableName));
                return null;
            }
        }
        catch (InternalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InternalException(InternalException.GENERIC_EXCEPTION);
        }
    }
    
    
    private Page getRightmostLeafPage(File file) throws InternalException {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            Page page = readPageHeader(randomAccessFile, 0);
            while (page.getPageType() == Page.INTERIOR_TABLE_PAGE && page.getRightNodeAddress() != Page.RIGHTMOST_PAGE) {
                page = readPageHeader(randomAccessFile, page.getRightNodeAddress());
            }
            randomAccessFile.close();
            return page;
        } catch (Exception e) {
            throw new InternalException(InternalException.GENERIC_EXCEPTION);
        }
    }

    
    
    public boolean createTable(String databaseName, String tableName) throws InternalException {
        try {
            File dirFile = new File(Utils.getDatabasePath(databaseName));
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File file = new File(Utils.getDatabasePath(databaseName) + "/" + tableName);
            if (file.exists()) {
                return false;
            }
            if (file.createNewFile()) {
                RandomAccessFile randomAccessFile;
                Page<DataRecord> page = Page.createNewEmptyPage(new DataRecord());
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.setLength(Page.PAGE_SIZE);
                boolean isTableCreated = writePageHeader(randomAccessFile, page);
                randomAccessFile.close();
                return isTableCreated;
            }
            return false;
        } catch (InternalException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InternalException(InternalException.GENERIC_EXCEPTION);
        }
    }

    public boolean databaseExists(String databaseName){ 
    	File databaseDir = new File(this.getDatabasePath(databaseName));
         return  databaseDir.exists();     
    }
    
    public  String getDatabasePath(String databaseName) {
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
}
