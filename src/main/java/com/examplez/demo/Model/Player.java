package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



public class Player {
    protected final List<Card> handCard=new ArrayList<>();
    int turn;

    /**
     * Creates a new player.
     *
     * @param handCard initial hand assigned to the player
     * @param turn player's turn identifier
     *
     * @throws IllegalArgumentException if the hand is null
     */
    public  Player(List<Card>handCard,int turn){

        if (handCard == null) {
            throw new IllegalArgumentException(
                    "The player's hand cannot be void."
            );
        }

        this.handCard.addAll(handCard);
        this.turn = turn;
    }
    public List<Card> getHandCard(){
        return Collections.unmodifiableList(handCard);

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
