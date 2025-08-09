package com.muaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Lab6App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var root = FXMLLoader.load(getClass().getResource("/ui/lotto.fxml"));
        stage.setTitle("Lab 6 - Quick Pick Lotto");
        stage.setScene(new Scene(root, 520, 380));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
