/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.map.creator;

/**
 *
 * @author ryan
 */
public class GlobalSettings {
    
    // Default values for map size comboboxes
    public static final Integer COLUMN_DEFAULT_VALUE = 10;
    public static final Integer ROW_DEFAULT_VALUE = 10;
    
    // Colors
    public final static String TILE_NEUTRAL_LIGHT_EDGE_COLOR = "#FFFFFF";
    public final static String TILE_NEUTRAL_MAIN_COLOR = "#C6C6C6";
    public final static String TILE_NEUTRAL_SHADOW_EDGE_COLOR = "#333333";
    
    public final static String TILE_BOULDER_LIGHT_EDGE_COLOR = "#FFFFFF";
    public final static String TILE_BOULDER_MAIN_COLOR = "#4F4F4F";
    public final static String TILE_BOULDER_SHADOW_EDGE_COLOR = "#000000"; 
    
    // Tile to edit
    public final static String TILE_BOULDER = "Boulder";
    public final static String TILE_NEUTRAL = "Neutral";
    public final static String TILE_PLAYER1 = "Player1";
    public final static String TILE_PLAYER2 = "Player2";
    
    // Log Messages
    public final static String LOG_WARNING = "[WARNING] ";
    public final static String LOG_ERROR = "[ERROR] ";
    public final static String LOG_TILE_OVERWRITTEN = "[TILE OVERWRITTEN] ";
    public final static String LOG_TILE_SET = "[TILE SET] ";
    public final static String LOG_NEW_MAP = "[NEW MAP CREATED] ";
    public final static String LOG_OPEN_MAP = "[FILE MAP OPENED] ";
    public final static String LOG_TITLE_SET = "[TITLE SET] ";
    public final static String LOG_BOULDER_OVERWRITTEN = "Boulder tile is overwritten!";
    public final static String LOG_OS_NOT_SUPPORTED = "Your Operating System is not yet supported.";
    
    
    public final static String LOG_BOULDER_ON = "[BOULDER TILE SELECTED]";
    public final static String LOG_NEUTRAL_ON = "[NEUTRAL TILE SELECTED]";
    public final static String LOG_PLAYER1_ON = "[PLAYER 1 START SELECTED]";
    public final static String LOG_PLAYER2_ON = "[PLAYER 2 START SELECTED]";
    
    // OS
    public static final String USER_OS = System.getProperty("os.name").toLowerCase();
    public static final String OS_WINDOWS = "win";
    public static final String OS_LINUX = "linux";
    public static final String OS_MAC = "mac";
}
