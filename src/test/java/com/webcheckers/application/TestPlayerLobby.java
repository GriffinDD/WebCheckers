package com.webcheckers.application;


import com.webcheckers.model.BoardView;
import com.webcheckers.model.Player;
import com.webcheckers.util.MenuState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class TestPlayerLobby {

    private final String name10 = "Johnny";
    private final String name20 = "Stephanie";
    private final String name30 = "Johnny";

    private PlayerLobby CuT;

    @BeforeEach
    public void setup() {
        CuT = new PlayerLobby();
    }

    /**
     * Test that a playerlobby will be created correctly using the constructor
     */
    @Test
    public void constructor(){
        final PlayerLobby CuT = new PlayerLobby();
        assertNull(CuT.getGame());
    }

    /**
     * Test that names are correctly added and accessed
     */
    @Test
    public void addNamesTest(){
        final PlayerLobby CuT = new PlayerLobby();
        boolean name1 = CuT.addPlayerName(name10);
        boolean name2 = CuT.addPlayerName(name20);
        boolean name3 = CuT.addPlayerName(name30);

        assert(name1);
        assert(name2);
        assert(!name3);
        assert(CuT.getNumberOfPlayers() == 2);
        CuT.clearPlayers();
        assert(CuT.getNumberOfPlayers() == 0);
    }

    /**
     * Test for getting the list of players
     */
    @Test
    public void generateNamesString(){
        CuT.addPlayerName(name10);
        CuT.addPlayerName(name20);
        CuT.addPlayerName(name30);

        String names = CuT.getListOfPlayers(new String(""));
        String expected = "Johnny<br>Stephanie<br>";
        
        assert(names.equals(expected));
    }

    /**
     * Test that a game can be created, accessed, and deleted correctly
     */
    @Test
    public void GameTests(){
        final PlayerLobby CuT = new PlayerLobby();
        assertNull(CuT.getGame());
        CuT.addGame();
        assertNotNull(CuT.getGame());
        CuT.getGame().setRedBoard(new BoardView(false));
        assertNotNull(CuT.getRedGameBoard());
        CuT.endGame();
        assertNull(CuT.getGame());
        CuT.addGame();
        assertNotNull(CuT.getGame());
        CuT.getGame().setWhiteBoard(new BoardView(true));
        assertNotNull(CuT.getWhiteGameBoard());
        CuT.endGame();
        assertNull(CuT.getGame());
    }


    /**
     * Test that the playersingame set is modified and used correctly
     */
    @Test
    public void PlayerInGames(){
        CuT.putInGame(new Player(name10));
        CuT.putInGame(new Player(name20));
        assertTrue(CuT.inGame(new Player(name10)));
        assertTrue(CuT.inGame(new Player(name20)));
        assertTrue(CuT.inGame(new Player(name30)));
        assertFalse(CuT.inGame(null));
        assertFalse(CuT.inGame(new Player("bob")));

        assertEquals(2, CuT.getNumberOfPlayersInGames());

        CuT.removePlayerFromGame(new Player(name20));
        assertFalse(CuT.inGame(new Player(name20)));
        assertNotEquals(2, CuT.getNumberOfPlayersInGames());

    }

    /**
     * Test that the players and playernames sets, which are closely related, are working correctly
     */
    @Test
    public void PlayersAndPlayerNames(){
        CuT.addPlayerName(name10);
        CuT.addPlayerName(name20);
        assertTrue(CuT.hasPlayer(name10));
        assertTrue(CuT.hasPlayer(name20));
        assertTrue(CuT.hasPlayer(name30));
        assertFalse(CuT.hasPlayer(null));
        assertFalse(CuT.hasPlayer("bob"));

        assertEquals(new Player(name10), CuT.getPlayer(name10));
        assertEquals(new Player(name20), CuT.getPlayer(name20));
        assertEquals(new Player(""), CuT.getPlayer("bob"));

        CuT.removePlayer(new Player(name20));
        assertFalse(CuT.hasPlayer(name20));

    }

    /**
     * Test that player menu status can be set and accessed correctly
     */
    @Test
    public void MenuStatusTests(){
        CuT.addPlayerName(name10);
        CuT.addPlayerName(name20);
        Player t;
        CuT.setPlayerState(new Player(name10), MenuState.INHELP);
        t = CuT.getPlayer(name10);
        assertEquals(t.getMenuState(), MenuState.INHELP);
        CuT.setPlayerState(new Player(name10), MenuState.INTHEME);
        t = CuT.getPlayer(name10);
        assertEquals(t.getMenuState(), MenuState.INTHEME);
        CuT.setPlayerState(new Player(name20), MenuState.INHELP);
        t = CuT.getPlayer(name20);
        assertEquals(t.getMenuState(), MenuState.INHELP);
        CuT.setPlayerState(new Player(name20), MenuState.INTHEME);
        t = CuT.getPlayer(name20);
        assertEquals(t.getMenuState(), MenuState.INTHEME);

    }

    /**
     * Test for when a current user is getting the list
     */
    @Test
    public void generateNamesString_exclude(){
        CuT.addPlayerName(name10);
        CuT.addPlayerName(name20);
        CuT.addPlayerName(name30);

        String names = CuT.getListOfPlayers(name10);
        String expected = "Stephanie<br>";
        
        assert(names.equals(expected));
    }
}
