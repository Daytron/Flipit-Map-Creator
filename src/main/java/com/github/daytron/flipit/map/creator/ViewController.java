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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

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

    // MainAPp object
    private MainApp app;

    private boolean isEditMapOn;
    private String tileToEdit;
    private GraphicsContext gc;

    // Log notes
    private StringBuilder logMessage;
    private boolean preventNewLineAtFirst;

    // Date Time
    private SimpleDateFormat df;

    // Map
    private Map map;

    // Map variables
    private double gridXSpace;
    private double gridYSpace;

    private double preferredHeight;
    private double preferredWidth;

    private double halfPaddingWidth;
    private double halfPaddingHeight;

    private int numberOfRows;
    private int numberOfColumns;

    private List<Double> rowCell;
    private List<Double> columnCell;

    // Tile
    private int tileEdgeEffect;
    private List<Integer[]> listOfBoulders;

    // User OS
    private String userOS = GlobalSettings.USER_OS;

    // Flag to differentiate from open and new map
    private boolean isOpeningAMap = false;

    // Flag to know if current map is save or not
    private boolean isCurrentMapSave = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // ################## INIT #################//
        // Extract GraphicsContext from canvas
        this.gc = this.canvas.getGraphicsContext2D();

        this.df = new SimpleDateFormat("hh:mm:ss");
        // Init logArea
        this.logMessage = new StringBuilder();
        this.preventNewLineAtFirst = true;

        this.logArea.setText("");
        this.logArea.setEditable(false);
        this.logArea.setWrapText(true);

        this.tileEdgeEffect = 2;
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
                = FXCollections.observableArrayList(
                        5, 6, 7, 8, 9, 10, 11, 12, 13,
                        14, 15, 16, 17, 18, 19, 20
                );

        // Define row list items
        ObservableList<Integer> rowOptions
                = FXCollections.observableArrayList(
                        5, 6, 7, 8, 9, 10, 11, 12, 13,
                        14, 15, 16, 17, 18, 19, 20
                );

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

    public void addNewLogMessage(String message) {
        Date date = new Date();
        String timeFormat = this.df.format(date);

        if (this.preventNewLineAtFirst) {
            this.logMessage.append("[")
                    .append(timeFormat)
                    .append("] ")
                    .append(message);
            this.preventNewLineAtFirst = false;
        } else {
            this.logMessage.append("\n[")
                    .append(timeFormat)
                    .append("] ")
                    .append(message);
        }

        this.logArea.setText(this.logMessage.toString());

        // This is necessary to trigger listener for 
        // textarea to autoscroll to bottom
        this.logArea.appendText("");
    }

    public void generateMap() {
        this.generateMap("");
    }

    public void generateMap(String path) {
        // ################## INIT #################//
        this.isEditMapOn = false;

        // Init logArea
        this.preventNewLineAtFirst = true;

        this.logArea.setText("");

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

        String msg = "";

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

            msg = GlobalSettings.LOG_NEW_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows";
        } else {
            // Fill grid tiles with neutral color
            for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
                for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {

                    this.paintTile(this.extractPositionColor(count_column, count_row, 1),
                            this.extractPositionColor(count_column, count_row, 2),
                            this.extractPositionColor(count_column, count_row, 3),
                            count_column, count_row);

                }
            }

            msg = GlobalSettings.LOG_OPEN_MAP + this.numberOfColumns
                    + " columns & " + this.numberOfRows + " rows"
                    + "\n Map file opened: " + path;
        }
        this.addNewLogMessage(msg);
    }

    // type 1: light edges color
    // type 2: main color
    // type 3: shadow color
    private String extractPositionColor(int column, int row, int type) {
        String tile_color = "";
        String tile_type = "neutral";

        for (Integer[] pos : this.map.getListOfBoulders()) {
            if (pos[0] == column + 1 && pos[1] == row + 1) {
                tile_type = "boulder";
            }
        }

        /*
         if (this.map.getListOfPlayer1StartPosition()[0] == column + 1
         && this.map.getListOfPlayer1StartPosition()[1] == row + 1) {
         tile_type = "player_1";
         }

         if (this.map.getListOfPlayer2StartPosition()[0] == column + 1
         && this.map.getListOfPlayer2StartPosition()[1] == row + 1) {
         tile_type = "player_2";
         } */
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

    @FXML
    private void generateBtnOnClick(ActionEvent event) {
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
        
        // Toggle flag if the map being generated is from opening a file or new
        this.isOpeningAMap = false;

        this.generateMap();

        // Toggle flag for detecting unsave map data
        this.isCurrentMapSave = false;
    }

    public void paintTile(String light_edge_color, String main_color, String shadow_edge_color,
            int count_column, int count_row) {
        // coloring the light top and left edges respectively
        this.gc.setFill(Color.web(light_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.gridXSpace, this.tileEdgeEffect);
        this.gc.fillRect(this.columnCell.get(count_column), this.rowCell.get(count_row), this.tileEdgeEffect, this.gridYSpace);

        // coloring main tile body
        this.gc.setFill(Color.web(main_color));
        this.gc.fillRect(this.columnCell.get(count_column) + this.tileEdgeEffect, this.rowCell.get(count_row) + this.tileEdgeEffect, this.gridXSpace - this.tileEdgeEffect, this.gridYSpace - this.tileEdgeEffect);

        // coloring tile's shadow for bottom and right edges respectively
        this.gc.setFill(Color.web(shadow_edge_color));
        this.gc.fillRect(this.columnCell.get(count_column) + this.tileEdgeEffect, this.rowCell.get(count_row) + this.gridYSpace - this.tileEdgeEffect, this.gridXSpace - this.tileEdgeEffect, this.tileEdgeEffect);
        this.gc.fillRect(this.columnCell.get(count_column) + this.gridXSpace - this.tileEdgeEffect, this.rowCell.get(count_row) + this.tileEdgeEffect, this.tileEdgeEffect, this.gridYSpace - this.tileEdgeEffect);
    }

    @FXML
    private void player1StartBtnOnClick(ActionEvent event) {
        this.isEditMapOn = true;
        this.tileToEdit = GlobalSettings.TILE_PLAYER1;

        this.addNewLogMessage(GlobalSettings.LOG_PLAYER1_ON);
    }

    @FXML
    private void player2StartBtnOnClick(ActionEvent event) {
        this.isEditMapOn = true;
        this.tileToEdit = GlobalSettings.TILE_PLAYER2;

        this.addNewLogMessage(GlobalSettings.LOG_PLAYER2_ON);
    }

    @FXML
    private void boulderBtnOnClick(ActionEvent event) {
        this.isEditMapOn = true;
        this.tileToEdit = GlobalSettings.TILE_BOULDER;

        this.addNewLogMessage(GlobalSettings.LOG_BOULDER_ON);
    }

    @FXML
    private void neutralBtnOnClick(ActionEvent event) {
        this.isEditMapOn = true;
        this.tileToEdit = GlobalSettings.TILE_NEUTRAL;

        this.addNewLogMessage(GlobalSettings.LOG_NEUTRAL_ON);
    }

    @FXML
    private void canvasOnClick(MouseEvent event) {
        if (this.isInsideTheGrid(event.getX(), event.getY())) {
            if (this.isEditMapOn) {
                int[] tilePos = this.getTilePosition(event.getX(), event.getY());
                boolean isAlreadyBoulder = false;
                Integer[] boulderReference = null;

                for (Integer[] boulder : this.listOfBoulders) {
                    if (tilePos[0] == boulder[0] && tilePos[1] == boulder[1]) {
                        isAlreadyBoulder = true;
                        boulderReference = boulder;
                    }
                }

                switch (this.tileToEdit) {
                    case GlobalSettings.TILE_BOULDER:

                        // If the tile selected is not a boulder, change it to boulder
                        if (!isAlreadyBoulder) {
                            // Add to list of boulders
                            this.listOfBoulders.add(new Integer[]{tilePos[0], tilePos[1]});

                            // paint tile to boulder
                            this.paintTile(GlobalSettings.TILE_BOULDER_LIGHT_EDGE_COLOR,
                                    GlobalSettings.TILE_BOULDER_MAIN_COLOR,
                                    GlobalSettings.TILE_BOULDER_SHADOW_EDGE_COLOR,
                                    tilePos[0] - 1,
                                    tilePos[1] - 1);

                            String msg8 = GlobalSettings.LOG_TILE_SET
                                    + "boulder tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.addNewLogMessage(msg8);

                        } else {
                            String msg7 = GlobalSettings.LOG_WARNING
                                    + "It's already a boulder tile.";
                            this.addNewLogMessage(msg7);
                        }

                        break;

                    case GlobalSettings.TILE_NEUTRAL:
                        if (isAlreadyBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // paint tile to neutral
                            this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                                    tilePos[0] - 1,
                                    tilePos[1] - 1);

                            String msg6 = GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN;
                            this.addNewLogMessage(msg6);

                            String msg9 = GlobalSettings.LOG_TILE_SET
                                    + "neutral tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.addNewLogMessage(msg9);

                        }
                        break;

                    case GlobalSettings.TILE_PLAYER1:
                        if (isAlreadyBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // paint tile to neutral
                            this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                                    tilePos[0] - 1,
                                    tilePos[1] - 1);

                            // Log message
                            String msg4 = GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN;
                            this.addNewLogMessage(msg4);
                        }

                        // If this is already a player 2 position, it resets the
                        // player 2 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        boolean isP2Overwritten = false;
                        if (this.map.getListOfPlayer2StartPosition() != null) {
                            if (tilePos[0] == this.map.getListOfPlayer2StartPosition()[0]
                                    && tilePos[1] == this.map.getListOfPlayer2StartPosition()[1]) {
                                this.map.setListOfPlayer2StartPosition(null);

                                String msg = GlobalSettings.LOG_WARNING + "Player 2 start position is overwritten!";
                                this.addNewLogMessage(msg);

                                isP2Overwritten = true;
                            }
                        }

                        // Add start position to map
                        this.map.setListOfPlayer1StartPosition(tilePos.clone());

                        // LOG Message
                        String msg2 = "";
                        if (isP2Overwritten) {
                            msg2 = GlobalSettings.LOG_TILE_OVERWRITTEN;
                        } else {
                            msg2 = GlobalSettings.LOG_TILE_SET;
                        }
                        msg2 += "Player 1 start position is now set to "
                                + "[" + tilePos[0] + "," + tilePos[1] + "]";
                        this.addNewLogMessage(msg2);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;

                    case GlobalSettings.TILE_PLAYER2:
                        if (isAlreadyBoulder) {
                            // Remove from boulder list
                            this.listOfBoulders.remove(boulderReference);

                            // paint tile to neutral
                            this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                                    GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                                    tilePos[0] - 1,
                                    tilePos[1] - 1);

                            // Log message
                            String msg5 = GlobalSettings.LOG_WARNING
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN;
                            this.addNewLogMessage(msg5);
                        }

                        // If this is already a player 1 position, it resets the
                        // player 1 start position to null. Null because it's easy to
                        // detect later on if it is properly configure before saving the map
                        boolean isP1Overwritten = false;
                        if (this.map.getListOfPlayer1StartPosition() != null) {
                            if (tilePos[0] == this.map.getListOfPlayer1StartPosition()[0]
                                    && tilePos[1] == this.map.getListOfPlayer1StartPosition()[1]) {
                                this.map.setListOfPlayer2StartPosition(null);

                                String msg = GlobalSettings.LOG_WARNING + "Player 1 start position is overwritten!";
                                this.addNewLogMessage(msg);

                                isP1Overwritten = true;
                            }
                        }

                        this.map.setListOfPlayer2StartPosition(tilePos.clone());

                        // LOG Message
                        String msg3 = "";
                        if (isP1Overwritten) {
                            msg3 = GlobalSettings.LOG_TILE_OVERWRITTEN;
                        } else {
                            msg3 = GlobalSettings.LOG_TILE_SET;
                        }
                        msg3 += "Player 2 start position is now set to "
                                + "[" + tilePos[0] + "," + tilePos[1] + "]";
                        this.addNewLogMessage(msg3);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;
                }

                // Update map list data
                this.map.setListOfBoulders(listOfBoulders);

            } else {
                String msg = GlobalSettings.LOG_WARNING + "No object/player selected!";
                this.addNewLogMessage(msg);
            }
        } else {
            String msg = GlobalSettings.LOG_WARNING + "Mouse clicked "
                    + "[" + event.getX() + "," + event.getY() + "] "
                    + "is outside the grid map!";
            this.addNewLogMessage(msg);
        }
    }

    public boolean isInsideTheGrid(double x_pos, double y_pos) {
        return (x_pos >= this.halfPaddingWidth && x_pos <= (this.canvas.getWidth() - this.halfPaddingWidth))
                && (y_pos >= this.halfPaddingHeight && y_pos <= (this.canvas.getWidth() - this.halfPaddingHeight));
    }

    public int[] getTilePosition(double x_pos, double y_pos) {
        int tile_x, tile_y;

        // For locating column position
        int highX = this.columnCell.size() - 1;
        int lowX = 0;
        int midX = 0;

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
        int midY = 0;

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

        /*
         System.out.println("tile: [" + tile_x + "," + tile_y + "]" );
        
        
         System.out.println("X clicked: " + x_pos);
        
         System.out.println("low: " + lowX);
         System.out.println(this.columnCell.get(lowX));
        
         System.out.println("high: " + highX);
         System.out.println(this.columnCell.get(highX));
        
         System.out.println("mid: " + midX);
         */
        return new int[]{tile_x, tile_y};
    }

    @FXML
    private void titleFieldOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (this.map != null) {
                String title = this.title_field.getText();

                if (title.isEmpty()) {
                    String warningMsg1 = GlobalSettings.LOG_WARNING
                            + "Title field is empty!";
                    this.addNewLogMessage(warningMsg1);
                } else {
                    // Only accepts letters, numbers and spaces
                    // No leading space allowed
                    // All trailing spaces are automatically trim
                    if (title.matches("^[a-zA-Z0-9][a-zA-Z0-9\\s]*$")) {
                        title = this.titleFormatter(title);

                        // Set title to the map
                        this.map.setName(title);

                        String successMsg = GlobalSettings.LOG_TITLE_SET
                                + "Title: " + title + " is set.";
                        this.addNewLogMessage(successMsg);
                    } else {
                        String invalidMsg = GlobalSettings.LOG_ERROR
                                + "Invalid title! Only use alphanumeric and space characters. No leading space.";
                        this.addNewLogMessage(invalidMsg);
                    }
                }
            } else {
                String noMapMsg = GlobalSettings.LOG_WARNING
                        + "Generate a map first!";
                this.addNewLogMessage(noMapMsg);
            }

        }

    }

    private String captilizeFirstLetter(String word) {
        // Removes any unnecessary leading and trailing space
        word = word.trim();

        // Shifts all letters to lowercase
        word = word.toLowerCase();

        // Convert the first letter to uppercase
        String firstLetterCapitalTitle = word.substring(0, 1).toUpperCase()
                + word.substring(1);

        // Converts all first letter of word separated by space to uppercase
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

    public String titleFormatter(String title) {
        // Capitilize all first letter of a word 
        // separated by space
        title = this.captilizeFirstLetter(title);

        // Update title field
        this.title_field.setText(title);

        // append row and column
        title += " " + this.numberOfColumns + "x" + this.numberOfRows;

        return title;
    }

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
            return;
        }
        String firstWord = filename.substring(0, 3);

        if (file.isFile() && "json".equals(ext) && firstWord.equalsIgnoreCase("Map")) {
            this.openMapFile(file);
        }

    }

    public void openMapFile(File file) {
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

            // Toggle flag for detecting unsave map
            this.isCurrentMapSave = false;

        } catch (IOException e) {
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Error loading map (invalid json map file). ";
            this.addNewLogMessage(errorMsg);
            
            this.showExceptionDialog(e);
            
            e.printStackTrace();
        }
    }

    @FXML
    private void menuFileSaveOnClick(ActionEvent event) {
        // Detect first if all requirements met
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
            return;
        }
        String firstWord = filename.substring(0, filename.lastIndexOf("."));

        if (!firstWord.startsWith("Map")) {
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + "Wrong filename format. Should be [Map] & [3 digit number].json";
            this.addNewLogMessage(wrongFormatNameMsg);
            return;
        }

        if (firstWord.contains(" ")) {
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + "Filename shouldn't contain space character";
            this.addNewLogMessage(wrongFormatNameMsg);
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
            String invalidExtMsg = GlobalSettings.LOG_ERROR
                    + "Invalid extension file!";
            this.addNewLogMessage(invalidExtMsg);
        }

    }

    private String formatMapID(String id) {
        return this.captilizeFirstLetter(id);
    }

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

            String successSaveMsg = GlobalSettings.LOG_SAVE_MAP
                    + "File is successfully saved at " + file.getPath();
            this.addNewLogMessage(successSaveMsg);

            // Toggle flag for detecting unsave map
            this.isCurrentMapSave = true;

        } catch (IOException e) {
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Unable to save file.";
            this.addNewLogMessage(errorMsg);
            
            this.showExceptionDialog(e);

            e.printStackTrace();
        }
    }

    @FXML
    private void menuFileNewOnClick(ActionEvent event) {
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
        
        // Toggle flag for detecting if the map being generated 
        // is an open file or new
        this.isOpeningAMap = false;

        // Generate the map
        this.generateMap();

        // Toggle flag for detecting unsave map
        this.isCurrentMapSave = false;
    }

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
                    event.consume();
                }

            }
        });
    }

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
            Platform.exit();
        }

    }

    private void showWarningDialog(String msgHead, String msgBody) {
        Dialogs.create()
                .owner(this.app.getStage())
                .title("Warning")
                .masthead(msgHead)
                .message(msgBody)
                .showWarning();
    }
    
    private void showExceptionDialog(Exception e) {
        Dialogs.create()
        .owner(this.app.getStage())
        .title("Exception Dialog")
        .masthead(e.toString())
        .message(e.getMessage())
        .showException(e);
    } 

    private Action showConfirmDialog(String msgHead, String msgBody) {
        Action response = Dialogs.create()
                .owner(this.app.getStage())
                .title("Confirm Dialog")
                .masthead(msgHead)
                .message(msgBody)
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showConfirm();

        return response;
    }

}
