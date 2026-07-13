package com.examplez.demo.Model;
import java.util.List;
/**
 * Automatically controlled specialization of {@link Player}.
 *
 * <p>The machine scans its hand in order and selects the first card that can be
 * played without exceeding the maximum permitted score.</p>
 *
 * @version 1.0
 */
public class PlayerMachine extends Player{
    /**
     * Creates a machine player with an initial hand and turn identifier.
     *
     * @param deckCards cards initially dealt to the machine
     * @param turn turn identifier assigned to the player
     */
    public PlayerMachine(List<Card> deckCards, int turn) {
        super(deckCards, turn);
                                                         }
     /**
      * Selects the first valid card found in the machine player's hand.
      *
      * @param currentSum score accumulated before the play
      * @param maximumSumPoints highest permitted score
      * @return selected card, or {@code null} when no card can be played
      */
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
    /**
     * Checks whether the machine has at least one card that can be played.
     *
     * @param currentSum score accumulated before the next play
     * @param maximumSum highest permitted score
     * @return {@code true} when at least one valid card exists
     */
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


