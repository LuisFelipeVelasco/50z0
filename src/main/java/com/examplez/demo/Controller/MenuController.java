package com.examplez.demo.Controller;

import com.examplez.demo.GameLauncher;
import com.examplez.demo.Model.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * FXML controller for the player-selection menu ({@code menu-view.fxml}).
 *
 * <p>Stores the primary {@link Stage}, handles the two-, three-, and four-player
 * buttons, and loads the main game view with the selected number of players.</p>
 *
 * @see PlayController
 * @version 1.0
 */
public class MenuController {
    // -----------------------------------------------------------------------
    // Instance state
    // -----------------------------------------------------------------------

    /** Primary application stage on which the game scenes are displayed. */
    private Stage stage;
    /** Number of participants selected from the menu. */
    int numberOfPlayers;
    /**
     * Stores the primary stage supplied by the application launcher.
     *
     * @param stage application stage used for subsequent scene changes
     */
    public void setStage(Stage stage){this.stage=stage;}
    // -----------------------------------------------------------------------
    // FXML event handlers
    // -----------------------------------------------------------------------

    /**
     * Starts a game configured for two players.
     *
     * @throws IOException if the game-view FXML resource cannot be loaded
     * @throws InterruptedException if game initialization is interrupted
     */
    @FXML
    protected void onButtonTwoPlayers() throws IOException, InterruptedException {
        numberOfPlayers=2;
        changeView();
    }
    /**
     * Starts a game configured for three players.
     *
     * @throws IOException if the game-view FXML resource cannot be loaded
     * @throws InterruptedException if game initialization is interrupted
     */
    @FXML
    protected void onButtonThreePlayers() throws IOException, InterruptedException {
        numberOfPlayers=3;
        changeView();
    }
    /**
     * Starts a game configured for four players.
     *
     * @throws IOException if the game-view FXML resource cannot be loaded
     * @throws InterruptedException if game initialization is interrupted
     */
    @FXML
    protected void onButtonFourPlayers() throws IOException, InterruptedException {
        numberOfPlayers=4;
        changeView();
    }
    // -----------------------------------------------------------------------
    // View navigation
    // -----------------------------------------------------------------------

    /**
     * Loads the game view, transfers the stage and player count to its
     * controller, and replaces the current menu scene.
     *
     * @throws IOException if {@code game-view.fxml} cannot be loaded
     * @throws InterruptedException if the game controller is interrupted while
     *                              starting the match
     */
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
