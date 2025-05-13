package com.webcheckers.model;

/**
 * A row has 8 columns, as well as an index integer value. When a row is initialized, it populates
 *
 * @author Benjamin LaGreca
 */

import java.util.Arrays;
import java.util.Iterator;

public class Row implements Iterable<Space>{
    static int NUM_COLS = 8;
    private final int index;

    private Space[] spaces;

    /**
     * gets the row index
     * @return index, this row's index
     */
    public int getIndex() {
        return index;
    }

    /**
     * construct this row with a given index
     * @param index, this row's index
     */
    public Row(int index) {
        this.index = index;
        this.spaces = initSpacesArr();
    }

    /**
     * gets the array of spaces in this row
     * @return spaces, a Space[] array of the 8 spaces in this row
     */
    public Space[] getSpaces(){
        return spaces;
    }

    /**
     * Creates and populates an array of empty spaces equal to # of columns.
     * @return array of spaces
     */
    private Space[] initSpacesArr(){
        spaces = new Space[NUM_COLS];
        for(int i = 0; i < spaces.length; i++){
            spaces[i] = new Space(i, this.index);
        }
        return spaces;
    }

    /**
     * creates an iterator stream of the spaces in this row
     * @return a stream of this row's spaces
     */
    @Override
    public Iterator<Space> iterator() {
        return Arrays.stream(spaces).iterator();
    }
}
