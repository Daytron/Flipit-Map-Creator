package com.github.daytron.flipit.map.creator;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import org.apache.commons.lang3.text.WordUtils;

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

    @FXML
    private void generateBtnOnClick(ActionEvent event) {
        // ################## INIT #################//
        this.isEditMapOn = false;

        // Init logArea
        this.logMessage = new StringBuilder();
        this.preventNewLineAtFirst = true;

        this.logArea.setText("");

        // Resets list of boulders
        this.listOfBoulders = new ArrayList<>();

        this.map = new Map();

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

        // Fill grid tiles with neutral color
        for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {

                this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                        GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                        GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                        count_column, count_row);

            }
        }
        String msg = GlobalSettings.LOG_NEW_MAP + "\n" + this.numberOfColumns + " columns & " + this.numberOfRows + " rows";
        this.addNewLogMessage(msg);

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

                            String msg8 = GlobalSettings.LOG_TILE_SET + "\n"
                                    + "boulder tile is set to "
                                    + "[" + tilePos[0] + "," + tilePos[1] + "]";
                            this.addNewLogMessage(msg8);

                        } else {
                            String msg7 = GlobalSettings.LOG_WARNING + "\n"
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

                            String msg6 = GlobalSettings.LOG_WARNING + "\n"
                                    + GlobalSettings.LOG_BOULDER_OVERWRITTEN;
                            this.addNewLogMessage(msg6);

                            String msg9 = GlobalSettings.LOG_TILE_SET + "\n"
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
                            String msg4 = GlobalSettings.LOG_WARNING + "\n"
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

                                String msg = GlobalSettings.LOG_WARNING + "\n" + "Player 2 start position is overwritten!";
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
                        msg2 += "\n" + "Player 1 start position is now set to "
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
                            String msg5 = GlobalSettings.LOG_WARNING + "\n"
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

                                String msg = GlobalSettings.LOG_WARNING + "\n" + "Player 1 start position is overwritten!";
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
                        msg3 += "\n" + "Player 2 start position is now set to "
                                + "[" + tilePos[0] + "," + tilePos[1] + "]";
                        this.addNewLogMessage(msg3);

                        // Reset isEditOn
                        this.isEditMapOn = false;

                        break;
                }

                // Update map list data
                this.map.setListOfBoulders(listOfBoulders);

            } else {
                String msg = GlobalSettings.LOG_WARNING + "\n" + "No object/player selected!";
                this.addNewLogMessage(msg);
            }
        } else {
            String msg = GlobalSettings.LOG_WARNING + "\n" + "Mouse clicked "
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
                    String warningMsg1 = GlobalSettings.LOG_WARNING + "/n"
                            + "Title field is empty!";
                    this.addNewLogMessage(warningMsg1);
                } else {
                    if (title.matches("^[a-zA-Z0-9][a-zA-Z0-9\\s]*$")) {
                        title = this.titleFormatter(title);

                        // Set title to the map
                        this.map.setName(title);

                        String successMsg = GlobalSettings.LOG_TITLE_SET
                                + "/n" + "Title: " + title + " is set.";
                        this.addNewLogMessage(successMsg);
                    } else  {
                        String invalidMsg = GlobalSettings.LOG_ERROR + "/n" 
                                + "Invalid title! Only use alphanumeric and space characters. No leading space.";
                        this.addNewLogMessage(invalidMsg);
                    }
                }
            } else {
                String noMapMsg = GlobalSettings.LOG_WARNING + "\n"
                        + "Generate a map first!";
                this.addNewLogMessage(noMapMsg);
            }

        }

    }

    public String titleFormatter(String title) {
        title = title.toLowerCase();

        title = WordUtils.capitalize(title);

        // Update title field
        this.title_field.setText(title);
        
        // append row and column
        title += " " + this.numberOfColumns + "x" + this.numberOfRows;

        return title;
    }

}
