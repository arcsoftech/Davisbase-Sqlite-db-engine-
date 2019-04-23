package com.davidbase.model.PageComponent;

import java.util.ArrayList;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.RIGHT_MOST_LEAF;

public class Page<T> {

    private PageHeader pageheader;
    private List<T> cells;

    public Page(){
        pageheader=new PageHeader();
        cells=new ArrayList<>();
    }
    
    public static <T> Page<T> createNewEmptyPage() {
        Page<T> page = new Page<>();
        PageHeader pageHeader = new PageHeader(0);
        pageHeader.setPage_type(PageType.table_leaf);
        pageHeader.setNext_page_pointer(RIGHT_MOST_LEAF);
        pageHeader.setNum_cells((byte)0x00);
        short[] offsets = new short[2];
        pageHeader.setData_cell_offset(offsets);
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
    
    public static short getHeaderFixedLength() {
        return (short)(Byte.BYTES + Byte.BYTES + Short.BYTES + Integer.BYTES);
    }

    public short getHeaderLength() {
        return pageheader.getSize();
    }
    
     public void setNumberOfCells(byte numberOfCells) {
         pageheader.setNum_cells(numberOfCells);
     }
    
     public byte getNumberOfCells() {
         return pageheader.getNum_cells();
     }

    public PageType getPage_type() {
        return pageheader.getPage_type();
    }

    public void setPage_type(PageType page_type) {
        pageheader.setPage_type(page_type);
    }

    public byte getNum_cells() {
        return pageheader.getNum_cells();
    }

    public void setNum_cells(byte num_cells) {
        pageheader.setNum_cells(num_cells);
    }

    public short getData_offset() {
        return pageheader.getData_offset();
    }

    public void setData_offset(short data_offset) {
        pageheader.setData_offset(data_offset);
    }

    public int getNext_page_pointer() {
        return pageheader.getNext_page_pointer();
    }

    public void setNext_page_pointer(int next_page_pointer) {
        pageheader.setNext_page_pointer(next_page_pointer);
    }

    public short[] getData_cell_offset() {
        return pageheader.getData_cell_offset();
    }

    public void setData_cell_offset(short[] data_cell_offset) {
        pageheader.setData_cell_offset(data_cell_offset);
    }

    public int getPage_number() {
        return pageheader.getPage_number();
    }

    public void setPage_number(int page_number) {
        pageheader.setPage_number(page_number);
    }



}
