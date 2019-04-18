package com.davidbase.model;

/**
 * This class represents a an Leaf cell in B tree
 */
public class LeafCell implements Cell {

    private CellHeader header;
    private CellPayload payload;

    public LeafCell(CellHeader header, CellPayload payload) {
        this.header = header;
        this.payload = payload;
    }

    public CellHeader getHeader() {
        return header;
    }

    public void setHeader(CellHeader header) {
        this.header = header;
    }

    public CellPayload getPayload() {
        return payload;
    }

    public void setPayload(CellPayload payload) {
        this.payload = payload;
    }
}
