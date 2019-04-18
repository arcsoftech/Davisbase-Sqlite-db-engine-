package com.davidbase.model;

import java.util.List;

/**
 * Record to be used for insert/update to a file.
 */
public class RawRecord {

    List<String> columns;
    List<String> columnValues;
    int rowID;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(List<String> columnValues) {
        this.columnValues = columnValues;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }
}
