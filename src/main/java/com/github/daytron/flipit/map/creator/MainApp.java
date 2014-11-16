/**
 * *
 * Created by Ryan Gilera <jalapaomaji-github@yahoo.com>
 */
package com.github.daytron.flipit.map.creator;

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
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    private final String MAIN_FXML = "View.fxml";
    private ViewController viewController;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        this.loadScene();
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    private void loadScene() {
        try {
            this.viewController = (ViewController) replaceScene(MAIN_FXML);
            this.viewController.setApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ViewController getView() {
        if (this.viewController != null) {
            return this.viewController;
        } else {
            return  null;
        }
    }

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
    
    Parent getRoot() throws IOException {
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
