// This is the main function it runs the whole app
package com.syengo.sufeeds.sufeeds.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        // Create Scene
        Scene scene = new Scene(root);

        // Add Stylesheet
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());

        // Stage Configuration
        primaryStage.setTitle("SU Feeds");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
