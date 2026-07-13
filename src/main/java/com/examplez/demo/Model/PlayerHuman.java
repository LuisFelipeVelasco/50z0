package com.examplez.demo.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
/**
 * Human-controlled specialization of {@link Player}.
 *
 * <p>In addition to the inherited hand, this class stores whether the
 * interface is currently accepting a human move and can determine whether at
 * least one card remains playable.</p>
 *
 * @version 1.0
 */
public class PlayerHuman extends Player{
    /**
     * Creates the human player with an initial hand and turn identifier.
     *
     * @param deckCards cards initially dealt to the human
     * @param turn turn identifier assigned to the player
     */
    public PlayerHuman(List<Card> deckCards, int turn) {
        super(deckCards, turn);
    }
    // -----------------------------------------------------------------------
    // Human turn state
    // -----------------------------------------------------------------------

    /** Indicates whether the controller is waiting for a human card selection. */
    boolean turnState;
    /**
     * Returns the current human-turn state.
     *
     * @return {@code true} while the human is expected to play
     */
    public boolean getTurnState(){
    return turnState;
}
    /**
     * Activates or completes the human player's turn.
     *
     * @param turnState new turn state
     */
    public void setTurnState(boolean turnState){
        this.turnState=turnState;
    }
    /**
     * Checks whether any card in the human hand can be played without exceeding
     * the maximum sum.
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
