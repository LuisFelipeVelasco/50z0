package com.examplez.demo.Model;
import java.util.List;

/**
 * Represents the draw deck from which players receive replacement cards.
 *
 * <p>Cards are removed from the end of the internal list. Cards recovered from
 * eliminated players or the discard pile can be added back to the deck.</p>
 *
 * @version 1.0
 */
public class Desk {
    // -----------------------------------------------------------------------
    // Deck state
    // -----------------------------------------------------------------------

    /** Mutable collection of cards currently available to draw. */
    List<Card> Desk;
    /**
     * Creates a draw deck backed by the supplied card list.
     *
     * @param Desk cards initially available in the deck
     */
    public Desk(List<Card>Desk){
        this.Desk=Desk;
    }
    /**
     * Adds a collection of cards back to the draw deck.
     *
     * @param newCards cards to append to the deck
     */
    public void addCardsToDesk(List<Card> newCards){
        Desk.addAll(newCards);
    }
    /**
     * Removes and returns the card stored at the end of the deck.
     *
     * @return card drawn from the deck
     */
    public Card getLastCard(){
        Card lastCard= Desk.get(Desk.size()-1);
        Desk.remove(lastCard);
        return lastCard;
    }
    /**
     * Return the cards of the desk
     *
     * @return List of cards of the desk
     */
    public List<Card> getDesk(){return Desk;}
}
