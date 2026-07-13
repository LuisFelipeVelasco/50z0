package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



public class Player {
    protected final List<Card> handCard=new ArrayList<>();
    int turn;
    public  Player(List<Card>handCard,int turn){
        this.handCard.addAll(handCard);
        this.turn = turn;
    }
    public List<Card> getHandCard(){
        return handCard;

    }
    public void addCardToHand(Card card){
        handCard.add(card);

    }

    public int getTurn(){
        return turn;
    }
    public void deleteCard(Card cardSelected){
        handCard.remove(cardSelected);
    }

}
