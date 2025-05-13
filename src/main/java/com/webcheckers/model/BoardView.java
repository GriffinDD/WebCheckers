package com.webcheckers.model;

import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The BoardView class represents the square board in which the checkers pieces are placed on. A board consists of
 * 8 rows and 8 columns (since it's a square, if this wasn't already clear), with each square colored red or
 * white in an alternating fashion. The board is divided vertically into two territories, with each corresponding with
 * one of the player colors.
 *
 *
 * @author Joseph Casale
 * @author Benjamin LaGreca
 */

public class BoardView implements Iterable<Row> {
    static final int NUM_ROWS = 8;

    //the rows to initialize as top and bottom
    private static final int[] BOTTOM_TERRITORY = {5,6,7};
    private static final int[] TOP_TERRITORY = {0,1,2};

    private Row[] rows;
    //the orientation this board is initialized in
    private final boolean isWhiteBottom;
    //lists of each player's "territory"
    private final int[] whiteTerritory;
    private final int[] redTerritory;

    /**
     * initializes the boardview with one player viewing from the bottom
     * @param isWhiteBottom, true if the white pieces should initialize at the bottom of the board
     */
    public BoardView(boolean isWhiteBottom) {
        initRowsArr();
        this.isWhiteBottom = isWhiteBottom;

        if(this.isWhiteBottom){
            whiteTerritory = BOTTOM_TERRITORY;
            redTerritory = TOP_TERRITORY;
        }
        else {
            whiteTerritory = TOP_TERRITORY;
            redTerritory = BOTTOM_TERRITORY;
        }
        addInitialPieces();

    }

    /**
     * completely refresh the board by setting all pieces and spaces to their initial states
     */
    public void refresh(){
        initRowsArr();
        addInitialPieces();
    }
    
    /**
     * Initialize a board's rows, creates a fresh array of rows for a new board
     */
    private void initRowsArr(){
        rows = new Row[NUM_ROWS];
        for(int i = 0; i < rows.length; i++){
            rows[i] = new Row(i);
        }
    }

    /**
     * sets the pieces for each player in their initial positions depending on the board's orientation
     */
    private void addInitialPieces(){
        // assume 0,0 is upper left because thats how programmers do it

        //place a single white piece in each starting white territory
        for (int whiteRow : whiteTerritory){
            for (Space currSpace : rows[whiteRow].getSpaces()) {
                if(isSpaceBlack(currSpace.getCellIdx(), whiteRow)){
                    currSpace.setPiece(new Piece(PieceType.SINGLE, GameColor.WHITE));
                }
            }
        }

        //place a single red piece in each starting red territory
        for (int redRow : redTerritory){
            for ( Space currSpace : rows[redRow].getSpaces()) {
                if(isSpaceBlack(currSpace.getCellIdx(), redRow)){
                    currSpace.setPiece(new Piece(PieceType.SINGLE, GameColor.RED));
                }
            }
        }
    }

    /**
     * get if a space at the given location is black
     * @param x, the x location of the space
     * @param y, the y location of the space
     * @return true if a space is black
     */
    private boolean isSpaceBlack(int x, int y) {
        // if x+y is odd its a black space
        return ((x+y) % 2) == 1;
    }

    /**
     * gets an iterator stream of the rows of this board
     * @return the row iterator stream
     */
    @Override
    public Iterator<Row> iterator() {
        return Arrays.stream(rows).iterator();
    }

