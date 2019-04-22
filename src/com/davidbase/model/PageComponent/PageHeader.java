package com.davidbase.model.PageComponent;

/**
 * This class represents a Page Header
 * All DavisBase table pages have an 8-byte page header
 */
public class PageHeader {

    /* The one-byte flag at offset 0 indicating the b-tree page type.
        • A value of 2 (0x02) means the page is an interior index b-tree page.
        • A value of 5 (0x05) means the page is an interior table b-tree page.
        • A value of 10 (0x0a) means the page is a leaf index b-tree page.
        • A value of 13 (0x0d) means the page is a leaf table b-tree page.
    Any other value for the b-tree page type is an error*/
    private PageType page_type;

    //The one-byte two’s complement signed integer at offset 1 gives the number of cells
    //on the page
    private byte num_cells;

    //The two-byte two’s complement signed integer at offset 2 designates the start of the
    //cell content area. A zero value for this integer is interpreted as 65536
    private short data_offset;

    /* The four-byte signed integer page pointer at offset 4 has a different role depending
       on the page type:
        • Table or Index interior page - rightmost child page number reference.
        • Table leaf page - right sibling page number reference.
        • Index leaf page - unused */
    private int next_page_pointer;

    /* An array of 2-byte integers that indicate the page offset location of each data cell.
    The array size is 2n, where n is the number of cells on the page. The array is
    maintained in key-sorted order—i.e. rowid order for a table file and index order for
    an index file.*/
    private short[] data_cell_offset;

    private int page_number;

    public PageHeader(int pageNum){
        this.page_number=pageNum;
    }

    public PageType getPage_type() {
        return page_type;
    }

    public void setPage_type(PageType page_type) {
        this.page_type = page_type;
    }

    public byte getNum_cells() {
        return num_cells;
    }

    public void setNum_cells(byte num_cells) {
        this.num_cells = num_cells;
    }

    public short getData_offset() {
        return data_offset;
    }

    public void setData_offset(short data_offset) {
        this.data_offset = data_offset;
    }

    public int getNext_page_pointer() {
        return next_page_pointer;
    }

    public void setNext_page_pointer(int next_page_pointer) {
        this.next_page_pointer = next_page_pointer;
    }

    public short[] getData_cell_offset() {
        return data_cell_offset;
    }

    public void setData_cell_offset(short[] data_cell_offset) {
        this.data_cell_offset = data_cell_offset;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }
}
