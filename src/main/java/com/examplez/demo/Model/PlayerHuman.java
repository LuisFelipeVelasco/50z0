package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
public class PlayerHuman extends Player{
    public PlayerHuman(List<Card> deckCards, int turn) {
        super(deckCards, turn);
    }
    boolean turnState;
    public boolean geTurnState(){
    return turnState;
}
    public void setTurnState(boolean turnState){
        this.turnState=turnState;
    }
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
