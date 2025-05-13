package com.webcheckers.model;


import com.webcheckers.util.GameColor;
import com.webcheckers.util.PieceType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-Tier")
public class BoardViewTest {

    private Piece piece;
    @BeforeEach
    public void setup(){
        piece = mock(Piece.class);
        when(piece.getColor()).thenReturn(GameColor.RED);
        when(piece.getType()).thenReturn(PieceType.KING);
    }

    /**
     * Test that a BoardView will be correctly initialized in either form
     */
    @Test
    public void constructor(){
        BoardView white = new BoardView(true);
        BoardView red = new BoardView(false);
        assertNotNull(white);
        assertNotNull(red);

    }

    /**
     * Test that the iterator function works, though the outcome cannot be tested due to the individual nature of the streams
     */
    @Test
    public void iteratortester(){
        BoardView CuT = new BoardView(false);
        CuT.iterator();

    }

    /**
     * Test that the make move function works without error
     */
    @Test
    public void MakeMoveTester(){
        BoardView CuT = new BoardView(false);
        BoardView CuT2 = new BoardView(true);
        Move ex = new Move(new Position(1,1), new Position(2,2));
        CuT.makeMove(ex);
        CuT2.makeMove(ex);
        Move move = mock(Move.class);
        when(move.getEnd()).thenReturn(new Position(2,2));
        when(move.getStart()).thenReturn(new Position(1,1));
        when(move.isJump()).thenReturn(true);
        CuT.makeMove(move);
        CuT2.makeMove(move);
    }


    /**
     * Test that the getMoves function is working
     */
    @Test
    public void getMovesTesters(){
        BoardView CuT = new BoardView(false);
        assertNotNull(CuT.getMoves());
        BoardView CuT2 = new BoardView(true);
        assertNotNull(CuT2.getMoves());
    }


    /**
     * Test getting jumps for a space
     */
    @Test
    public void testGetJumpsForSpace(){
        Set<Move> jumps = new HashSet<>();
        BoardView CuT = new BoardView(false);
        Space testSpace = CuT.getSpace(5, 2);
        System.out.println(testSpace);
        CuT.getJumpsForSpace(jumps, testSpace);
        assert(jumps.isEmpty());

        CuT.makeMove(new Move(new Position(0, 1), new Position(4, 3)));
        CuT.getJumpsForSpace(jumps, testSpace);
        assert(jumps.size() == 1);
        assert(!CuT.outOfMoves());
    }

    /**
     * Test the twoString for a space on a board.
     */
    @Test
    public void testToString(){
        String expected = "Space(cell, row): (2, 5); Piece: RED SINGLE";
        BoardView CuT = new BoardView(false);
        Space testSpace = CuT.getSpace(5, 2);
        
        assertEquals(expected, testSpace.toString());
    }

    /**
     * Test that a piece is properly kinged.
     */
    @Test
    public void testKingingPiece(){
        BoardView CuT = new BoardView(false);
        Space space = CuT.getSpace(0, 1);
        
        assertEquals(space.getTypeOfPiece(), PieceType.SINGLE);
        assertEquals(space.getColorOfPiece(), GameColor.WHITE);
        // force a kinging
        Move kingingMove = new Move(new Position(0, 1), new Position(7, 0));
        CuT.makeMove(kingingMove);

        space = CuT.getSpace(7, 0);
        assertEquals(space.getTypeOfPiece(), PieceType.KING);
        assertEquals(space.getColorOfPiece(), GameColor.WHITE);
    }

    /**
     * Test that a kinged piece has the proper moves
     * Involves setup
     */
    @Test
    public void testKingedPieceMoves(){
        BoardView CuT = new BoardView(false);
        Move forwardRight = new Move(new Position(4, 3), new Position(3, 4));
        Move forwardLeft = new Move(new Position(4, 3), new Position(3, 2));
        Move backwardRight = new Move(new Position(4, 3), new Position(5, 4));
        Move backwardLeft = new Move(new Position(4, 3), new Position(5, 2)); 
        Move kingingMove = new Move(new Position(7, 0), new Position(0, 1));
        Move middle = new Move(new Position(0, 1), new Position(4, 3));


        Move nextMiddle = new Move(new Position(4, 3), new Position(3, 4));
        Move nextbackwardRight = new Move(new Position(3, 4), new Position(4, 5));
        Move nextbackwardLeft = new Move(new Position(3, 4), new Position(4, 3));

        // force a kinging
        CuT.makeMove(kingingMove);
        // place it somewhere it can make moves
        CuT.makeMove(middle);

        // Move to the middle of the board with lots of space to move
        List<Set<Move>> actions = CuT.getMoves();
        // should contain these
        Set<Move> regularMoves = actions.get(0);
        assert(regularMoves.contains(forwardRight));
        assert(regularMoves.contains(forwardLeft));
        assertFalse(regularMoves.contains(backwardLeft));
        assertFalse(regularMoves.contains(backwardRight));

        CuT.makeMove(nextMiddle);
        actions = CuT.getMoves();
        regularMoves = actions.get(0);
        assert(regularMoves.contains(nextbackwardRight));
        assert(regularMoves.contains(nextbackwardLeft));
    }

