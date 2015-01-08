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
package com.github.daytron.flipit.map.creator.utility;

import java.util.List;
import javafx.collections.FXCollections;

/**
 * A static class that holds all of application's constants and settings
 *
 * @author Ryan Gilera
 */
public final class GlobalSettings {

    /**
     * Prevents other classes from instantiating this class.
     */
    private GlobalSettings() {
    }

    /**
     * The application version number
     */
    public static final String VERSION = "1.0.0";
    
    /**
     * Application Icon
     */
    public static final String ICON_PATH = "/images/icon.png";

    // COMBOXES
    /**
     * The default value for the Column Combobox
     */
    public static final Integer COLUMN_DEFAULT_VALUE = 10;
    /**
     * The default value for the Row Combobox
     */
    public static final Integer ROW_DEFAULT_VALUE = 10;

    /**
     * List of possible number of tiles for both row and column Combobox
     */
    public static final List<Integer> LIST_POSSIBLE_TILES
            = FXCollections.observableArrayList(
                    5, 6, 7, 8, 9, 10, 11, 12, 13,
                    14, 15, 16, 17, 18, 19, 20
            );

    // TILES AND COLORS
    /**
     * Neutral tile color for lighter edges.
     */
    public static final String TILE_NEUTRAL_LIGHT_EDGE_COLOR = "#FFFFFF";
    /**
     * Neutral tile color for the body.
     */
    public static final String TILE_NEUTRAL_MAIN_COLOR = "#C6C6C6";
    /**
     * Neutral tile color for shadow edges.
     */
    public static final String TILE_NEUTRAL_SHADOW_EDGE_COLOR = "#333333";

    /**
     * Boulder tile color for lighter edges.
     */
    public static final String TILE_BOULDER_LIGHT_EDGE_COLOR = "#FFFFFF";
    /**
     * Boulder tile color for the body.
     */
    public static final String TILE_BOULDER_MAIN_COLOR = "#4F4F4F";
    /**
     * Boulder tile color for shadow edges.
     */
    public static final String TILE_BOULDER_SHADOW_EDGE_COLOR = "#000000";

    /**
     * The tile edge width that gives a 3D impression. Use for both light and
     * shadow edges.
     */
    public static final int TILE_EDGE_WIDTH = 2;

    // TILE FLAG FOR EDIT
    /**
     * Tile flag for boulder tile
     */
    public static final String TILE_BOULDER = "Boulder";
    /**
     * Tile flag for neutral tile
     */
    public static final String TILE_NEUTRAL = "Neutral";
    /**
     * Tile flag for Player 1 Start tile
     */
    public static final String TILE_PLAYER1 = "Player1";
    /**
     * Tile flag for Player 2 Start tile
     */
    public static final String TILE_PLAYER2 = "Player2";

    // Log Messages
    /**
     * Log String separator, place on top of each log event
     */
    public static final String LOG_SEPARATOR = "--------------------------------"
            + "-------------------\n";
    /**
     * Log title for common events
     */
    public static final String LOG_NOTE = "[NOTE]\n";
    /**
     * Log title for warning events
     */
    public static final String LOG_WARNING = "[WARNING]\n";
    /**
     * Log title for error events
     */
    public static final String LOG_ERROR = "[ERROR]\n   ";
    /**
     * Log title for any tile overwritten events
     */
    public static final String LOG_TILE_OVERWRITTEN = "[TILE OVERWRITTEN]\n";
    /**
     * Log title for tile set verification events
     */
    public static final String LOG_TILE_SET = "[TILE SET]\n";
    /**
     * Log title for new map created events
     */
    public static final String LOG_NEW_MAP = "[NEW MAP CREATED]\n";
    /**
     * Log title for an open map events
     */
    public static final String LOG_OPEN_MAP = "[FILE MAP OPENED]\n";
    /**
     * Log title for saved map events
     */
    public static final String LOG_SAVE_MAP = "[FILE MAP SAVED]\n";
    /**
     * Log title for title set verification events
     */
    public static final String LOG_TITLE_SET = "[TITLE SET]\n";
    /**
     * Log message for any boulder tile overwritten events
     */
    public static final String LOG_BOULDER_OVERWRITTEN = "Boulder tile is overwritten!";
    /**
     * Log message for Operating System not supported events
     */
    public static final String LOG_OS_NOT_SUPPORTED_HEAD_MSG = "OS Not Supported";
    public static final String LOG_OS_NOT_SUPPORTED_BODY_MSG = "Your Operating System is not yet supported.";
    /**
     * Log message for raising a warning to generate a map first event
     */
    public static final String LOG_GENERATE_OPEN_MAP_FIRST = "Generate or open a map first.";

    /**
     * Log message confirmation that boulder tile is selected
     */
    public static final String LOG_BOULDER_ON = "[BOULDER TILE SELECTED]";
    /**
     * Log message confirmation that neutral tile is selected
     */
    public static final String LOG_NEUTRAL_ON = "[NEUTRAL TILE SELECTED]";
    /**
     * Log message confirmation that Player 1 Start tile is selected
     */
    public static final String LOG_PLAYER1_ON = "[PLAYER 1 START SELECTED]";
    /**
     * Log message confirmation that Player 2 Start tile is selected
     */
    public static final String LOG_PLAYER2_ON = "[PLAYER 2 START SELECTED]";

    // OS
    /**
     * Extracts user's running OS
     */
    public static final String USER_OS = System.getProperty("os.name").toLowerCase();
    /**
     * WINDOWS OS
     */
    public static final String OS_WINDOWS = "win";
    /**
     * LINUX OS
     */
    public static final String OS_LINUX = "linux";
    /**
     * MAC OS
     */
    public static final String OS_MAC = "mac";

    // DIALOG MESSAGES
    /**
     * Dialog head message for application exit
     */
    public static final String DIALOG_QUIT_HEAD_MSG = "Application Exit";
    /**
     * Dialog body message for application exit
     */
    public static final String DIALOG_QUIT_BODY_MSG = "Are you sure you want to exit?";
    /**
     * Dialog head message for missing requirement on map save
     */
    public static final String DIALOG_WARNING_SAVE_HEAD_MSG = "Missing Requirement";
    /**
     * Dialog head message for space character detection warning
     */
    public static final String DIALOG_SAVE_NAME_SPACE_HEAD_MSG = "Space Character Detected";
    /**
     * Dialog body message for space character detection warning
     */
    public static final String DIALOG_SAVE_NAME_SPACE_BODY_MSG = "Please remove unnecessary space";
    /**
     * Dialog head message for unsupported file extension
     */
    public static final String DIALOG_INVALID_EXTENSION_HEAD_MSG = "Unsupported File Extension";
    /**
     * Dialog body message for unsupported file extension
     */
    public static final String DIALOG_INVALID_EXTENSION_BODY_MSG = "Please use only .json extension.";
    /**
     * Dialog head message for unsave map in exiting application
     */
    public static final String DIALOG_QUIT_HEAD_MSG_NOT_SAVE = "Unsave Map Detected.";
    /**
     * Dialog body message for unsave map in exiting application
     */
    public static final String DIALOG_QUIT_BODY_MSG_NOT_SAVE = "Are you sure you want to exit? Please make sure current map is save or it will be lost.";
    /**
     * Dialog head message for unsave map in creating a new map
     */
    public static final String DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE = "Unsave Map Detected.";
    /**
     * Dialog body message for unsave map in creating a new map
     */
    public static final String DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE = "Are you sure you want to continue? Please make sure current map is save or it will be lost.";
}
