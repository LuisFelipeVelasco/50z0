package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import com.examplez.demo.Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayController {
    private Player player;
    private Desk desk;
    private DiscardPile discardPile;
    private Game game;
    //public void playController(Player player){
      //  this.player=player;
    //}
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

    @FXML
    private void onPlayButton() {

        game = new Game(numberOfPlayers);     // Humano + 3 máquinas
        game.startGame();

        showHandCardPlayer();
    }
    protected void showHandCardPlayer(){
        List<Card> hand = game.getHandHumanPlayer();

        for(Card card : hand){
            System.out.println(card);//quitar despues este sout
        }



    }
    protected void addListener(List<Card>Cards){

    }


}
