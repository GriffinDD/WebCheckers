package com.webcheckers.model;

import com.webcheckers.util.GameColor;
import com.webcheckers.util.MenuState;

/**
 * The Game class contains two players and a game board, as well as relevant attributes of those players.
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 * @author Benjamin LaGreca
 */

public class Game {

    //game has a player, BoardView, MenuState for each of the 2 players, red and white
    private Player redPlayer;
    private Player whitePlayer;
    private BoardView whiteboard;
    private BoardView redboard;
    private MenuState redState = MenuState.HOME;
    private MenuState whiteState = MenuState.HOME;
    //contains the current active color of the game
    private GameColor activeColor;

    /**
     * sets this game's red player
     * @param redPlayer, the player to be assigned red for the game's duration
     */
    public void setRedPlayer(Player redPlayer) {
        this.redPlayer = redPlayer;
    }
    /**
     * sets this game's white player
     * @param whitePlayer, the player to be assigned white for the game's duration
     */
    public void setWhitePlayer(Player whitePlayer) {
        this.whitePlayer = whitePlayer;
    }
    /**
     * sets this game's red board
     * @param board, the board to be assigned to the red player for the games duration
     */
    public void setRedBoard(BoardView board) {
        this.redboard = board;
    }
    /**
     * sets this game's white board
     * @param board, the board to be assigned to the white player for the games duration
     */
    public void setWhiteBoard(BoardView board) {
        this.whiteboard = board;
    }
    /**
     * gets this game's red player
     * @return redPlayer, this game's red player for the duration
     */
    public Player getRedPlayer() {
        return redPlayer;
    }
    /**
     * gets this game's white player
     * @return whitePlayer, this game's white player for the duration
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * gets the other player than the given player
     * @param player, the player who is not being searched for
     * @return the player corresponding to the color other than given
     */
    public Player getOtherPlayer(Player player){
        if(player.equals(redPlayer)){
            return whitePlayer;
        }else if(player.equals(whitePlayer)){
            return redPlayer;
        }
        return new Player("None (this is an error to work out if this happens)");
    }
    /**
     * gets this game's red board
     * @return redboard, this game's red board for the duration
     */
    public BoardView getRedBoard() {
        return redboard;
    }
    /**
     * gets this game's white board
     * @return whiteboard, this game's white board for the duration
     */
    public BoardView getWhiteBoard() {
        return whiteboard;
    }

    /**
     * ends the game, nullifying the players and incrementing the winner's victories
     * @param winner, the player to recieve a won game
     */
    public void endGame(Player winner){
        this.redPlayer = null;
        this.whitePlayer = null;
        winner.addWin();
    }

    /**
     * sets this game's red menu state
     * @param state, the menu state of the red side
     */
    public void setRedState(MenuState state){
        redState = state;
    }
    /**
     * sets this game's white menu state
     * @param state, the menu state of the white side
     */
    public void setWhiteState(MenuState state){
        whiteState = state;
    }

    /**
     * gets this game's red menu state
     * @return the red side's menu state
     */
    public MenuState getRedState(){
        return this.redState;
    }
    /**
     * gets this game's white menu state
     * @return the white side's menu state
     */
    public MenuState getWhiteState(){
        return this.whiteState;
    }

    /**
     * gets this game's active color
     * @return the color denoted as active, whose turn it is
     */
    public GameColor getActiveColor() {
        return activeColor;
    }
    /**
     * sets this game's active color to the given color
     * @param g, the GameColor to be set as active
     */
    public void setActiveColor(GameColor g){
        activeColor = g;
    }
}
