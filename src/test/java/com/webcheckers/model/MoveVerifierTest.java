package com.webcheckers.model;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@Tag("Model-Tier")
public class MoveVerifierTest {

    private static final Position GOOD_START = new Position(5, 0);
    private static final Position GOOD_END = new Position(4, 1);
    private static final Position JUMP_END = new Position(3, 2);
    private static final Position OUT_OF_BOUNDS = new Position(-1, 7);
    private static final Position OUT_OF_BOUNDS_2 = new Position(0, 8);
    private static final Move move1 = new Move(GOOD_START, GOOD_END);
    private static final Move move2 = new Move(GOOD_START, JUMP_END);
    private static final BoardView board = new BoardView(false);

    /**
     * Test that a move verifier will be created correctly using the constructor and will correctly set the board
     * when prompted
     */
    @Test
    public void constructor(){
        final MoveVerifier CuT = new MoveVerifier();
        CuT.setBoard(board);
    }

    /**
     * Test that the moveisvalid function is working as expected
     */
    @Test
    public void moveisvalid(){
        final MoveVerifier CuT = new MoveVerifier();
        CuT.setBoard(board);
        assert(CuT.moveIsValid(move1));
        assertFalse(CuT.moveIsValid(move2));
        assert(CuT.getLastVerifiedMoves().get(0).equals(move1));
    }

    /**
     * Test that checks move reflection
     */
    @Test
    public void reflect_move_test(){
        final MoveVerifier CuT = new MoveVerifier();
        final Move move1ReflectedExpected = new Move(new Position(2, 7), new Position(3, 6));

        final Move move1Reflected = CuT.reflectMove(move1);
    
        assertFalse(move1.equals(move1ReflectedExpected));
        assert(move1Reflected.equals(move1ReflectedExpected));
    }

    /**
     * Tests move preservation mechanism
     */
    @Test
    public void move_preservation_test(){
        final MoveVerifier CuT = new MoveVerifier();
        
        try{
            CuT.moveIsValid(move1); // bonus test
            assert false;
        }catch(NullPointerException e){
            assert true;
        }
        board.refresh();
        CuT.setBoard(board);
        CuT.moveIsValid(move1);
        System.out.println(board.getMoves());
        assert(CuT.getLastVerifiedMoves().get(0).equals(move1));
        
        CuT.addLastSubmittedMove(CuT.getLastVerifiedMoves().get(0));

        assert(CuT.getLastSubmittedMoves().remove(0).equals(move1));
        assert(CuT.getLastSubmittedMoves().size() == 0);

        CuT.undoLastVerify();
        assert(CuT.getLastVerifiedMoves().isEmpty());
    }
    /**
     * Test that an out-of-bounds move will be correctly detected
     */
    @Test
    public void out_of_bounds_tests(){
        Move badmove = new Move(OUT_OF_BOUNDS, OUT_OF_BOUNDS_2);
        MoveVerifier CuT = new MoveVerifier();
        CuT.setBoard(board);

        assertFalse(CuT.moveIsValid(badmove));
        assert(CuT.getLastVerifiedMoves().isEmpty());
    }

    /**
     * Test that the Verifier will properly empty the array list when given viable double jumps
     */
    @Test
    public void successful_call_doublejump() {
        MoveVerifier CuT = new MoveVerifier();
        CuT.setBoard(board);

        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(new Position(2, 2), new Position(4, 4)));
        moves.add(new Move(new Position(4, 4), new Position(6, 6)));
        
        CuT.UpdateBoard(board, moves);

        assert(moves.isEmpty());
    }
}
