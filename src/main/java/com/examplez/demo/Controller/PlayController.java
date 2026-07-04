package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayController {
    int numberOfPlayers;
    Stage stage;
    public void setStage(Stage stage){this.stage=stage;}
    public void setNumberOfPlayers(int numberOfPlayers){this.numberOfPlayers=numberOfPlayers;}
    private void initialize(){}
    private void changeView(int idWinner) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/examplez/demo/view/final-view.fxml"));
        Parent root = fxmlLoader.load();
        FinalController finalController =fxmlLoader.getController();
        finalController.showWinner(idWinner);
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
