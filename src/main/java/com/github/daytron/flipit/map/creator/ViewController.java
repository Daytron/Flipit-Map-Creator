package com.github.daytron.flipit.map.creator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
        this.row_combo.getSelectionModel().select(
            GlobalSettings.ROW_DEFAULT_VALUE);
        
        // Apply listeners
        this.column_combo.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                column_combo.getSelectionModel().select(newValue);
            }
        });
        
        this.row_combo.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                row_combo.getSelectionModel().select(newValue);
            }
        });
        
    }    

    @FXML
    private void generateBtnOnClick(ActionEvent event) {
    }

    @FXML
    private void player1StartBtnOnClick(ActionEvent event) {
    }

    @FXML
    private void player2StartBtnOnClick(ActionEvent event) {
    }

    @FXML
    private void boulderBtnOnClick(ActionEvent event) {
    }

    @FXML
    private void canvasOnClick(MouseEvent event) {
    }
}
