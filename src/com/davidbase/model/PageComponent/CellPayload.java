package com.davidbase.model.PageComponent;

import com.davidbase.utils.DataType;

import java.util.List;

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

    // not serializable
    private List<Object> colValues;
    private List<DataType> colTypes;
    private short payloadSize;

    public CellPayload() {
    }

    public CellPayload(byte num_columns, byte[] data_type) {
        this.num_columns = num_columns;
        this.data_type = data_type;
    }

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

    public List<Object> getColValues() {
        return colValues;
    }

    public void setColValues(List<Object> colValues) {
        this.colValues = colValues;
    }

    public List<DataType> getColTypes() {
        return colTypes;
    }

    public void setColTypes(List<DataType> colTypes) {
        this.colTypes = colTypes;
    }

    public void generateByteData(){
        this.data_type = new byte[colTypes.size()];
        fillSizeArray();

        this.data = new byte[colTypes.size()];
        fillDataArray();
    }

    public void fillSizeArray() {
        for(int i=0;i<colTypes.size();i++){
            if(colTypes.get(i)==DataType.TEXT)
                this.data_type[i]= (byte)( colTypes.get(i).getSize()*
                        (colValues.get(i)!=null? String.valueOf(colValues.get(i)).length():0));
            else
                this.data_type[i]=colTypes.get(i).getSize();
            this.payloadSize+=this.data_type[i];
        }
    }

    private void fillDataArray() {
        for(int i=0;i<colTypes.size();i++){
            this.data[i]=Byte.valueOf(String.valueOf(colValues.get(i)));
        }
    }

    public short getPayloadSize() {
        return payloadSize;
    }

    public void setPayloadSize(short payloadSize) {
        this.payloadSize = payloadSize;
    }
}

