package com.examplez.demo.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FinalController {
    @FXML
    private Label winnerLabel;
    public void showWinner(int idWinner){
        winnerLabel.setText("player"+ idWinner+ "is the winner");
    }
}
