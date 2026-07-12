package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
    public void setNumberOfPlayers(int numberOfPlayers) throws InterruptedException {this.numberOfPlayers=numberOfPlayers;}
    @FXML

    //TASK: Generate the cards on the board depending on the number of players

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
                        System.out.println(game.currentSumGame);
                        if(t==0){
                            Thread.sleep(1000);
                            PlayerHuman playerHuman= game.getHumanPlayer();
                            playerHuman.setTurnState(true);
                            Platform.runLater(()->{
                                labelGame.setText("is your turn");});
                            try {
                                game.waitUntilRoundEnds();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            Platform.runLater(() -> {
                            labelGame.setText("is turn of player" + t);});
                            if (game.isMachinePlayerAbleToPlay(t)) {
                                /*
                                int waitingTime= ThreadLocalRandom.current().nextInt(1, 4);
                                Thread thread =new Thread(()->{
                                    try {
                                        Thread.sleep(waitingTime* 1000L);
                                        Platform.runLater(()->{
                                            game.processCardPlayedByMachinePlayer(t);
                                            showCardPile();
                                        });
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                                thread.start();*/
                                int waitingTime = ThreadLocalRandom.current().nextInt(1, 4);

                                // Espera para simular que la IA está pensando
                                Thread.sleep(waitingTime * 1000L);

                                // La IA juega
                                Platform.runLater(() -> {

                                    game.processCardPlayedByMachinePlayer(t);

                                    showCardPile();
                                });

                                // Espera un poco para que el usuario vea la carta antes del siguiente turno
                                Thread.sleep(500);
                            }
                            else {
                                game.eliminatePlayer(t);
                                eliminatePlayer(t);
                                labelGame.setText("Player " + t + "was eliminated");

                            }
                    }

                    }
                    turnPlayers=game.getTurnPlayers();
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

            int aceValue = card.getCardValue();


            if("40".equals(card.getIdCard())){
                System.out.println("Entró al if del As");
                aceValue = askAceValue();
            }

            game.processCardPlayedByHumanPlayer(card.getIdCard(), aceValue);
            showCardPile();
            showHandCardPlayer();
            showCardPile();
            game.endRound();
        }
        else {labelGame.setText("The card selected is not valid");}
    }
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
            vbBot3.setManaged(false);
        }
    }
    private int askAceValue() {

        ChoiceDialog<Integer> dialog =
                new ChoiceDialog<>(10, List.of(1, 10));

        dialog.setTitle("Ace");
        dialog.setHeaderText("Choose the value of the Ace");
        dialog.setContentText("Value:");

        return dialog.showAndWait().orElse(1);
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