    /**
     * Populates the given list of moves with the possible jumps given this space and piece type
     * this function is primarily used when double jumping with kings, where the space alone is not enough to determine
     * the possible paths.
     * @param jumps, the list of moves to be updated
     * @param space, the space being evaluated
     * @param piece, the piece whose moves are being calculated. Used to check for king based jumps
     */
    public void getJumpsForSpace(Set<Move> jumps, Space space, Piece piece){
        GameColor color = piece.getColor();
        PieceType type = piece.getType();
        int row = space.getIndex();
        int cell = space.getCellIdx();
        int up1 = row - 1;
        int up2 = row - 2;
        int right1 = cell + 1;
        int right2 = cell + 2;
        int left1 = cell - 1;
        int left2 = cell - 2;
        int down1 = row + 1;
        int down2 = row + 2;

        //checks if it can jump forward to the right
        try{
            Space upright1 = this.getSpace(up1, right1);
            GameColor colorU1 = upright1.getColorOfPiece();
            if(!colorU1.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space upright2 = this.getSpace(up2, right2);
                if(upright2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(up2, right2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        //checks if it can jump forward to the left
        try{
            Space upleft1 = this.getSpace(up1, left1);
            GameColor colorU2 = upleft1.getColorOfPiece();
            if(!colorU2.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space upleft2 = this.getSpace(up2, left2);
                if(upleft2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(up2, left2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        // Is this piece a king?
        if(!type.equals(PieceType.KING)){
            return;
        }

        // For the king!
        //checks if the king can jump back and to the right
        try{
            Space downright1 = this.getSpace(down1, right1);
            GameColor colorD1 = downright1.getColorOfPiece();
            if(!colorD1.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space downright2 = this.getSpace(down2, right2);
                if(downright2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(down2, right2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        //checks if the king can jump back and to the left
        try{
            Space downleft1 = this.getSpace(down1, left1);
            GameColor colorD2 = downleft1.getColorOfPiece();
            if(!colorD2.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space downleft2 = this.getSpace(down2, left2);
                if(downleft2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(down2, left2)));
                    // recurse
                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}
        return;
    }

    /**
     * Populates a set of moves with all possible Jump moves from the given space
     * @param jumps set of moves 
     * @param space space to check at.
     */
    public void getJumpsForSpace(Set<Move> jumps, Space space){
        GameColor color = space.getColorOfPiece();
        PieceType type = space.getTypeOfPiece();
        int row = space.getIndex();
        int cell = space.getCellIdx();
        int up1 = row - 1;
        int up2 = row - 2;
        int right1 = cell + 1;
        int right2 = cell + 2;
        int left1 = cell - 1;
        int left2 = cell - 2;
        int down1 = row + 1;
        int down2 = row + 2;

        //check if it can jump forward to the right
        try{
            Space upright1 = this.getSpace(up1, right1);
            GameColor colorU1 = upright1.getColorOfPiece();
            if(!colorU1.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space upright2 = this.getSpace(up2, right2);
                if(upright2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(up2, right2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        //checks if it can jump forward to the left
        try{
            Space upleft1 = this.getSpace(up1, left1);
            GameColor colorU2 = upleft1.getColorOfPiece();
            if(!colorU2.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space upleft2 = this.getSpace(up2, left2);
                if(upleft2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(up2, left2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        // Is this piece a king?
        if(!type.equals(PieceType.KING)){
            return;
        }

        // For the king!

        //checks if this king can jump backwards to the right
        try{
            Space downright1 = this.getSpace(down1, right1);
            GameColor colorD1 = downright1.getColorOfPiece();
            if(!colorD1.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space downright2 = this.getSpace(down2, right2);
                if(downright2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(down2, right2)));

                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}

        //checks if this king can jump backwards to the left
        try{
            Space downleft1 = this.getSpace(down1, left1);
            GameColor colorD2 = downleft1.getColorOfPiece();
            if(!colorD2.equals(color)){ //intermediate piece's color does not equal current spaces color
                Space downleft2 = this.getSpace(down2, left2);
                if(downleft2.isValid()){
                    jumps.add(new Move(new Position(row, cell), new Position(down2, left2)));
                    // recurse
                }
            }
        }
        catch(Exception e){/**The space does not exist (indexoutofbounds), The piece is null (Nullpointer)*/}
        return;
    }

    /**
     * Get a list of the two types of moves, basic moves and jumps. All moves possible in the game for all pieces
     * that match the perspective assigned to this board.
     * Called after a move is submitted, if the last move made triggers this condition, then a player loses
     * @return [Set<Move> moves, Set<Move> jumps] to be used by MoveVerifier.
     */
    public List<Set<Move>> getMoves(){

        // single moves
        Set<Move> movesFromSpace = new HashSet<>();
        Set<Move> moves = new HashSet<>();
        
        // jump moves
        Set<Move> jumpsFromSpace = new HashSet<>();
        Set<Move> jumps = new HashSet<>();


        for(Row row : this){
            for(Space space : row){
                movesFromSpace.clear();
                jumpsFromSpace.clear();
                // all possible moves from this space.
                if(space.getPiece() == null){
                    continue;
                }
                GameColor color = space.getColorOfPiece();
                if(isWhiteBottom && color.equals(GameColor.WHITE)){
                    getMovesForSpace(movesFromSpace, space);
                    getJumpsForSpace(jumpsFromSpace, space);
                }
                if(!isWhiteBottom && color.equals(GameColor.RED)){
                    getMovesForSpace(movesFromSpace, space);
                    getJumpsForSpace(jumpsFromSpace, space);
                }
                moves.addAll(movesFromSpace);
                jumps.addAll(jumpsFromSpace);
            }
        }
        List<Set<Move>> actions = new ArrayList<>();
        actions.add(moves);
        actions.add(jumps);
        return actions;
    }

    
    /**
     * Populates the moves set with all possible moves at this location.
     * This method assumes that the space it is checking has a piece of the same
     * color as the player making the move.
     * @param moves set to populate
     * @param space space to check at
     */
    private void getMovesForSpace(Set<Move> moves, Space space){
        
        int row = space.getIndex();
        int cell = space.getCellIdx();
        PieceType type = space.getTypeOfPiece();
        // for regular moves
        int up = row - 1;
        int right = cell + 1;
        int down = row + 1;
        int left = cell - 1;

        //checks if this piece can move forward to the right
        try{
            Space upRight = this.getSpace(up, right);
            if(upRight.isValid() && Objects.isNull(upRight.getPiece()))
                moves.add(new Move(new Position(row, cell), new Position(up, right)));
        }catch(IndexOutOfBoundsException e){}

        //checks if this piece can move forward to the left
        try{
            Space upLeft = this.getSpace(up, left);
            if(upLeft.isValid() && Objects.isNull(upLeft.getPiece()))
                moves.add(new Move(new Position(row, cell), new Position(up, left)));
        }catch(IndexOutOfBoundsException e){}

        if(type.equals(PieceType.KING)){//only check backward for king
            //check if this king can move backward to the left
            try{
                Space downLeft = this.getSpace(down, left);
                if(downLeft.isValid() && Objects.isNull(downLeft.getPiece()))
                    moves.add(new Move(new Position(row, cell), new Position(down, left)));
            }catch(IndexOutOfBoundsException e){}

            //check if this king can move backward to the right
            try{
                Space downRight = this.getSpace(down, right);
                if(downRight.isValid() && Objects.isNull(downRight.getPiece()))
                    moves.add(new Move(new Position(row, cell), new Position(down, right)));
            }catch(IndexOutOfBoundsException e){}
        }

    }

    /**
     * Return the space associated with a row and cell pair.
     * @param row, the row being searched
     * @param cell, the row location being checked
     * @returns space, the space found at the given location
     * @throws IndexOutOfBoundsException if the space is not a real space
     */
    public Space getSpace(int row, int cell) throws IndexOutOfBoundsException{
        return rows[row].getSpaces()[cell];
    }

    /**
     * Given a verified move, update the pieces on the board.
     * This method will also handle making a piece a king if it should.
     * 
     * @param move move to make
     */
    public void makeMove(Move move){

        Position start = move.getStart();
        int startrow = start.getRow();
        int startcell = start.getCell();

        Position end = move.getEnd();
        int endrow = end.getRow();
        int endcell = end.getCell();

        GameColor color = getSpace(startrow, startcell).getColorOfPiece();
        PieceType type = getSpace(startrow, startcell).getTypeOfPiece();
        if(endrow == 7 || endrow == 0){ 
            //this will always work because a move that ends on this row had to MOVE there.
            type = PieceType.KING;
        }

        if(!move.isJump()){
            //clears the old space and moves piece to end
            this.getSpace(startrow, startcell).setPiece(null);
            this.getSpace(endrow, endcell).setPiece(new Piece(type, color));
        }else{
            //clears old space and jumped space, then moves piece to end
            this.getSpace(startrow, startcell).setPiece(null);
            this.getSpace(endrow, endcell).setPiece(new Piece(type, color));

            // re-using variables for intermediate piece.
            startrow = (startrow + endrow) / 2;
            startcell= (startcell + endcell) / 2;
            this.getSpace(startrow, startcell).setPiece(null);

        }
        return;
    }

    /**
     * checks if there are any possible moves on this board for the given perspective (white bottom or red bottom)
     * @return true if there are no jump moves possible and no normal moves possible
     */
    public boolean outOfMoves(){
        List<Set<Move>> actions = getMoves();
        return(actions.get(0).size() == 0 && actions.get(1).size() == 0);
    }

    /**
     * gets the string representation of the board
     * @return the string containing the collected representation of each space on the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Row row : rows){
            for(Space space : row){
                sb.append(space.representation());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * gets the piece associated with a position
     * @param position, the position being searched
     * @return the piece object associated with the target space
     */
    public Piece getPieceForPosition(Position position){
        int row = position.getRow();
        int cell = position.getCell();
        return this.getSpace(row, cell).getPiece();
    }

    /**
     * clears the piece associated with a position
     * @param position, the position being searched
     */
    public void clearSpace(Position position){
        int row = position.getRow();
        int cell = position.getCell();
        this.getSpace(row, cell).setPiece(null);
    }

    /**
     * sets the piece associated with a position
     * @param position, the position being searched
     * @param piece, the piece to be assigned to that position
     */
    public void setPiece(Position position, Piece piece){
        int row = position.getRow();
        int cell = position.getCell();
        this.getSpace(row, cell).setPiece(piece);
    }
}

