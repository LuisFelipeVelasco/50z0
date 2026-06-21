package com.examplez.demo.Model;
import java.util.List;
public class DiscardPile {
    List<Card>DiscardPile;
    DiscardPile(List<Card>DiscardPile){
        this.DiscardPile=DiscardPile;
    }
    void addNewCard(Card newCard){
        DiscardPile.add(newCard);
    }
    List<Card> getCardsExceptLastOne(){
        return DiscardPile.subList(0,DiscardPile.size()-2);
    }
}
