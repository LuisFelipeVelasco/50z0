package com.examplez.demo.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        winnerLabel.setText("player"+ idWinner+ "is the winner");
    }
}
