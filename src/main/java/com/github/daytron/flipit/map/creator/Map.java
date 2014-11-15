/**
 *
 * Created by Ryan Gilera <jalapaomaji-github@yahoo.com>
 */
package com.github.daytron.flipit.map.creator;

import java.util.List;

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

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public String getMapID() {
        return mapID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public int[] getSize() {
        return this.size;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setListOfBoulders(List<Integer[]> listOfBoulders) {
        this.listOfBoulders = listOfBoulders;
    }

    public List<Integer[]> getListOfBoulders() {
        return listOfBoulders;
    }

    public void setListOfPlayer1StartPosition(int[] listOfPlayer1StartPosition) {
        this.listOfPlayer1StartPosition = listOfPlayer1StartPosition;
    }

    public int[] getListOfPlayer1StartPosition() {
        return listOfPlayer1StartPosition;
    }

    public void setListOfPlayer2StartPosition(int[] listOfPlayer2StartPosition) {
        this.listOfPlayer2StartPosition = listOfPlayer2StartPosition;
    }

    public int[] getListOfPlayer2StartPosition() {
        return listOfPlayer2StartPosition;
    }

    public void setListOfPlayer3StartPosition(int[] listOfPlayer3StartPosition) {
        this.listOfPlayer3StartPosition = listOfPlayer3StartPosition;
    }

    public int[] getListOfPlayer3StartPosition() {
        return listOfPlayer3StartPosition;
    }

    public void setListOfPlayer4StartPosition(int[] listOfPlayer4StartPosition) {
        this.listOfPlayer4StartPosition = listOfPlayer4StartPosition;
    }

    public int[] getListOfPlayer4StartPosition() {
        return listOfPlayer4StartPosition;
    }
}
