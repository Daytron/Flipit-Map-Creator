/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.map.creator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.controls.Commons;
import org.loadui.testfx.framework.robot.impl.FxRobotImpl;
import org.loadui.testfx.utils.RunWaitUtils;

/**
 *
 * @author ryan
 */
public class MainAppTest extends FxRobotImpl {

    private MainApp app;
    public static Stage primaryStage;

    public MainAppTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        // Start the Toolkit and block until the primary Stage was retrieved.

        //primaryStage = FxLifecycle.registerPrimaryStage();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // Construct the Application and call start() with the primary Stage. 
        //Application demoApplication = FxLifecycle.setupApplication(MainApp.class);

        // Wait for the primary Stage to be shown by start().
        //RunWaitUtils.waitFor(10, TimeUnit.SECONDS, primaryStage.showingProperty());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test for generate button.
     */
    @Test
    @Ignore
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
    }

    /**
     * Test of comboboxes and generate button.
     */
    @Test
    @Ignore
    public void clickComboBoxesAndGenerateTest() {
        clickOn("#column_combo").clickOn("5");
        sleep(2, SECONDS);
        clickOn("#row_combo").clickOn("6");
        sleep(1, SECONDS);
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
    }

    /**
     * Test of generate button and title field.
     */
    @Test
    @Ignore
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
    }

    @Test
    @Ignore
    public void newTest() {
        System.out.println(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test of generate button and player 1 start button. Note: There is a bug
     * currently in TestFX 3.1.2, method moveBy causes a DeathThread
     * RuntimeException, author said bug is fix by next version.
     */
    @Test
    @Ignore
    public void clickGenerateThenPlayer1StartAndCanvasTest() {
        clickOn("#generate_map_btn");
        Date date = new Date();

        sleep(1, SECONDS);
        clickOn("#p1_start_btn").moveBy(-400.00, -200.00).clickOn();

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat = df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + GlobalSettings.LOG_NEW_MAP
                + "10 columns & 10 rows"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + GlobalSettings.LOG_TILE_SET
                + "Player 1 start position is now set to [8,14]";

        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
    }

    protected Parent getRootNode() {
        try {
            this.app = new MainApp();
            return app.getRoot();
        } catch (IOException ex) {
            Logger.getLogger(MainAppTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
