package com.examplez.demo.Model;
import java.util.List;
public class PlayerMachine extends Player{
    public PlayerMachine(List<Card> deckCards, int turn) {
        super(deckCards, turn);
                                                         }
     public Card cardPlayed(int currentSum,int maximumSumPoints){
        if (currentSum<=maximumSumPoints){
            for (Card Card: handCard ){
                if(Card.getCardValue()==-1 && ( 10+currentSum<=maximumSumPoints || 1+currentSum<=maximumSumPoints ) ){
                    return Card;
                }
                else if(Card.getCardValue()+currentSum<=maximumSumPoints && Card.getCardValue()!=-1){
                    return Card;
                }
            }
        }
     return null;}
    public boolean isAbleToPlay(int currentSum,int maximumSum){
        for (Card Card: handCard){
            if(Card.getCardValue()==-1 && ( 10+currentSum<=maximumSum || 1+currentSum<=maximumSum ) ){
                return true;
            }
            else if (Card.getCardValue()+currentSum<=maximumSum && Card.getCardValue()!=-1){
                return true;
            }
        }
        return false; }
}


