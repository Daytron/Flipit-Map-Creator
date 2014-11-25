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
package com.github.daytron.flipit.map.creator.controller;

import com.github.daytron.flipit.map.creator.MainApp;
import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import com.github.daytron.flipit.map.creator.model.Map;
import com.github.daytron.flipit.map.creator.model.DialogManager;
import com.github.daytron.flipit.map.creator.model.GraphicsManager;
import com.github.daytron.flipit.map.creator.model.LogManager;
import com.github.daytron.flipit.map.creator.model.MapManager;
import com.github.daytron.flipit.map.creator.utility.StringUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.Dialog;

/**
 * The controller class that manages and handles all of application's user
 * events.
 *
 * @author Ryan Gilera
 */
public class ViewController implements Initializable {

    @FXML
    private ComboBox<Integer> row_combo;
    @FXML
    private ComboBox<Integer> column_combo;
    @FXML
    private Button generate_map_btn;
    @FXML
    private TextField title_field;
    @FXML
    private Button player1_start_btn;
    @FXML
    private Button player2_start_btn;
    @FXML
    private Button boulder_btn;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane pane_canvas;
    @FXML
    private Button neutral_btn;
    @FXML
    private TextArea logArea;
    @FXML
    private MenuBar menubar;
    @FXML
    private MenuItem menuFileNew;
    @FXML
    private MenuItem menuFileOpen;
    @FXML
    private MenuItem menuFileSave;
    @FXML
    private MenuItem menuHelpAbout;
    @FXML
    private MenuItem menuFileQuit;
    @FXML
    private MenuItem menuLogClear;
    @FXML
    private Menu menuFileRecent;

    // MainApp object
    private MainApp app;

    // Flag for letting canvas know an object can be applied to the map
    private boolean isEditMapOn;

    // The key word for the canvas to know which button is pressed and 
    // apply necessary tile modification when user click the canvas
    private String tileToEdit;

    // Map
    private Map map;
    private File currentFileOpened;

    // Map variables
    private int numberOfRows;
    private int numberOfColumns;

    // Tile  variables
    private List<Integer[]> listOfBoulders;

    // Flag to differentiate from open and new map
    private boolean isOpeningAMap = false;

    // Flag to know if current map is save or not
    private boolean isCurrentMapSave = true;

    // Flag to know if no map is opened or generated
    private boolean isThereAMapVisible = false;

    // List of opened map files
    private List<File> listOfRecentFiles;

    private GraphicsManager graphicsManager;
    private LogManager logManager;

