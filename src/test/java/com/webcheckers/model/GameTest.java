package com.webcheckers.model;


import com.webcheckers.util.MenuState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@Tag("Model-Tier")
public class GameTest {

    private static final String name1 = "bob";
    private static final String name2 = "john";
    private Player testPlayer;
    private BoardView board;
    private Game CuT = new Game();

    @BeforeEach
    public void setup(){
        board = mock(BoardView.class);
        testPlayer = mock(Player.class);
    }

    /**
     * Test that a Game will be correctly updated and store values
     */
    @Test
    public void settersAndGetters(){
        Player redplayer = new Player(name1);
        Player whiteplayer = new Player(name2);
        CuT.setRedPlayer(redplayer);
        CuT.setWhitePlayer(whiteplayer);
        CuT.setRedBoard(board);
        CuT.setWhiteBoard(board);
        assertEquals(redplayer,  CuT.getRedPlayer());
        assertEquals(whiteplayer,  CuT.getWhitePlayer());
        assertEquals(board,  CuT.getRedBoard());
        assertEquals(board,  CuT.getWhiteBoard());

        assertEquals(MenuState.HOME, CuT.getRedState());
        assertEquals(MenuState.HOME, CuT.getWhiteState());
        CuT.setRedState(MenuState.INTHEME);
        assertEquals(MenuState.INTHEME, CuT.getRedState());
        CuT.setRedState(MenuState.INHELP);
        assertEquals(MenuState.INHELP, CuT.getRedState());
        CuT.setRedState(MenuState.TRANSITION);
        assertEquals(MenuState.TRANSITION, CuT.getRedState());
        CuT.setWhiteState(MenuState.INTHEME);
        assertEquals(MenuState.INTHEME, CuT.getWhiteState());
        CuT.setWhiteState(MenuState.INHELP);
        assertEquals(MenuState.INHELP, CuT.getWhiteState());
        CuT.setWhiteState(MenuState.TRANSITION);
        assertEquals(MenuState.TRANSITION, CuT.getWhiteState());

        assertEquals(whiteplayer,  CuT.getOtherPlayer(redplayer));
        assertEquals(redplayer,  CuT.getOtherPlayer(whiteplayer));
        assertEquals(new Player("None (this is an error to work out if this happens)"),
                CuT.getOtherPlayer(testPlayer));

        CuT.endGame(whiteplayer);
        assertEquals(null,  CuT.getRedPlayer());
        assertEquals(null,  CuT.getWhitePlayer());
    }
}
