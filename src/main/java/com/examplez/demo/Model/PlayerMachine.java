package com.examplez.demo.Model;
import java.util.List;
public class PlayerMachine extends Player{

    public PlayerMachine(List<Card> deckCards, int turn) {
        super(deckCards, turn);
                                                         }
    //change 50 with a parameter call maximumSumPoints
    //add a method called isAbleToPlay(int currentSum , int maximumSum )that iterate each card on the hand and return true if there is a card to play , and false if not
    //Change name playCardInt by cardPlayed

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