    /**
     * An override method implemented from Initializable interface. Initialize
     * all necessary configurations in launching the application's view.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // ################## INIT #################//
        // Get the only instance of GraphicsManager
        this.graphicsManager = GraphicsManager.getInstance();
        this.graphicsManager.init(this.canvas, this.map);

        // Get the only instance of LogManager
        this.logManager = LogManager.getInstance();
        this.logManager.init(this.logArea);

        // Resets the flag for detecting button pressed from objects
        this.isEditMapOn = false;

        // Init listener for log textarea to autoscroll bottom
        this.logArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                logArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                // Can use Double.MIN_VALUE to scroll to the top 
            }
        });

        // Add Keyboard shortcuts
        this.menuFileNew.setAccelerator(new KeyCodeCombination(KeyCode.N,
                KeyCodeCombination.CONTROL_DOWN));
        this.menuFileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O,
                KeyCodeCombination.CONTROL_DOWN));
        this.menuFileSave.setAccelerator(new KeyCodeCombination(KeyCode.S,
                KeyCodeCombination.CONTROL_DOWN));
        this.menuFileQuit.setAccelerator(new KeyCodeCombination(KeyCode.Q,
                KeyCodeCombination.CONTROL_DOWN));

        this.menuLogClear.setAccelerator(new KeyCodeCombination(KeyCode.C,
                KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));

        this.menuHelpAbout.setAccelerator(new KeyCodeCombination(KeyCode.A,
                KeyCodeCombination.ALT_DOWN));

        // Initialize list of recent map files
        this.listOfRecentFiles = new ArrayList<>();

        // ################## MAP SIZE COMBO BOXES #################//
        // Define column list items
        ObservableList<Integer> columnOptions
                = (ObservableList<Integer>) GlobalSettings.LIST_POSSIBLE_TILES;

        // Define row list items
        ObservableList<Integer> rowOptions
                = (ObservableList<Integer>) GlobalSettings.LIST_POSSIBLE_TILES;

        // Attach lists to their corresponding combo object
        this.column_combo.setItems(columnOptions);
        this.row_combo.setItems(rowOptions);

        // Set default value for each combobox
        this.column_combo.getSelectionModel().select(
                GlobalSettings.COLUMN_DEFAULT_VALUE);
        // Saves current column selection
        this.numberOfColumns = (int) GlobalSettings.COLUMN_DEFAULT_VALUE;

        this.row_combo.getSelectionModel().select(
                GlobalSettings.ROW_DEFAULT_VALUE);
        // Saves current row selection
        this.numberOfRows = (int) GlobalSettings.ROW_DEFAULT_VALUE;

        // Apply listeners
        this.column_combo.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                column_combo.getSelectionModel().select(newValue);
                numberOfColumns = (int) newValue;
            }
        });

        this.row_combo.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                row_combo.getSelectionModel().select(newValue);
                numberOfRows = (int) newValue;
            }
        });

    }

    /**
     * Generates a map with no arguments. This is called only when Generate
     * button or File>New menu item is clicked.
     */
    private void generateMap() {
        this.generateMap("");
    }

    /**
     * Overloads generateMap method with path as an argument. This is called
     * only when File>Open event starts.
     *
     * @param path The full directory path of the newly opened map.
     */
    private void generateMap(String path) {
        // ################## INIT #################//
        // Resets any previous toggles of this flag
        // Use to notify player/object button is clicked if true.
        this.isEditMapOn = false;

        if (!this.isOpeningAMap) {
            // Resets list of boulders and new map
            // only if it is a new map
            this.listOfBoulders = new ArrayList<>();
            this.map = new Map();
        }

        // Apply appropriate edge effect
        if (this.numberOfColumns > 8 && this.numberOfRows > 8) {
            this.graphicsManager.setTileEdgeEffect(2);
        } else {
            this.graphicsManager.setTileEdgeEffect(4);
        }

        // ################## GENERATE MAP #################//
        this.graphicsManager.generateMap(this.numberOfRows, this.numberOfColumns, this.map);

        // String log message
        String msgMapDrawnLog;

        if (!this.isOpeningAMap) {
            // Calls graphics manager to draw new map
            this.graphicsManager.drawNewMap();

            // Prepare log message
            msgMapDrawnLog = GlobalSettings.LOG_NEW_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows";
        } else {
            // Calls graphics manager to draw opened map
            this.graphicsManager.drawOpenMap();

            // Paint tile for player 1 start position
            this.graphicsManager.paintPlayerStart(1,
                    this.map.getListOfPlayer1StartPosition()[0],
                    this.map.getListOfPlayer1StartPosition()[1]);

            // Paint tile for player 2 start position
            this.graphicsManager.paintPlayerStart(2,
                    this.map.getListOfPlayer2StartPosition()[0],
                    this.map.getListOfPlayer2StartPosition()[1]);

            // Prepare log message
            msgMapDrawnLog = GlobalSettings.LOG_OPEN_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows"
                    + "\n Map file opened: " + path;
        }

        // Notify user in log area
        this.logManager.addNewLogMessage(msgMapDrawnLog);

        // Toggles a flag to know a map is now visible from the canvas.
        // This is use for preventing the object buttons (player, boulderm 
        // and neutral tiles) to initiate
        // Alternatively the condition (this.map != null) can also be used)
        // But a more verbose variable name is better suited
        this.isThereAMapVisible = true;
    }