    /**
     * Test that a kinged piece has the proper jumps.
     * involves forced setup
     */
    @Test
    public void testKingedPieceJumps(){
        BoardView CuT = new BoardView(false);
        Move kingingMove = new Move(new Position(7, 0), new Position(0, 1));
        Move middle = new Move(new Position(0, 1), new Position(4, 3));
        // jump over 3, 2
        Move backwardJumpPrep = new Move(new Position(6, 5), new Position(0, 1));
        Move backwardJumpPrep2 = new Move(new Position(0, 3), new Position(5, 4));
        Move backwardJump = new Move(new Position(4, 3), new Position(6, 5));

        // jump over 5, 4
        Move forwardJumpPrep = new Move(new Position(0, 7), new Position(3, 4));
        Move forwardJumpPrep2= new Move(new Position(2, 5), new Position(0, 2));

        Move forwardJump = new Move(new Position(4, 3), new Position(2, 5));

        // force a kinging
        CuT.makeMove(kingingMove);
        // place it somewhere it can make moves
        CuT.makeMove(middle);

        // Move to the middle of the board with lots of space to move
        List<Set<Move>> actions = CuT.getMoves();
        Set<Move> jumps = actions.get(1);

        assert(jumps.size() == 0);

        CuT.makeMove(backwardJumpPrep);
        CuT.makeMove(forwardJumpPrep);
        CuT.makeMove(backwardJumpPrep2);
        CuT.makeMove(forwardJumpPrep2);
        actions = CuT.getMoves();
        jumps = actions.get(1);
        assert(jumps.contains(forwardJump));
        assert(jumps.contains(backwardJump));
        assert(jumps.size() == 3);
        assertFalse(jumps.contains(middle));
    }

    /**
     * Test that the full representation of the board is correct (tostring)
     */
    @Test
    public void testRepresentation(){
        BoardView CuT = new BoardView(false);
        Move kingingMove1 = new Move(new Position(7, 0), new Position(0, 1));
        Move kingingMove2 = new Move(new Position(0, 3), new Position(7, 6));
        CuT.makeMove(kingingMove1);
        CuT.makeMove(kingingMove2);

        
        String actual = " wb  rk  wb  rb  wb  ws  wb  ws \n" +
        " ws  wb  ws  wb  ws  wb  ws  wb \n" +
        " wb  ws  wb  ws  wb  ws  wb  ws \n" +
        " rb  wb  rb  wb  rb  wb  rb  wb \n" +
        " wb  rb  wb  rb  wb  rb  wb  rb \n" +
        " rs  wb  rs  wb  rs  wb  rs  wb \n" +
        " wb  rs  wb  rs  wb  rs  wb  rs \n" +
        " rb  wb  rs  wb  rs  wb  wk  wb \n"; 
        assertEquals(CuT.toString(), actual);
    }

    /**
     * Test function for clear space method
     */
    @Test
    public void testClearSpace(){
        BoardView CuT = new BoardView(false);

        Space space = CuT.getSpace(0, 1);
        assert(space.getPiece() != null);

        CuT.clearSpace(new Position(0, 1));

        assert(space.getPiece() == null);
    }

    /**
     * Tests that both getjumps methods return empty sets when they have no jumps
     */
    @Test
    public void testGetJumpsForSpaceGivenPieceNone(){
        BoardView CuT = new BoardView(false);

        Space space = CuT.getSpace(0, 1);
        assert(space.getPiece() != null);
        List<Set<Move>> actions = CuT.getMoves();
        Set<Move> jumps = actions.get(1);
        assert(jumps.size() == 0);

        CuT.getJumpsForSpace(jumps, space);
        assert(jumps.isEmpty());
        piece = new Piece(PieceType.KING, GameColor.RED);
        CuT.getJumpsForSpace(jumps, space, piece);
        assert(jumps.isEmpty());
        assertFalse(CuT.outOfMoves());
    }

    /**
     * Test that getJumpsForSpace with a piece parameter occupies a larger list with a KING set piece.
     */
    @Test
    public void testGetJumpsForSpaceGivenPieceHasJumps(){
        BoardView CuT = new BoardView(false);
        Move kingingMove = new Move(new Position(7, 0), new Position(0, 1));
        Move middle = new Move(new Position(0, 1), new Position(4, 3));
        // jump over 3, 2
        Move backwardJumpPrep = new Move(new Position(6, 5), new Position(0, 1));
        Move backwardJumpPrep2 = new Move(new Position(0, 3), new Position(5, 4));

        // jump over 5, 4
        Move forwardJumpPrep = new Move(new Position(0, 7), new Position(3, 4));
        Move forwardJumpPrep2= new Move(new Position(2, 5), new Position(0, 2));


        // force a kinging
        CuT.makeMove(kingingMove);
        // place it somewhere it can make moves
        CuT.makeMove(middle);

        // Move to the middle of the board with lots of space to move
        List<Set<Move>> actions = CuT.getMoves();
        Set<Move> jumps = actions.get(1);

        assert(jumps.size() == 0);

        CuT.makeMove(backwardJumpPrep);
        CuT.makeMove(forwardJumpPrep);
        CuT.makeMove(backwardJumpPrep2);
        CuT.makeMove(forwardJumpPrep2);

        piece = new Piece(PieceType.SINGLE, GameColor.RED);
        CuT.getJumpsForSpace(jumps, CuT.getSpace(middle.getEndRow(), middle.getEndCell()), piece);

        int jumpsIfSingle = jumps.size();
        assert(jumpsIfSingle != 0);
        piece = new Piece(PieceType.KING, GameColor.RED);

        CuT.getJumpsForSpace(jumps, CuT.getSpace(middle.getEndRow(), middle.getEndCell()), piece);
        int jumpsIfKing = jumps.size();

        assert(jumpsIfKing > jumpsIfSingle);

    }
}
