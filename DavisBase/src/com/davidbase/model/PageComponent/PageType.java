package com.davidbase.model.PageComponent;

public enum PageType {
    index_node(0x02),table_node(0x05),index_leaf(0x0a),table_leaf(0x0d);

    byte val;

    PageType(int value){
        this.val=(byte)value;
    }

    public byte getVal() {
        return this.val;
    }

    public static PageType getType(byte value){
        if(value==(byte)0x02)
            return index_node;
        else if(value==(byte)0x05)
            return table_node;
        else if(value==(byte)0x0a)
            return index_leaf;
        else if(value==(byte)0x0d)
            return table_leaf;
        return null;
    }
}
