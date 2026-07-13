package com.examplez.demo.Model;
import java.util.List;
public class DiscardPile {
   List<Card>DiscardPile;
   public DiscardPile(List<Card>DiscardPile){
        this.DiscardPile=DiscardPile;
    }
    void addNewCard(Card newCard){
        DiscardPile.add(newCard);
    }
   public Card getLastCard(){return DiscardPile.get(DiscardPile.size()-1);}
    List<Card> getCardsExceptLastOne(){
        return DiscardPile.subList(0,DiscardPile.size()-2);
    }

}
