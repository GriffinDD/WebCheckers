package com.webcheckers.application;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.MenuState;


import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
/**
 * This object helps maintain data independent of specific game instance
 * Think of this as the "master class" that holds all players and Game(s)
 * (may add multiple game functionality in the future
 * @author Joseph Casale
 * @author Griffin Danner-Doran
 * @author Benjamin LaGreca
 */
public class PlayerLobby {
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    // Attributes
    private Set<String> playerNames = new HashSet<>();
    private Set<Player> players = new  HashSet<>();
    private Set<Player> playersInGame = new HashSet<>();
    private Game game;  // Would need to change this if trying to implement multiple games

    // Methods
    /**
     * Default constructor, starts as a blank lobby with no current game
     */
    public PlayerLobby(){
        this.game = null;
    }

    /**
     * Method adds a new name to the set of names, unless the name already exists.
     * @param name name to add
     * @return Boolean true if successful, false if name already exists.
     */
    public boolean addPlayerName(String name){
        if(playerNames.contains(name)){
            return false;
        }
        playerNames.add(name);
        players.add(new Player(name));
        LOG.finer("Player joined the Lobby: " + name);
        return true;
    }

    /**
     * Adds a new player to the game
     * @param name
     */
    private void addPlayerNameInGame(String name){
        playersInGame.add(new Player(name));
        LOG.finer("Player joined the Current Game Lobby: " + name);
    }

    /**
     * Gets list of players excluding the player that called it
     * @param currentUser The user to exclude
     * @return String separated by breaklines for html formatting
     */
    public String getListOfPlayers(String currentUser){
        StringBuilder builder = new StringBuilder();
        
        playerNames.stream().filter(name -> !name.equals(currentUser))
                            .forEach( (String name) -> builder.append(name + "<br>"));

        return builder.toString();
    }

    /**
     * Puts a player in the game
     * @param p player to put in the game
     */
    public void putInGame(Player p) {
        this.addPlayerNameInGame(p.getName());
    }

    /**
     * Remove players from the in game list
     */
    public void endGame(){
        this.game = null;
        playersInGame.clear();
    }

    /**
     * Returns true if the player is in the game
     * @param p player to check for
     * @return true or false
     */
    public boolean inGame(Player p){return playersInGame.contains(p);}

    /**
     * Get the game object
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Makes a new game and updates the player lobby's corresponding attribute
     */
    public void addGame(){
        this.game = new Game();
    }

    /**
     * Returns true if the player is in a game
     * @param name name of the player to check for
     * @return true or false
     */
    public boolean hasPlayer(String name){
        return playerNames.contains(name);
    }

    /**
     * Gets the number of players in the lobby to use on the homepage
     * @return number of players
     */
    public int getNumberOfPlayers(){
        return playerNames.size();
    }

    /**
     * clear the lobby
     */
    public void clearPlayers(){
        playerNames.clear();
    }

    /**
     * Remove a player from the game lobby
     * @param name
     */
    private void removePlayerName(String name){
        Player rem = new Player(name);
        players.remove(rem);
        playerNames.remove(name);
    }

    /**
     * Remove a Player from the game based off player object
     * @param player to remove
     */

    public void removePlayer(Player player){
        removePlayerName(player.getName());
    }

    /**
     * Remove a player from the game so they can be in a new one later.
     * @param player
     */
    public void removePlayerFromGame(Player player){
        playersInGame.remove(player);
    }

    /**
     * Gets a player from the game lobby
     * @param name player name
     * @return the player
     */
    public Player getPlayer(String name){
        for (Player p : players){
            String n = p.getName();
            if (n.equals(name)){
                return p;
            }
            
        }
        return new Player("");
    }

    /**
     * Gets the gameboard for the red player. ( used to prevent law of demeter violations)
     * @return the red board
     */
    public BoardView getRedGameBoard(){
        return game.getRedBoard();
    }


    /**
     * Gets the gameboard for the white player. ( used to prevent law of demeter violations)
     * @return the white board
     */
    public BoardView getWhiteGameBoard(){
        return game.getWhiteBoard();
    }

    /**
     * Gets the sets the target players state in the player lobby lists. ( used to prevent law of demeter violations
     * and to reduce logic repetition)
     * @param player, the player being updated
     * @param state, the menu state to be assigned to the target player
     */
    public void setPlayerState(Player player, MenuState state){
        Player p = getPlayer(player.getName());
        p.setMenuState(state);
    }


    /** Gets the number of players currently in games
     * @return number of players in the game set
     */
    public int getNumberOfPlayersInGames(){
        return playersInGame.size();
    }
}
