package com.davidbase.model;

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
    private byte page_type;

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
}
