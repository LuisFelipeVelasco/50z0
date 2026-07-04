package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



public class Player {
    //change id for turn  ,change DeckCard by HandCard intilizate it the Turn and the HandCard in the constructor use camelCase
    protected final List<Card> HandCard= new ArrayList<>();
    int id;
    public  Player(List<Card>DeckCard,int Turn){}
    //Use camelCase
    public List<Card> getHandCard(){
        return Collections.unmodifiableList(HandCard);

    }
    public void addCardToHand(Card card){
        HandCard.add(card);

    }
    //change name of getId for getTurn
    public int getId(){
        return id;
    }
}
