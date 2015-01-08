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

import com.github.daytron.flipit.map.creator.MainApp;
import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import com.github.daytron.flipit.map.creator.utility.StringUtils;
import com.github.daytron.simpledialogfx.data.DialogResponse;
import com.github.daytron.simpledialogfx.data.DialogText;
import com.github.daytron.simpledialogfx.data.DialogType;
import com.github.daytron.simpledialogfx.dialog.Dialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 * A static class for map file management.
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
public final class MapManager {

    private MapManager() {
    }

    /**
     * Saves the map object as a JSON file.
     *
     * @param file The map file to save.
     */
    public static boolean saveFile(File file, Map map,
            Canvas canvas) {
        LogManager logManager = LogManager.getInstance();
        String userOS = GlobalSettings.USER_OS;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(map);

        try {
            //write converted json data to a file 
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();

            // Add new log
            String successSaveMsg = GlobalSettings.LOG_SAVE_MAP
                    + "Map is successfully saved.";
            logManager.addNewLogMessage(successSaveMsg);

            // Prepares for taking snapshot of the canvas
            WritableImage writableImage = new WritableImage(
                    (int) canvas.getWidth(),
                    (int) canvas.getHeight());

            canvas.snapshot(null, writableImage);

            // Get the last filepath to use for saving image file
            String filePath;

            if (userOS.startsWith(GlobalSettings.OS_LINUX)
                    || userOS.startsWith(GlobalSettings.OS_MAC)) {
                filePath = file.getPath().substring(0, file.getPath().lastIndexOf("/") + 1);
            } else {
                // Other OS is already filtered above, so this is definitely Windows
                filePath = file.getPath().substring(0, file.getPath().lastIndexOf("\\") + 1);
            }

            // Build full path 
            String imageFilePath = filePath + map.getMapID() + ".png";

            // Create file
            File fileImage = new File(imageFilePath);

            // Try saving it as a PNG image file
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),
                        "png", fileImage);
            } catch (Exception s) {
                Dialog exceptionDialog = new Dialog(s);
                exceptionDialog.showAndWait();

                return false;
            }

        } catch (IOException e) {
            // Add new log
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Unable to save file.";
            logManager.addNewLogMessage(errorMsg);

            // Show exception dialog
            Dialog exceptionDialog = new Dialog(e);
            exceptionDialog.showAndWait();

            e.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * Opens the JSON map file
     *
     * @param file The file object that holds the map data
     * @param isCurrentMapSave A boolean flag that tracks if the current map
     * generated is saved or not
     * @param app The MainApp object for retrieving the stage object
     * @return the Map object if successfully opened, otherwise
     * <code>null</code>
     */
    public static Map openFile(File file, boolean isCurrentMapSave) {
        LogManager logManager = LogManager.getInstance();

        // Confirmation dialog
        if (!isCurrentMapSave) {
            Dialog confirmDialog = new Dialog(
                    DialogType.CONFIRMATION,
                    DialogText.CONFIRMATION_HEADER.getText(),
                    GlobalSettings.DIALOG_NEW_MAP_HEAD_MSG_NOT_SAVE,
                    GlobalSettings.DIALOG_NEW_MAP_BODY_MSG_NOT_SAVE);

            confirmDialog.showAndWait();

            if (confirmDialog.getResponse() == DialogResponse.NO
                    || confirmDialog.getResponse() == DialogResponse.CLOSE) {
                // Cancel opening file if user press cancel or press close (x)
                return null;
            }
        }

        Gson gson = new Gson();

        BufferedReader br;
        try {
            br = new BufferedReader(
                    new FileReader(file));

            //convert the json string back to object
            Map map = gson.fromJson(br, Map.class);

            return map;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MapManager.class.getName()).log(Level.SEVERE, null, ex);
            String errorMsg = GlobalSettings.LOG_ERROR
                    + "IOEXCEPTION! Error loading map (invalid json map file). ";
            logManager.addNewLogMessage(errorMsg);

            Dialog exceptionDialog = new Dialog(ex);
            exceptionDialog.showAndWait();

            return null;
        }

    }

    /**
     * Verifies if the File object is a valid map file for opening the file.
     *
     * @param file The file object that holds the map data
     * @param app The MainApp object for retrieving the stage object
     * @return <code>true</code> if the file is valid, otherwise
     * <code>false</code>
     */
    public static boolean verifyFileToOpen(File file) {
        LogManager logManager = LogManager.getInstance();
        String userOS = GlobalSettings.USER_OS;

        // Possible action if user press cancel button on open file dialog
        // Skip all remaining lines
        if (file == null) {
            return false;
        }

        // Extract extension name
        String ext = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

        // Extract filename
        String filename = "";
        if (userOS.startsWith(GlobalSettings.OS_LINUX)
                || userOS.startsWith(GlobalSettings.OS_MAC)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
        } else if (userOS.startsWith(GlobalSettings.OS_WINDOWS)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
        } else {
            String noOSsupportMsg = GlobalSettings.LOG_ERROR
                    + GlobalSettings.LOG_OS_NOT_SUPPORTED_BODY_MSG;
            logManager.addNewLogMessage(noOSsupportMsg);
            return false;
        }

        // Get the first 3 letters for inspection
        String firstWord = filename.substring(0, 3);

        if (file.isFile()) {
            if ("json".equals(ext)) {
                if (firstWord.equals("Map")) {
                    return true;
                } else {
                    String msgHead = "Invalid Filename";
                    String msgBody = "Map files must "
                            + "start at \"Map\" followed by a number";
                    String invalidPatterName = GlobalSettings.LOG_ERROR
                            + msgHead + ". " + msgBody;

                    logManager.addNewLogMessage(invalidPatterName);

                    Dialog errorDialog = new Dialog(
                            DialogType.ERROR, msgHead, msgBody);
                    errorDialog.showAndWait();
                    return false;
                }
            } else {
                String msgHead = "Invalid Extension";
                String msgBody = "Map files are "
                        + ".json files";
                String invalidExtension = GlobalSettings.LOG_ERROR
                        + msgHead + ". " + msgBody;

                logManager.addNewLogMessage(invalidExtension);

                Dialog errorDialog = new Dialog(
                        DialogType.ERROR, msgHead, msgBody);
                errorDialog.showAndWait();
                return false;
            }
        } else {
            String msgHead = "No File Detected";
            String msgBody = "Not a proper file.";
            String invalidFile = GlobalSettings.LOG_ERROR
                    + msgHead + ". " + msgBody;

            logManager.addNewLogMessage(invalidFile);

            Dialog errorDialog = new Dialog(
                    DialogType.ERROR, msgHead, msgBody);
            errorDialog.showAndWait();
            return false;
        }

    }

    /**
     *
     * @param file The file object that holds the map data
     * @param logManager The LogManager that handles log events
     * @param app The MainApp object for retrieving the stage object
     * @param map The Map object that holds the map data
     * @param numberOfColumns The number of columns
     * @param numberOfRows The number of rows
     * @return <code>true</code> if the file is valid, otherwise
     * <code>false</code>
     */
    public static boolean verifyFileToSave(File file,
            LogManager logManager, Map map, int numberOfColumns, 
            int numberOfRows) {
        String userOS = GlobalSettings.USER_OS;

        // Possible action if user press cancel button on save file dialog
        // Skip all remaining lines
        if (file == null) {
            return false;
        }

        String ext = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);

        String filename = "";
        if (userOS.startsWith(GlobalSettings.OS_LINUX)
                || userOS.startsWith(GlobalSettings.OS_MAC)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("/") + 1);
        } else if (userOS.startsWith(GlobalSettings.OS_WINDOWS)) {
            filename = file.getPath().substring(file.getPath().lastIndexOf("\\") + 1);
        } else {
            String noOSsupportMsg = GlobalSettings.LOG_ERROR
                    + GlobalSettings.LOG_OS_NOT_SUPPORTED_BODY_MSG;
            logManager.addNewLogMessage(noOSsupportMsg);

            Dialog errorDialog = new Dialog(
                    DialogType.ERROR, 
                    GlobalSettings.LOG_OS_NOT_SUPPORTED_HEAD_MSG, 
                    GlobalSettings.LOG_OS_NOT_SUPPORTED_BODY_MSG);
            errorDialog.showAndWait();
            return false;
        }

        String firstWord = filename.substring(0, filename.lastIndexOf("."));

        // Prevents user from using a space character on Title
        if (filename.contains(" ")) {
            // Add new log
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + "Filename shouldn't contain space character";
            logManager.addNewLogMessage(wrongFormatNameMsg);

            // Show a warning dialog
            Dialog warningDialog = new Dialog(
                    DialogType.WARNING, 
                    GlobalSettings.DIALOG_SAVE_NAME_SPACE_HEAD_MSG, 
                    GlobalSettings.DIALOG_SAVE_NAME_SPACE_BODY_MSG);
            warningDialog.showAndWait();
            return false;
        }

        if (!isValidFileName(firstWord)) {
            String msgHead = "Invalid Filename Format";
            String msgBody = "Should be [\"Map\"] + [3 digit number].json."
                    + "Example: Map004.json";
            String wrongFormatNameMsg = GlobalSettings.LOG_WARNING
                    + msgHead + ". " + msgBody;
            logManager.addNewLogMessage(wrongFormatNameMsg);
            
            Dialog errorDialog = new Dialog(
                    DialogType.ERROR, 
                    msgHead, 
                    msgBody);
            errorDialog.showAndWait();

            return false;
        }

        if ("json".equalsIgnoreCase(ext)) {
            // Formay the mapId to proper accepted format
            // E.g. Map002
            firstWord = formatMapID(firstWord);

            // Apply data to map file
            map.setMapID(firstWord);
            map.setSize(new int[]{numberOfColumns, numberOfRows});

            return true;
        } else {
            // Update log
            String invalidExtMsg = GlobalSettings.LOG_ERROR
                    + "Invalid extension file!";
            logManager.addNewLogMessage(invalidExtMsg);

            // Show error dialog
            Dialog errorDialog = new Dialog(
                    DialogType.ERROR, 
                    GlobalSettings.DIALOG_INVALID_EXTENSION_HEAD_MSG, 
                    GlobalSettings.DIALOG_INVALID_EXTENSION_BODY_MSG);
            errorDialog.showAndWait();
            return false;
        }
    }

    /**
     * Verifies if the filename follows the name convention: "Map" + [3 digit
     * number].
     *
     * @param filename The filename to inspect.
     * @return <code>true</code> if the filename is valid, otherwise
     * <code>false</code>.
     */
    private static boolean isValidFileName(String filename) {
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
     * Formats the map's ID for capital first letter and lowercase on preceding
     * characters.
     *
     * @param id The ID to be formatted.
     * @return Returns the formatted ID.
     */
    private static String formatMapID(String id) {
        id = id.toLowerCase();
        return StringUtils.capitalizeFirstLetterWord(id);
    }

}
