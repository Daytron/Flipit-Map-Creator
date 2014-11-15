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
    public final static String LOG_WARNING = "[WARNING]\n";
    public final static String LOG_ERROR = "[ERROR]\n";
    public final static String LOG_TILE_OVERWRITTEN = "[TILE OVERWRITTEN]\n";
    public final static String LOG_TILE_SET = "[TILE SET]\n";
    public final static String LOG_NEW_MAP = "[NEW MAP CREATED]\n";
    public final static String LOG_OPEN_MAP = "[FILE MAP OPENED]\n";
    public final static String LOG_SAVE_MAP = "[FILE MAP SAVED]\n";
    public final static String LOG_TITLE_SET = "[TITLE SET]\n";
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

    // DIALOG MSGs
    public static final String DIALOG_QUIT_HEAD_MSG = "Application Exit";
    public static final String DIALOG_QUIT_BODY_MSG = "Are you sure you want to exit?";
    
    public static final String DIALOG_WARNING_SAVE_HEAD_MSG = "Missing Requirement";
    
    public static final String DIALOG_QUIT_HEAD_MSG_NOT_SAVE = "Unsave map detected.";
    public static final String DIALOG_QUIT_BODY_MSG_NOT_SAVE = "Are you sure you want to exit? Please make sure current map is save or it will be lost.";
    
    public static final String DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE = "Unsave map detected.";
    public static final String DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE = "Are you sure you want to continue? Please make sure current map is save or it will be lost.";
}
