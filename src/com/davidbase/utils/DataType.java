package com.davidbase.utils;

import com.davidbase.model.DavidBaseValidationException;

public enum DataType {
    NULL("NULL",0,0x00),
    TINYINT("TINYINT",1,0x01),
    SMALLINT("SMALLINT",2,0x02),
    INT("INT",4,0x03),
    BIGINT("BIGINT",8,0x04),
    REAL("REAL",8,0x05),
    YEAR("YEAR",1,0x06),
    TIME("TIME",4,0x08),
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

    public static DataType getTypeFromSerialCode(byte serialCode)  {

        switch (serialCode){
            case 0x00: return NULL;
            case 0x01: return TINYINT;
            case 0x02: return SMALLINT;
            case 0x03: return INT;
            case 0x04: return BIGINT;
            case 0x05: return REAL;
            case 0x06: return YEAR;
            case 0x08: return TIME;
            case 0x0A: return DATETIME;
            case 0x0B: return DATE;
            default: return TEXT;
        }
    }

    public static DataType getTypeFromText(String typeText) throws DavidBaseValidationException {

        switch (typeText){
            case "TINYINT": return TINYINT;
            case "SMALLINT": return SMALLINT;
            case "INT": return INT;
            case "BIGINT": return BIGINT;
            case "REAL": return REAL;
            case "NULL": return NULL;
            case "DATETIME": return DATETIME;
            case "DATE": return DATE;
            case "TEXT": return TEXT;
            case "TIME": return TIME;
            case "YEAR": return YEAR;
        }
        throw new DavidBaseValidationException("Unrecognized data type");
    }
}
