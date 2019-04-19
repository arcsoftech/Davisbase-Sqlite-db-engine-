package com.davidbase.utils;

public class DavidBaseConstants {

    public static final String prompt = "davisql> ";
    public static final String version = "v1.0b(example)";
    public static final String copyright = "©2016 Chris Irwin Davis";

    public static final String defaultDataDirectory = "data";
    public static final String defaultCatalogDatabaseName = "catalog";
    public static final String defaultCatalogTableName = "davisbase_tables";
    public static final String defaultCatalogColumnName = "davisbase_columns";
    /*
    * Page size for alll files is 512 bytes by default.
    */
    public static final long pageSize = 512;

    public static final String fileDir = "src/data/";
    public static final String fileExt = ".tbl";
    public static final int rightMostLeaf = 0xFFFFFFFF;
}
