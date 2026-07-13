package com.examplez.demo.Model;

public class Card {
    int cardValue;
    String idCard;
   public Card(int cardValue,String idCard){
        this.cardValue=cardValue;
        this.idCard=idCard;
    }
  public  int getCardValue(){
        return cardValue;
    }
   public String getIdCard(){
        return idCard;
    }
}
