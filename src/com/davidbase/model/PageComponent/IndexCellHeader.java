package com.davidbase.model.PageComponent;

/**
 * This class represent a Data Cell Header
 */
public class IndexCellHeader {

    //A 2-byte SMALLINT which is the total number of bytes of payload, i.e. excluding the 6-byte header
    private short payload_size;


    public IndexCellHeader() {
    }

    public IndexCellHeader(short payload_size, int row_id) {
        this.payload_size = payload_size;
    }

    public short getPayload_size() {
        return payload_size;
    }

    public void setPayload_size(short payload_size) {
        this.payload_size = payload_size;
    }


    public static short getSize(){
        return (short)(Short.BYTES + Integer.BYTES);
    }
}
