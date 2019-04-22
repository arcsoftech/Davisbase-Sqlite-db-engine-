package com.davidbase.model.PageComponent;

/**
 * This class represent a Data Cell Header
 */
public class CellHeader {

    //A 2-byte SMALLINT which is the total number of bytes of payload, i.e. excluding the 6-byte header
    private short payload_size;

    // A 4-byte INT which is the integer key, a.k.a. "rowid"
    private int row_id;

    public CellHeader() {
    }

    public CellHeader(short payload_size, int row_id) {
        this.payload_size = payload_size;
        this.row_id = row_id;
    }

    public short getPayload_size() {
        return payload_size;
    }

    public void setPayload_size(short payload_size) {
        this.payload_size = payload_size;
    }

    public int getRow_id() {
        return row_id;
    }

    public void setRow_id(int row_id) {
        this.row_id = row_id;
    }

    public static short getSize(){
        return (short)(Short.BYTES + Integer.BYTES);
    }
}
