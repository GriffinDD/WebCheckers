package com.webcheckers.model;

import com.webcheckers.util.MenuState;

import java.util.Objects;

/**
 * Player has a name, games won, and menu state. Base unit of the model tier
 *
 * @author Griffin Danner-Doran
 * @author Joseph Casale
 */
public class Player {
    private String Name;
    //games won, not really used in this project
    private int gamesWon;
    //stores the menu state of the individual player, which is used for checks in menu
    private MenuState state = MenuState.HOME;

    /**
     * creates a new player with the given name
     * @param PlayerName, the given name of this player
     */
    public Player(String PlayerName){
        this.Name = PlayerName;
        this.gamesWon = 0;
    }
    /**
     * gets the name of this player
     * @return this player's name
     */
    public String getName() {
        return Name;
    }
    /**
     * adds one game to this player's number of games won
     */
    public void addWin(){
        gamesWon++;
    }
    /**
     * gets the number of games won by this player
     * @return number of games won
     */
    public int getNumGamesWon(){
        return gamesWon;
    }
    /**
     * sets the menu state of this player
     * @param s, the state to be assigned to this player
     */
    public void setMenuState(MenuState s){
        state = s;
    }
    /**
     * returns this player's menu state
     * @return state, this player's MenuState
     */
    public MenuState getMenuState(){
        return state;
    }
    /**
     * overrides the default equality for player objects
     * @return true if this object is equal to the given
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(Name, player.Name);
    }

    /**
     * overrides the default hashcode representation of the player
     * @return the hashCode representation of this player
     */
    @Override
    public int hashCode() {
        return Objects.hash(Name);
    }
}
