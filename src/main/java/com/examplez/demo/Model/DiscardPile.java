package com.examplez.demo.Model;
import java.util.List;
public class DiscardPile {
    private List<Card>DiscardPile;
    DiscardPile(List<Card>DiscardPile){
        this.DiscardPile=DiscardPile;
    }
    void addNewCard(Card newCard){
        DiscardPile.add(newCard);
    }
    Card getLastCard(){return DiscardPile.get(DiscardPile.size()-1);}
    List<Card> getCardsExceptLastOne(){
        return DiscardPile.subList(0,DiscardPile.size()-2);
    }

}
