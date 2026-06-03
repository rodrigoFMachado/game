package uno.engine;

import java.util.ArrayList;
import java.util.List;
import uno.model.*;
import uno.api.GameObserver;

public class GameContext {

    private List<Player> players;
    private Deck drawPile;
    private Deck discardPile;
    private int currentPlayerIndex;
    private boolean isClockwise;
    private Color currentColor;
    
    // Lista de monitores ligados a este CPU
    private List<GameObserver> observers;

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
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameObserver obs) {
        this.observers.add(obs);
    }

    private void broadcast(String message) {
        for (GameObserver obs : observers) {
            obs.logEvent(message);
        }
    }

    // --- Boot Sequence ---
    public void setupGame(int cardsPerPlayer) {
        Card initialDiscard = drawPile.drawTop();
        if (initialDiscard.getColor() == Color.WILD) {
            throw new IllegalStateException("Initial discard cannot be WILD.");
        }
        
        discardPile.addTop(initialDiscard);
        this.currentColor = initialDiscard.getColor();

        // Distribui cartas 1 a 1, em círculo
        for (int round = 0; round < cardsPerPlayer; round++) {
            for (Player p : players) {
                Card drawn = drawPile.drawTop();
                p.getHand().addCard(drawn);
            }
        }

        // Output idêntico à Secção 1.4.2 do PDF
        broadcast("GAME START players=" + players.size());
        broadcast("EVENT TOP_CARD " + initialDiscard.getColor() + "-" + initialDiscard.getRank() + " color=" + currentColor);
        
        for (Player p : players) {
            StringBuilder handStr = new StringBuilder("EVENT HAND player=" + p.getId() + " cards=");
            for (int i = 0; i < p.getHand().getSize(); i++) {
                Card c = p.getHand().getCard(i);
                handStr.append(c.getColor()).append("-").append(c.getRank()).append(" ");
            }
            broadcast(handStr.toString().trim());
        }
        broadcast("EVENT TURN_START player=" + currentPlayerIndex);
    }

    // --- APIs para os Efeitos das Cartas usarem ---
    public void reverseDirection() {
        this.isClockwise = !this.isClockwise;
    }

    public void skipNextPlayer() {
        // Por agora não faz nada, vamos escrever a matemática dos turnos depois
    }

    public void forceDraw(int numCards) {
        // Por agora não faz nada
    }

    // --- Os métodos originais do teu UML ---
    public void playCard(int playerId, int cardIndex) {
        // Vamos ligar isto ao ScriptParser na próxima fase
    }

    public void drawCard(int playerId) {
        // Vamos ligar isto ao ScriptParser na próxima fase
    }
}