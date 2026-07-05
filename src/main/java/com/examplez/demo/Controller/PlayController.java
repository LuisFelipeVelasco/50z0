package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import com.examplez.demo.Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayController {
    private Player player;
    private Desk desk;
    private DiscardPile discardPile;
    private Game game;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;
    @FXML
    private ImageView card3;
    @FXML
    private ImageView card4;
    private ImageView[] cardsImages;
    @FXML
    private ImageView pileCard;
    int numberOfPlayers;
    Stage stage;
    public void setStage(Stage stage){this.stage=stage;}
    public void setNumberOfPlayers(int numberOfPlayers){this.numberOfPlayers=numberOfPlayers;}
    @FXML
    private void initialize(){
        cardsImages = new ImageView[]{
                card1,
                card2,
                card3,
                card4
        };
        startGame();



    }
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
    protected void showHandCardPlayer(){
        List<Card> hand = game.getHandHumanPlayer();

        for(int i = 0; i < hand.size(); i++){
            Card card = hand.get(i);

            String path =
                    "/com/examplez/demo/images/Cards/"
                            + card.getIdCard()
                            + "-"
                            + String.format("%02d", card.getCardValue())
                            + ".png";

            Image image = new Image(getClass().getResourceAsStream(path));

            cardsImages[i].setImage(image);

            addListener(cardsImages[i], card);
        }



    }
    protected void addListener(ImageView imageView, Card card){
        imageView.setOnMouseClicked(event -> {

            String id= card.getIdCard();
            int turnMachinePlayer=0;
           game.processCardPlayedByHumanPlayer(turnMachinePlayer,id);
           showHandCardPlayer();
           showCardPile();



        });

    }
    private void showCardPile(){

        Card card=discardPile.getLastCard();
        String path = "/com/examplez/demo/images/Cards/"
                + card.getIdCard()
                + "-"
                + String.format("%02d", card.getCardValue())
                + ".png";
        Image image = new Image(getClass().getResourceAsStream(path));

        pileCard.setImage(image);

    }

    protected void startGame(){
        game = new Game(numberOfPlayers);     // Humano + 3 máquinas
        game.startGame();
        showHandCardPlayer();
        showCardPile();


    }




}
