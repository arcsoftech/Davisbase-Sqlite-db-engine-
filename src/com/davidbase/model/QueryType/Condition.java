package com.davidbase.model.QueryType;


import com.davidbase.utils.DataType;



public class Condition {

    public static final short EQUALS = 0;
    public static final short LESS_THAN = 1;
    public static final short GREATER_THAN = 2;
    public static final short LESS_THAN_EQUALS = 3;
    public static final short GREATER_THAN_EQUALS = 4;

    private byte index;

    private short conditionType;

    private Object value;

    private DataType valType;

    public static Condition CreateCondition(byte index, short conditionType, DataType valType, Object value) {
        Condition condition = new Condition(index, conditionType, valType, value);
        return condition;
    }

//    public static Condition CreateCondition(int index, short conditionType, DataType valType, Object value) {
//        Condition condition = new Condition(index, conditionType, valType, value);
//        return condition;
//    }

    public Condition() {}

    private Condition(byte index, short conditionType, DataType valType, Object value) {
        this.index = index;
        this.conditionType = conditionType;
        this.valType=valType;
        this.value = value;
    }

    private Condition(int index, short conditionType, DataType valType,Object value) {
        this.index = (byte) index;
        this.conditionType = conditionType;
        this.valType = valType;
        this.value = value;
    }

    public byte getIndex() {
        return index;
    }

    public void setIndex(byte index) {
        this.index = index;
    }

    public short getConditionType() {
        return conditionType;
    }

    public void setConditionType(short conditionType) {
        this.conditionType = conditionType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public DataType getValType() {
        return valType;
    }

    public void setValType(DataType valType) {
        this.valType = valType;
    }
}