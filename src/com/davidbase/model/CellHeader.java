package com.davidbase.model;

/**
 * This class represent a Data Cell Header
 */
public class CellHeader {

    //A 2-byte SMALLINT which is the total number of bytes of payload, i.e. excluding the 6-byte header
    private short payload_size;

    // A 4-byte INT which is the integer key, a.k.a. "rowid"
    private int row_id;

}
