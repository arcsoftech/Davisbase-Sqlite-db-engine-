package com.davidbase.model.PageComponent;

import com.davidbase.utils.DataType;

import java.util.List;

/**
 * Record to be used for insert/update to a file.
 */
public class RawRecord {

    List<String> column;
    List<DataType> columnType;
    List<Object> columnValue;
    int rowID;
    byte[] sizeOfCol;
    short totSize;
    byte[] coleVal;

    public RawRecord() {
        this.rowID = -1;
        this.totSize = 0;
    }

    public RawRecord(List<String> column, List<DataType> columnType, List<Object> columnValue, int rowID) {
        this.column = column;
        this.columnType = columnType;
        this.columnValue = columnValue;
        this.rowID = rowID;
        setSize();
        setValues();
    }

    public List<String> getColumns() {
        return column;
    }

    public void setColumns(List<String> column) {
        this.column = column;
        setSize();
    }

    public List<Object> getColumnValues() {
        return columnValue;
    }

    public void setColumnValues(List<Object> columnValue) {
        this.columnValue = columnValue;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public byte[] getSizeOfCol() {
        return sizeOfCol;
    }

    public short getTotSize() {
        return totSize;
    }

    public byte[] getColeVal() {
        return coleVal;
    }

    public void setSize() {
        for(int i=0;i<columnValue.size();i++){
            if(columnType.get(i)==DataType.TEXT)
                this.sizeOfCol[i]= (byte)( columnType.get(i).getSize()*
                        (columnValue.get(i)!=null? String.valueOf(columnValue.get(i)).length():0));
            else
                this.sizeOfCol[i]=columnType.get(i).getSize();
            this.totSize+=this.sizeOfCol[i];
        }
    }

    private void setValues() {
        for(int i=0;i<columnValue.size();i++){
            this.coleVal[i]=Byte.valueOf(String.valueOf(columnValue.get(i)));
        }
    }
}
