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

import com.github.daytron.flipit.map.creator.data.FXMLFilePath;
import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * The controller class that initialize AboutAppView view with contents as an
 * about app window dialog.
 *
 * @author ryan
 */
public class AboutView extends Stage implements Initializable {

    @FXML
    private TextArea textArea;
    @FXML
    private Label version_label;
    @FXML
    private Button okButton;
    
    private Scene scene;

    public AboutView() {
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource(FXMLFilePath.ABOUT_VIEW.getPath()));
            
            fxmlLoader.setController(this);
            
            this.scene = new Scene((Parent) fxmlLoader.load());
            setScene(scene);
            centerOnScreen();
            
            setResizable(false);
        } catch (IOException ex) {
            Logger.getLogger(AboutView.class.getName()).log(Level.SEVERE, null, ex);
            
            Dialog exceptionDialog = new Dialog(ex);
            exceptionDialog.showAndWait();
            
            close();
        }
    }

    /**
     * Initializes the controller class. Loads the text to the text area.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                 okButton.requestFocus();
            }
        });
        
        // Sets the version label
        this.version_label.setText(
            GlobalSettings.VERSION);

        // Set text area to uneditable
        this.textArea.setEditable(false);
        // Set it to wrap text on every line
        this.textArea.setWrapText(true);

        // Prepares for the text message.
        StringBuilder coreMessage = new StringBuilder();

        coreMessage.append("Map editor for the game, Flipit. \n")
                .append("Visit the application development site at\n")
                .append("https://github.com/Daytron/Flipit-Map-Creator\n\n")
                .append("SimpleDialogFX\n")
                .append("For all window dialogs. ")
                .append("Copyright (c) 2014, SimpleDialogFX, Ryan Gilera.\n")
                .append("https://github.com/Daytron/SimpleDialogFX \n\n")
                .append("Gson\n")
                .append("For coverting pojo files to json and vice versa.\n")
                .append("Copyright (c) 2008-2009 Google Inc.");

        // Apply text
        this.textArea.setText(coreMessage.toString());

    }
    
    @FXML
    private void okButton_onClick(ActionEvent event) {
        close();
    }

}
