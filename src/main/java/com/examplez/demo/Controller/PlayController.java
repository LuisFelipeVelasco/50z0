package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import com.examplez.demo.Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayController {
    //TASK: card1, card2, card3 are not in the fxml of game view , instead call the vbox that save the four images and then iterate it with getchildren ,
    private Game game;

    @FXML
    private VBox vbBot1;

    @FXML
    private VBox vbBot2;

    @FXML
    private VBox vbBot3;
    private ArrayList<ImageView> cardsImages;
    @FXML
    private ImageView imCardInPlay;
    @FXML
    private HBox hbPlayerCards;
    @FXML
    private Label labelGame;
    int numberOfPlayers;
    Stage stage;
    public void setStage(Stage stage){this.stage=stage;}
    public void setNumberOfPlayers(int numberOfPlayers) throws InterruptedException {this.numberOfPlayers=numberOfPlayers;
        updateBoard();
        startGame();
    }
    @FXML

    //TASK: Generate the cards on the board depending on the number of players

    private void initialize() throws InterruptedException {

        cardsImages = new ArrayList<>();


        for (Node node : hbPlayerCards.getChildren()) {
            if (node instanceof ImageView imageView) {
                cardsImages.add(imageView);
            }
        }


    }
    protected void startGame() throws InterruptedException {
        game = new Game(numberOfPlayers);     // Humano + 3 máquinas
        game.startGame();
        showHandCardPlayer();
        showCardPile();
        controlTurns();
    }
    protected void controlTurns() throws InterruptedException {
        List<Integer>turnPlayers=game.getTurnPlayers();
        while(turnPlayers.size()>1){
            for(int t: game.getTurnPlayers()){
                labelGame.setText("is turn of player " + t );
                if(t==0){
                    PlayerHuman playerHuman= game.getHumanPlayer();
                    playerHuman.setTurnState(true);
                    labelGame.setText("is your turn");
                    game.waitUntilRoundEnds();
                }
                else{
                    if(game.isMachinePlayerAbleToPlay(t)){
                        game.processCardPlayedByMachinePlayer(t);
                        showCardPile();
                    }
                    //TASK: Delete cards of player in the board
                    else{
                        labelGame.setText("Player "+ t+ "was eliminated");

                    }
                }
            }
        }
        labelGame.setText("the game is over");
    }
    //TASK : image could be null , why?
    protected void showHandCardPlayer(){
        List<Card> hand = game.getHandHumanPlayer();

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);

            String path = "/com/examplez/demo/images/Cards/"
                    + card.getIdCard()
                    + "-"
                    + card.getCardValue()
                    + ".png";

            InputStream stream = getClass().getResourceAsStream(path);

            if (stream != null) {
                Image image = new Image(stream);
                cardsImages.get(i).setImage(image);
            } else {
                System.out.println("Image not found: " + path);
            }

            addListener(cardsImages.get(i), card);
        }
    }
    protected void addListener(ImageView imageView, Card card){
        imageView.setOnMouseClicked(event -> {
            if(game.getHumanPlayer().geTurnState()){
                playCard(card);
            }
            else{
                labelGame.setText("Is not your turn");
            }
        });
    }
    private void showCardPile(){

        Card card=game.getLastCardPlayed();
        String path = "/com/examplez/demo/images/Cards/"
                + card.getIdCard()
                + "-"
                + card.getCardValue()
                + ".png";
        Image image = new Image(getClass().getResourceAsStream(path));

        imCardInPlay.setImage(image);
    }

    protected void playCard(Card card){

        if(game.isPlayerHumanCardValid(card)){
            game.processCardPlayedByHumanPlayer(card.getIdCard());
            showCardPile();
            showHandCardPlayer();
            showCardPile();
            game.endRound();
        }
        else {labelGame.setText("The card selected is not valid");}
    }
    //Task: Delete the cards on the view of that player
    protected void eliminatePlayer(int turnPlayer) {
        game.eliminatePlayer(turnPlayer);

        if (turnPlayer==1) {
            vbBot1.setVisible(false);
            vbBot1.setManaged(false);
        }
        if (turnPlayer==2){
            vbBot2.setVisible(false);
            vbBot2.setManaged(false);
        }
        if (turnPlayer==3){
            vbBot3.setVisible(false);
            vbBot3.setVisible(false);
        }
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
    private void updateBoard() {

        vbBot1.setVisible(numberOfPlayers >= 2);
        vbBot1.setManaged(numberOfPlayers >= 2);

        vbBot2.setVisible(numberOfPlayers >= 3);
        vbBot2.setManaged(numberOfPlayers >= 3);

        vbBot3.setVisible(numberOfPlayers >= 4);
        vbBot3.setManaged(numberOfPlayers >= 4);
    }
}
