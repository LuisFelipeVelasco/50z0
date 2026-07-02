package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
public class PlayerMachine extends Player{

    public PlayerMachine(List<Card> deckCards, int turn) {
        super(deckCards, turn);
                                                         }

     public Card playCardInt(int SumaPuntos){
        if (SumaPuntos<50){
            for (Card Card: HandCard ){
                if(Card.getCardValue()+SumaPuntos<=50){
                    return Card;
                }
            }
        }
     return null;}

}


