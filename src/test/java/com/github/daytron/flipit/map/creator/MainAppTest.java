/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.daytron.flipit.map.creator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.controls.Commons;

/**
 *
 * @author ryan
 */
public class MainAppTest extends GuiTest {
    private MainApp app;
    
    public MainAppTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
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
        click("#generate_map_btn");
        Date date = new Date();
        
        sleep(1, SECONDS);
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR +
                "[" + timeFormat + "] " + "[NEW MAP CREATED]\n"
                + "10 columns & 10 rows";
        
        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
    }

    /**
     * Test of comboboxes and generate button.
     */
    @Test
    @Ignore
    public void clickComboBoxesAndGenerateTest() {
        click("#column_combo").click("5");
        sleep(2, SECONDS);
        click("#row_combo").click("6");
        sleep(1, SECONDS);
        click("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR +
                "[" + timeFormat + "] " + "[NEW MAP CREATED]\n"
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
        click("#generate_map_btn");
        Date date = new Date();
        
        sleep(1, SECONDS);
        click("#title_field").type("eye of the world").push(KeyCode.ENTER);
        Date date2 = new Date();
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String timeFormat2 =  df.format(date2);
        
        String outputLog = GlobalSettings.LOG_SEPARATOR +
                "[" + timeFormat + "] " + GlobalSettings.LOG_NEW_MAP
                + "10 columns & 10 rows" 
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat2 + "] " +  GlobalSettings.LOG_TITLE_SET
                + "Title: Eye Of The World 10x10 is set.";
        
        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
        Assertions.verifyThat("#title_field", Commons.hasText("Eye Of The World"));
    }
    
    @Test
    public void newTest() {
        System.out.println(this.app.getView().isThereAMapVisible());
    }

    /**
     * Test of generate button and player 1 start button.
     * Note: There is a bug currently in TestFX 3.1.2, method moveBy causes a
     * DeathThread RuntimeException, author said bug is fix by next version.
     */
    @Test
    @Ignore
    public void clickGenerateThenPlayer1StartAndCanvasTest(){
        click("#generate_map_btn");
        Date date = new Date();
        
        sleep(1, SECONDS);
        click("#p1_start_btn").moveBy(-400.00, -200.00).click();
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String outputLog = GlobalSettings.LOG_SEPARATOR +
                "[" + timeFormat + "] " + GlobalSettings.LOG_NEW_MAP
                + "10 columns & 10 rows"
                + "\n" + GlobalSettings.LOG_SEPARATOR
                + "[" + timeFormat + "] " + GlobalSettings.LOG_TILE_SET
                + "Player 1 start position is now set to [8,14]";
        
        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
    }
    
    @Override
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
