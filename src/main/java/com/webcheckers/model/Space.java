package com.webcheckers.model;

import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;

/**
 * A space has a cellIdx, index value, and occupied piece (null if no piece is on spot). It provides two constructors
 * for initializing unoccupied and occupied spaces.
 *
 * @author Benjamin LaGreca
 * @author Joseph Casale
 */

public class Space {
    // cellIdx: int 0-7
    private int cellIdx;
    private Piece piece;
    private int index;

    /**
     * Initializes without piece occupying
     * @param cellIdx, the cellIdx of the new space
     * @param index, the index of the new space
     */
    public Space(int cellIdx, int index) {
        this.cellIdx = cellIdx;
        this.index = index;
        this.piece = null;
    }

    /**
     * Initializes with piece occupying
     * @param cellIdx, the cellIdx of the new space
     * @param index, the index of the new space
     * @param piece, the piece with type and color occupying the space
     */
    public Space(int cellIdx, int index, Piece piece) {
        this.cellIdx = cellIdx;
        this.index = index;
        this.piece = piece;
    }


    /**
     * gets the space's cellIdx
     * @return cellIdx, the cellIdx of this space
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * sets this space's piece
     * @param piece, the piece to be set here
     */
    public void setPiece(Piece piece){
        this.piece = piece;
    }

    /**
     * gets the space's current piece
     * @return piece, this space's piece
     */
    public Piece getPiece(){
        return piece;
    }

    public boolean isEmpty(){
        return piece == null;
    }


    /**
     * Checks if a space is valid to be moved to by a piece of any color
     * @return true if space is not outside bounds, occupied by piece, or white.
     */
    public boolean isValid(){
        Boolean outbounds = ((cellIdx > 7 || cellIdx < 0) || (index > 7 || index < 0));
        Boolean taken = (this.piece != null);
        Boolean white = (((this.cellIdx+this.index) % 2) != 1);
        return !(outbounds || taken || white);
    }
    /**
     * 
     * @return true if and only if space is within the bounds
     */
    public boolean isWithinBounds(){
        return !((cellIdx > 7 || cellIdx < 0) || (index > 7 || index < 0));
    }

    /**
     * gets the space's index
     * @return index, the index of this space
     */
    public int getIndex(){
        return index;
    }

    /**
     * gets the color of the piece occupying this space
     * @return GameColor, the color of this space's piece
     */
    public GameColor getColorOfPiece(){
        if(this.piece == null){
            return null;
        }
        return this.piece.getColor();
    }

    /**
     * gets the type of the piece occupying this space
     * @return PieceType, the type of this space's piece
     */
    public PieceType getTypeOfPiece(){
        if(this.piece == null){
            return null;
        }
        return this.piece.getType();
    }

    /**
     * gets the color of this space as determined by its side of the board
     * @return GameColor, the color of this space
     */
    public GameColor getColorOfSpace(){
        if((((this.cellIdx+this.index) % 2) != 1)){
            return GameColor.WHITE;
        }
        else{
            return GameColor.RED;
        }
    }

    /**
     * generates a string representation of the piece in this space, including the type of piece and its color
     * @return a string with the 2 parameter representation of the piece in this space
     */
    public String representation(){
        PieceType type = getTypeOfPiece();
        GameColor color = getColorOfSpace();
        String colorString = null;
        String typeString = null;
        if(type == null){
            typeString = "b";
        }else if(type == PieceType.SINGLE){
            typeString = "s";
            color = getColorOfPiece();
        }else{
            typeString = "k"; //king
            color = getColorOfPiece();
        }

        if(color == GameColor.RED){
            colorString = "r";
        }else{
            colorString = "w";
        }
        return String.format(" %s%s ", colorString, typeString);
    }

    /**
     * gets the string representation of this space
     * @return a string representation of this space
     */
    @Override
    public String toString() {
        return String.format("Space(cell, row): (%d, %d); Piece: %s", cellIdx, index, piece);
    }
    
}
