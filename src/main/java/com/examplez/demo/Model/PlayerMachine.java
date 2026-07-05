package com.examplez.demo.Model;
import java.util.List;
public class PlayerMachine extends Player{
    public PlayerMachine(List<Card> deckCards, int turn) {
        super(deckCards, turn);
                                                         }
     public Card cardPlayed(int currentSum,int maximumSumPoints){
        if (currentSum<maximumSumPoints){
            for (Card Card: handCard ){
                if(Card.getCardValue()+currentSum<=maximumSumPoints){
                    return Card;
                }
            }
        }
     return null;}

    public boolean isAbleToPlay(int currentSum,int maximumSum){
        for (Card Card: handCard){
            if (Card.getCardValue()+currentSum<=maximumSum){
                return true;
            }

        }
        return false; }
    
}


