package com.examplez.demo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {

        card1 = new Card(5, "01");
        card2 = new Card(10, "02");

        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        player = new Player(cards, 0);

    }


    @Test
    @DisplayName("The player must start with the cards received")
    void shouldInitializePlayerWithCards() {

        assertEquals(2, player.getHandCard().size());
    }


    @Test
    @DisplayName("It must return the correct turn.")
    void shouldReturnCorrectTurn() {

        assertEquals(0, player.getTurn());
    }


    @Test
    @DisplayName("Adding a card should increase the hand size.")
    void shouldAddCardToHand() {

        Card newCard = new Card(-10, "03");
        player.addCardToHand(newCard);

        assertEquals(3, player.getHandCard().size());
        assertTrue(player.getHandCard().contains(newCard));
    }


    @Test
    @DisplayName("Discarding a card must reduce the hand size.")
    void shouldDeleteCard() {

        player.deleteCard(card1);

        assertEquals(1, player.getHandCard().size());
        assertFalse(player.getHandCard().contains(card1));
    }


    @Test
    @DisplayName("Removing a non-existent card must not modify the hand.")
    void shouldNotRemoveCardIfCardDoesNotExist() {

        Card otherCard = new Card(8, "05");

        player.deleteCard(otherCard);

        assertEquals(2, player.getHandCard().size());
    }

}
