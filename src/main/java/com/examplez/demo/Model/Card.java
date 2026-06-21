package com.examplez.demo.Model;

public class Card {
    private final int cardValue;
    private final String idCard;
    Card(int cardValue,String idCard){
        this.cardValue=cardValue;
        this.idCard=idCard;
    }
    int getCardValue(){
        return cardValue;
    }
    String getIdCard(){
        return idCard;
    }
}
