package com.davidbase.model;

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
}
