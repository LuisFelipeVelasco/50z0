package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



public class Player {
    protected final List<Card> HandCard= new ArrayList<>();
    int id;
    public  Player(List<Card>DeckCard,int Turn){


    }
    public List<Card> getHandCard(){
        return Collections.unmodifiableList(HandCard);

    }
    public void addCardToHand(Card card){
        HandCard.add(card);

    }
    public int getId(){
        return id;
    }
}
