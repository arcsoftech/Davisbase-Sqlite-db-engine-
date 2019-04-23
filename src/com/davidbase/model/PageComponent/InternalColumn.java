package com.davidbase.model.PageComponent;

import com.davidbase.utils.DataType;

public class InternalColumn {
	 private int index;

	    private Object value;

	    private String name;

	    private DataType dataType;

	    private boolean isPrimary;

	    private boolean isNullable;

	    private byte ordinalPosition;

	    public InternalColumn() {

	    }

	    public InternalColumn(String name, DataType dataType, boolean isPrimary, boolean isNullable) {
	        this.name = name;
	        this.dataType = dataType;
	        this.isPrimary = isPrimary;
	        this.isNullable = isNullable;
	    }

	    public int getIndex() {
	        return index;
	    }

	    public void setIndex(int index) {
	        this.index = index;
	    }

	    public Object getValue() {
	        return value;
	    }

	    public void setValue(Object value) {
	        this.value = value;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public DataType getDataType() {
	        return dataType;
	    }

	    public void setDataType(DataType dataType) {
	        this.dataType = dataType;
	    }

	    public boolean isPrimary() {
	        return isPrimary;
	    }

	    public void setPrimary(boolean primary) {
	        isPrimary = primary;
	    }

	    public String getStringIsPrimary() {
	        return isPrimary ? "YES" : "NO";
	    }

	    public boolean isNullable() {
	        return isNullable;
	    }

	    public void setNullable(boolean nullable) {
	        isNullable = nullable;
	    }

	    public String getStringIsNullable() {
	        return isNullable ? "YES" : "NO";
	    }

	    public byte getOrdinalPosition() {
	        return ordinalPosition;
	    }

	    public void setOrdinalPosition(byte ordinalPosition) {
	        this.ordinalPosition = ordinalPosition;
	    }
}
