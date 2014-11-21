/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.map.creator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * The controller class that initialize AboutAppView view with contents
 * as an about app window dialog.
 * 
 * @author ryan
 */
public class AboutAppViewController implements Initializable {
    @FXML
    private TextArea textArea;

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
        // Set text area to uneditable
        this.textArea.setEditable(false);
        // Set it to wrap text on every line
        this.textArea.setWrapText(true);
        
        // Prepares for the text message.
        StringBuilder coreMessage = new StringBuilder();
        
        coreMessage.append("Map editor for the game, Flipit. \n")
                .append("Visit the application development site at\n")
                .append("https://github.com/Daytron/Flipit-Map-Creator\n\n")
                .append("ControlsFX\n")
                .append("For all window dialogs. ")
                .append("Copyright (c) 2013, 2014, ControlsFX.\n\n")
                .append("Gson\n")
                .append("For coverting pojo files to json and vice versa.\n")
                .append("Copyright (c) 2008-2009 Google Inc.");
        
        // Apply text
        this.textArea.setText(coreMessage.toString());
        
    }    
    
}
