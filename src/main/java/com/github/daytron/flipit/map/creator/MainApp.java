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

import com.github.daytron.flipit.map.creator.controller.ViewController;
import com.github.daytron.flipit.map.creator.utility.GlobalSettings;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The main class. Entry point of the application. Sets and loads the view of
 * the application.
 *
 * @author Ryan Gilera
 */
public class MainApp extends Application {

    private Stage stage;
    private final String MAIN_FXML = "View.fxml";
    private ViewController viewController;

    /**
     * The first method to launch in a JavaFX application. Calls loadScene() to
     * load the view fxml file.
     *
     * @param stage Stage object pass by the System
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        this.loadScene();
        stage.getIcons().add(
                new Image(MainApp.class.getResourceAsStream(
                                GlobalSettings.ICON_PATH)));
        
        stage.show();
    }

    /**
     * @return Returns the application's stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Loads the scene by calling the replaceScene() method
     */
    private void loadScene() {
        try {
            this.viewController = (ViewController) replaceScene(MAIN_FXML);
            this.viewController.setApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return Returns the view controller object
     */
    public ViewController getView() {
        if (this.viewController != null) {
            return this.viewController;
        } else {
            return null;
        }
    }

    /**
     * Replace a scene based on the argument passed and set it to stage.
     *
     * @param fxml The name of the fxml file to load.
     * @return Returns a Initializable object that can be cast to ViewController
     * later on.
     * @throws IOException if file is not a valid fxml file.
     */
    private Initializable replaceScene(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream("/fxml/" + fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource("/fxml/" + fxml));

        Parent pane;
        try {
            pane = loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(pane);
        stage.setTitle("Flipit Map Creator");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

    /**
     * @return Returns a Parent object, that is the root of the application.
     * @throws IOException Throws an IOException if it is not a valid fxml file.
     */
    public Parent getRoot() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/View.fxml"));
        return root;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
