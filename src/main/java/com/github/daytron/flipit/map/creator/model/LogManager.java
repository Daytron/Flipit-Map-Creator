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

import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TextArea;

/**
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
public class LogManager {
    private StringBuilder logMessage;
    
    // The TextArea reference from the View.fxml
    private TextArea logArea;
    
    private boolean preventNewLineAtFirst;
    
    private LogManager() {
    }
    
    public static LogManager getInstance() {
        return LogManagerHolder.INSTANCE;
    }
    
    private static class LogManagerHolder {
        private static final LogManager INSTANCE = new LogManager();
    }
    
    public void init(TextArea textArea) {
        this.logArea = textArea;
        this.logArea.setText("");
        this.logArea.setEditable(false);
        this.logArea.setWrapText(true);
        
        this.logMessage = new StringBuilder();
        
        this.preventNewLineAtFirst = true;
    }
    
    public void reset() {
        this.logMessage = new StringBuilder();
        this.logArea.setText("");
        this.preventNewLineAtFirst = true;
    }
    
    /**
     * Adds and process a new log message from the received String argument.
     *
     * @param message The full text message of the log event
     */
    public void addNewLogMessage(String message) {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm");
        String timeFormat = dateFormatter.format(date);

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

    
}
