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

    public void playCard(int playerId, int cardIndex) {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Validações de Erro Fatal
        if (playerId != currentPlayer.getId()) {
            throw new IllegalArgumentException("Not player " + playerId + " turn");
        }
        
        if (cardIndex < 0 || cardIndex >= currentPlayer.getHand().getSize()) {
            throw new IllegalArgumentException("Invalid card index " + cardIndex);
        }

        if (isWaitingForColor == true) {
            throw new IllegalArgumentException("Must choose a color before playing another card");
        }


        // Ler a carta
        Card currentCard = currentPlayer.getHand().getCard(cardIndex);

        // Move ilegal
        if (currentCard.getColor() != this.currentColor && currentCard.getRank() != discardPile.peekTop().getRank() && currentCard.getColor() != Color.WILD) { 
            throw new IllegalArgumentException("Card " + currentCard.getColor() + "-" + currentCard.getRank() + " is not playable");
        }

        // 2. Executar a jogada fisicamente na memória
        currentPlayer.getHand().removeCard(cardIndex); // Tira a carta da mão
        discardPile.addTop(currentCard);               // Mete a carta na mesa


        currentCard.getEffect().execute(this);    // Chamado efeito da carta
        
        // 3. Atualizar o estado e imprimir o Output dependendo da carta
        if (currentCard.getRank() == Rank.WILD || currentCard.getRank() == Rank.WILD_DRAW_FOUR) {
            // É um Wild. Não mudamos a cor ainda, só informamos.
            broadcast("EVENT PLAY_CARD Player " + playerId + " played " + currentCard.getRank() + " (color will be chosen)");
            
            isWaitingForColor = true;
            
        } else {
            // Carta normal. Atualizamos a cor da mesa para a cor da carta.
            this.currentColor = currentCard.getColor();
            broadcast("EVENT PLAY_CARD Player " + playerId + " played " + currentCard.getColor() + "-" + currentCard.getRank());
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
        
        broadcast("EVENT DRAW_CARD Player " + playerId + " draws 1 card (" + drawnCard.getColor() + "-" + drawnCard.getRank() + ")");
    }

    public void advanceTurn() {
        if (isWaitingForColor) {
            return;
        }

        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }

        broadcast("EVENT TURN_ADVANCE Next player: " + currentPlayerIndex);
    }

    // --- APIs para os Efeitos das Cartas usarem ---
    public void reverseDirection() {
        this.isClockwise = !this.isClockwise;

        // Em jogos com 2 jogadores, inverter a direção também salta o próximo jogador
        if (players.size() == 2) {
            skipNextPlayer();
        }
    }

    public void skipNextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
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




}