    /**
     * The event handler when Generate button is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void generateBtnOnClick(ActionEvent event) {
        // Confirmation dialog before proceeding
        // Checks for any unsave previous map
        if (!isCurrentMapSave) {
            if (DialogManager.showConfirmDialog(
                    GlobalSettings.DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE,
                    GlobalSettings.DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE,
                    this.app)
                    == Dialog.ACTION_CANCEL) {
                // Cancel opening file if user press cancel
                return;
            }
        }

        // Toggle flag if the map being generated is from opening a file or new
        this.isOpeningAMap = false;

        this.generateMap();

        // Toggle flag for detecting unsave map data
        this.isCurrentMapSave = false;
    }

    /**
     * The event handler when player 1 start button is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void player1StartBtnOnClick(ActionEvent event) {
        if (this.isThereAMapVisible) {
            this.isEditMapOn = true;
            this.tileToEdit = GlobalSettings.TILE_PLAYER1;
            this.logManager.addNewLogMessage(GlobalSettings.LOG_PLAYER1_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.logManager.addNewLogMessage(noMapMsg);
        }

    }

    @FXML
    private void player2StartBtnOnClick(ActionEvent event) {
        if (this.isThereAMapVisible) {
            this.isEditMapOn = true;
            this.tileToEdit = GlobalSettings.TILE_PLAYER2;
            this.logManager.addNewLogMessage(GlobalSettings.LOG_PLAYER2_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.logManager.addNewLogMessage(noMapMsg);
        }
    }

    /**
     * The event handler when Boulder button is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void boulderBtnOnClick(ActionEvent event) {
        if (this.isThereAMapVisible) {
            this.isEditMapOn = true;
            this.tileToEdit = GlobalSettings.TILE_BOULDER;
            this.logManager.addNewLogMessage(GlobalSettings.LOG_BOULDER_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.logManager.addNewLogMessage(noMapMsg);
        }
    }

    /**
     * The event handler when Neutral button is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void neutralBtnOnClick(ActionEvent event) {
        if (this.isThereAMapVisible) {
            this.isEditMapOn = true;
            this.tileToEdit = GlobalSettings.TILE_NEUTRAL;
            this.logManager.addNewLogMessage(GlobalSettings.LOG_NEUTRAL_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.logManager.addNewLogMessage(noMapMsg);
        }
    }

    /**
     * The event handler when Canvas is clicked.
     *
     * @param event The MouseEvent object
     */
    @FXML
    private void canvasOnClick(MouseEvent event) {
        // Check first if the mouseclick event is inside the grip map
        if (this.graphicsManager.isInsideTheGrid(event.getX(), event.getY())) {
            // Extract tile position from mouseclick coordinates
            int[] tilePos = this.graphicsManager.getTilePosition(
                    event.getX(), event.getY());

            // Check if any tile button (boulder/neutral/player) is activated
            if (this.isEditMapOn) {
                boolean isBoulder = false;
                boolean isPlayer1Start = false;
                boolean isPlayer2Start = false;

                Integer[] boulderReference = null;

                // Check current selected tile if it is a boulder tile
                for (Integer[] boulder : this.listOfBoulders) {
                    if (tilePos[0] == boulder[0] && tilePos[1] == boulder[1]) {
                        isBoulder = true;
                        boulderReference = boulder;
                    }
                }

                // Check current selected tile if it is a player 1 start tile
                if (this.map.getListOfPlayer1StartPosition() != null) {
                    if (tilePos[0] == this.map.getListOfPlayer1StartPosition()[0]
                            && tilePos[1] == this.map.getListOfPlayer1StartPosition()[1]) {
                        isPlayer1Start = true;
                    }
                }

                // Check current selected tile if it is a player 2 start tile
                if (this.map.getListOfPlayer2StartPosition() != null) {
                    if (tilePos[0] == this.map.getListOfPlayer2StartPosition()[0]
                            && tilePos[1] == this.map.getListOfPlayer2StartPosition()[1]) {
                        isPlayer2Start = true;
                    }
                }

                // Apply necessary action to the tile selected
                switch (this.tileToEdit) {
                    case GlobalSettings.TILE_BOULDER:
                        // If the tile selected is not a boulder, change it to boulder
                        if (!isBoulder) {
                            // Check if it is previously a player 1 start tile
                            if (isPlayer1Start) {
                                // Remove player 1 start reference
                                this.map.setListOfPlayer1StartPosition(null);
                                this.logManager.addNewLogMessage(
                                        GlobalSettings.LOG_WARNING
                                        + "Player 1 start position is overwritten!");
                            }

                            // Check if it is previously a player 2 start tile
                            if (isPlayer2Start) {
                                // Remove player 2 start reference
                                this.map.setListOfPlayer2StartPosition(null);
                                this.logManager.addNewLogMessage(
                                        GlobalSettings.LOG_WARNING
                                        + "Player 2 start position is overwritten!");
                            }

                            // Add tile to list of boulders
                            this.listOfBoulders.add(new Integer[]{tilePos[0], tilePos[1]});

                            // Paint tile to boulder
                            this.graphicsManager.paintBoulderTile(tilePos[0], tilePos[1]);

                            // Toggle flag for detecting unsave map
                            this.isCurrentMapSave = false;

                            String msgBoulderSet = GlobalSettings.LOG_TILE_SET
                                    + "Boulder tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.logManager.addNewLogMessage(msgBoulderSet);
                        } else {
                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_NOTE
                                    + "It's already a boulder tile.");
                        }

                        break;

                    case GlobalSettings.TILE_NEUTRAL:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);
                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // Check if it is previously a player 1 start tile
                        if (isPlayer1Start) {
                            // Remove player 1 start reference
                            this.map.setListOfPlayer1StartPosition(null);
                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + "Player 1 start position is overwritten!");
                        }

                        // Check if it is previously a player 2 start tile
                        if (isPlayer2Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer2StartPosition(null);
                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + "Player 2 start position is overwritten!");
                        }

