package com.davidbase.utils;

import com.davidbase.model.DavidBaseValidationException;

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
    DATETIME("DATETIME",8,0x0A),
    DATE("DATE",8,0x0B),
    TEXT("TEXT",1,0x0C);

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

    public static DataType getTypeFromSerialCode(byte serialCode) throws DavidBaseValidationException {

        switch (serialCode){
            case 0x00: return NULL_TINYINT;
            case 0x01: return NULL_SMALLINT;
            case 0x02: return NULL_INT;
            case 0x03: return NULL_DOUBLE_DATE;
            case 0x04: return TINYINT;
            case 0x05: return SMALLINT;
            case 0x06: return INT;
            case 0x07: return BIGINT;
            case 0x08: return REAL;
            case 0x09: return DOUBLE;
            case 0x0A: return DATETIME;
            case 0x0B: return DATE;
            case 0x0C: return TEXT;
        }
        throw new DavidBaseValidationException("Unrecognized serial code");
    }

    public static DataType getTypeFromText(String typeText) throws DavidBaseValidationException {

        switch (typeText){
            case "TINYINT": return TINYINT;
            case "SMALLINT": return SMALLINT;
            case "INT": return INT;
            case "BIGINT": return BIGINT;
            case "REAL": return REAL;
            case "DOUBLE": return DOUBLE;
            case "DATETIME": return DATETIME;
            case "DATE": return DATE;
            case "TEXT": return TEXT;
        }
        throw new DavidBaseValidationException("Unrecognized data type");
    }
}
