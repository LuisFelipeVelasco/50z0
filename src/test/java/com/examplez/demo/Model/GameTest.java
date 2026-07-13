package com.examplez.demo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(4);
        game.startGame();
    }


    @Test
    @DisplayName("The game must create the correct number of players.")
    void shouldCreateCorrectNumberOfPlayers() {

        assertEquals(4, game.players.size());
    }


    @Test
    @DisplayName("There must be a human player, and their turn must be 0.")
    void shouldReturnHumanPlayer() {

        PlayerHuman player = game.getHumanPlayer();

        assertNotNull(player);
        assertEquals(0, player.getTurn());
    }


    @Test
    @DisplayName("The human player must start with four cards.")
    void shouldStartHumanPlayerWithFourCards() {

        assertEquals(4, game.getHandHumanPlayer().size());
    }


    @Test
    @DisplayName("A valid card must not exceed the maximum amount.")
    void shouldAcceptValidCard() {

        game.currentSumGame = 40;

        Card card = new Card(5,"01");

        assertTrue(game.isPlayerHumanCardValid(card));
    }

    @Test
    @DisplayName("An invalid card must exceed the maximum amount.")
    void shouldRejectInvalidCard() {

        game.currentSumGame = 49;

        Card card = new Card(5,"01");

        assertFalse(game.isPlayerHumanCardValid(card));
    }


    @Test
    @DisplayName("It must return the number of turns in a round based on the number of players.")
    void shouldReturnPlayersTurns() {

        assertEquals(4, game.getTurnPlayers().size());

        assertTrue(game.getTurnPlayers().contains(0));
    }


    @Test
    @DisplayName("Removing a player must decrease the number of players.")
    void shouldEliminatePlayer() {

        game.eliminatePlayer(2);

        assertEquals(3, game.players.size());
    }


    @Test
    @DisplayName("The game must start with one card in the discard pile.")
    void shouldHaveInitialDiscardCard() {

        assertNotNull(game.getLastCardPlayed());
    }

}