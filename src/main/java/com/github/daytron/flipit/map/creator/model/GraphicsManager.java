/*
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
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

import com.github.daytron.flipit.map.creator.model.Map;
import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
public class GraphicsManager {

    // Size of a tile
    private double gridXSpace;
    private double gridYSpace;

    // Preferred map dimension
    private double preferredHeight;
    private double preferredWidth;

    // The padding for x and y position of the map to the canvas
    private double halfPaddingWidth;
    private double halfPaddingHeight;

    // List of coordinates that dictates the boundary of a tile
    private List<Double> rowCell;
    private List<Double> columnCell;

    // Columns and rows
    private int numberOfRows;
    private int numberOfColumns;

    private GraphicsContext gc;
    
    private Canvas canvas;
    private Map map;
    private int tileEdgeEffect;

    private GraphicsManager() {
    }

    public static GraphicsManager getInstance() {
        return SingletonContainer.INSTANCE;
    }

    private static class SingletonContainer {

        private static final GraphicsManager INSTANCE = new GraphicsManager();
    }

    public void init(Canvas canvas, Map map) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.map = map;
        this.tileEdgeEffect = GlobalSettings.TILE_EDGE_WIDTH;
    }

    public void generateMap(int numberOfRows,
            int numberOfColumns, Map map) {
        // Resets map
        this.map = map;
        
        // Resets columns and rows
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        
        // Resets new columnCell and rowCell
        this.rowCell = new ArrayList<>();
        this.columnCell = new ArrayList<>();
        
        // Clears any previous drawings
        this.gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        
        double x = this.canvas.getWidth();
        double y = this.canvas.getHeight();

        // Map preferred size (double)
        this.preferredHeight = ((int) y / this.numberOfRows) * (double) this.numberOfRows;
        this.preferredWidth = ((int) x / this.numberOfColumns) * (double) this.numberOfColumns;

        // Canvas padding space for width and height
        this.halfPaddingWidth = (x - preferredWidth) / 2;
        this.halfPaddingHeight = (y - preferredHeight) / 2;

        // space between each cell
        this.gridXSpace = this.preferredWidth / this.numberOfColumns;
        this.gridYSpace = this.preferredHeight / this.numberOfRows;

        this.gc.setLineWidth(2);

        // generate rows
        // Note: rowCell is base 0 not 1, so row 1 is actually 0, 2 is 1, etc
        for (double yi = this.halfPaddingHeight; yi <= (y - this.halfPaddingHeight); yi = yi + this.gridYSpace) {
            //gc.strokeLine(halfPaddingWidth, yi, x - halfPaddingWidth, yi);
            this.rowCell.add(yi);
        }

        // generate columns
        // Note: columnCell is base 0 not 1, so column 1 is actually 0, 2 is 1, etc
        for (double xi = this.halfPaddingWidth; xi <= (x - this.halfPaddingWidth); xi = xi + this.gridXSpace) {
            //gc.strokeLine(xi, halfPaddingHeight, xi, y - halfPaddingHeight);
            this.columnCell.add(xi);
        }
    }

    public void drawNewMap() {
        // Fill grid tiles with neutral color
        for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {
                this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                        GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                        GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                        count_column, count_row);
            }
        }
    }

    public void drawOpenMap() {
        // Fill grid tiles from the save map data file
        for (int count_row = 0; count_row < this.numberOfRows; count_row++) {
            for (int count_column = 0; count_column < this.numberOfColumns; count_column++) {
                this.paintTile(this.extractPositionColor(count_column, count_row, 1),
                        this.extractPositionColor(count_column, count_row, 2),
                        this.extractPositionColor(count_column, count_row, 3),
                        count_column, count_row);
            }
        }
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

        // If current tile is a boulder, set tile_type to boulder
        for (Integer[] pos : this.map.getListOfBoulders()) {
            if (pos[0] == column + 1 && pos[1] == row + 1) {
                tile_type = "boulder";
                break;
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
    
    public void paintPlayerStart(int playerNumber, int x, int y) {
        double smallestSide = Math.min(this.gridXSpace, this.gridYSpace);

        double padding;
        double widthRing, heightRing;
        double xAllowance, yAllowance;

        // For drawing the ring
        // Calculate necessary adjustments
        // Padding is set to 10% of the smallest side
        padding = 0.1 * smallestSide;

        // Drawing line width is set to 5% of the smallest side
        this.gc.setLineWidth(0.05 * smallestSide);

        // Calculate the diameter of the ring
        widthRing = smallestSide - (padding * 2);
        heightRing = widthRing;

        // Calculate extra x,y allowance to position the ring
        xAllowance = (this.gridXSpace - widthRing) / 2;
        yAllowance = (this.gridYSpace - heightRing) / 2;

        // Draw the ring
        this.gc.strokeOval(this.columnCell.get(x - 1) + xAllowance,
                this.rowCell.get(y - 1) + yAllowance,
                widthRing, heightRing);

        // For text draw
        int smallestSidePos = Math.min(this.numberOfColumns,
                this.numberOfRows);

        /* 
         * this calculation is based on these definitions:
         * with smallest side (row or column) 5, font size is 40
         * with 6, size is 38
         * with 7, size is 36
         * until 20, size is 10
         */
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

        // Get a reference to the current font, to be retained later 
        Font oldFont = this.gc.getFont();
        // Set a new font
        this.gc.setFont(Font.font("Verdana", fontSize));

        // Draw the player number as text
        this.gc.strokeText(Integer.toString(playerNumber),
                this.columnCell.get(x - 1) + xAllowance
                + (widthRing * xPaddingPercentage),
                this.rowCell.get(y - 1) + yAllowance
                + (widthRing * yPaddingPercentage));

        // Return back to its original width and font
        this.gc.setLineWidth(1.0);
        this.gc.setFont(oldFont);
    }

    public void paintNeutralTile(int x, int y) {
        this.paintTile(GlobalSettings.TILE_NEUTRAL_LIGHT_EDGE_COLOR,
                GlobalSettings.TILE_NEUTRAL_MAIN_COLOR,
                GlobalSettings.TILE_NEUTRAL_SHADOW_EDGE_COLOR,
                x - 1, y - 1);
    }
    
    public void paintBoulderTile(int x, int y) {
        this.paintTile(GlobalSettings.TILE_BOULDER_LIGHT_EDGE_COLOR,
                GlobalSettings.TILE_BOULDER_MAIN_COLOR,
                GlobalSettings.TILE_BOULDER_SHADOW_EDGE_COLOR,
                x - 1, y - 1);
    }
    
    public boolean isInsideTheGrid(double x_pos, double y_pos) {
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
    public int[] getTilePosition(double x_pos, double y_pos) {
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

    
    public void setTileEdgeEffect(int tileEdgeEffect) {
        this.tileEdgeEffect = tileEdgeEffect;
    }
    
    
}
