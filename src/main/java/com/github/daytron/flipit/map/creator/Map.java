/**
 *
 * Created by Ryan Gilera <jalapaomaji-github@yahoo.com>
 */
package com.github.daytron.flipit.map.creator;

import java.util.List;

/**
 * A plain old Java object (POJO) class as the template for Map json file. 
 * @author Ryan Gilera
 */
public class Map {
    private String mapID;
    private String name;
    private int[] size;
    private int numOfPlayers;

    private int[] listOfPlayer1StartPosition;
    private int[] listOfPlayer2StartPosition;
    private int[] listOfPlayer3StartPosition;
    private int[] listOfPlayer4StartPosition;
    private List<Integer[]> listOfBoulders;

    /**
     * @param mapID Sets the map's ID
     */
    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    /**
     * @return Returns the map's ID
     */
    public String getMapID() {
        return mapID;
    }

    /**
     * @param name Sets the map's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the map's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param size Sets the map's size
     */
    public void setSize(int[] size) {
        this.size = size;
    }

    /**
     * @return Returns the map's size
     */
    public int[] getSize() {
        return this.size;
    }

    /**
     * @param numOfPlayers Sets the map's number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * @return Returns the map's number of players
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * @param listOfBoulders Sets the map's list of boulder tiles positions
     */
    public void setListOfBoulders(List<Integer[]> listOfBoulders) {
        this.listOfBoulders = listOfBoulders;
    }

    /**
     * @return Returns the map's list of boulder tile positions
     */
    public List<Integer[]> getListOfBoulders() {
        return listOfBoulders;
    }

    /**
     * @param listOfPlayer1StartPosition Sets the map's Player 1 start tile position
     */
    public void setListOfPlayer1StartPosition(int[] listOfPlayer1StartPosition) {
        this.listOfPlayer1StartPosition = listOfPlayer1StartPosition;
    }

    /**
     *
     * @return Returns the map's Player 1 start tile position
     */
    public int[] getListOfPlayer1StartPosition() {
        return listOfPlayer1StartPosition;
    }

    /**
     *
     * @param listOfPlayer2StartPosition Sets the map's Player 2 start tile position
     */
    public void setListOfPlayer2StartPosition(int[] listOfPlayer2StartPosition) {
        this.listOfPlayer2StartPosition = listOfPlayer2StartPosition;
    }

    /**
     * @return Returns the map's Player 2 start tile position
     */
    public int[] getListOfPlayer2StartPosition() {
        return listOfPlayer2StartPosition;
    }

    /**
     * @param listOfPlayer3StartPosition Sets the map's Player 3 start tile position
     */
    public void setListOfPlayer3StartPosition(int[] listOfPlayer3StartPosition) {
        this.listOfPlayer3StartPosition = listOfPlayer3StartPosition;
    }

    /**
     *
     * @return Returns the map's Player 3 start tile position
     */
    public int[] getListOfPlayer3StartPosition() {
        return listOfPlayer3StartPosition;
    }

    /**
     * @param listOfPlayer4StartPosition Sets the map's Player 4 start tile position
     */
    public void setListOfPlayer4StartPosition(int[] listOfPlayer4StartPosition) {
        this.listOfPlayer4StartPosition = listOfPlayer4StartPosition;
    }

    /**
     * @return Returns the map's Player 4 start tile position
     */
    public int[] getListOfPlayer4StartPosition() {
        return listOfPlayer4StartPosition;
    }
}
