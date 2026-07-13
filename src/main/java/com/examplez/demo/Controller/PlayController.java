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

/**
 * FXML controller for the active card-game view ({@code game-view.fxml}).
 *
 * <p>Coordinates the JavaFX interface with the {@link Game} model, renders the
 * human player's hand and the discard pile, manages human card selection, and
 * runs the turn loop for human and machine players on a background task.</p>
 *
 * <p>All interface changes made from the background task are delegated to the
 * JavaFX Application Thread through {@link Platform#runLater(Runnable)}.</p>
 *
 * @see Game
 * @see PlayerHuman
 * @see PlayerMachine
 * @version 1.0
 */
public class PlayController {
    // -----------------------------------------------------------------------
    // Game and FXML state
    // -----------------------------------------------------------------------

    /** Model that contains the rules and mutable state of the current match. */
    private Game game;
    /** Container used to represent the first machine player. */
    @FXML
    private VBox vbBot1;
    /** Container used to represent the second machine player. */
    @FXML
    private VBox vbBot2;
    /** Container used to represent the third machine player. */
    @FXML
    private VBox vbBot3;
    /** Container used to represent the human player. */
    @FXML
    private VBox vbHuman;
    /** Image views that display the cards currently held by the human player. */
    private ArrayList<ImageView> cardsImages;
    /** Image view that displays the last card placed on the discard pile. */
    @FXML
    private ImageView imCardInPlay;
    /** Horizontal container whose children represent the human player's cards. */
    @FXML
    private HBox hbPlayerCards;
    /** Label used for turn information, validation messages, and game feedback. */
    @FXML
    private Label labelGame;
    /** Label that displays the current accumulated game score. */
    @FXML
    private Label labelScore;
    /** Number of participants selected before the match starts. */
    int numberOfPlayers;
    /** Primary stage used to navigate to the final result view. */
    Stage stage;
    // -----------------------------------------------------------------------
    // Controller setup
    // -----------------------------------------------------------------------

    /**
     * Stores the primary application stage for later scene changes.
     *
     * @param stage application stage that hosts the game
     */
    public void setStage(Stage stage){this.stage=stage;}
    /**
     * Initializes the board for the selected number of players.
     *
     * <p>The method updates player visibility, collects the card image views
     * from the FXML container, and starts a new game session.</p>
     *
     * @param numberOfPlayers total number of human and machine players
     * @throws InterruptedException if game startup is interrupted
     */
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
    /**
     * Creates the game model, deals the initial cards, renders the starting
     * interface, and launches the turn-management task.
     *
     * @throws InterruptedException if startup is interrupted
     */
    protected void startGame() throws InterruptedException {
        game = new Game(numberOfPlayers);
        game.startGame();
        showHandCardPlayer();
        showCardPile();
        createThreadTurnTask();
    }
    // -----------------------------------------------------------------------
    // Turn management
    // -----------------------------------------------------------------------

    /** Creates and starts the daemon thread that executes the turn loop. */
    protected void createThreadTurnTask(){
        Task<Void> task=controlTurnTask();
        Thread thread =new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    /**
     * Builds the background task that controls successive player turns until
     * only one participant remains.
     *
     * <p>Human turns wait for a card-selection event, while machine turns are
     * processed automatically after a short randomized delay. Eliminated
     * players are removed from the model and hidden from the interface.</p>
     *
     * @return a task containing the complete turn-control loop
     */
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
    // -----------------------------------------------------------------------
    // Card rendering and interaction
    // -----------------------------------------------------------------------

    /**
     * Displays the current human hand by resolving each card's image resource
     * and attaching its click handler.
     */
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
    /**
     * Registers the click action for one card image.
     *
     * <p>The card is processed only while the human player's turn flag is
     * active.</p>
     *
     * @param imageView image node associated with the card
     * @param card model card represented by the image
     */
    protected void addListener(ImageView imageView, Card card){
        imageView.setOnMouseClicked(event -> {
            if(game.getHumanPlayer().geTurnState()){
                playCard(card);
            }
        });
    }
    /** Displays the last played card and refreshes the accumulated score. */
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

    // -----------------------------------------------------------------------
    // Human actions
    // -----------------------------------------------------------------------

    /**
     * Attempts to play a card selected by the human player.
     *
     * <p>Valid ace cards may require the player to choose between the values
     * 1 and 10. After a valid play, the hand, pile, and round state are
     * updated. Invalid cards produce a feedback message instead.</p>
     *
     * @param card card selected from the human player's hand
     */
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
    /**
     * Removes a player from the game and hides the corresponding interface
     * container.
     *
     * @param turnPlayer turn identifier of the player to eliminate
     */
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
    /**
     * Opens a choice dialog that asks the human player to assign a value to an
     * ace card.
     *
     * @return selected ace value, or {@code 1} when the dialog is dismissed
     */
    }
    protected int askAceValue() {
        ChoiceDialog<Integer> dialog =
                new ChoiceDialog<>(10, List.of(1, 10));

        dialog.setTitle("Ace");
        dialog.setHeaderText("Choose the value of the Ace");
        dialog.setContentText("Value:");
        return dialog.showAndWait().orElse(1);
    }

    // -----------------------------------------------------------------------
    // View navigation and layout
    // -----------------------------------------------------------------------

    /**
     * Loads the final view and passes the winner identifier to its controller.
     *
     * @param idWinner turn identifier of the last remaining player
     * @throws IOException if {@code final-view.fxml} cannot be loaded
     */
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
    /**
     * Shows or hides machine-player containers according to the selected
     * number of participants. The managed state is changed together with the
     * visible state so hidden containers do not occupy layout space.
     */
    protected void updateBoard() {

        vbBot1.setVisible(numberOfPlayers >= 2);
        vbBot1.setManaged(numberOfPlayers >= 2);

        vbBot2.setVisible(numberOfPlayers >= 3);
        vbBot2.setManaged(numberOfPlayers >= 3);

        vbBot3.setVisible(numberOfPlayers >= 4);
        vbBot3.setManaged(numberOfPlayers >= 4);
    }
}
