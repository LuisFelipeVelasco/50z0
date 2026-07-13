package com.examplez.demo.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * FXML controller for the final game view ({@code final-view.fxml}).
 *
 * <p>Receives the identifier of the remaining player and displays the
 * corresponding winner message in the interface.</p>
 *
 * @version 1.0
 */
public class FinalController {
    // -----------------------------------------------------------------------
    // FXML-injected controls
    // -----------------------------------------------------------------------

    /** Label used to announce the winner of the completed game. */
    @FXML
    private Label winnerLabel;
    // -----------------------------------------------------------------------
    // Public interface
    // -----------------------------------------------------------------------

    /**
     * Updates the result label with the identifier of the winning player.
     *
     * @param idWinner turn identifier assigned to the player who won
     */
    public void showWinner(int idWinner){
        if(idWinner==0){
            winnerLabel.setText("You did it , You are the Champ");
        }
        else{
            winnerLabel.setText("Bot " + idWinner+ " is the winner");
        }
    }
    @FXML
    private void onContinueButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/examplez/demo/view/menu-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage =(Stage)winnerLabel.getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        MenuController menuController = fxmlLoader.getController();
        menuController.setStage(stage);
        stage.setTitle("Menu");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void onCloseButton(){
        Platform.exit();
    }
}
