package com.examplez.demo.Model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Central model for the turn-based card game.
 *
 * <p>The class creates the card set, deals hands, tracks the accumulated score,
 * validates human and machine moves, manages the draw and discard piles, and
 * removes players who can no longer make a valid play.</p>
 *
 * <p>The human turn is coordinated with the controller through the synchronized
 * {@link #waitUntilRoundEnds()} and {@link #endRound()} methods.</p>
 *
 * @see Card
 * @see Player
 * @see Desk
 * @see DiscardPile
 * @version 1.0
 */
public class Game {
    // -----------------------------------------------------------------------
    // Game configuration and mutable state
    // -----------------------------------------------------------------------

    /** Total number of players participating in the match. */
    int numberOfPlayers;
    /** Number of cards dealt to each player at the beginning of the game. */
    int numberOfCardsInHand = 4;
    /** Sum produced by all cards played in the current match. */
    int currentSumGame = 0;
    /** Maximum sum that a valid play is allowed to reach. */
    int maximumSumGame = 50;
    /** Active human and machine players in turn order. */
    List<Player> players;
    /** Draw deck used to replace played cards. */
    Desk deskGame;
    /** Pile that stores the initial card and all subsequently played cards. */
    DiscardPile discardPileGame;


    /**
     * Creates a game configured for the requested number of participants.
     *
     * @param numberOfPlayers total number of players in the match
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    // -----------------------------------------------------------------------
    // Game initialization
    // -----------------------------------------------------------------------

    /**
     * Creates the complete card set, chooses the initial discard, deals the
     * player hands, and initializes the draw deck.
     */
    public void startGame() {
        players=new ArrayList<>();
        List<Card> setCards = setCards();
        Card initialCard = drawRandomCard(setCards);
        discardPileGame = new DiscardPile(new ArrayList<>(List.of(initialCard)));
        if(initialCard.getCardValue()==-1)currentSumGame=1;
        else currentSumGame = initialCard.getCardValue();
        setPlayers(setCards);
        deskGame = new Desk(setCards);
    }
    /**
     * Builds the available card set by reading card filenames from the resource
     * directory and extracting each card identifier and value.
     *
     * @return mutable list containing every playable card resource
     */
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
    /**
     * Creates the human player at turn zero and the remaining machine players,
     * dealing the configured number of cards to each participant.
     *
     * @param setCards shared card set from which the hands are dealt
     */
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
   // -----------------------------------------------------------------------
   // Move validation
   // -----------------------------------------------------------------------

   /**
    * Determines whether a card selected by the human player can be added
    * without exceeding the maximum score.
    *
    * @param cardPlayed card selected by the human player
    * @return {@code true} when the card can be played under the current score
    */
   public boolean isPlayerHumanCardValid(Card cardPlayed) {
       if(cardPlayed.getCardValue()==-1 && ( 10+currentSumGame<=maximumSumGame || 1+currentSumGame<=maximumSumGame )) return true;
       return (maximumSumGame >= cardPlayed.getCardValue() + currentSumGame && !(cardPlayed.getCardValue()==-1));
    }
    /**
     * Calculates the ace values that remain valid for the current score.
     *
     * @return list containing {@code 1}, {@code 10}, or both valid values
     */
    public List<Integer> getPossibleAceValues(){
        List<Integer> possibleAceValues= new ArrayList<>();
        if(10+currentSumGame<=maximumSumGame) {
            possibleAceValues.add(10);
            possibleAceValues.add(1);
            return possibleAceValues;
        }
        possibleAceValues.add(1);
        return possibleAceValues;
    }


    /**
     * Checks whether a specified machine player has at least one valid card.
     *
     * @param turnPlayer turn identifier of the machine player
     * @return {@code true} when the machine can make a valid play
     */
    public boolean isMachinePlayerAbleToPlay(int turnPlayer) {
            PlayerMachine currentPlayer = getMachinePlayerByTurn(turnPlayer);
            return currentPlayer.isAbleToPlay(currentSumGame, maximumSumGame);
    }

    /**
     * Checks whether the human player has at least one valid card.
     *
     * @return {@code true} when the human can make a valid play
     */
    public boolean isHumanPlayerAbleToPlay() {
        PlayerHuman currentPlayer = getHumanPlayer();
        return currentPlayer.isAbleToPlay(currentSumGame, maximumSumGame);
    }

    // -----------------------------------------------------------------------
    // Card processing
    // -----------------------------------------------------------------------

    /**
     * Selects and processes a valid card for a machine player.
     *
     * @param turnMachinePlayer turn identifier of the machine player
     */
    public void processCardPlayedByMachinePlayer(int turnMachinePlayer){
        PlayerMachine playerMachine = getMachinePlayerByTurn(turnMachinePlayer);
        Card cardPlayed = playerMachine.cardPlayed(currentSumGame, maximumSumGame);
        int value = cardPlayed.getCardValue();
        if (cardPlayed.getCardValue()==-1) value = getAceValueForMachine();
        currentSumGame += value;
        addCardPlayedToDiscardPile(cardPlayed);
        playerMachine.deleteCard(cardPlayed);
        if(deskGame.getDesk().size()==1)restartDesk();
    }

    /**
     * Processes a card selected by the human player, removes it from the hand,
     * and draws a replacement card.
     *
     * @param id identifier of the selected card
     * @param aceValue effective value chosen for the card when it is an ace
     */
    public void processCardPlayedByHumanPlayer(String id,int aceValue){
        Player playerHuman=getHumanPlayer();
        Card cardPlayed= getCardById(id);
        currentSumGame+=aceValue;
        addCardPlayedToDiscardPile(cardPlayed);
        playerHuman.deleteCard(cardPlayed);
        addDeskCardToPlayerHand(0);
        if(deskGame.getDesk().isEmpty())restartDesk();
    }

    /**
     * Adds a played card to the discard pile.
     *
     * @param cardPlayed card to append to the pile
     */
    void addCardPlayedToDiscardPile(Card cardPlayed) {
        discardPileGame.addNewCard(cardPlayed);
    }
    /**
     * Draws one card from the deck and adds it to the player identified by the
     * supplied turn value. The deck is restarted when it becomes empty.
     *
     * @param turnPlayer turn identifier of the player receiving the card
     */
    public void addDeskCardToPlayerHand(int turnPlayer){
        for(int i=0;i<players.size();i++){
            Player currentPlayer=players.get(i);
            if(currentPlayer.getTurn()==turnPlayer){
                currentPlayer.addCardToHand(deskGame.getLastCard());
            }
        }
    }
    // -----------------------------------------------------------------------
    // Player and card lookup
    // -----------------------------------------------------------------------

    /**
     * Finds a machine player by turn identifier.
     *
     * @param turnPlayer requested turn identifier
     * @return matching machine player, or {@code null} when none is found
     */
    PlayerMachine getMachinePlayerByTurn(int turnPlayer){
        for (Player p:players ) {
            if (p.getTurn() == turnPlayer && p instanceof PlayerMachine machine) {
                return machine;
            }
        }
        return null;
    }
    /**
     * Returns the human player assigned to turn zero.
     *
     * @return human player, or {@code null} when it is no longer active
     */
    public PlayerHuman getHumanPlayer(){
        for (Player p:players ) {
            if (p.getTurn() == 0 && p instanceof PlayerHuman playerHuman) {
                return playerHuman;
            }
        }
        return null;
    }
    /**
     * Returns the most recently played card.
     *
     * @return active card at the top of the discard pile
     */
    public Card getLastCardPlayed() {
        return discardPileGame.getLastCard();
    }
    /**
     * Finds a card in the human player's hand by identifier.
     *
     * @param id card identifier to locate
     * @return matching card, or {@code null} when the card is not found
     */
    Card getCardById(String id){
        for(Card c:getHandHumanPlayer()){
            if(c.getIdCard().equals(id)){
                return c;
            }
        }
        return null;
    }

   /**
    * Returns the cards held by the human player.
    *
    * @return human hand, or an empty immutable list if no human player remains
    */
   public List<Card> getHandHumanPlayer() {
        for (Player player : players) {
            if (player.getTurn() == 0) {
                return player.getHandCard();
            }
        }
        return List.of();
    }

    /**
     * Builds the current sequence of active turn identifiers.
     *
     * @return list of turns for all players still in the match
     */
    public List<Integer>getTurnPlayers(){
        List<Integer> turnPlayers= new ArrayList<>();
        for(Player p:players){
            turnPlayers.add(p.getTurn());
        }
        return turnPlayers;
    }


    // -----------------------------------------------------------------------
    // Deck and player maintenance
    // -----------------------------------------------------------------------

    /**
     * Removes and returns a randomly selected card from the supplied set.
     *
     * @param setCards mutable card set from which to draw
     * @return randomly selected card
     */
    Card drawRandomCard(List<Card> setCards) {
        int randomIndex = ThreadLocalRandom.current().nextInt(1, setCards.size());
        Card randomCard = setCards.get(randomIndex);
        setCards.remove(randomIndex);
        return randomCard;
    }

   /** Recycles eligible discarded cards back into the draw deck. */
   public void restartDesk() {
        deskGame.addCardsToDesk(discardPileGame.getCardsExceptLastOne());
    }

    /**
     * Removes a player from the active match and returns that player's hand to
     * the draw deck.
     *
     * @param turnPlayer turn identifier of the player to eliminate
     */
    public void eliminatePlayer(int turnPlayer) {
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            if (currentPlayer.getTurn() == turnPlayer) {
                deskGame.addCardsToDesk(currentPlayer.getHandCard());
                players.remove(i);
            }
        }
    }

    // -----------------------------------------------------------------------
    // Human-turn synchronization
    // -----------------------------------------------------------------------

    /**
     * Suspends the calling thread while the human player's turn remains active.
     *
     * @throws InterruptedException if the waiting thread is interrupted
     */
    public synchronized void waitUntilRoundEnds() throws InterruptedException {
        while (getHumanPlayer().getTurnState()) {
            wait();
        }
    }
    /** Marks the human turn as complete and wakes threads waiting for it. */
    public synchronized void endRound() {
        getHumanPlayer().setTurnState(false);
        notifyAll();
    }
    // -----------------------------------------------------------------------
    // Score access
    // -----------------------------------------------------------------------

    /**
     * Chooses the highest valid value for an ace played by a machine.
     *
     * @return {@code 10} when allowed; otherwise {@code 1}
     */
    public int getAceValueForMachine() {
        if (currentSumGame + 10 <= maximumSumGame) {
            return 10;
        }
        return 1;
    }
    /**
     * Returns the score accumulated by all played cards.
     *
     * @return current game sum
     */
    public int getCurrentSumGame(){
        return currentSumGame;
    }


}