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
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.controls.Commons;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;
import org.loadui.testfx.utils.RunWaitUtils;
import org.testfx.api.FxLifecycle;

/**
 *
 * @author Ryan Gilera
 */
public class UITest extends FxRobotImpl {

    private MainApp app;
    public static Stage primaryStage;

    public UITest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            // Start the Toolkit and block until the primary Stage was retrieved.
            primaryStage = FxLifecycle.registerPrimaryStage();
        } catch (TimeoutException ex) {
            Logger.getLogger(UITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            // Construct the Application and call start() with the primary Stage.
            this.app = (MainApp) FxLifecycle.setupApplication(MainApp.class);

            // Wait for the primary Stage to be shown by start().
            RunWaitUtils.waitFor(10, TimeUnit.SECONDS, primaryStage.showingProperty());

        } catch (TimeoutException ex) {
            Logger.getLogger(UITest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test event:
     * <ul>
     * <li> 1. click generate button
     * </ul>
     */
    @Test
    public void clickGenerateButtonAtStartTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + "[NEW MAP CREATED]\n"
                + "10 columns & 10 rows";

        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        assertTrue(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click column combo box
     * <li> 2. select and click number 5
     * <li> 3. click row combo box
     * <li> 4. select and click number 6
     * <li> 5. click generate button
     * </ul>
     */
    @Test
    public void clickComboBoxesAndGenerateTest() {
        clickOn("#column_combo").clickOn("5");

        clickOn("#row_combo").clickOn("6");

        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + "[NEW MAP CREATED]\n"
                + "5 columns & 6 rows";
        /*
         TextArea ta = find("#logArea");
         System.out.println(ta.getText());
         System.out.println(outputLog);
         System.out.println(ta.getText().equals(outputLog));
         */
        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        assertTrue(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click generate button
     * <li> 2. click on title field
     * <li> 3. write "eye of the world" in the field
     * <li> 4. press ENTER key
     * </ul>
     */
    @Test
    public void clickGenerateAndEnterTitleTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        clickOn("#title_field");
        write("eye of the world").push(KeyCode.ENTER);
        Date date2 = new Date();
        sleep(2, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String timeFormat2 = df.format(date2);

        String outputLog = GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + GlobalSettings.LOG_NEW_MAP
                + "10 columns & 10 rows"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat2 + "] " + GlobalSettings.LOG_TITLE_SET
                + "Title: Eye Of The World 10x10 is set.";

        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        Assertions.verifyThat("#title_field", Commons.hasText("Eye Of The World"));
        assertTrue(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click generate button
     * <li> 2. click player 1 start button
     * <li> 3. click tile 8,4
     * <li> 4. click player 2 start button
     * <li> 5. click tile 9,3
     * </ul>
     */
    @Test
    public void clickGenerateThenPlayer1ThenPlayer2AndCanvasTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        clickOn("#p1_start_btn");
        Date date2 = new Date();

        moveBy(-400.00, -200.00).clickOn();
        Date date3 = new Date();

        clickOn("#p2_start_btn");
        Date date4 = new Date();

        moveBy(-300.00, -300.00).clickOn();
        Date date5 = new Date();
        sleep(2, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String timeFormat2 = df.format(date2);
        String timeFormat3 = df.format(date3);
        String timeFormat4 = df.format(date4);
        String timeFormat5 = df.format(date5);

        String outputLog = GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + GlobalSettings.LOG_NEW_MAP
                + "10 columns & 10 rows"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat2 + "] " + GlobalSettings.LOG_PLAYER1_ON
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat3 + "] " + GlobalSettings.LOG_TILE_SET
                + "Player 1 start position is now set to [8,4]"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat4 + "] " + GlobalSettings.LOG_PLAYER2_ON
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat5 + "] " + GlobalSettings.LOG_TILE_SET
                + "Player 2 start position is now set to [9,3]";

        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        assertTrue(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click generate button
     * <li> 2. click player 1 start button
     * <li> 3. click on tile 8,4
     * <li> 4. click on tile 3,4
     * <li> 5. click player 1 start button
     * <li> 6. click on tile 8,8
     * <li> 7. click player 1 start button
     * <li> 8. click on tile 8,8 (click again on the same tile)
     * <li> 9. click player 2 start button
     * <li> 10. click tile 9,3
     * <li> 11. click player 2 start button
     * <li> 12. click tile 9,3 (click again on the same tile)
     * <li> 13. click player 2 start button
     * <li> 14. click tile 8,1
     * <li> 15. click player 2 start button (overwrite p1 start tile )
     * <li> 16. click tile 8,8
     * </ul>
     */
    @Test
    public void clickGenerateThenPlayer1ThenPlayer2AndCanvasTwiceTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        clickOn("#p1_start_btn");
        Date date2 = new Date();

        moveBy(-400.00, -200.00).clickOn();
        Date date3 = new Date();

        moveBy(-400.00, 0.00).clickOn();
        Date date4 = new Date();

        clickOn("#p1_start_btn");
        Date date5 = new Date();

        moveBy(-400.00, 0.0).clickOn();
        Date date6 = new Date();

        clickOn("#p1_start_btn");
        Date date7 = new Date();

        moveBy(-400.00, 0.0).clickOn();
        Date date8 = new Date();

        clickOn("#p2_start_btn");
        Date date9 = new Date();

        moveBy(-300.00, -300.00).clickOn();
        Date date10 = new Date();

        clickOn("#p2_start_btn");
        Date date11 = new Date();

        moveBy(-300.00, -300.00).clickOn();
        Date date12 = new Date();

        clickOn("#p2_start_btn");
        Date date13 = new Date();

        moveBy(-350.00, -400.00).clickOn();
        Date date14 = new Date();

        clickOn("#p2_start_btn");
        Date date15 = new Date();

        moveBy(-400.00, 0.0).clickOn();
        Date date16 = new Date();

        sleep(2, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String timeFormat2 = df.format(date2);
        String timeFormat3 = df.format(date3);
        String timeFormat4 = df.format(date4);
        String timeFormat5 = df.format(date5);
        String timeFormat6 = df.format(date6);
        String timeFormat7 = df.format(date7);
        String timeFormat8 = df.format(date8);
        String timeFormat9 = df.format(date9);
        String timeFormat10 = df.format(date10);
        String timeFormat11 = df.format(date11);
        String timeFormat12 = df.format(date12);
        String timeFormat13 = df.format(date13);
        String timeFormat14 = df.format(date14);
        String timeFormat15 = df.format(date15);
        String timeFormat16 = df.format(date16);

        StringBuilder outputLog = new StringBuilder();

        // clickOn("#generate_map_btn");
        outputLog.append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat).append("] ");
        outputLog.append(GlobalSettings.LOG_NEW_MAP);
        outputLog.append("10 columns & 10 rows");

        // clickOn("#p1_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat2).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER1_ON);

        // moveBy(-400.00, -200.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat3).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 1 start position is now set to [8,4]");

        // moveBy(-400.00, 0.0).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat4).append("] ");
        outputLog.append(GlobalSettings.LOG_NOTE);
        outputLog.append("Nothing selected. [3,4]");

        // clickOn("#p1_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat5).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER1_ON);

        // moveBy(-400.00, 0.0).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat6).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 1 start position is now set to [8,8]");

        // clickOn("#p1_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat7).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER1_ON);

        // moveBy(-400.00, 0.0).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat8).append("] ");
        outputLog.append(GlobalSettings.LOG_ERROR);
        outputLog.append("You already have selected this tile.");

        // clickOn("#p2_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat9).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER2_ON);

        // moveBy(-300.00, -300.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat10).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 2 start position is now set to [9,3]");

        // clickOn("#p2_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat11).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER2_ON);

        // moveBy(-300.00, -300.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat12).append("] ");
        outputLog.append(GlobalSettings.LOG_ERROR);
        outputLog.append("You already have selected this tile.");

        // clickOn("#p2_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat13).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER2_ON);

        // moveBy(-350.00, -400.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat14).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Player 2 start position is now set to [8,1]");

        // clickOn("#p2_start_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat15).append("] ");
        outputLog.append(GlobalSettings.LOG_PLAYER2_ON);

        // moveBy(-400.00, 0.0).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat16).append("] ");
        outputLog.append(GlobalSettings.LOG_WARNING);
        outputLog.append("Player 1 start position is overwritten!");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat16).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_OVERWRITTEN);
        outputLog.append("Player 2 start position is now set to [8,8]");

        Assertions.verifyThat("#logArea",
                Commons.hasText(outputLog.toString()));
        assertTrue(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test events:
     * <ul>
     * <li> 1. click generate button
     * <li> 2, click boulder button
     * <li> 3. click on tile 8,6
     * <li> 4. click neutral button (overwrites boulder button)
     * <li> 5. click on tile 8,6
     * </ul>
     */
    @Test
    public void clickGenerateThenBoulderAndOverwrittenByNeutralTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);

        clickOn("#boulder_btn");
        Date date2 = new Date();

        moveBy(-400.00, -200.00).clickOn();
        Date date3 = new Date();

        clickOn("#neutral_btn");
        Date date4 = new Date();

        moveBy(-400.00, -250.00).clickOn();
        Date date5 = new Date();

        sleep(2, SECONDS);

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String timeFormat2 = df.format(date2);
        String timeFormat3 = df.format(date3);
        String timeFormat4 = df.format(date4);
        String timeFormat5 = df.format(date5);

        StringBuilder outputLog = new StringBuilder();

        // clickOn("#generate_map_btn");
        outputLog.append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat).append("] ");
        outputLog.append(GlobalSettings.LOG_NEW_MAP);
        outputLog.append("10 columns & 10 rows");

        // clickOn("#boulder_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat2).append("] ");
        outputLog.append(GlobalSettings.LOG_BOULDER_ON);

        // moveBy(-400.00, -200.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat3).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Boulder tile is set to [8,6]");

        // clickOn("#neutral_btn");
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat4).append("] ");
        outputLog.append(GlobalSettings.LOG_NEUTRAL_ON);

        // moveBy(-400.00, -250.00).clickOn();
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat5).append("] ");
        outputLog.append(GlobalSettings.LOG_WARNING);
        outputLog.append(GlobalSettings.LOG_BOULDER_OVERWRITTEN);
        outputLog.append("\n").append(GlobalSettings.LOG_SEPARATOR);
        outputLog.append("[").append(timeFormat5).append("] ");
        outputLog.append(GlobalSettings.LOG_TILE_SET);
        outputLog.append("Neutral tile is set to [8,6]");

        Assertions.verifyThat("#logArea",
                Commons.hasText(outputLog.toString()));
        assertTrue(this.app.getView().isThereAMapVisible());
        assertTrue(this.app.getView().isEditMapOn());
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
        sleep(1, SECONDS);

        clickOn("#title_field");
        write("eye of the world").push(KeyCode.ENTER);
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
        sleep(3, SECONDS);

        write(mapFile).push(KeyCode.ENTER);
        Date date7 = new Date();
        sleep(4, SECONDS);

        // Open current map
        KeyCodeCombination ctrlO = new KeyCodeCombination(
                KeyCode.O, KeyCodeCombination.CONTROL_DOWN);
        push(ctrlO);
        sleep(3, SECONDS);

        write(mapFile).push(KeyCode.ENTER);
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

        File mapJson;
        File mapImage;
        if (GlobalSettings.USER_OS.contains(GlobalSettings.OS_WINDOWS)) {
            mapJson = new File(System.getProperty("user.home") 
                    + "\\" + mapFile);
            mapImage = new File(System.getProperty("user.home") 
                    + "\\" + "Map010.png");
        } else {
            mapJson = new File(System.getProperty("user.home") 
                    + "/" + mapFile);
            mapImage = new File(System.getProperty("user.home") 
                    + "/" + "Map010.png");
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

    /**
     * Test events:
     * <ul>
     * <li> 1. click help menu
     * <li> 2. click about menu item
     * <li> 3. click OK button (on the About App dialog window)
     * </ul>
     */
    @Test
    public void testAboutAppDialog() {
        KeyCodeCombination altA = new KeyCodeCombination(
                KeyCode.A, KeyCodeCombination.ALT_DOWN);

        push(altA);
        sleep(2, SECONDS);

        clickOn("OK");
    }

}
