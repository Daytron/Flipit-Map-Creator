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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
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
public class MainAppTest extends FxRobotImpl {

    private MainApp app;
    public static Stage primaryStage;

    public MainAppTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            // Start the Toolkit and block until the primary Stage was retrieved.
            primaryStage = FxLifecycle.registerPrimaryStage();
        } catch (TimeoutException ex) {
            Logger.getLogger(MainAppTest.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(MainAppTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for generate button.
     */
    @Test
    public void clickGenerateButtonAtStartTest() throws Exception {
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
     * Test of comboboxes and generate button.
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
     * Test of generate button and title field.
     */
    @Test
    public void clickGenerateAndEnterTitleTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();

        sleep(1, SECONDS);
        clickOn("#title_field").write("eye of the world").push(KeyCode.ENTER);
        Date date2 = new Date();

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
     * Test of generate button and player 1 start button. 
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
     * Test of generate button and player 1 start button. 
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
                + "[" + timeFormat2 + "] " + GlobalSettings.LOG_BOULDER_ON
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat3 + "] " + GlobalSettings.LOG_TILE_SET
                + "Boulder tile is set to [8,6]"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat4 + "] " + GlobalSettings.LOG_NEUTRAL_ON
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat5 + "] " + GlobalSettings.LOG_WARNING 
                + GlobalSettings.LOG_BOULDER_OVERWRITTEN
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat5 + "] " + GlobalSettings.LOG_TILE_SET
                + "Neutral tile is set to [8,6]";

        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        assertTrue(this.app.getView().isThereAMapVisible());
        assertTrue(this.app.getView().isEditMapOn());
    }
    
    @Test
    @Ignore
    public void clickFileOpenMapTest() {
        clickOn("#file").clickOn("#open");
        sleep(1, SECONDS);
        
        //clickOn(".file-path-textfield").write("/home/user/fixtures/enterprise-data.xml"); 
    }

    @Test
    public void clickHelpAboutAppTest() {
        clickOn("#help").clickOn("#about");
        sleep(1, SECONDS);
        closeCurrentWindow();
        sleep(1, SECONDS);
    }
}
