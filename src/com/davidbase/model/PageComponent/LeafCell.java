package com.davidbase.model.PageComponent;

/**
 * This class represents a an Leaf cell in B tree
 */
public class LeafCell implements Cell {

    private CellHeader header;
    private CellPayload payload;

    // non serializable fields
    private int pageNumber;
    private short offset;

    public LeafCell(){
        this.header = new CellHeader();
        this.payload = new CellPayload();
    }

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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public short getOffset() {
        return offset;
    }

    public void setOffset(short offset) {
        this.offset = offset;
    }

    // to be called every time we are ready to wrote a Leaf Cell to file
    public void initializeLeafForWrite(){
        this.getPayload().generateByteData();
        short payloadSize = this.getPayload().getPayloadSize();
        this.getHeader().setPayload_size(payloadSize);
    }
}
