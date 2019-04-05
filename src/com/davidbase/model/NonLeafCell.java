package com.davidbase.model;

/**
 * This class represents a Non Leaf cell in B-Tree
 */
public class NonLeafCell implements Cell{

    //A 4-byte INT page number which is the left child pointer.
    private int page_number;

    //An INT which is the integer key delimiter between the left and right child nodes.
    private int key_delim;

}
