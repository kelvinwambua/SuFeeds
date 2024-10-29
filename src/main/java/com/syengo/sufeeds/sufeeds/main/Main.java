package com.syengo.sufeeds.sufeeds.main;

import com.syengo.sufeeds.sufeeds.ui.LoginForm;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginForm loginForm = new LoginForm();
        Scene scene = new Scene(loginForm.getRoot(), 300, 200);
        primaryStage.setTitle("Student Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
