package com.examplez.demo;

import com.examplez.demo.Controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameLauncher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameLauncher.class.getResource("view/menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        MenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }
}
