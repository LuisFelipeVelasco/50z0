package com.examplez.demo.Model;
import java.util.List;

public class Desk {
    private List<Card> Desk;
    Desk(List<Card>Desk){
        this.Desk=Desk;
    }
    void addCardsToDesk(List<Card> newCards){
        Desk.addAll(newCards);
    }
    Card getLastCard(){
        return Desk.get(Desk.size()-1);
    }
}
