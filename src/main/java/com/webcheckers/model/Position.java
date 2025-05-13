package com.webcheckers.model;

/**
 * Class represents a Position. This is difference from Space and Row, since this
 * is just something for Ajax to work with.
 *
 * @author Joseph Casale
 * @author Benjamin LaGreca
 */

public class Position {
    private int row;
    private int cell;
    /**
     * assigns a position at this row and cell
     * @param row, the row of this position
     * @param cell, the cell of this position
     */
    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }
    /**
     * gets the cell of this position
     * @return this position's cell
     */
    public int getCell() {
        return cell;
    }
    /**
     * gets the row of this position
     * @return this position's row
     */
    public int getRow() {
        return row;
    }
    /**
     * gets the string representation of this position
     * @return this position's cell
     */
    @Override
    public String toString(){
        return String.format("(Row: %d, Cell: %d)", row, cell);
    }
    /**
     * overrides the base equality operator for positions
     * @return true if this object is equal to the given
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Position){
            Position other = (Position) o;
            return (cell == other.cell && row == other.row);
        }
        return false;
    }
}  
