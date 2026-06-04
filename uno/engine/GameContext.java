package uno.engine;

import java.util.ArrayList;
import java.util.List;
import uno.api.GameObserver;
import uno.model.*;

public class GameContext {

    private List<Player> players;
    private Deck drawPile;
    private Deck discardPile;
    private Color currentColor;
    private int currentPlayerIndex;
    private boolean isClockwise;
    private boolean isWaitingForColor;
    private int skips = 0; // nao usado antes, vai dar jeito para crazy ruleset
    
    // Lista de monitores ligados a este 
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
        this.isWaitingForColor = false;
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
        broadcast("GAME_START players=" + players.size()); 
        broadcast("EVENT TOP_CARD " + initialDiscard.toString() + " color=" + currentColor);
        
        for (Player p : players) {
            StringBuilder handStr = new StringBuilder("EVENT HAND player=" + p.getId() + " cards=");
            for (int i = 0; i < p.getHand().getSize(); i++) {
                Card c = p.getHand().getCard(i);
                // Muito mais limpo agora!
                handStr.append(c.toString()).append(" "); 
            }
            broadcast(handStr.toString().trim());
        }
        broadcast("EVENT TURN_START player=" + currentPlayerIndex);
    }



    // Este método permite ao ScriptParser usar o barramento de output do CPU
    public void logCommand(String message) {
        broadcast(message);
    }

    public void playCard(int playerId, int cardIndex) {
        Player currentPlayer = players.get(currentPlayerIndex);

        // 1. Validações de Erro Fatal
        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Not player " + playerId + " turn");
        }
        if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().getSize()) {
            throw new IllegalArgumentException("Invalid card index " + cardIndex);
        }
        if (isWaitingForColor) {
            throw new IllegalArgumentException("Must choose a color before playing another card");
        }

        // Ler a carta
        Card currentCard = currentPlayer.getHand().getCard(cardIndex);

        // Move ilegal
        if (currentCard.getColor() != this.currentColor && currentCard.getRank() != discardPile.peekTop().getRank() && currentCard.getColor() != Color.WILD) { 
            throw new IllegalArgumentException("Card " + currentCard.toString() + " is not playable");
        }

        // 2. Tira a carta da mão e mete na mesa
        currentPlayer.getHand().removeCard(cardIndex); 
        discardPile.addTop(currentCard);               

        // 3. Output e Atualização de Cor (SEMPRE ANTES de executar o efeito, porque o efeito vai mexer nos índices do turno)
        String baseLog = "EVENT PLAY_CARD Player " + playerId + " played ";
        
        // Esta fórmula é a mesma que usaste no teu forceDraw (descobre quem é a vítima do +2 ou +4)
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

        // 4. Executar o efeito da carta (Vai forçar compras e rodar os turnos conforme programaste)
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

        // 1. Validações do PDF
        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Only player " + currentPlayer.getId() + " can choose the color");
        }

        if (!isWaitingForColor) {
            throw new IllegalArgumentException("Cannot choose color - no wild card was played");
        }

        // 2. Traduzir o "R" / "G" do script para o teu Enum e validar
        Color newColor;
        switch (colorCode.toUpperCase()) {
            case "R": newColor = Color.RED; break;
            case "Y": newColor = Color.YELLOW; break;
            case "G": newColor = Color.GREEN; break;
            case "B": newColor = Color.BLUE; break;
            case "W": 
                throw new IllegalArgumentException("Cannot choose WILD as a color");
            default:
                throw new IllegalArgumentException("Invalid color code");
        }

        // 3. Atualizar a memória
        this.currentColor = newColor;
        this.isWaitingForColor = false;

        broadcast("EVENT CHOOSE_COLOR Player " + playerId + " chose color " + this.currentColor);
    }


    public void drawCard(int playerId) {
        Player currentPlayer = players.get(currentPlayerIndex);

        // 1. Validações
        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Not player " + playerId + " turn");
        }
        
        if (isWaitingForColor) {
            throw new IllegalArgumentException("Must choose a color before drawing");
        }

        // Logica de draw
        Card drawnCard = drawPile.drawTop();
        
        // Se o baralho estiver vazio, o jogo acaba sem vencedor.
        if (drawnCard == null) {
            broadcast("EVENT GAME_END No cards available to draw");
            broadcast("GAME END");
            System.exit(0); 
        }

        // 3. Atualizar a memória e avisar o ecrã
        currentPlayer.getHand().addCard(drawnCard);
        
        broadcast("EVENT DRAW_CARD Player " + playerId + " draws 1 card (" + drawnCard.toString() + ")");
    
    }

    public void advanceTurn() {
        // Se estivermos à espera da cor, o turno congela
        if (isWaitingForColor) {
            return;
        }

        // 1 passo normal + acumulados
        int step = 1 + this.skips;
        int direction = isClockwise ? 1 : -1;
        int numPlayers = players.size();

        currentPlayerIndex = (currentPlayerIndex + (direction * step)) % numPlayers;
        
        if (currentPlayerIndex < 0) {
            currentPlayerIndex += numPlayers;
        }

        // O turno rodou, limpamos os saltos para a próxima pessoa!
        this.skips = 0; 

        broadcast("EVENT TURN_ADVANCE Next player: " + currentPlayerIndex);
    }


    // --- APIs para os Efeitos das Cartas usarem ---
    public void setColor() {
        Player currentPlayer = players.get(currentPlayerIndex);

        Card currentCard = currentPlayer.getHand().getCard(currentPlayerIndex);

        this.currentColor = currentCard.getColor();
    }


    public void broadcastCardEffect() {
        Player currentPlayer = players.get(currentPlayerIndex);

        Card currentCard = currentPlayer.getHand().getCard(currentPlayerIndex);

        String effectDescription = "EVENT PLAY_CARD Player " + currentPlayer.getId() + " played ";


        switch (currentCard.getRank()) {
            case Rank.SKIP: broadcast(effectDescription + "SKIP;"); break;
            case Rank.REVERSE: broadcast(effectDescription + "REVERSE;"); break;
            case Rank.DRAW_TWO: broadcast(effectDescription + "DRAW_TWO; Player" + currentPlayer.getId() + "draws 2 and is skipped"); break;
            case Rank.WILD: broadcast(effectDescription + "WILD (color will be chosen)"); break;
            case Rank.WILD_DRAW_FOUR: broadcast(effectDescription + "WILD_DRAW_FOUR; Player" + currentPlayer.getId() + "draws 4 and is skipped"); break;
            default: broadcast(effectDescription + "(" + currentCard.getColor() + "-" + currentCard.getRank() + ")");
        }
    }


    public void reverseDirection() {
        this.isClockwise = !this.isClockwise;

        // Num jogo de 2 pessoas, o Reverse funciona como um Skip
        if (players.size() == 2) {
            skipNextPlayer(); 
        }
    }

    public void skipNextPlayer() {
        // Apenas acumula o salto! Não mexe em quem está a jogar agora.
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
