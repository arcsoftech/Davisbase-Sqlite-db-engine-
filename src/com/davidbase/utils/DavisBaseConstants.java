package com.davidbase.utils;

public interface DavisBaseConstants {

	
	  public static final String PROMPT = "davissql> ";
	  public static final String VERSION = "v1.0";
	  public static final String COPYRIGHT = "©2019 Chris Irwin Davis";

	  public static final String DEFAULT_FILE_EXTENSION = ".tbl";
	  public static final String DEFAULT_DATA_DIRNAME = "data";
	  public static final String DEFAULT_CATALOG_DATABASENAME = "catalog";
	  public static final String SYSTEM_TABLES_TABLENAME = "davisbase_tables";
	  public static final  String SYSTEM_COLUMNS_TABLENAME = "davisbase_columns";
	    /*
	    * Page size for   is 512 bytes by default.
	    */
	    public static final long PAGE_SIZE = 512;

	    public static final String fileDir = "src/data/";
	    public static final String fileExt = ".tbl";
	    public static final byte RIGHT_MOST_LEAF = 0xFFFFFFFF;
	   
	    public static final byte LEAF_TABLE_PAGE = 0x0D;


}
