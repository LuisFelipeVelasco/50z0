package com.examplez.demo.Exceptions;

/**
 * Thrown when the requested player cannot be found in the current game.
 *
 * <p>This exception indicates an unexpected game state and therefore
 * extends of the RuntimeException.</p>
 */
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException() {
        super("The requested player does not exist.");
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }

}