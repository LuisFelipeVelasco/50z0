package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import com.examplez.demo.Model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    private Stage stage;
    int numberOfPlayers;
    public void setStage(Stage stage){this.stage=stage;}
    @FXML
    protected void onButtonTwoPlayers() throws IOException, InterruptedException {
        numberOfPlayers=2;
        changeView();
    }
    @FXML
    protected void onButtonThreePlayers() throws IOException, InterruptedException {
        numberOfPlayers=3;
        changeView();
    }
    @FXML
    protected void onButtonFourPlayers() throws IOException, InterruptedException {
        numberOfPlayers=4;
        changeView();
    }
    protected void changeView() throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/examplez/demo/view/game-view.fxml"));
        Parent root = fxmlLoader.load();
        PlayController playController =fxmlLoader.getController();
        playController.setStage(stage);
        playController.initialize(numberOfPlayers);
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
