package com.davidbase.model;

/**
 * This class represents a Non Leaf cell in B-Tree
 */
public class NonLeafCell implements Cell{

    //A 4-byte INT page number which is the left child pointer.
    private int page_number;

    //An INT which is the integer key delimiter between the left and right child nodes.
    private int key_delim;

    public NonLeafCell(int page_number, int key_delim) {
        this.page_number = page_number;
        this.key_delim = key_delim;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getKey_delim() {
        return key_delim;
    }

    public void setKey_delim(int key_delim) {
        this.key_delim = key_delim;
    }
}
