package com.davidbase.model;

/**
 * This class represents a an Leaf cell in B tree
 */
public class LeafCell implements Cell {

    private CellHeader header;
    private CellPayload payload;
}
