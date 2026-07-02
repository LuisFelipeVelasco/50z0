package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
public class PlayerHuman extends Player{
    public PlayerHuman(List<Card> deckCards, int turn) {
        super(deckCards, turn);
    }
boolean RondState=false;
public boolean getRondState(){
    return RondState;
}


}
