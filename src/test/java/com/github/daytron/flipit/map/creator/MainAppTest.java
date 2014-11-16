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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javax.swing.text.DateFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
     * Test for Generating button works as the first event.
     * Test of start method, of class MainApp.
     */
    @Test
    @Ignore
    public void clickGenerateButtonAtStartTest() throws Exception {
        click("#generate_map_btn");
        Date date = new Date();
        
        sleep(1, SECONDS);
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String outputLog = "[" + timeFormat + "] " + "[NEW MAP CREATED]\n"
                + "10 columns & 10 rows";
        
        Assertions.verifyThat("#logArea", Commons.hasText(outputLog));
    }

    /**
     * Test of getStage method, of class MainApp.
     */
    @Test
    @Ignore
    public void comboBoxexClickAndGenerateTest() {
        click("#column_combo").click("5");
        sleep(2, SECONDS);
        click("#row_combo").click("6");
        sleep(1, SECONDS);
        click("#generate_map_btn");
        Date date = new Date();
        sleep(1, SECONDS);
        
        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String timeFormat =  df.format(date);
        String outputLog = 
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
     * Test of main method, of class MainApp.
     */
    @Test
    @Ignore
    public void clickFileQuitWithDialogTest() {
        click("#file").click("#open");
        sleep(2, SECONDS);
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
