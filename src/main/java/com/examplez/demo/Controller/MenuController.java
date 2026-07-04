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
    private void onButtonTwoPlayers() throws IOException {
        numberOfPlayers=2;
        changeView();
    }
    @FXML
    private void onButtonThreePlayers() throws IOException {
        numberOfPlayers=3;
        changeView();
    }
    @FXML
    private void onButtonFourPlayers() throws IOException {
        numberOfPlayers=4;
        changeView();
    }
    private void changeView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/examplez/demo/view/game-view.fxml"));
        Parent root = fxmlLoader.load();
        PlayController playController =fxmlLoader.getController();
        playController.setStage(stage);
        playController.setNumberOfPlayers(numberOfPlayers);
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
