package com.davidbase.utils;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.PageComponent.RawRecord;
import com.davidbase.model.PageComponent.*;
import com.davidbase.model.QueryType.*;

import exceptions.InternalException;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import static com.davidbase.utils.DavisBaseConstants.*;

/**
 * Class to read/write the Database catalog files. Since the Catalog files are
 * also treated as system tables they will be read/written using
 * DavisBaseFileHandler *
 */
public class DavisBaseCatalogHandler {

    private DavisBaseFileHandler filehandler;

    public DavisBaseCatalogHandler() {
        filehandler = new DavisBaseFileHandler();
    }

    public static void initialize() {
        File baseDir = new File(DavisBaseConstants.DEFAULT_DATA_DIRNAME);
        if (!baseDir.exists()) {
            File catalogDir = new File(
                    DavisBaseConstants.DEFAULT_DATA_DIRNAME + "/" + DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME);
            if (!catalogDir.exists()) {
                if (catalogDir.mkdirs()) {
                    new DavisBaseCatalogHandler().createCatalogDatabase();
                }
            }
        }

    }

    public boolean createCatalogDatabase() {
        try {
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                    DavisBaseConstants.SYSTEM_TABLES_TABLENAME);
            this.createTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                    DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME);
            int startingRowId = this.updateSystemTablesTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                    DavisBaseConstants.SYSTEM_TABLES_TABLENAME, 2);
             startingRowId *=
             this.updateSystemTablesTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
             DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, 7);
            if (startingRowId >= 0) {
                List<InternalColumn> columns = new ArrayList<>();
                columns.add(new InternalColumn("rowid", DataType.INT, false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT, false, false));
                // columns.add(new InternalColumn("record_count", DataType.INT, false, false));
                // columns.add(new InternalColumn("col_tbl_st_rowid", DataType.INT, false,
                // false));
                // columns.add(new InternalColumn("nxt_avl_col_tbl_rowid", DataType.INT, false,
                // false));
                this.updateSystemColumnsTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, 1, columns);
                columns.clear();
                columns.add(new InternalColumn("rowid", DataType.INT, false, false));
                columns.add(new InternalColumn("table_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("column_name", DataType.TEXT, false, false));
                columns.add(new InternalColumn("data_type", DataType.TEXT, false, false));
                columns.add(new InternalColumn("ordinal_position", DataType.TINYINT, false, false));
                columns.add(new InternalColumn("primary_key", DataType.TEXT, false, false));
                columns.add(new InternalColumn("is_nullable", DataType.TEXT, false, false));
                this.updateSystemColumnsTable(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                        DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, 7, columns);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError("Error");
        }
        // return false;
    }

    public boolean updateSystemColumnsTable(String databaseName, String tableName, int startingRowId,
            List<InternalColumn> columns) {
        try {
            /*
             * System Tables Table Schema: Column_no Name Data_type 1 rowid INT 2
             * database_name TEXT 3 table_name TEXT 4 column_name TEXT 5 data_type TEXT 6
             * column_key TEXT 7 ordinal_position TINYINT 8 is_nullable TEXT
             */
            // IOManager manager = new IOManager();
            if (columns != null && columns.size() == 0)
                return false;
            int i = 0;
            for (; i < columns.size(); i++) {
                LeafCell record = new LeafCell();
                record.getHeader().setRow_id(startingRowId++);

                List<Object> colVal = new ArrayList<>();
                List<DataType> colValTypes = new ArrayList<>();

                colValTypes.add(DataType.INT);
                colVal.add(startingRowId);

                colValTypes.add(DataType.TEXT);
                colVal.add(tableName);

                colValTypes.add(DataType.TEXT);
                colVal.add(columns.get(i).getName());

                colValTypes.add(DataType.TEXT);
                colVal.add(columns.get(i).getDataType().getDataTypeName());

                colValTypes.add(DataType.INT);
                colVal.add(i+1);

                colValTypes.add(DataType.TEXT);
                colVal.add(columns.get(i).getStringIsPrimary());

                colValTypes.add(DataType.TEXT);
                colVal.add(columns.get(i).getStringIsNullable());

                record.getPayload().setColTypes(colValTypes);
                record.getPayload().setColValues(colVal);
                record.initializeLeafForWrite();
                record.getPayload().setColTypes(colValTypes);
                if (!filehandler.writeLeafCell(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                        DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, record)) {
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError("Error");
        }
        // return false;
    }

    public int updateSystemTablesTable(String databaseName, String tableName, int columnCount) {
        try {
            /*
             * System Tables Table Schema: Column_no Name Data_type 1 rowid INT 2 table_name
             * TEXT
             */
            List<Condition> conditions = new ArrayList<>();
            conditions.add(Condition.CreateCondition(DavisBaseConstants.TABLES_TABLE_SCHEMA_TABLE_NAME,
                    Condition.EQUALS, DataType.TEXT, tableName));
            conditions.add(Condition.CreateCondition(DavisBaseConstants.TABLES_TABLE_SCHEMA_DATABASE_NAME,
                    Condition.EQUALS, DataType.TEXT, databaseName));
            List<LeafCell> result = filehandler.findRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                    DavisBaseConstants.SYSTEM_TABLES_TABLENAME, conditions, true);
            if (result != null && result.size() == 0) {
                int returnValue = 1;
                Page<LeafCell> page = this.getLastRecordAndPage(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                        DavisBaseConstants.SYSTEM_TABLES_TABLENAME);
                // Check if record exists
                LeafCell lastRecord = null;
                if (page.getCells().size() > 0) {
                    lastRecord = page.getCells().get(0);
                }
                LeafCell newLeaf = new LeafCell();
                if (lastRecord == null) {
                    newLeaf.getHeader().setRow_id(1);
                } else {
                    newLeaf.getHeader().setRow_id(lastRecord.getHeader().getRow_id() + 1);
                }
               

                List<DataType> colTypes = new ArrayList<>();
                colTypes.add(DataType.INT);
                colTypes.add(DataType.TEXT);

                List<Object> colValues = new ArrayList<>();
                colValues.add(newLeaf.getHeader().getRow_id());
                // colValues.add(databaseName);
                System.out.println(tableName);
                colValues.add(tableName);
                // colValues.add(0);
                ;
                // if (lastRecord == null) {
                //
                // colTypes.add(DataType.INT);
                // colValues.add(1);
                //
                // colTypes.add(DataType.INT);
                // colValues.add(columnCount + 1);
                //
                // } else {
                //
                // int startingColumnIndex =
                // (Integer)lastRecord.getPayload().getColValues().get(DavisBaseConstants.TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID);
                //
                // returnValue = startingColumnIndex;
                //
                // colTypes.add(DataType.INT);
                // colValues.add(returnValue);
                //
                // colTypes.add(DataType.INT);
                // colValues.add(returnValue + columnCount);
                //
                //// record.getColumnValueList().add(new DataType_Int(returnValue));
                //// record.getColumnValueList().add(new DataType_Int(returnValue +
                // columnCount));
                // }
                newLeaf.getPayload().setColTypes(colTypes);
                newLeaf.getPayload().setColValues(colValues);
                newLeaf.initializeLeafForWrite();
                filehandler.writeLeafCell(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME,
                        DavisBaseConstants.SYSTEM_TABLES_TABLENAME, newLeaf);
                return returnValue;
            } else {
                throw new DavidBaseError("Error: Table already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DavidBaseError("Error");
        }
    }

    public Page<LeafCell> getLastRecordAndPage(String databaseName, String tableName) {
//        try {
//            File file = new File(getDatabasePath(databaseName) + "/" + tableName + FILE_EXT);
//            if (file.exists()) {
//                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//                Page<LeafCell> page = filehandler.getRightmostLeafPage(file);
//                if (page.getPageheader().getNum_cells() > 0) {
//                    randomAccessFile.seek((PAGE_SIZE * page.getPageheader().getPage_number())
//                            + Page.getHeaderFixedLength() + ((page.getPageheader().getNum_cells() - 1) * Short.BYTES));
//                    short address = randomAccessFile.readShort();
//                    LeafCell dataCell = filehandler.readLeaf(randomAccessFile, page.getPageheader().getPage_number(),
//                            address);
//                    if (dataCell != null)
//                        page.getCells().add(dataCell);
//                }
//                randomAccessFile.close();
//                return page;
//            } else {
//                throw new DavidBaseError("Table doesn't exist." + databaseName + " " + tableName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new DavidBaseError(e.getMessage());
//        }
    	return null;
    }

    public boolean createTable(String databaseName, String tableName) {
        try {
            File dirFile = new File(getDatabasePath(databaseName));
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File file = new File(getDatabasePath(databaseName) + "/" + tableName + FILE_EXT);
            if (file.exists()) {
                return false;
            }
            if (file.createNewFile()) {
                RandomAccessFile randomAccessFile;
                Page<LeafCell> page = Page.createNewEmptyPage();
                randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.setLength(PAGE_SIZE);
                boolean isTableCreated = filehandler.writeFirstPageHeader(randomAccessFile, page);
                randomAccessFile.close();
                return isTableCreated;
            }
            return false;
        } catch (Exception e) {
            throw new DavidBaseError(e);
        }
    }

    public static boolean databaseExists(String databaseName) {
        return DavisBaseFileHandler.databaseExists(databaseName);
    }

    public static String getDatabasePath(String databaseName) {
        return DavisBaseFileHandler.getDatabasePath(databaseName);
    }

    public boolean tableExists(String databaseName, String tableName) {
    	
    	   File file = new File(getDatabasePath(DEFAULT_DATA_DIRNAME) + "/" + tableName + FILE_EXT);
    	   
    	   return file.exists();
    		
    }

    public List<String> fetchAllTableColumns(String databaseName, String tableName){
    	
  	  
    	List<String> columnNames = new ArrayList<>();
          List<Condition> conditions = new ArrayList<>();
//          conditions.add(Condition.CreateCondition(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, Condition.EQUALS, DataType.TEXT ,databaseName));
          conditions.add(Condition.CreateCondition(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_TABLE_NAME, Condition.EQUALS, DataType.TEXT,tableName));

          List<LeafCell> records = filehandler.findRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions,null, false);

          for (LeafCell record : records) {
        	  
//        	  System.out.print(record.getPayload().getColValues().get(2) + "\n");
        	  
//              Object object = record.getColumns().get(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);
//              columnNames.add(((String) object));
          }

          return columnNames;
   
    }

    public boolean checkNullConstraint(String databaseName, String tableName, HashMap<String, Integer> columnMap) {
    	
    	
    	
    	
        return true;
    }

    public HashMap<String, String> fetchAllTableColumnDataTypes(String databaseName, String tableName) {
    	
    	List<Condition> conditions = new ArrayList<>();
//        conditions.add(InternalCondition.CreateCondition(CatalogDatabaseHelper.COLUMNS_TABLE_SCHEMA_DATABASE_NAME, InternalCondition.EQUALS, new DataType_Text(databaseName)));
        conditions.add(Condition.CreateCondition(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_TABLE_NAME, Condition.EQUALS, DataType.TEXT,tableName));

        List<LeafCell> records = filehandler.findRecord(DavisBaseConstants.DEFAULT_CATALOG_DATABASENAME, DavisBaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, false);
        HashMap<String, String> columDataTypeMapping = new HashMap<>();

        for (LeafCell record : records) {
            Object object = record.getPayload().getColValues().get(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_COLUMN_NAME);
            Object dataTypeObject = record.getPayload().getColValues().get(DavisBaseConstants.COLUMNS_TABLE_SCHEMA_DATA_TYPE);

            String columnName = ((String) object);
            String columnDataType = ((String)dataTypeObject );
            columDataTypeMapping.put(columnName.toLowerCase(), columnDataType);
        }

        System.out.print(columDataTypeMapping);
        return columDataTypeMapping;
    }

    public String getTablePrimaryKey(String databaseName, String tableName) {
        return null;
    }

    public int getTableRecordCount(String databaseName, String tableName) {
        return 0;
    }

    public boolean checkIfValueForPrimaryKeyExists(String databaseName, String tableName, int value) {
        return true;
    }

    public int getLastRowId(String databasename, String tableName) {
        //Get the Last record in last leaf
        Page<LeafCell> lastPage = getLastRecordAndPage(databasename,tableName);
        if(lastPage!=null && lastPage.getCells()!=null){
            return lastPage.getCells().get(0).getHeader().getRow_id();
        }
        throw new DavidBaseError("table does not exist, no row id found");
    }

    public static void main(String[] args) {
        DavisBaseCatalogHandler ctlg = new DavisBaseCatalogHandler();
        ctlg.createTable("db1", "test2");
        ctlg.fetchAllTableColumns("db1", "davisbase_columns");
    }
}
