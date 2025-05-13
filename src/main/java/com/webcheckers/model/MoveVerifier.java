package com.webcheckers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class handles the verification of moves made on a board given.
 * ONLY ONE of these is used.
 *
 * @author Joseph Casale
 * @author Griffin Danner-Doran
 */
public class MoveVerifier {

    private List<Move> lastVerifiedMoves;
    private List<Move> lastSubmittedMove;
    private BoardView board;

    /**
     * Constructor is empty and setBoard() must be called.
     */
    public MoveVerifier(){
        this.lastVerifiedMoves = new ArrayList<>();
        this.board = null;
        this.lastSubmittedMove = new ArrayList<>();
    }

    /**
     * Sets the current board the verifier is working with
     * @param board
     */
    public void setBoard(BoardView board){
        this.board = board;
    }

    /**
     * Returns true if the move is within the bounds of the game.
     * @param move, the move being evaluated
     * @return boolean true or false
     */
    private boolean moveWithinBounds(Move move){
        // get spaces
        Position start = move.getStart();
        Position end = move.getEnd();
        int startRow = start.getRow();
        int startCell = start.getCell();
        int endRow = end.getRow();
        int endCell = end.getCell();

        Space startSpace = null;
        Space endSpace = null;
        try{
            startSpace = board.getSpace(startRow, startCell);
            endSpace = board.getSpace(endRow, endCell);
        }catch(IndexOutOfBoundsException e){
            return false;
        }
        if( (!startSpace.isWithinBounds()) || (!endSpace.isWithinBounds()) ) return false;
        
        return true;
    }


    /**
     * High-level method for determining validity of a given move. 
     * @param move, the move being evaluated
     * @return true if valid, false otherwise
     */
    public boolean moveIsValid(Move move){
        if(!moveWithinBounds(move)){
            return false;
        }
        List<Set<Move>> actions = board.getMoves();
        Set<Move> moves = actions.get(0);
        Set<Move> jumps = actions.get(1);
        if(jumps.isEmpty()){
            if(moves.contains(move)) {
                lastVerifiedMoves.add(move);
                return true;
            }
        } else if(jumps.contains(move)){
            lastVerifiedMoves.add(move);
            return true;
        }
        return false;
    }

    /**
     * Returns the last verified move
     * @return Move object
     */
    public List<Move> getLastVerifiedMoves(){
        return lastVerifiedMoves;
    }

    /**
     * Returns the current board the verifier is working with.
     * @return BoardView object.
     */
    public BoardView getBoard(){
        return board;
    }

    /**
     * Add the last submitted move to the new back-log of moves.
     * @param move, the move to be added to the submitted move log
     */
    public void addLastSubmittedMove(Move move){
        this.lastSubmittedMove.add(move);
    }

    /**
     * Called by PostCheckTurnRoute to get the backlog of last moves that was submitted.
     * @return Move object
     */
    public List<Move> getLastSubmittedMoves(){
        return this.lastSubmittedMove;
    }

    /**
     * Reflects a move on the board across both x and y so that the move a player made
     * can be enacted on the other player's board.
     * @param move, the move to be reflected
     * @return reflected move
     */
    public Move reflectMove(Move move){
        Position start = move.getStart();
        Position end = move.getEnd();
        Position newStart = new Position(Math.abs(start.getRow() - 7), Math.abs(start.getCell() - 7));
        Position newEnd = new Position(Math.abs(end.getRow() - 7), Math.abs(end.getCell() - 7));
        return new Move(newStart, newEnd);
    }

    /**
     * Removes the last move from the verified list and sets that piece to its original location
     */
    public void undoLastVerify(){
        Move move = lastVerifiedMoves.remove(lastVerifiedMoves.size() - 1);
        board.setPiece(move.getEnd(), null);
    }

    /**
     * Updates the enemy board with a reflection of the made move
     * @param board, the board to be updated
     * @param moves, the list of moves to be reflected onto this board
     */
    public void UpdateBoard(BoardView board, List<Move> moves) {
        Move move = null;
        if (moves.size() > 0) {
            do {
                move = moves.remove(0);
                move = this.reflectMove(move);
                board.makeMove(move);
            } while (moves.size() > 0);
        }
    }

    /**
     * gets the first item in the list of verified moves
     * @return Move, the move in the zero index of the verified moves list
     */
    public Move getFirstVerifiedMove(){
        try{
            return lastVerifiedMoves.get(0);
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
    /**
     * clears the list of verified moves
     */
    public void clearLastVerifiedMoves(){
        lastVerifiedMoves.clear();
    }
    /**
     * clears the list of submitted moves
     */
    public void clearLastSubmittedMoves(){
        lastSubmittedMove.clear();
    }
    /**
     * adds a move to the verified move list
     * @param move, the move to be added to the list
     */
    public void addVerifiedMove(Move move){
        lastVerifiedMoves.add(move);
    }
}
