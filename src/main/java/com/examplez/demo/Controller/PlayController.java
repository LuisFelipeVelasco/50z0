package com.examplez.demo.Controller;
import com.examplez.demo.Model.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
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
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayController {
    private Game game;
    @FXML
    private VBox vbBot1;
    @FXML
    private VBox vbBot2;
    @FXML
    private VBox vbBot3;
    @FXML
    private VBox vbHuman;
    private ArrayList<ImageView> cardsImages;
    @FXML
    private ImageView imCardInPlay;
    @FXML
    private HBox hbPlayerCards;
    @FXML
    private Label labelGame;
    @FXML
    private Label labelScore;
    int numberOfPlayers;
    Stage stage;
    public void setStage(Stage stage){this.stage=stage;}
    @FXML

    public void initialize(int numberOfPlayers) throws InterruptedException {
        this.numberOfPlayers=numberOfPlayers;
        updateBoard();
        cardsImages = new ArrayList<>();
        for (Node node : hbPlayerCards.getChildren()) {
            if (node instanceof ImageView imageView) {
                cardsImages.add(imageView);
            }
        }
        startGame();
    }
    protected void startGame() throws InterruptedException {
        game = new Game(numberOfPlayers);
        game.startGame();
        showHandCardPlayer();
        showCardPile();
        createThreadTurnTask();
    }
    protected void createThreadTurnTask(){
        Task<Void> task=controlTurnTask();
        Thread thread =new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    protected Task<Void> controlTurnTask(){
        Task<Void> task = new Task<>(){
            @Override
            protected Void call() throws Exception{
                List<Integer>turnPlayers=game.getTurnPlayers();
                while(turnPlayers.size()>1){
                    for(int t: game.getTurnPlayers()){
                        Platform.runLater(() -> {
                            if(t==0)labelGame.setText("Is your turn");
                            else labelGame.setText("Is turn of bot " + t);});
                        Thread.sleep(1000);
                        if(t==0){
                            PlayerHuman playerHuman= game.getHumanPlayer();
                            playerHuman.setTurnState(true);
                            if(game.isHumanPlayerAbleToPlay()){
                                try {
                                    game.waitUntilRoundEnds();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else{
                                game.eliminatePlayer(0);
                                eliminatePlayer(0);
                                Platform.runLater(() -> {
                                    labelGame.setText("You didn´t have valid cards");
                                });
                                Thread.sleep(2000);
                            }
                        }
                        else {
                            if (game.isMachinePlayerAbleToPlay(t)) {
                                int waitingPlayTime = ThreadLocalRandom.current().nextInt(2, 4);
                                Thread.sleep(waitingPlayTime * 1000L);
                                game.processCardPlayedByMachinePlayer(t);
                                Platform.runLater(() -> {
                                    showCardPile();
                                    labelGame.setText("Bot " + t + " is drawing a card");
                                });
                                int waitingDrawTime = ThreadLocalRandom.current().nextInt(2, 4);
                                Thread.sleep(waitingDrawTime * 1000L);
                                game.addDeskCardToPlayerHand(t);
                            }
                            else {
                                game.eliminatePlayer(t);
                                eliminatePlayer(t);
                                Platform.runLater(() -> {
                                    labelGame.setText("Bot " + t + " didn´t have valid cards");

                                });
                                Thread.sleep(2000);;

                            }
                    }
                        Platform.runLater(() -> {
                            labelScore.setText(game.getCurrentSumGame() + "");
                        });
                        turnPlayers=game.getTurnPlayers();
                        if(turnPlayers.size()==1) break;

                    }
                }
                int winner = turnPlayers.get(0);
                Platform.runLater(() -> {
                    try {
                        changeView(winner);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            return null;}
          };
        return task;
    }
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
        });
    }
    protected void showCardPile(){
        Card card=game.getLastCardPlayed();
        String path = "/com/examplez/demo/images/Cards/"
                + card.getIdCard()
                + "-"
                + card.getCardValue()
                + ".png";
        Image image = new Image(getClass().getResourceAsStream(path));
        imCardInPlay.setImage(image);
        Platform.runLater(() -> {
            labelScore.setText(game.getCurrentSumGame() + "");
        });
    }

    protected void playCard(Card card){

        if(game.isPlayerHumanCardValid(card)){
            int aceValue = card.getCardValue();
            if(aceValue==-1){
                List<Integer> posibleAceValues= game.getPossibleAceValues();
                if(posibleAceValues.size()==2){
                    aceValue = askAceValue();
                }
                else{
                    aceValue= posibleAceValues.get(0);
                }
            }
            game.processCardPlayedByHumanPlayer(card.getIdCard(), aceValue);
            showHandCardPlayer();
            showCardPile();
            game.endRound();
        }
        else {labelGame.setText("The card selected is not valid");}
    }
    protected void eliminatePlayer(int turnPlayer) {
        game.eliminatePlayer(turnPlayer);

        if (turnPlayer==0) {
            vbHuman.setVisible(false);
            vbHuman.setManaged(false);
        }

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
            vbBot3.setManaged(false);
        }
    }
    protected int askAceValue() {
        ChoiceDialog<Integer> dialog =
                new ChoiceDialog<>(10, List.of(1, 10));

        dialog.setTitle("Ace");
        dialog.setHeaderText("Choose the value of the Ace");
        dialog.setContentText("Value:");
        return dialog.showAndWait().orElse(1);
    }

    protected void changeView(int idWinner) throws IOException {
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
    protected void updateBoard() {

        vbBot1.setVisible(numberOfPlayers >= 2);
        vbBot1.setManaged(numberOfPlayers >= 2);

        vbBot2.setVisible(numberOfPlayers >= 3);
        vbBot2.setManaged(numberOfPlayers >= 3);

        vbBot3.setVisible(numberOfPlayers >= 4);
        vbBot3.setManaged(numberOfPlayers >= 4);
    }
}
