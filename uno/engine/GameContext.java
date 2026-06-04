package uno.engine;

import java.util.ArrayList;
import java.util.List;
import uno.api.GameObserver;
import uno.model.*;

public class GameContext {

    // 1. ATRIBUTOS PROTECTED PARA A FASE 2 PODER LER E MEXER
    protected List<Player> players;
    protected Deck drawPile;
    protected Deck discardPile;
    protected Color currentColor;
    protected int currentPlayerIndex;
    protected boolean isClockwise;
    protected boolean isWaitingForColor;
    protected int skips = 0;    
    
    protected List<GameObserver> observers; 
    /**
     * Creates a new GameContext with the specified number of players and the loaded deck.
     * @param numPlayers the number of players
     * @param loadedDeck the loaded deck
     */
    public GameContext(int numPlayers, Deck loadedDeck) {
        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            this.players.add(new Player(i));
        }
        
        this.drawPile = loadedDeck;
        this.discardPile = new Deck();
        this.currentPlayerIndex = 0;
        this.isClockwise = true;
        this.isWaitingForColor = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameObserver obs) {
        this.observers.add(obs);
    }

    // 2. MÉTODO PROTECTED PARA A FASE 2 CONSEGUIR IMPRIMIR NO ECRÃ
    protected void broadcast(String message) {
        for (GameObserver obs : observers) {
            obs.logEvent(message);
        }
    }

    public void setupGame(int cardsPerPlayer) {
        Card initialDiscard = drawPile.drawTop();
        if (initialDiscard.getColor() == Color.WILD) {
            throw new IllegalStateException("Initial discard cannot be WILD.");
        }
        
        discardPile.addTop(initialDiscard);
        this.currentColor = initialDiscard.getColor();

        for (int round = 0; round < cardsPerPlayer; round++) {
            for (Player p : players) {
                Card drawn = drawPile.drawTop();
                p.getHand().addCard(drawn);
            }
        }

        broadcast("GAME_START players=" + players.size()); 
        broadcast("EVENT TOP_CARD " + initialDiscard.toString() + " color=" + currentColor);
        
        for (Player p : players) {
            StringBuilder handStr = new StringBuilder("EVENT HAND player=" + p.getId() + " cards=");
            for (int i = 0; i < p.getHand().getSize(); i++) {
                Card c = p.getHand().getCard(i);
                handStr.append(c.toString()).append(" "); 
            }
            broadcast(handStr.toString().trim());
        }
        broadcast("EVENT TURN_START player=" + currentPlayerIndex);
    }

    public void logCommand(String message) {
        broadcast(message);
    }

    public void playCard(int playerId, int cardIndex) {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Not player " + playerId + " turn");
        }
        if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().getSize()) {
            throw new IllegalArgumentException("Invalid card index " + cardIndex);
        }
        if (isWaitingForColor) {
            throw new IllegalArgumentException("Must choose a color before playing another card");
        }

        Card currentCard = currentPlayer.getHand().getCard(cardIndex);

        if (currentCard.getColor() != this.currentColor && currentCard.getRank() != discardPile.peekTop().getRank() && currentCard.getColor() != Color.WILD) { 
            throw new IllegalArgumentException("Card " + currentCard.toString() + " is not playable");
        }

        currentPlayer.getHand().removeCard(cardIndex); 
        discardPile.addTop(currentCard);               

        String baseLog = "EVENT PLAY_CARD Player " + playerId + " played ";
        int targetPlayerIndex = (currentPlayerIndex + (isClockwise ? 1 : -1) + players.size()) % players.size();

        switch (currentCard.getRank()) {
            case WILD:
                broadcast(baseLog + "WILD (color will be chosen)");
                this.isWaitingForColor = true;
                break;
            case WILD_DRAW_FOUR:
                broadcast(baseLog + "WILD_DRAW_FOUR; Player " + targetPlayerIndex + " draws 4 and is skipped");
                this.isWaitingForColor = true;
                break;
            case DRAW_TWO:
                this.currentColor = currentCard.getColor();
                broadcast(baseLog + "DRAW_TWO; Player " + targetPlayerIndex + " draws 2 and is skipped");
                break;
            case SKIP:
                this.currentColor = currentCard.getColor();
                broadcast(baseLog + "SKIP");
                break;
            case REVERSE:
                this.currentColor = currentCard.getColor();
                broadcast(baseLog + "REVERSE");
                break;
            default:
                this.currentColor = currentCard.getColor();
                broadcast(baseLog + currentCard.toString());
                break;
        }

        currentCard.getEffect().execute(this);

        if (currentPlayer.getHand().getSize() == 0) {
            broadcast("EVENT GAME_END Player " + playerId + " wins");
            broadcast("EVENT WINNER player=" + playerId);
            broadcast("GAME_END");
            System.exit(0);
        }
    }

    public void chooseColor(int playerId, String colorCode) {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Only player " + currentPlayer.getId() + " can choose the color");
        }
        if (!isWaitingForColor) {
            throw new IllegalArgumentException("Cannot choose color - no wild card was played");
        }

        Color newColor;
        switch (colorCode.toUpperCase()) {
            case "R": newColor = Color.RED; break;
            case "Y": newColor = Color.YELLOW; break;
            case "G": newColor = Color.GREEN; break;
            case "B": newColor = Color.BLUE; break;
            case "W": throw new IllegalArgumentException("Cannot choose WILD as a color");
            default: throw new IllegalArgumentException("Invalid color code");
        }

        this.currentColor = newColor;
        this.isWaitingForColor = false;

        broadcast("EVENT CHOOSE_COLOR Player " + playerId + " chose color " + this.currentColor);
    }

    public void drawCard(int playerId) {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Not player " + playerId + " turn");
        }
        if (isWaitingForColor) {
            throw new IllegalArgumentException("Must choose a color before drawing");
        }

        Card drawnCard = drawPile.drawTop();
        
        if (drawnCard == null) {
            broadcast("EVENT GAME_END No cards available to draw");
            broadcast("GAME END");
            System.exit(0); 
        }

        currentPlayer.getHand().addCard(drawnCard);
        broadcast("EVENT DRAW_CARD Player " + playerId + " draws 1 card (" + drawnCard.toString() + ")");
    }

    public void advanceTurn() {
        if (isWaitingForColor) {
            return;
        }

        int step = 1 + this.skips;
        int direction = isClockwise ? 1 : -1;
        int numPlayers = players.size();

        currentPlayerIndex = (currentPlayerIndex + (direction * step)) % numPlayers;
        
        if (currentPlayerIndex < 0) {
            currentPlayerIndex += numPlayers;
        }

        this.skips = 0; 
        broadcast("EVENT TURN_ADVANCE Next player: " + currentPlayerIndex);
    }

    public void reverseDirection() {
        this.isClockwise = !this.isClockwise;
        if (players.size() == 2) {
            skipNextPlayer(); 
        }
    }

    public void skipNextPlayer() {
        this.skips++; 
    }

    public void forceDraw(int numCards) {
        int targetPlayerIndex;
        if (isClockwise) {
            targetPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            targetPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }

        Player targetPlayer = players.get(targetPlayerIndex);

        for (int i = 0; i < numCards; i++) {
            Card drawnCard = drawPile.drawTop();
            if (drawnCard == null) {
                broadcast("EVENT GAME_END No cards available to draw");
                broadcast("GAME_END");
                System.exit(0);
            }
            targetPlayer.getHand().addCard(drawnCard);
        }
    }

    public void waitForColor() {
        this.isWaitingForColor = true;
    }
}