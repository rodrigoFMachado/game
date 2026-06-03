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





    // Este método permite ao ScriptParser usar o barramento de output do CPU
    public void logCommand(String message) {
        broadcast(message);
    }

    // --- Os métodos originais do teu UML ---
    public void playCard(int playerId, int cardIndex) {
        if (playerId != currentPlayerIndex) {
            throw new IllegalStateException("It's not player " + playerId + "'s turn.");
        }
        Player currentPlayer = players.get(currentPlayerIndex);

        Card currentCard = currentPlayer.getHand().getCard(cardIndex);

        if (currentCard.getRank() == Wild)


            broadcast("PLAY_CARD Player playerid played Wild (color will be chosen)");

        else if (currentCard.getColor() == currentColor || currentCard.getRank() == discardPile.getTop)
            currentPlayerIndex++;
            currentColor = currentCard.getColor();
            discardPile.addTop(currentCard);

    }

    public void chooseColor(int playerId, String colorCode) {
        // Lógica de escolher cor a implementar
    }

    public void advanceTurn() {
        // Lógica de avançar turno a implementar
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



    public void drawCard(int playerId) {
        // Vamos ligar isto ao ScriptParser na próxima fase
    }
}