/* 
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera ryangilera@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.map.creator.model;

import java.util.List;

/**
 * A plain old Java object (POJO) class as the template for Map json file.
 *
 * @author Ryan Gilera
 */
public class Map {

    private String mapID;
    private String name;
    private int[] size;
    private int numOfPlayers;

    private int[] listOfPlayer1StartPosition;
    private int[] listOfPlayer2StartPosition;
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
     * @param listOfPlayer1StartPosition Sets the map's Player 1 start tile
     * position
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
     * @param listOfPlayer2StartPosition Sets the map's Player 2 start tile
     * position
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

}
