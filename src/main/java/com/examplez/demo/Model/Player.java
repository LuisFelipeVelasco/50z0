package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;



/**
 * Base representation of a participant in the card game.
 *
 * <p>The class stores the player's hand and immutable-in-practice turn
 * identifier. Human and machine players extend this class with their specific
 * turn behavior.</p>
 *
 * @see PlayerHuman
 * @see PlayerMachine
 * @version 1.0
 */
public class Player {
    // -----------------------------------------------------------------------
    // Player state
    // -----------------------------------------------------------------------

    /** Mutable list of cards currently held by the player. */
    List<Card> handCard=new ArrayList<>();
    /** Turn identifier assigned when the player is created. */
    int turn;
    /**
     * Creates a player with an initial hand and turn identifier.
     *
     * @param handCard cards initially held by the player
     * @param turn position used by the turn-management logic
     */
    public  Player(List<Card>handCard,int turn){
        this.handCard.addAll(handCard);
        this.turn = turn;
    }
    /**
     * Returns the player's current hand.
     *
     * @return mutable list of cards held by the player
     */
    public List<Card> getHandCard(){
        return handCard;

    }
    /**
     * Adds one card to the player's hand.
     *
     * @param card card drawn from the deck
     */
    public void addCardToHand(Card card){
        handCard.add(card);

    }
    /**
     * Returns the player's turn identifier.
     *
     * @return turn value assigned to the player
     */
    public int getTurn(){
        return turn;
    }
    /**
     * Removes a played card from the player's hand.
     *
     * @param cardSelected card to remove
     */
    public void deleteCard(Card cardSelected){
        handCard.remove(cardSelected);
    }

}
