package com.davidbase.utils;

public interface DavisBaseConstants {

	
	  public static final String PROMPT = "davissql> ";
	  public static final String VERSION = "v1.0";
	  public static final String COPYRIGHT = "©2019 Chris Irwin Davis";

	  public static final String DEFAULT_DATA_DIRNAME = "data";
	  public static final String DEFAULT_CATALOG_DATABASENAME = "catalog";
	  public static final String SYSTEM_TABLES_TABLENAME = "davisbase_tables";
	  public static final String SYSTEM_COLUMNS_TABLENAME = "davisbase_columns";
	  
		public static final byte TABLES_TABLE_SCHEMA_ROWID = 0;
	    public static final byte TABLES_TABLE_SCHEMA_DATABASE_NAME = 1;
	    public static final byte TABLES_TABLE_SCHEMA_TABLE_NAME = 2;
	    public static final byte TABLES_TABLE_SCHEMA_RECORD_COUNT = 3;
	    public static final byte TABLES_TABLE_SCHEMA_COL_TBL_ST_ROWID = 4;
	    public static final byte TABLES_TABLE_SCHEMA_NXT_AVL_COL_TBL_ROWID = 5;

	  
	    public static final byte COLUMNS_TABLE_SCHEMA_ROWID = 0;
//	    public static final byte COLUMNS_TABLE_SCHEMA_DATABASE_NAME = 1;
	    public static final byte COLUMNS_TABLE_SCHEMA_TABLE_NAME = 1;
	    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_NAME = 3;
	    public static final byte COLUMNS_TABLE_SCHEMA_DATA_TYPE = 4;
	    public static final byte COLUMNS_TABLE_SCHEMA_COLUMN_KEY = 5;
	    public static final byte COLUMNS_TABLE_SCHEMA_ORDINAL_POSITION = 6;
	    public static final byte COLUMNS_TABLE_SCHEMA_IS_NULLABLE = 7;
	    /*
	    * Page size for   is 512 bytes by default.
	    */
	    public static final long PAGE_SIZE = 512;

	    public static final String FILE_EXT = ".tbl";
	    public static final byte RIGHT_MOST_LEAF = 0xFFFFFFFF;
	    public static final byte LEAF_TABLE_PAGE = 0x0D;


}
