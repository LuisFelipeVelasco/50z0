package com.examplez.demo.Exceptions;

/**
 * Thrown when a player attempts to play a card that would
 * cause the total game score to exceed the maximum allowed value.
 *
 * <p>This is a checked exception because the caller is expected
 * to handle the invalid move and allow the player to choose
 * another card.</p>
 */
public class InvalidCardException extends Exception {

    public InvalidCardException() {
        super("The selected card exceeds the match's permitted limit.");
    }

    public InvalidCardException(String message) {
        super(message);
    }

}