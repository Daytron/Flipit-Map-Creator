/**
 *
 * Created by Ryan Gilera <jalapaomaji-github@yahoo.com>
 */
package com.github.daytron.flipit.map.creator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

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

    // MainApp object
    private MainApp app;

    // Flag for letting canvas know an object can be applied to the map
    private boolean isEditMapOn;

    // The key word for the canvas to know which button is pressed and 
    // apply necessary tile modification when user click the canvas
    private String tileToEdit;

    // The GraphicsContext of the canvas
    private GraphicsContext gc;

    // Log notes
    private StringBuilder logMessage;
    private boolean preventNewLineAtFirst;

    // Time formatter
    private SimpleDateFormat dateFormatter;

    // Map
    private Map map;

    // Map variables
    // Size of a tile
    private double gridXSpace;
    private double gridYSpace;

    private double preferredHeight;
    private double preferredWidth;

    // The padding for x and y position of the map to the canvas
    private double halfPaddingWidth;
    private double halfPaddingHeight;

    private int numberOfRows;
    private int numberOfColumns;

    // List of coordinates that dictates the boundary of a tile
    private List<Double> rowCell;
    private List<Double> columnCell;

    // Tile  variables
    private int tileEdgeEffect;
    private List<Integer[]> listOfBoulders;

    // User OS
    private String userOS = GlobalSettings.USER_OS;

    // Flag to differentiate from open and new map
    private boolean isOpeningAMap = false;

    // Flag to know if current map is save or not
    private boolean isCurrentMapSave = true;

    // Flag to know if no map is opened or generated
    private boolean isThereAMapVisible = false;

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
        // Extract GraphicsContext from canvas
        this.gc = this.canvas.getGraphicsContext2D();

        this.dateFormatter = new SimpleDateFormat("hh:mm");
        // Init logArea
        this.logMessage = new StringBuilder();
        this.preventNewLineAtFirst = true;

        this.logArea.setText("");
        this.logArea.setEditable(false);
        this.logArea.setWrapText(true);

        this.tileEdgeEffect = GlobalSettings.TILE_EDGE_WIDTH;

        // Resets the flag for detecting button pressed from objects
        this.isEditMapOn = false;

        // Init listener for log textarea to autoscroll bottom
        this.logArea.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                    Object newValue) {
                logArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top 
            }
        });

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
        this.numberOfColumns = (int) GlobalSettings.COLUMN_DEFAULT_VALUE;

        this.row_combo.getSelectionModel().select(
                GlobalSettings.ROW_DEFAULT_VALUE);
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
     * Adds and process a new log message from the received String argument.
     *
     * @param message The full text message of the log event
     */
    private void addNewLogMessage(String message) {
        Date date = new Date();
        String timeFormat = this.dateFormatter.format(date);

        String separator = GlobalSettings.LOG_SEPARATOR;

        // Prevents to create new line on first log
        if (this.preventNewLineAtFirst) {
            this.preventNewLineAtFirst = false;
        } else {
            this.logMessage.append("\n");
        }
        
        this.logMessage.append(separator)
                    .append("[")
                    .append(timeFormat)
                    .append("] ")
                    .append(message);

        this.logArea.setText(this.logMessage.toString());

        // This is necessary to trigger listener for 
        // textarea to autoscroll to bottom
        this.logArea.appendText("");
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
        this.isEditMapOn = false;

        if (!this.isOpeningAMap) {
            // Resets list of boulders and new map
            // only if it is a new map
            this.listOfBoulders = new ArrayList<>();
            this.map = new Map();
        }
        // Resets new columnCell and rowCell
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();

        // Apply appropriate edge effect
        if (this.numberOfColumns > 8 && this.numberOfRows > 8) {
            this.tileEdgeEffect = 2;
        } else {
            this.tileEdgeEffect = 4;
        }

        // Clears any previous drawings
        this.gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        // ################## GENERATE MAP #################//
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();

        this.preferredHeight = ((int) y / this.numberOfRows) * (double) this.numberOfRows;
        this.preferredWidth = ((int) x / this.numberOfColumns) * (double) this.numberOfColumns;

        // Padding space for width and height
        this.halfPaddingWidth = (x - preferredWidth) / 2;
        this.halfPaddingHeight = (y - preferredHeight) / 2;

        // space between each cell
        this.gridXSpace = this.preferredWidth / this.numberOfColumns;
        this.gridYSpace = this.preferredHeight / this.numberOfRows;

        this.gc.setLineWidth(2);

        // generate rows
        for (double yi = this.halfPaddingHeight; yi <= (y - this.halfPaddingHeight); yi = yi + this.gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }

        // generate columns
        for (double xi = this.halfPaddingWidth; xi <= (x - this.halfPaddingWidth); xi = xi + this.gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }

        String msgMapDrawnLog;

        if (!this.isOpeningAMap) {
            // Fill grid tiles with neutral color
            for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
                for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {
                    this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                            GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                            GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                            count_column, count_row);

                }
            }

            // Prepare log message
            msgMapDrawnLog = GlobalSettings.LOG_NEW_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows";
        } else {
            // Fill grid tiles from the save map data file
            for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
                for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {
                    this.paintTile(this.extractPositionColor(count_column, count_row, 1),
                            this.extractPositionColor(count_column, count_row, 2),
                            this.extractPositionColor(count_column, count_row, 3),
                            count_column, count_row);

                }
            }

            // Paint tile for player 1 start position
            this.paintPlayerStart(1,
                    this.map.getListOfPlayer1StartPosition()[0],
                    this.map.getListOfPlayer1StartPosition()[1]);

            // Paint tile for player 2 start position
            this.paintPlayerStart(2,
                    this.map.getListOfPlayer2StartPosition()[0],
                    this.map.getListOfPlayer2StartPosition()[1]);

            // Prepare log message
            msgMapDrawnLog = GlobalSettings.LOG_OPEN_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows"
                    + "\n Map file opened: " + path;
        }

        // Notify user in log area
        this.addNewLogMessage(msgMapDrawnLog);

        // Toggles a flag to know a map is now visible from the canvas.
        // This is use for preventing the object buttons (player, boulderm 
        // and neutral tiles) to initiate
        // Alternatively the condition (this.map != null) can also be used)
        // But a more verbose variable name is better suited
        this.isThereAMapVisible = true;
    }

    /**
     * Extracts the tile color of a tile when generating a new map.
     *
     * @param column The column position (with 0 as base, instead of 1)
     * @param row The row position (with 0 as base, instead of 1)
     * @param type The color type. 1 is for the light edge color, 2 is for the
     * main body color and 3 is for shadow edge color.
     * @return The tile color (Hex).
     */
    private String extractPositionColor(int column, int row, int type) {
        String tile_color = "";
        String tile_type = "neutral";

        for (Integer[] pos : this.map.getListOfBoulders()) {
            if (pos[0] == column + 1 && pos[1] == row + 1) {
                tile_type = "boulder";
            }
        }

        switch (tile_type) {
            case "boulder":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.TILE_BOULDER_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettings.TILE_BOULDER_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettings.TILE_BOULDER_SHADOW_EDGE_COLOR;
                        break;
                }
                break;

            case "neutral":
                switch (type) {
                    case 1:
                        tile_color = GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR;
                        break;
                    case 2:
                        tile_color = GlobalSettings.TILE_NEUTRAL_MAIN_COLOR;
                        break;
                    case 3:
                        tile_color = GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR;
                        break;
                }
                break;
        }

        return tile_color;
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
            if (showConfirmDialog(
                    GlobalSettings.DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE,
                    GlobalSettings.DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE)
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

    private void paintTile(String light_edge_color, String main_color, String shadow_edge_color,
            int count_column, int count_row) {
        // Coloring the light top and left edges respectively
        this.gc.setFill(Color.web(light_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, this.tileEdgeEffect);
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.tileEdgeEffect, this.gridYSpace);

        // Coloring main tile body
        this.gc.setFill(Color.web(main_color));
        this.gc.fillRect(this.columnCell.get(count_column) + this.tileEdgeEffect, this.rowCell.get(count_row) + this.tileEdgeEffect, this.gridXSpace - this.tileEdgeEffect, this.gridYSpace - this.tileEdgeEffect);

        // Coloring tile's shadow for bottom and right edges respectively
        this.gc.setFill(Color.web(shadow_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column) + this.tileEdgeEffect, this.rowCell.get(count_row) + this.gridYSpace - this.tileEdgeEffect, this.gridXSpace - this.tileEdgeEffect, this.tileEdgeEffect);
        this.gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - this.tileEdgeEffect, this.rowCell.get(count_row) + this.tileEdgeEffect, this.tileEdgeEffect, this.gridYSpace - this.tileEdgeEffect);
    }

    private void paintPlayerStart(int playerNumber, int x, int y) {

        double smallestSide = Math.min(this.gridXSpace, this.gridYSpace);

        double padding;
        double widthRing, heightRing;
        double xAllowance, yAllowance;

        // For drawing the ring
        // Calculate necessary adjustments
        padding = 0.1 * smallestSide;
        this.gc.setLineWidth(0.05 * smallestSide);

        widthRing = smallestSide - (padding * 2);
        heightRing = widthRing;

        xAllowance = (this.gridXSpace - widthRing) / 2;
        yAllowance = (this.gridYSpace - heightRing) / 2;

        this.gc.strokeOval(this.columnCell.get(x - 1) + xAllowance,
                this.rowCell.get(y - 1) + yAllowance,
                widthRing,
                heightRing);

        // For text draw
        int smallestSidePos = Math.min(this.numberOfColumns,
                this.numberOfRows);

        // this is based on these definitions
        // with smallest side (row or column) 5, font size is 40
        // with 6, size is 38
        // with 7, size is 36
        // until 20, size is 10
        int fontSize = 45 - smallestSidePos - (smallestSidePos - 5);

        // Text padding
        double xPaddingPercentage;
        double yPaddingPercentage;

        if (smallestSidePos > 4 && smallestSidePos < 7) {
            xPaddingPercentage = 0.33;
            yPaddingPercentage = 0.65;
        } else if (smallestSidePos > 6 && smallestSidePos < 10) {
            xPaddingPercentage = 0.31;
            yPaddingPercentage = 0.7;
        } else if (smallestSidePos > 9 && smallestSidePos < 13) {
            xPaddingPercentage = 0.29;
            yPaddingPercentage = 0.75;
        } else if (smallestSidePos > 12 && smallestSidePos < 16) {
            xPaddingPercentage = 0.28;
            yPaddingPercentage = 0.75;
        } else if (smallestSidePos > 15 && smallestSidePos < 18) {
            xPaddingPercentage = 0.32;
            yPaddingPercentage = 0.7;
        } else {
            xPaddingPercentage = 0.35;
            yPaddingPercentage = 0.7;
        }

        Font oldFont = this.gc.getFont();
        this.gc.setFont(Font.font("Verdana", fontSize));

        this.gc.strokeText(Integer.toString(playerNumber),
                this.columnCell.get(x - 1) + xAllowance
                + (widthRing * xPaddingPercentage),
                this.rowCell.get(y - 1) + yAllowance
                + (widthRing * yPaddingPercentage));

        // Return back to its original width and font
        this.gc.setLineWidth(1.0);
        this.gc.setFont(oldFont);
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

            this.addNewLogMessage(GlobalSettings.LOG_PLAYER1_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.addNewLogMessage(noMapMsg);
        }

    }

    @FXML
    private void player2StartBtnOnClick(ActionEvent event) {
        if (this.isThereAMapVisible) {
            this.isEditMapOn = true;
            this.tileToEdit = GlobalSettings.TILE_PLAYER2;

            this.addNewLogMessage(GlobalSettings.LOG_PLAYER2_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.addNewLogMessage(noMapMsg);
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

            this.addNewLogMessage(GlobalSettings.LOG_BOULDER_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.addNewLogMessage(noMapMsg);
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

            this.addNewLogMessage(GlobalSettings.LOG_NEUTRAL_ON);
        } else {
            String noMapMsg = GlobalSettings.LOG_WARNING
                    + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
            this.addNewLogMessage(noMapMsg);
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
        if (this.isInsideTheGrid(event.getX(), event.getY())) {
            // Extract tile position from mouseclick coordinates
            int[] tilePos = this.getTilePosition(event.getX(), event.getY());

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
                                this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 1 start position is overwritten!");
                            }

                            // Check if it is previously a player 2 start tile
                            if (isPlayer2Start) {
                                // Remove player 2 start reference
                                this.map.setListOfPlayer2StartPosition(null);
                                this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 2 start position is overwritten!");
                            }

                            // Add tile to list of boulders
                            this.listOfBoulders.add(new Integer[]{tilePos[0], tilePos[1]});

                            // Paint tile to boulder
                            this.paintBoulderTile(tilePos[0], tilePos[1]);

                            // Toggle flag for detecting unsave map
                            this.isCurrentMapSave = false;

                            String msgBoulderSet = GlobalSettings.LOG_TILE_SET
                                    + "Boulder tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.addNewLogMessage(msgBoulderSet);
                        } else {
                            this.addNewLogMessage(GlobalSettings.LOG_NOTE
                                    + "It's already a boulder tile.");
                        }
                        break;

                    case GlobalSettings.TILE_NEUTRAL:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);
                            this.addNewLogMessage(GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // Check if it is previously a player 1 start tile
                        if (isPlayer1Start) {
                            // Remove player 1 start reference
                            this.map.setListOfPlayer1StartPosition(null);
                            this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 1 start position is overwritten!");
                        }

                        // Check if it is previously a player 2 start tile
                        if (isPlayer2Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer2StartPosition(null);
                            this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 2 start position is overwritten!");
                        }

                        // This is necessary to avoid overpainting previous neutral tile
                        if (isBoulder || isPlayer1Start || isPlayer2Start) {
                            // Paint tile to neutral
                            this.paintNeutralTile(tilePos[0], tilePos[1]);

                            // Toggle flag for detecting unsave map
                            this.isCurrentMapSave = false;

                            String msgNeutralSet = GlobalSettings.LOG_TILE_SET
                                    + "Neutral tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.addNewLogMessage(msgNeutralSet);

                        } else {
                            this.addNewLogMessage(GlobalSettings.LOG_NOTE
                                    + "It's already a neutral tile.");
                        }
                        break;

                    case GlobalSettings.TILE_PLAYER1:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // Revert it to neutral tile
                            this.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.addNewLogMessage(GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // Check if it is previously a player 2 start tile
                        // If this is already a player 2 position, it resets the
                        // player 2 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        if (isPlayer2Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer2StartPosition(null);

                            // Revert it to neutral tile
                            this.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 2 start position is overwritten!");
                        }

                        // Paint tile to player 1 start
                        this.paintPlayerStart(1, tilePos[0], tilePos[1]);

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
                        this.addNewLogMessage(msgPlayer1Log);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;

                    case GlobalSettings.TILE_PLAYER2:
                        if (isBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // Revert it to neutral tile
                            this.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.addNewLogMessage(GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN);
                        }

                        // Check if it is previously a player 1 start tile
                        // If this is already a player 1 position, it resets the
                        // player 1 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        if (isPlayer1Start) {
                            // Remove player 2 start reference
                            this.map.setListOfPlayer1StartPosition(null);

                            // Revert it to neutral tile
                            this.paintNeutralTile(tilePos[0], tilePos[1]);

                            this.addNewLogMessage(GlobalSettings.LOG_WARNING + "Player 1 start position is overwritten!");
                        }

                        // Paint tile to player 2 start
                        this.paintPlayerStart(2, tilePos[0], tilePos[1]);

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
                        this.addNewLogMessage(msgPlayer2Log);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;
                }
                // Update map list boulder data
                this.map.setListOfBoulders(listOfBoulders);
            } else {
                String msgNoObjectSelected = GlobalSettings.LOG_NOTE + "Nothing selected. "
                        + "[" + tilePos[0] + "," + tilePos[1] + "]";
                this.addNewLogMessage(msgNoObjectSelected);
            }
        } else {
            String msg = GlobalSettings.LOG_WARNING + "Mouse clicked "
                    + "[" + event.getX() + "," + event.getY() + "] "
                    + "is outside the grid map!";
            this.addNewLogMessage(msg);
        }
    }

    private void paintNeutralTile(int x, int y) {
        this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                x - 1, y - 1);
    }

    private void paintBoulderTile(int x, int y) {
        this.paintTile(GlobalSettings.TILE_BOULDER_LIGHT_EDGE_COLOR,
                GlobalSettings.TILE_BOULDER_MAIN_COLOR,
                GlobalSettings.TILE_BOULDER_SHADOW_EDGE_COLOR,
                x - 1, y - 1);
    }

    private boolean isInsideTheGrid(double x_pos, double y_pos) {
        return (x_pos >= this.halfPaddingWidth && x_pos <= (this.canvas.getWidth() - this.halfPaddingWidth))
                && (y_pos >= this.halfPaddingHeight && y_pos <= (this.canvas.getWidth() - this.halfPaddingHeight));
    }

    /**
     * Extracts grid position from mouseclick coordinates. Uses a simple binary
     * search.
     *
     * @param x_pos Mouseclick x coordinate
     * @param y_pos Mouseclick y coordinate
     * @return Returns tile position [column,row]
     */
    private int[] getTilePosition(double x_pos, double y_pos) {
        int tile_x, tile_y;

        // For locating column position
        int highX = this.columnCell.size() - 1;
        int lowX = 0;
        int midX;

        while (lowX <= highX) {
            midX = lowX + (highX - lowX) / 2;

            if (x_pos < this.columnCell.get(midX)) {
                highX = midX;
            } else if (x_pos > this.columnCell.get(midX)) {
                lowX = midX;
            } else {
                break;
            }

            if (lowX + 1 == highX) {
                break;
            }
        }

        // For locating row position
        tile_x = lowX + 1;

        int highY = this.rowCell.size() - 1;
        int lowY = 0;
        int midY;

        while (lowY <= highY) {
            midY = lowY + (highY - lowY) / 2;

            if (y_pos < this.rowCell.get(midY)) {
                highY = midY;
            } else if (y_pos > this.rowCell.get(midY)) {
                lowY = midY;
            } else {
                break;
            }

            if (lowY + 1 == highY) {
                break;
            }
        }
        tile_y = lowY + 1;

        return new int[]{tile_x, tile_y};
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
                    this.addNewLogMessage(warningMsg1);

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
                        this.addNewLogMessage(successMsg);
                    } else {
                        String msgHead = "Invalid Title";
                        String msgBody = "Only use alphanumeric and space characters. No leading space.";

                        String invalidMsg = GlobalSettings.LOG_ERROR
                                + msgHead + ". " + msgBody;
                        this.addNewLogMessage(invalidMsg);

                        this.showErrorDialog(msgHead, msgBody);
                    }
                }
            } else {
                String noMapMsg = GlobalSettings.LOG_WARNING
                        + GlobalSettings.LOG_GENERATE_OPEN_MAP_FIRST;
                this.addNewLogMessage(noMapMsg);
            }

        }
    }

    /**
     * Capitalize the first letter of a word.
     *
     * @param word The word to be capitilized
     * @return Returns the formatted word
     */
    private String captilizeFirstLetter(String word) {
        // Removes any unnecessary leading and trailing space
        word = word.trim();

        // Shifts all letters to lowercase
        word = word.toLowerCase();

        // Convert the first letter to uppercase
        String firstLetterCapitalTitle = word.substring(0, 1).toUpperCase()
                + word.substring(1);

        // Converts all first letter of words separated by space to uppercase
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {
                firstLetterCapitalTitle = firstLetterCapitalTitle.substring(0, i + 1)
                        + firstLetterCapitalTitle.substring(i + 1, i + 2).toUpperCase()
                        + firstLetterCapitalTitle.substring(i + 2);
            }
        }

        // Get back the output string reference
        return firstLetterCapitalTitle;
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
        title = this.captilizeFirstLetter(title);

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
        // Confirmation dialog
        if (!isCurrentMapSave) {
            if (showConfirmDialog(
                    GlobalSettings.DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE,
                    GlobalSettings.DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE)
                    == Dialog.ACTION_CANCEL) {
                // Cancel opening file if user press cancel
                return;
            }
        }

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

        // Possible action if user press cancel button on open file dialog
        // Skip all remaining lines
        if (file == null) {
            return;
        }

        // Extract extension name
        String ext = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

        // Extract filename
        String filename = "";
        if (this.userOS.startsWith(GlobalSettings.OS_LINUX)
                || this.userOS.startsWith(GlobalSettings.OS_MAC)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
        } else if (this.userOS.startsWith(GlobalSettings.OS_WINDOWS)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
        } else {
            String noOSsupportMsg = GlobalSettings.LOG_ERROR
                    + GlobalSettings.LOG_OS_NOT_SUPPORTED;
            this.addNewLogMessage(noOSsupportMsg);
            return;
        }

        // Get the first 3 letters for inspection
        String firstWord = filename.substring(0, 3);

        if (file.isFile()) {
            if ("json".equals(ext)) {
                if (firstWord.equals("Map")) {
                    this.openMapFile(file);
                } else {
                    String msgHead = "Invalid Filename";
                    String msgBody = "Map files must "
                            + "start at \"Map\" followed by a number";
                    String invalidPatterName = GlobalSettings.LOG_ERROR
                            + msgHead + ". " + msgBody;

                    this.addNewLogMessage(invalidPatterName);
                    this.showErrorDialog(msgHead, msgBody);
                }
            } else {
                String msgHead = "Invalid Extension";
                String msgBody = "Map files are "
                        + ".json files";
                String invalidExtension = GlobalSettings.LOG_ERROR
                        + msgHead + ". " + msgBody;

                this.addNewLogMessage(invalidExtension);
                this.showErrorDialog(msgHead, msgBody);
            }
        } else {
            String msgHead = "No File Detected";
            String msgBody = "Not a proper file.";
            String invalidFile = GlobalSettings.LOG_ERROR
                    + msgHead + ". " + msgBody;

            this.addNewLogMessage(invalidFile);
            this.showErrorDialog(msgHead, msgBody);
        }

    }

    /**
     * Opens the map json file and set it to the map instance variable.
     *
     * @param file The map json file
     */
    private void openMapFile(File file) {
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file));

            //convert the json string back to object
            this.map = gson.fromJson(br, Map.class);

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

            this.generateMap(file.getPath());

        } catch (IOException e) {
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Error loading map (invalid json map file). ";
            this.addNewLogMessage(errorMsg);

            this.showExceptionDialog(e);

            e.printStackTrace();
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
            this.addNewLogMessage(emptyMapMsg);
            this.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyMapMsg);
            return;
        }

        if (this.map.getListOfPlayer1StartPosition() == null) {
            String emptyP1StartMsg = GlobalSettings.LOG_ERROR
                    + "Player 1 start position is not set!";
            this.addNewLogMessage(emptyP1StartMsg);
            this.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyP1StartMsg);
            return;
        }

        if (this.map.getListOfPlayer2StartPosition() == null) {
            String emptyP2StartMsg = GlobalSettings.LOG_ERROR
                    + "Player 2 start position is not set!";
            this.addNewLogMessage(emptyP2StartMsg);
            this.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyP2StartMsg);
            return;
        }

        if (this.map.getName() == null) {
            String emptyTitleMsg = GlobalSettings.LOG_ERROR
                    + "Map title is not set!";
            this.addNewLogMessage(emptyTitleMsg);
            this.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyTitleMsg);
            return;
        } else if (this.map.getName().isEmpty()) {
            String emptyTitleMsg = GlobalSettings.LOG_ERROR
                    + "Map title is not set!";
            this.addNewLogMessage(emptyTitleMsg);
            this.showWarningDialog(
                    GlobalSettings.DIALOG_WARNING_SAVE_HEAD_MSG,
                    emptyTitleMsg);
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

        // Possible action if user press cancel button on save file dialog
        // Skip all remaining lines
        if (file == null) {
            return;
        }

        String ext = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

        String filename = "";
        if (this.userOS.startsWith(GlobalSettings.OS_LINUX)
                || this.userOS.startsWith(GlobalSettings.OS_MAC)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
        } else if (this.userOS.startsWith(GlobalSettings.OS_WINDOWS)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
        } else {
            String noOSsupportMsg = GlobalSettings.LOG_ERROR
                    + GlobalSettings.LOG_OS_NOT_SUPPORTED;
            this.addNewLogMessage(noOSsupportMsg);

            this.showErrorDialog("OS Not Supported", GlobalSettings.LOG_OS_NOT_SUPPORTED);
            return;
        }

        String firstWord = filename.substring(0, filename.lastIndexOf("."));

        // Prevents user from using a space character on Title
        if (filename.contains(" ")) {
            // Add new log
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + "Filename shouldn't contain space character";
            this.addNewLogMessage(wrongFormatNameMsg);

            // Show a warning dialog
            this.showWarningDialog(
                    GlobalSettings.DIALOG_SAVE_NAME_SPACE_HEAD_MSG,
                    GlobalSettings.DIALOG_SAVE_NAME_SPACE_BODY_MSG);
            return;
        }

        if (!this.isValidFileName(firstWord)) {
            String msgHead = "Invalid Filename Format";
            String msgBody = "Should be [\"Map\"] + [3 digit number].json."
                    + "Example: Map004.json";
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + msgHead + ". " + msgBody;
            this.addNewLogMessage(wrongFormatNameMsg);
            this.showErrorDialog(msgHead, msgBody);

            return;
        }

        if ("json".equalsIgnoreCase(ext)) {
            // Formay the mapId to proper accepted format
            // E.g. Map002
            firstWord = this.formatMapID(firstWord);

            // Apply data to map file
            this.map.setMapID(firstWord);
            this.map.setSize(new int[]{this.numberOfColumns, this.numberOfRows});

            this.saveFile(file);
        } else {
            // Update log
            String invalidExtMsg = GlobalSettings.LOG_ERROR
                    + "Invalid extension file!";
            this.addNewLogMessage(invalidExtMsg);

            // Show error dialog
            this.showErrorDialog(
                    GlobalSettings.DIALOG_INVALID_EXTENSION_HEAD_MSG,
                    GlobalSettings.DIALOG_INVALID_EXTENSION_BODY_MSG);
        }

    }

    /**
     * Verifies if the filename follows the name convention: "Map" + [3 digit
     * number].
     *
     * @param filename The filename to inspect.
     * @return Returns true if filename is valid, otherwise false.
     */
    private boolean isValidFileName(String filename) {
        if (!filename.startsWith("Map")) {
            return false;
        }

        if (filename.length() != 6) {
            return false;
        }

        if (!(Character.isDigit(filename.charAt(3))
                || Character.isDigit(filename.charAt(4))
                || Character.isDigit(filename.charAt(5)))) {
            return false;
        }

        return true;
    }

    /**
     * Formats the map's ID.
     *
     * @param id The ID to be formatted.
     * @return Returns the formatted ID.
     */
    private String formatMapID(String id) {
        return this.captilizeFirstLetter(id);
    }

    /**
     * Saves the map object as a json file.
     *
     * @param file The map file to save.
     */
    private void saveFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(this.map);

        try {
            //write converted json data to a file 
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();

            // Add new log
            String successSaveMsg = GlobalSettings.LOG_SAVE_MAP
                    + "File is successfully saved at " + file.getPath();
            this.addNewLogMessage(successSaveMsg);

            // Toggle flag for detecting unsave map
            this.isCurrentMapSave = true;

            // Prepares for taking snapshot of the canvas
            WritableImage writableImage = new WritableImage(
                    (int) this.canvas.getWidth(),
                    (int) this.canvas.getHeight());

            this.canvas.snapshot(null, writableImage);

            // Get the last filepath to use for saving image file
            String filePath;

            if (this.userOS.startsWith(GlobalSettings.OS_LINUX)
                    || this.userOS.startsWith(GlobalSettings.OS_MAC)) {
                filePath = file.getPath().substring(0, file.getPath().lastIndexOf("/") + 1);
            } else {
                // Other OS is already filtered above, so this is definitely Windows
                filePath = file.getPath().substring(0, file.getPath().lastIndexOf("\\") + 1);
            }

            // Build full path 
            String imageFilePath = filePath + this.map.getMapID() + ".png";

            // Create file
            File fileImage = new File(imageFilePath);

            // Try saving it as a PNG image file
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),
                        "png", fileImage);
            } catch (Exception s) {
                this.showExceptionDialog(s);
            }

        } catch (IOException e) {
            // Add new log
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Unable to save file.";
            this.addNewLogMessage(errorMsg);

            // Show exception dialog
            this.showExceptionDialog(e);

            e.printStackTrace();
        }
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

                if (showConfirmDialog(msgHead,
                        msgBody) == Dialog.ACTION_CANCEL) {
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

        if (this.showConfirmDialog(msgHead,
                msgBody) == Dialog.ACTION_OK) {
            // This is a preferred exit solution for any JavaFX application
            // instead of System.exit(0)
            Platform.exit();
        }

    }

    /**
     * Creates and shows a warning dialog.
     *
     * @param msgHead The masthead message for the dialog.
     * @param msgBody The message body for the dialog.
     */
    private void showWarningDialog(String msgHead, String msgBody) {
        Dialogs.create()
                .owner(this.app.getStage())
                .title("Warning")
                .masthead(msgHead)
                .message(msgBody)
                .showWarning();
    }

    /**
     * Creates and shows an error dialog.
     *
     * @param msgHead The masthead message for the dialog.
     * @param msgBody The message body for the dialog.
     */
    private void showErrorDialog(String msgHead, String msgBody) {
        Dialogs.create()
                .owner(this.app.getStage())
                .title("Error")
                .masthead(msgHead)
                .message(msgBody)
                .showError();
    }

    /**
     * Creates and shows an exception dialog.
     *
     * @param e The exception to show.
     */
    private void showExceptionDialog(Exception e) {
        Dialogs.create()
                .owner(this.app.getStage())
                .title("Exception")
                .masthead(e.toString())
                .message(e.getMessage())
                .showException(e);
    }

    /**
     * Creates and shows a confirmation dialog, with available actions, OK and
     * CANCEL
     *
     * @param msgHead The masthead message for the dialog
     * @param msgBody The message body for the dialog
     * @return returns an Action object as the response from the confirmation
     * dialog created
     */
    private Action showConfirmDialog(String msgHead, String msgBody) {
        Action response = Dialogs.create()
                .owner(this.app.getStage())
                .title("Confirmation")
                .masthead(msgHead)
                .message(msgBody)
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showConfirm();

        return response;
    }

    /**
     * The event handler when Log>Clear menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void meuLogClearLogOnClick(ActionEvent event) {
        this.logMessage = new StringBuilder();
        this.preventNewLineAtFirst = true;
        this.logArea.setText("");
    }

    /**
     * The event handler when About>About App menu item is clicked.
     *
     * @param event The ActionEvent object
     */
    @FXML
    private void menuAboutOnClick(ActionEvent event) {
    }

    /**
     * @return returns <tt>true</tt> if a map is generated on the canvas,
     * otherwise false
     */
    public boolean isThereAMapVisible() {
        return isThereAMapVisible;
    }

}
