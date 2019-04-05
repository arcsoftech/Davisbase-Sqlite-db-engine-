package com.davidbase.model;

/**
 * This class represents the Data Cell payload
 */
public class CellPayload {

    //1-byte TINYINT that indicates the number of columns n.
    private byte num_columns;

    //n-bytes which are Serial Type Codes, one for each of n columns
    private byte[] data_type;

    //binary column data. Unlike SQLite, NULL values occupy 1, 2, 4, or 8 bytes
    private byte[] data;

}

