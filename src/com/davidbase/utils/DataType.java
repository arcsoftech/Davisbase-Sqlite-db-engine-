package com.davidbase.utils;

public enum DataType {
    NULL_TINYINT("NULL",1,0x00),
    NULL_SMALLINT("NULL",2,0x01),
    NULL_INT("NULL",4,0x02),
    NULL_DOUBLE_DATE("NULL",8,0x03),
    TINYINT("TINYINT",1,0x04),
    SMALLINT("SMALLINT",2,0x05),
    INT("INT",4,0x06),
    BIGINT("BIGINT",8,0x07),
    REAL("REAL",4,0x08),
    DOUBLE("DOUBLE",8,0x09),
    DATETIME("DOUBLE",8,0x0A),
    DATE("DOUBLE",8,0x0B),
    TEXT("DOUBLE",1,0x0C);

    private byte size;
    private byte serialCode;
    private String dataTypeName;

    DataType(String dataTypeName, byte size, byte serialCode){
        this.dataTypeName = dataTypeName;
        this.size = size;
        this.serialCode = serialCode;
    }

    DataType(String dataTypeName, int size, int serialCode){
        this.dataTypeName = dataTypeName;
        this.size = (byte) size;
        this.serialCode = (byte) serialCode;
    }

    public byte getSize() {
        return size;
    }

    public byte getSerialCode() {
        return serialCode;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }
}
