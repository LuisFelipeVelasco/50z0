package com.examplez.demo.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    int numberOfPlayers;
    int numberOfCardsInHand = 4;
    public int currentSumGame = 0;
    int maximumSumGame = 50;
    List<Player> players;
    Desk deskGame;
    DiscardPile discardPileGame;
    Card currenCardPlayed;


    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void startGame() {
        players=new ArrayList<>();
        List<Card> setCards = setCards();
        Card initialCard = drawRandomCard(setCards);
        discardPileGame = new DiscardPile(new ArrayList<>(List.of(initialCard)));
        currentSumGame = initialCard.getCardValue();
        setPlayers(setCards);
        deskGame = new Desk(setCards);
    }

    List<Card> setCards() {
        List<Card> setCards = new ArrayList<>();
        Path directory = Path.of("src/main/resources/com/examplez/demo/images/Cards");
        try (Stream<Path> files = Files.list(directory)) {
            files.forEach(path -> {
                String fileName = String.valueOf(path.getFileName());
                String id = fileName.substring(0, 2);
                //Task:Handle Exception
                int valueCard = Integer.parseInt(fileName.substring(3,fileName.length()-4));
                Card card = new Card(valueCard, id);
                if(!id.equals("00"))  setCards.add(card);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return setCards;
    }

    void setPlayers(List<Card> setCards) {
        for (int i = 0; i < numberOfPlayers; i++) {
            List<Card> handPlayer = new ArrayList<>();
            for (int j = 0; j < numberOfCardsInHand; j++) {
                handPlayer.add(drawRandomCard(setCards));
            }
            if (i == 0) {
                PlayerHuman player = new PlayerHuman(handPlayer, i);
                players.add(player);
            } else {
                PlayerMachine player = new PlayerMachine(handPlayer, i);
                players.add(player);
            }
        }
    }



   public boolean isPlayerHumanCardValid(Card cardPlayed) {
        if (maximumSumGame < cardPlayed.getCardValue() + currentSumGame) return false;
        return true;
    }


    public boolean isMachinePlayerAbleToPlay(int turnPlayer) {
            PlayerMachine currentPlayer = getMachinePlayerByTurn(turnPlayer);
            return currentPlayer.isAbleToPlay(currentSumGame, maximumSumGame);
    }
    public void processCardPlayedByMachinePlayer(int turnMachinePlayer){
      /*  PlayerMachine playerMachine= getMachinePlayerByTurn(turnMachinePlayer);
        Card cardPlayed=playerMachine.cardPlayed(currentSumGame, maximumSumGame);
        currentSumGame+=cardPlayed.getCardValue();
        addCardPlayedToDiscardPile(cardPlayed);
        currenCardPlayed=cardPlayed;
        addDeskCardToPlayerHand(turnMachinePlayer);
        playerMachine.deleteCard(cardPlayed);*/
        PlayerMachine playerMachine = getMachinePlayerByTurn(turnMachinePlayer);

        Card cardPlayed = playerMachine.cardPlayed(currentSumGame, maximumSumGame);

        int value = cardPlayed.getCardValue();

        if ("A".equals(cardPlayed.getIdCard())) {
            value = getAceValueForMachine();
        }

        currentSumGame += value;

        addCardPlayedToDiscardPile(cardPlayed);
        currenCardPlayed = cardPlayed;
        addDeskCardToPlayerHand(turnMachinePlayer);
        playerMachine.deleteCard(cardPlayed);
    }

    public void processCardPlayedByHumanPlayer(String id,int aceValue){
        Player playerHuman=getHumanPlayer();
        Card cardPlayed= getCardById(id);
        int cardValued= cardPlayed.getCardValue();
        currentSumGame+=cardPlayed.getCardValue();
        addCardPlayedToDiscardPile(cardPlayed);
        currenCardPlayed=cardPlayed;
        addDeskCardToPlayerHand(0);
        playerHuman.deleteCard(cardPlayed);
    }

    void addCardPlayedToDiscardPile(Card cardPlayed) {
        discardPileGame.addNewCard(cardPlayed);
    }
    void addDeskCardToPlayerHand(int turnPlayer){
        for(int i=0;i<players.size();i++){
            Player currentPlayer=players.get(i);
            if(currentPlayer.getTurn()==turnPlayer){
                currentPlayer.addCardToHand(deskGame.getLastCard());
            }
        }
        if(deskGame.isEmpty()) restartDesk();
    }
    PlayerMachine getMachinePlayerByTurn(int turnPlayer){
        for (Player p:players ) {
            if (p.getTurn() == turnPlayer && p instanceof PlayerMachine machine) {
                return machine;
            }
        }
        return null;
    }
    public PlayerHuman getHumanPlayer(){
        for (Player p:players ) {
            if (p.getTurn() == 0 && p instanceof PlayerHuman playerHuman) {
                return playerHuman;
            }
        }
        return null;
    }
    public Card getLastCardPlayed() {
        return discardPileGame.getLastCard();
    }
    Card getCardById(String id){
        for(Card c:getHandHumanPlayer()){
            if(c.getIdCard().equals(id)){
                return c;
            }
        }
        return null;
    }

   public List<Card> getHandHumanPlayer() {
        for (Player player : players) {
            if (player.getTurn() == 0) {
                return player.getHandCard();
            }
        }
        return List.of();
    }

    public List<Integer>getTurnPlayers(){
        List<Integer> turnPlayers= new ArrayList<>();
        for(Player p:players){
            turnPlayers.add(p.getTurn());
        }
        return turnPlayers;
    }


    Card drawRandomCard(List<Card> setCards) {
        int randomIndex = ThreadLocalRandom.current().nextInt(1, setCards.size());
        Card randomCard = setCards.get(randomIndex);
        setCards.remove(randomIndex);
        return randomCard;
    }

   public void restartDesk() {
        deskGame.addCardsToDesk(discardPileGame.getCardsExceptLastOne());
    }

    public void eliminatePlayer(int turnPlayer) {
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            if (currentPlayer.getTurn() == turnPlayer) {
                deskGame.addCardsToDesk(currentPlayer.getHandCard());
                players.remove(i);
            }
        }
    }

    public synchronized void waitUntilRoundEnds() throws InterruptedException {
        while (getHumanPlayer().geTurnState()) {
            wait();
        }
    }

    public synchronized void endRound() {
        getHumanPlayer().setTurnState(false);
        notifyAll();
    }
    public int getAceValueForMachine() {
        if (currentSumGame + 10 <= maximumSumGame) {
            return 10;
        }
        return 1;
    }

}