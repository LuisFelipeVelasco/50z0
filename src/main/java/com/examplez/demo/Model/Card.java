package com.examplez.demo.Model;

public class Card {
    private final int cardValue;
    private final String idCard;

    /**
     * Creates a new card.
     *
     * @param cardValue the numerical value of the card
     * @param idCard unique identifier of the card image
     *
     * @throws IllegalArgumentException if the identifier is null
     *         or the card value is greater than 10
     */
   public Card(int cardValue,String idCard){

       if (idCard == null) {
           throw new IllegalArgumentException(
                   "The card must have an id"
           );
       }
       if (cardValue > 10) {
           throw new IllegalArgumentException(
                   "The maximum value for a card is 10."
           );
       }

        this.cardValue=cardValue;
        this.idCard=idCard;
    }
  public  int getCardValue(){
        return cardValue;
    }
   public String getIdCard(){
        return idCard;
    }
}