                        // This is necessary to avoid overpainting previous neutral tile
                        if (isBoulder || isPlayer1Start || isPlayer2Start) {
                            // Paint tile to neutral
                            this.graphicsManager.paintNeutralTile(tilePos[0], tilePos[1]);

                            // Toggle flag for detecting unsave map
                            this.isCurrentMapSave = false;

                            String msgNeutralSet = GlobalSettings.LOG_TILE_SET
                                    + "Neutral tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.logManager.addNewLogMessage(msgNeutralSet);

                        } else {
                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_NOTE
                                    + "It's already a neutral tile.");
                        }

                        break;

                    case GlobalSettings.TILE_PLAYER1:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // Revert it to neutral tile
                            this.graphicsManager.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // If there is a previous player 1 start tile
                        if (this.map.getListOfPlayer1StartPosition() != null) {
                            // Check if the selected tile is on the same start position
                            // No need to repaint it again
                            if (tilePos[0] == this.map.getListOfPlayer1StartPosition()[0]
                                    && tilePos[1] == this.map.getListOfPlayer1StartPosition()[1]) {
                                this.logManager.addNewLogMessage(
                                        GlobalSettings.LOG_ERROR
                                        + "You already have selected this tile.");

                                // Reset isEditOn
                                this.isEditMapOn = false;

                                // Break early
                                break;
                            } else {
                                // If not, remove previous start tile 
                                // and repaint it to neutral
                                this.graphicsManager.paintNeutralTile(
                                        this.map.getListOfPlayer1StartPosition()[0],
                                        this.map.getListOfPlayer1StartPosition()[1]);

                                // Just in case, to be safe, set it back to null
                                this.map.setListOfPlayer1StartPosition(null);
                            }
                        }

                        // Check if it is previously a player 2 start tile
                        // If this is already a player 2 position, it resets the
                        // player 2 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        if (isPlayer2Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer2StartPosition(null);

                            // Revert it to neutral tile
                            this.graphicsManager.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + "Player 2 start position is overwritten!");
                        }

                        // Paint tile to player 1 start
                        this.graphicsManager.paintPlayerStart(1, tilePos[0], tilePos[1]);

                        // Toggle flag for detecting unsave map
                        this.isCurrentMapSave = false;

                        // Add start position to map
                        this.map.setListOfPlayer1StartPosition(tilePos.clone());

                        // LOG Message
                        String msgPlayer1Log;
                        if (isPlayer2Start) {
                            msgPlayer1Log = GlobalSettings.LOG_TILE_OVERWRITTEN;
                        } else {
                            msgPlayer1Log = GlobalSettings.LOG_TILE_SET;
                        }
                        msgPlayer1Log += "Player 1 start position is now set to "
                                + "[" + tilePos[0] + "," + tilePos[1] + "]";
                        this.logManager.addNewLogMessage(msgPlayer1Log);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;

                    case GlobalSettings.TILE_PLAYER2:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // Revert it to neutral tile
                            this.graphicsManager.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // If there is a previous player 2 start tile
                        if (this.map.getListOfPlayer2StartPosition() != null) {
                            // Check if the selected tile is on the same start position
                            // No need to repaint it again
                            if (tilePos[0] == this.map.getListOfPlayer2StartPosition()[0]
                                    && tilePos[1] == this.map.getListOfPlayer2StartPosition()[1]) {
                                this.logManager.addNewLogMessage(
                                        GlobalSettings.LOG_ERROR
                                        + "You already have selected this tile.");

                                // Reset isEditOn
                                this.isEditMapOn = false;

                                // Break early
                                break;
                            } else {
                                // If not, remove previous start tile 
                                // and repaint it to neutral
                                this.graphicsManager.paintNeutralTile(
                                        this.map.getListOfPlayer2StartPosition()[0],
                                        this.map.getListOfPlayer2StartPosition()[1]);

                                // Just in case, to be safe, set it back to null
                                this.map.setListOfPlayer2StartPosition(null);
                            }
                        }

                        // Check if it is previously a player 1 start tile
                        // If this is already a player 1 position, it resets the
                        // player 1 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        if (isPlayer1Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer1StartPosition(null);

                            // Revert it to neutral tile
                            this.graphicsManager.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.logManager.addNewLogMessage(
                                    GlobalSettings.LOG_WARNING
                                    + "Player 1 start position is overwritten!");
                        }

                        // Paint tile to player 2 start
                        this.graphicsManager.paintPlayerStart(2, tilePos[0], tilePos[1]);

                        // Toggle flag for detecting unsave map
                        this.isCurrentMapSave = false;

                        // Add start position to map
                        this.map.setListOfPlayer2StartPosition(tilePos.clone());

                        // LOG Message
                        String msgPlayer2Log;
                        if (isPlayer1Start) {
                            msgPlayer2Log = GlobalSettings.LOG_TILE_OVERWRITTEN;
                        } else {
                            msgPlayer2Log = GlobalSettings.LOG_TILE_SET;
                        }
                        msgPlayer2Log += "Player 2 start position is now set to "
                                + "[" + tilePos[0] + "," + tilePos[1] + "]";
                        this.logManager.addNewLogMessage(msgPlayer2Log);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;
                }
                // Update map list boulder data
                this.map.setListOfBoulders(listOfBoulders);
            } else {
                String msgNoObjectSelected = GlobalSettings.LOG_NOTE + "Nothing selected. "
                        + "[" + tilePos[0] + "," + tilePos[1] + "]";
                this.logManager.addNewLogMessage(msgNoObjectSelected);
            }
        } else {
            String msg = GlobalSettings.LOG_WARNING + "Mouse clicked "
                    + "[" + event.getX() + "," + event.getY() + "] "
                    + "is outside the grid map!";
            this.logManager.addNewLogMessage(msg);
        }
    }

    /**
     * The event handler when Title field is on focus.
     *
     * @param event The KeyEvent object
     */
    @FXML
    private void titleFieldOnKeyPressed(KeyEvent event) {
        // Detect if key pressed is Enter key
        if (event.getCode() == KeyCode.ENTER) {
            // If a map is generated either by opening a file or new map,
            // save map title to current map object
            if (this.isThereAMapVisible) {
                String title = this.title_field.getText();

                if (title.isEmpty()) {
                    String warningMsg1 = GlobalSettings.LOG_WARNING
                            + "Title field is empty!";
                    this.logManager.addNewLogMessage(warningMsg1);

                    // Better be safe than sorry
                    return;
                } else {
                    // Only accepts letters, numbers and spaces
                    // No leading space allowed
                    // All trailing spaces are automatically trim
                    if (title.matches("^[a-zA-Z0-9][a-zA-Z0-9\\s]*$")) {
                        title = this.titleFormatter(title);

                        // Set title to the map
                        this.map.setName(title);

                        // Toggle flag for detecting unsave map
                        this.isCurrentMapSave = false;

                        String successMsg = GlobalSettings.LOG_TITLE_SET
                                + "Title: " + title + " is set.";
                        this.logManager.addNewLogMessage(successMsg);
                    } else {
                        String msgHead = "Invalid Title";
                        String msgBody = "Only use alphanumeric and space characters. No leading space.";

                        String invalidMsg = GlobalSettings.LOG_ERROR
                                + msgHead + ". " + msgBody;
                        this.logManager.addNewLogMessage(invalidMsg);

                        DialogManager.showErrorDialog(msgHead, msgBody,
                                this.app);
                    }
                }
            } else {
                String noMapMsg = GlobalSettings.LOG_WARNING
                        + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
                this.logManager.addNewLogMessage(noMapMsg);
            }

        }
    }

    /**
     * Formats the title of the map.
     *
     * @param title The map's title
     * @return Returns the formatted title
     */
    private String titleFormatter(String title) {
        // Capitilize all first letter of a word 
        // separated by space
        title = title.toLowerCase();
        title = StringUtils.capitalizeFirstLetterEachWord(title);

        // Update title field
        this.title_field.setText(title);

        // append row and column
        title += " " + this.numberOfColumns + "x" + this.numberOfRows;

        return title;
    }

    /**
     * The event handler when Menu>Open menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuFileOpenOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Set filechooser title
        fileChooser.setTitle("Open Map File (.json)");

        // Set filechooser initial directory to "home" depending on user OS
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        // Add json extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("JSON Map files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        // Launch filechooser and get open file
        File file = fileChooser.showOpenDialog(this.app.getStage());

        if (this.verifyFileToOpen(file)) {
            this.openMap(file);
        }
    }

    public boolean verifyFileToOpen(File file) {
        return MapManager.verifyFileToOpen(file, this.app);
    }
    
    /**
     * Opens the map json file and set it to the map instance variable.
     *
     * @param file The map json file
     */
    public void openMap(File file) {

        Map tempMapHolder = MapManager.openFile(file, 
                this.isCurrentMapSave, this.app);
        
        if (tempMapHolder != null) {
            this.map = tempMapHolder;
        } else {
            return;
        }
        
        // Toggle flag that the map is an open map file
        this.isOpeningAMap = true;

        // Extract columns and rows
        this.numberOfColumns = this.map.getSize()[0];
        this.numberOfRows = this.map.getSize()[1];

        // Set comboboxes
        this.column_combo.setValue(this.numberOfColumns);
        this.row_combo.setValue(this.numberOfRows);

        // Set title
        String rawTitle = this.map.getName();
        String title = rawTitle.substring(0, rawTitle.lastIndexOf(" "));
        this.title_field.setText(title);

        // Set list of boulders
        this.listOfBoulders = this.map.getListOfBoulders();

        // Generate map
        this.generateMap(file.getPath());

            // Set to current file map
        // Use to compare for recent map menu item
        this.currentFileOpened = file;

        // Add to list of current opened files
        if (!this.listOfRecentFiles.contains(file)) {
            this.listOfRecentFiles.add(file);
        }

        // Update recent files menu items and add listeners
        if (!this.listOfRecentFiles.isEmpty()) {
            // Resets menu items
            this.menuFileRecent.getItems().clear();

            for (File aFile : this.listOfRecentFiles) {
                // Create new menu item
                MenuItem aMenuItem = new MenuItem(aFile.getPath());

                // Add menu item to the recent open menu
                this.menuFileRecent.getItems().add(aMenuItem);

                // Set handler
                aMenuItem.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        // Get back the reference from the menuitem object
                        MenuItem anObject = (MenuItem) event.getSource();

                        // only opens a map if it is not "open" currently
                        if (!currentFileOpened.getPath().equals(anObject.getText())) {
                            openMap(new File(anObject.getText()));
                        } else {
                            String itsAlreadyOpenLogMessage = GlobalSettings.LOG_NOTE
                                    + "Map is already open.";
                            logManager.addNewLogMessage(itsAlreadyOpenLogMessage);
                        }
                    }
                });
            }
        }

    }

    public class RecentMapEventHandler implements EventHandler<ActionEvent> {

        private File openedFile;

        public RecentMapEventHandler(File file) {
            this.openedFile = file;
        }

        @Override
        public void handle(ActionEvent event) {
            System.out.println("it's clicked");
            openMap(openedFile);
        }

    }

    /**
     * The event handler when File>Save menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuFileSaveOnClick(ActionEvent event) {
        // Detect first if all requirements are met
        if (this.map == null) {
            String emptyMapMsg = GlobalSettings.LOG_ERROR
                    + "No map opened or generated.";
            this.logManager.addNewLogMessage(emptyMapMsg);
            DialogManager.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyMapMsg,
                    this.app);
            return;
        }

        if (this.map.getListOfPlayer1StartPosition() == null) {
            String emptyP1StartMsg = GlobalSettings.LOG_ERROR
                    + "Player 1 start position is not set!";
            this.logManager.addNewLogMessage(emptyP1StartMsg);
            DialogManager.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyP1StartMsg,
                    this.app);
            return;
        }

        if (this.map.getListOfPlayer2StartPosition() == null) {
            String emptyP2StartMsg = GlobalSettings.LOG_ERROR
                    + "Player 2 start position is not set!";
            this.logManager.addNewLogMessage(emptyP2StartMsg);
            DialogManager.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyP2StartMsg,
                    this.app);
            return;
        }

        if (this.map.getName() == null) {
            String emptyTitleMsg = GlobalSettings.LOG_ERROR
                    + "Map title is not set!";
            this.logManager.addNewLogMessage(emptyTitleMsg);
            DialogManager.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyTitleMsg,
                    this.app);
            return;
        } else if (this.map.getName().isEmpty()) {
            String emptyTitleMsg = GlobalSettings.LOG_ERROR
                    + "Map title is not set!";
            this.logManager.addNewLogMessage(emptyTitleMsg);
            DialogManager.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyTitleMsg,
                    this.app);
            return;
        }

        // Create a new filchooser for saving file
        FileChooser fileChooser = new FileChooser();

        // Set filechooser title
        fileChooser.setTitle("Save Map File");

        // Set filechooser initial directory to "home" depending on user OS
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );

        // Add json extension filter
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter("JSON Map files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);

        // Launch filechooser and get open file
        File file = fileChooser.showSaveDialog(this.app.getStage());

        if (verifyFileToSave(file)) {
            this.saveMap(file);
        }
    }
    
    public boolean verifyFileToSave(File file) {
        return MapManager.verifyFileToSave(file, this.logManager,
                this.app, this.map, this.numberOfColumns,
                this.numberOfRows);
    }
    
    public void saveMap(File file) {
        this.isCurrentMapSave = MapManager.saveFile(file,
                    this.map, this.canvas, this.app);
    }

    /**
     * The event handler when File>New menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuFileNewOnClick(ActionEvent event) {
        this.generateBtnOnClick(event);
    }

    /**
     * Gets a reference to the MainApp object, once ViewController is initiated.
     * <p>
     * This also setups an eventHandler for handling on close window button
     * click to launch a confirmation dialog
     *
     * @param app The main MainApp object initiated
     */
    public void setApp(MainApp app) {
        this.app = app;

        // For detecting user pressing close window button
        // for exit dialog confirmation process
        this.app.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                String msgHead, msgBody;
                if (isCurrentMapSave) {
                    msgHead = GlobalSettings.DIALOG_QUIT_HEAD_MSG;
                    msgBody = GlobalSettings.DIALOG_QUIT_BODY_MSG;
                } else {
                    msgHead = GlobalSettings.DIALOG_QUIT_HEAD_MSG_NOT_SAVE;
                    msgBody = GlobalSettings.DIALOG_QUIT_BODY_MSG_NOT_SAVE;
                }

                if (DialogManager.showConfirmDialog(msgHead,
                        msgBody, app) == Dialog.ACTION_CANCEL) {
                    // event consume halts any further action (shutting down)
                    // WARNING: Do not mess with else condition with
                    // event.consume() or user won't be able to close the app!!
                    // If somehow you toyed with this idea and can't close
                    // it via window close button, you can alternatively use
                    // File quit menu item and don't forget to remove your stuff.
                    event.consume();
                }

            }
        });
    }

    /**
     * The event handler when File>Quit menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuFileQuitOnClick(ActionEvent event) {
        String msgHead, msgBody;
        if (this.isCurrentMapSave) {
            msgHead = GlobalSettings.DIALOG_QUIT_HEAD_MSG;
            msgBody = GlobalSettings.DIALOG_QUIT_BODY_MSG;
        } else {
            msgHead = GlobalSettings.DIALOG_QUIT_HEAD_MSG_NOT_SAVE;
            msgBody = GlobalSettings.DIALOG_QUIT_BODY_MSG_NOT_SAVE;
        }

        if (DialogManager.showConfirmDialog(msgHead,
                msgBody, this.app) == Dialog.ACTION_OK) {
            // This is a preferred exit solution for any JavaFX application
            // instead of System.exit(0)
            Platform.exit();
        }

    }

    /**
     * The event handler when Log>Clear menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void meuLogClearLogOnClick(ActionEvent event) {
        this.logManager.reset();
    }

    /**
     * The event handler when About>About App menu item is clicked. Opens a
     * dialog window about the application.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuAboutOnClick(ActionEvent event) {
        try {
            Dialog aboutDialog = new Dialog(this.app.getStage(), "About");

            Node viewNode = FXMLLoader.load(getClass().getResource("/fxml/AboutAppView.fxml"));
            aboutDialog.setIconifiable(false);
            aboutDialog.setResizable(false);

            aboutDialog.setContent(viewNode);

            aboutDialog.show();
        } catch (IOException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            DialogManager.showExceptionDialog(ex, this.app);
        }
    }

    /**
     * @return Returns <tt>true</tt> if a map is generated on the canvas,
     * otherwise false
     */
    public boolean isThereAMapVisible() {
        return isThereAMapVisible;
    }

    /**
     * @return Returns true if any of player or object buttons is clicked
     */
    public boolean isEditMapOn() {
        return isEditMapOn;
    }

    @FXML
    private void menuFileRecentOnClick(ActionEvent event) {

    }

}
