package com.webcheckers.model;

/**
 * Handles moves made in the game. Moves are viewed as a start and an end position, which can be used to evaluate
 * their validity.
 *
 * @author Joseph Casale
 * @author Benjamin LaGreca
 */
public class Move {
    //the beginning and end of the move
    private Position start;
    private Position end;
    //whether this move is a jump or not
    private boolean isJump;

    /**
     * initalizes a new move with a start and end position
     * @param start, the start Position of this move
     * @param end, the end Position of this move
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
        
        if(Math.abs(start.getCell() - end.getCell()) == 2 && Math.abs(start.getRow() - end.getRow()) == 2){
            this.isJump = true;
        }
    }
    /**
     * gets the end of this move
     * @return end, the Position at this move's end
     */
    public Position getEnd() {
        return end;
    }
    /**
     * gets the end position's cell attribute
     * @return the end cell
     */
    public int getEndCell(){
        return end.getCell();
    }
    /**
     * gets the end row of this move
     * @return the end row
     */
    public int getEndRow(){
        return end.getRow();
    }
    /**
     * gets the start of this move
     * @return start, the Position at this move's start
     */
    public Position getStart() {
        return start;
    }
    /**
     * gets if this move is a jump or not
     * @return end, the Position at this move's end
     */
    public boolean isJump(){
        return isJump;
    }

    /**
     * gets the start row of this move
     * @return the start row
     */
    public int getStartRow(){
        return start.getRow();
    }

    /**
     * gets the start cell of this move
     * @return the start cell
     */
    public int getStartCell(){
        return start.getCell();
    }

    /**
     * reverses this move by creating a move with switched start and end
     * @return a Move with opposite start and end to this move
     */
    public Move reverse(){
        return new Move(end, start);
    }

    /**
     * gets the string representation of this move
     * @return a formatted string with the start and end positions
     */
    @Override
    public String toString(){
        return String.format("From: %s, To: %s", start, end);
    }

    /**
     * overrides the default hashcode of the move object
     * @return the int hashcode of this object
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

     /**
     * overrides the default equals method of the move object
     * @return true if this object is equal to the given one
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Move){
            Move other = (Move)o;
            return other.getStart().equals(start) && other.getEnd().equals(end);
        }
        return false;
    }

}
