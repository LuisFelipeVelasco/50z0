package com.examplez.demo.Model;
import java.util.List;
/**
 * Stores the ordered collection of cards that have already been played.
 *
 * <p>The last element represents the active card shown in the game interface.
 * Earlier cards may be returned to the draw deck when it becomes empty.</p>
 *
 * @version 1.0
 */
public class DiscardPile {
   // -----------------------------------------------------------------------
   // Pile state
   // -----------------------------------------------------------------------

   /** Mutable list whose final element is the most recently played card. */
   List<Card>DiscardPile;
   /**
    * Creates a discard pile with its initial card collection.
    *
    * @param DiscardPile cards initially stored in the pile
    */
   public DiscardPile(List<Card>DiscardPile){
        this.DiscardPile=DiscardPile;
    }
    /**
     * Appends a newly played card to the discard pile.
     *
     * @param newCard card that has just been played
     */
    void addNewCard(Card newCard){
        DiscardPile.add(newCard);
    }
   /**
    * Returns the active card without removing it.
    *
    * @return most recently played card
    */
   public Card getLastCard(){return DiscardPile.get(DiscardPile.size()-1);}
    /**
     * Returns the portion of the pile intended to be recycled into the draw
     * deck while preserving the newest cards in the pile.
     *
     * @return list view containing recyclable discarded cards
     */
    List<Card> getCardsExceptLastOne(){
        return DiscardPile.subList(0,DiscardPile.size()-1);
    }

}
