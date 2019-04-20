package com.davidbase.model.PageComponent;

import java.util.List;

public class Page<T> {

    private PageHeader pageheader;
    private List<T> cells;

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
    
    
}
