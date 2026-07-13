package com.examplez.demo.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class DiscardPileTest {

    private DiscardPile discardPile;

    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {

        card1 = new Card(5, "01");
        card2 = new Card(10, "02");

        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        discardPile = new DiscardPile(cards);
    }


    @Test
    @DisplayName("You must return the last card from the discard pile")
    void shouldReturnLastCard() {

        assertSame(card2, discardPile.getLastCard());
    }


    @Test
    @DisplayName("The discard must be initialized correctly")
    void shouldInitializeDiscardPile() {

        assertEquals(card2, discardPile.getLastCard());

    }


    @Test
    @DisplayName("Adding a card must make it the last card in the discard pile")
    void shouldAddNewCard() {

        card3 = new Card(-10, "03");
        discardPile.addNewCard(card3);

        assertEquals(card3, discardPile.getLastCard());
    }


    @Test
    @DisplayName("You must return all the cards except the last one")
    void shouldReturnCardsExceptLastOne() {

        List<Card> cards = discardPile.getCardsExceptLastOne();

        assertEquals(1, cards.size());

        assertTrue(cards.contains(card1));

        assertFalse(cards.contains(card2));
    }

}
