package com.examplez.demo.Model;
import com.examplez.demo.Exceptions.InvalidCardException;
import com.examplez.demo.Exceptions.PlayerNotFoundException;

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


    /**
     * Creates a new game instance.
     *
     * @param numberOfPlayers number of players participating
     *        in the game
     *
     * @throws IllegalArgumentException if fewer than two players
     *         are specified
     */
    public Game(int numberOfPlayers) {

        if (numberOfPlayers < 2) {
            throw new IllegalArgumentException(
                    "The game must have at least two players"
            );
        }

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


    /**
     * Processes the card automatically selected by the machine player.
     *
     * @param turnMachinePlayer turn identifier of the machine player
     *
     * @throws InvalidCardException if the selected card is not
     *         valid according to the current game rules via the
     *         validateCard method
     */
    public void processCardPlayedByMachinePlayer(int turnMachinePlayer) throws InvalidCardException {
        PlayerMachine playerMachine= getMachinePlayerByTurn(turnMachinePlayer);
        Card cardPlayed=playerMachine.cardPlayed(currentSumGame, maximumSumGame);

        validateCard(cardPlayed);

        currentSumGame+=cardPlayed.getCardValue();
        addCardPlayedToDiscardPile(cardPlayed);
        currenCardPlayed=cardPlayed;
        addDeskCardToPlayerHand(turnMachinePlayer);
        playerMachine.deleteCard(cardPlayed);
    }


    /**
     * Processes the card selected by the human player and updates
     * the game state.
     *
     * @param id identifier of the selected card
     *
     * @throws InvalidCardException if the selected card cannot
     *         legally be played via the validateCard method
     */
    public void processCardPlayedByHumanPlayer(String id) throws InvalidCardException{
        Player playerHuman=getHumanPlayer();
        Card cardPlayed= getCardById(id);

        validateCard(cardPlayed);

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


    /**
     * Returns the machine player assigned to the specified turn.
     *
     * @param turnPlayer machine player's turn identifier
     *
     * @return the corresponding machine player
     *
     * @throws PlayerNotFoundException if no machine player
     *         matches the specified turn
     */
    PlayerMachine getMachinePlayerByTurn(int turnPlayer){
        for (Player p:players ) {
            if (p.getTurn() == turnPlayer && p instanceof PlayerMachine machine) {
                return machine;
            }
        }
        throw new PlayerNotFoundException("Machine player with turn " + turnPlayer + " was not found.");
    }

    /**
     * Returns the human player participating in the game.
     *
     * @return the human player
     *
     * @throws PlayerNotFoundException if the human player
     *         does not exist in the current game
     */
    public PlayerHuman getHumanPlayer(){
        for (Player p:players ) {
            if (p.getTurn() == 0 && p instanceof PlayerHuman playerHuman) {
                return playerHuman;
            }
        }
        throw new PlayerNotFoundException("Human player was not found");
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


    /**
     * Validates whether a card can be played without exceeding
     * the maximum game score.
     *
     * @param cardPlayed card selected by the player
     *
     * @throws InvalidCardException if playing the card would
     *         cause the accumulated score to exceed the maximum
     *         allowed value
     */
    public void validateCard(Card cardPlayed) throws InvalidCardException {

        if (maximumSumGame < cardPlayed.getCardValue() + currentSumGame) {

            throw new InvalidCardException(
                    "It is not possible to play this card because the sum would exceed " + maximumSumGame
            );
        }

    }

}