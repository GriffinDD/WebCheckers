package com.webcheckers.model;


import com.webcheckers.util.MenuState;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class PlayerTest {

    private static final String name1 = "bob";
    private static final String name2 = "john";
    private static final String name3 = "john";
    private static final int wins = 3;

    /**
     * Test that a player will be created correctly using the constructor
     */
    @Test
    public void constructor(){
        final Player CuT = new Player(name1);
        assertEquals(name1, CuT.getName());
    }

    /**
     * Test that a players win record is correctly stored and accessed
     */
    @Test
    public void winRecords(){
        Player CuT = new Player(name1);
        assertEquals(0, CuT.getNumGamesWon());
        CuT.addWin();
        CuT.addWin();
        CuT.addWin();
        assertEquals(wins, CuT.getNumGamesWon());
    }

    /**
     * Test that the equivalence between players is accurately calculated between player objects
     */
    @Test
    public void equivalenceAmongPlayers(){
        final Player p1 = new Player(name1);
        final Player p2 = new Player(name2);
        Player p3 = new Player(name3);
        assertNotEquals(p2, p1);
        assertEquals(p3, p2);
        p3.addWin();
        assertEquals(p3, p2);
    }

    /**
     * Test that the equivalence of the Player object works when compared to itself and non-Player objects
     */
    @Test
    public void objectEquivalence(){
        final Player CuT = new Player(name1);
        assertEquals(CuT, CuT);
        assertNotEquals(CuT, null);
        assertFalse(CuT.equals(wins));
        assertFalse(CuT.equals(name1));
    }

    /**
     * Test that a players menu state is correctly set and accessed
     */
    @Test
    public void MenuStates(){
        Player CuT = new Player(name1);
        CuT.setMenuState(MenuState.HOME);
        assertEquals(MenuState.HOME, CuT.getMenuState());
        CuT.setMenuState(MenuState.INHELP);
        assertEquals(MenuState.INHELP, CuT.getMenuState());
        CuT.setMenuState(MenuState.INTHEME);
        assertEquals(MenuState.INTHEME, CuT.getMenuState());
        CuT.setMenuState(MenuState.TRANSITION);
        assertEquals(MenuState.TRANSITION, CuT.getMenuState());
    }
}
