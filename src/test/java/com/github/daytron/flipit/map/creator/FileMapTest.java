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
package com.github.daytron.flipit.map.creator;

import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.controls.Commons;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

/**
 * Test class for testing map file saving and opening events.
 * @author Ryan Gilera
 */
@Category({AllTest.class})
public class FileMapTest extends FxRobot {

    private MainApp app;
    public static Stage primaryStage;

    public FileMapTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            // Start the Toolkit and block until the primary Stage was retrieved.
            primaryStage = FxToolkit.registerPrimaryStage();
        } catch (TimeoutException ex) {
            Logger.getLogger(FileMapTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            // Construct the Application and call start() with the primary Stage.
            this.app = (MainApp) FxToolkit.setupApplication(MainApp.class);

            // Wait for the primary Stage to be shown by start().
            WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, primaryStage.showingProperty());

        } catch (TimeoutException ex) {
            Logger.getLogger(FileMapTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click generate button
     * <li> 2. click on title field
     * <li> 3. type "eye of the world" in the field
     * <li> 4. press ENTER key
     * <li> 5. click player 1 button
     * <li> 6. click on tile 8,4
     * <li> 7. click player 2 button
     * <li> 8. click on tile 8,1
     * <li> 9. press control + S to launch save map
     * <li> 10. write filename with extension to save
     * <li> 11. press ENTER key
     * <li> 12. press control + O to launch open map
     * <li> 13. write filename with extension to open
     * <li> 14. press ENTER key
     * </ul>
     */
    @Test
    public void generateMapThenSaveThenOpenTest() {
        String mapFile = "Map010.json";

        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(2, SECONDS);

        clickOn("#title_field");
        write("eye of the world").push(ENTER);
        
        Date date2 = new Date();

        clickOn("#p1_start_btn");
        Date date3 = new Date();

        moveBy(-400.00, -200.00).clickOn();
        Date date4 = new Date();

        clickOn("#p2_start_btn");
        Date date5 = new Date();

        moveBy(-350.00, -400.00).clickOn();
        Date date6 = new Date();
        
        // Save the current map
        KeyCodeCombination ctrlS = new KeyCodeCombination(
                KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        
        push(ctrlS);
        //clickOn("#file");
        //sleep(2, SECONDS);
        //clickOn("#save");
        sleep(3, SECONDS);
        
        write(mapFile).push(ENTER);
        Date date7 = new Date();
        sleep(4, SECONDS);

        // Open current map
        KeyCodeCombination ctrlO = new KeyCodeCombination(
                KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
        push(ctrlO);
        //clickOn("#file");
        //sleep(2, SECONDS);
        //clickOn("#open");
        sleep(3, SECONDS);
        
        write(mapFile).push(ENTER);
        Date date8 = new Date();
        sleep(4, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String timeFormat2 = df.format(date2);
        String timeFormat3 = df.format(date3);
        String timeFormat4 = df.format(date4);
        String timeFormat5 = df.format(date5);
        String timeFormat6 = df.format(date6);
        String timeFormat7 = df.format(date7);
        String timeFormat8 = df.format(date8);

        StringBuilder outputLog = new StringBuilder();

        // clickOn("#generate_map_btn");
        outputLog.append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat).append("] ");
        outputLog.append(GlobalSettings.LOG_NEW_MAP);
        outputLog.append("10 columns & 10 rows");

        // clickOn("#title_field").write("eye of the world").push(KeyCode.ENTER);
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat2).append("] ");
        outputLog.append(GlobalSettings.LOG_TITLE_SET);
        outputLog.append("Title: Eye Of The World 10x10 is set.");

        // clickOn("#p1_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat3).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER1_ON);

        // moveBy(-400.00, -200.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat4).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 1 start position is now set to [8,4]");

        // clickOn("#p2_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat5).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER2_ON);

        // moveBy(-350.00, -400.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat6).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 2 start position is now set to [8,1]");

        // Save the map
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat7).append("] ");
        outputLog.append(GlobalSettings.LOG_SAVE_MAP);
        outputLog.append("Map is successfully saved.");

        // Open the map
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat8).append("] ");
        outputLog.append(GlobalSettings.LOG_OPEN_MAP);
        outputLog.append("10 columns & 10 rows\nMap file opened: ");

        //File fileDirectory = new File(System.getProperty("user.home"));
        //File[] list = fileDirectory.listFiles();

        // Retrieves map file and its image preview
        File mapJson;
        File mapImage;
        if (GlobalSettings.USER_OS.contains(GlobalSettings.OS_WINDOWS)) {
            mapJson = new File(System.getProperty("user.home") 
                    + "\\" + mapFile);
            mapImage = new File(System.getProperty("user.home") 
                    + "\\" + mapFile);
        } else {
            mapJson = new File(System.getProperty("user.home") 
                    + "/" + mapFile);
            mapImage = new File(System.getProperty("user.home") 
                    + "/" + mapFile);
        }

        outputLog.append(mapJson.getPath());

        // Delete temporary map and its image preview
        mapJson.delete();
        mapImage.delete();

        Assertions.verifyThat("#logArea",
                Commons.hasText(outputLog.toString()));
        Assertions.verifyThat("#title_field",
                Commons.hasText("Eye Of The World"));
    }

}
