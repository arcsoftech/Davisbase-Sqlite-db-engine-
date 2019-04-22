package com.davidbase.model.PageComponent;

import java.util.ArrayList;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.RIGHT_MOST_LEAF;

public class Page<T> {

    private PageHeader pageheader;
    private List<T> cells;
    private byte numberOfCells;
    private PageType pagePage;
    
    public static <T> Page<T> createNewEmptyPage() {
        Page<T> page = new Page<>();
        PageHeader pageHeader = new PageHeader(0);
        pageHeader.setPage_type(PageType.table_leaf);
        pageHeader.setNext_page_pointer(RIGHT_MOST_LEAF);
        page.setNumberOfCells((byte)0x00);
        page.setPageheader(pageHeader);
        return page;
    }


    public PageHeader getPageheader() {
        return pageheader;
    }

    public void setPageheader(PageHeader pageheader) {
        this.pageheader = pageheader;
    }

    public List<T> getCells() {
        return cells;
    }

    public void setCells(List<T> cells) {
        this.cells = cells;
    }
    
    public static int getHeaderFixedLength() {
        return Byte.BYTES + Byte.BYTES + Short.BYTES + Integer.BYTES;
    }
    
    public void setNumberOfCells(byte numberOfCells) {
        this.numberOfCells = numberOfCells;
    }
    
    public byte getNumberOfCells() {
        return numberOfCells;
    }
    
    
}
