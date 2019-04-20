package com.davidbase.model.PageComponent;

/**
 * This class represents the Data Cell payload
 */
public class CellPayload {

    //1-byte TINYINT that indicates the number of columns n.
    @Deprecated
    private byte num_columns;

    //n-bytes which are Serial Type Codes, one for each of n columns
    private byte[] data_type;

    //binary column data. Unlike SQLite, NULL values occupy 1, 2, 4, or 8 bytes
    private byte[] data;

    public CellPayload(byte num_columns, byte[] data_type, byte[] data) {
        this.num_columns = num_columns;
        this.data_type = data_type;
        this.data = data;
    }

    public byte getNum_columns() {
        return num_columns;
    }

    public void setNum_columns(byte num_columns) {
        this.num_columns = num_columns;
    }

    public byte[] getData_type() {
        return data_type;
    }

    public void setData_type(byte[] data_type) {
        this.data_type = data_type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

