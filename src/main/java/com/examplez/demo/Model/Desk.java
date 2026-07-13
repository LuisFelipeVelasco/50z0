package com.examplez.demo.Model;
import java.util.List;

public class Desk {
    List<Card> Desk;
    Desk(List<Card>Desk){
        this.Desk=Desk;
    }
    void addCardsToDesk(List<Card> newCards){
        Desk.addAll(newCards);
    }
    Card getLastCard(){
        Card lastCard= Desk.get(Desk.size()-1);
        Desk.remove(lastCard);
        return lastCard;
    }
    boolean isEmpty(){return Desk.isEmpty();}
}
