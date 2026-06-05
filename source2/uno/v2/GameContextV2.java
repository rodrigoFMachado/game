package uno.v2;

import uno.engine.GameContext;
import uno.model.*;

public class GameContextV2 extends GameContext {

    private String activeExtension;

    public GameContextV2(int numPlayers, Deck loadedDeck, String activeExtension) {
        super(numPlayers, loadedDeck);
        this.activeExtension = activeExtension;
    }

    @Override
    public void playCard(int playerId, int cardIndex) {
        Player currentPlayer = players.get(currentPlayerIndex);
        
        // Validações herdadas e idênticas à Fase 1
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

        // O OUTPUT ADAPTADO À FASE 2
        switch (currentCard.getRank()) {
            case DRAW_THREE:
                this.currentColor = currentCard.getColor();
                logCommand(baseLog + "DRAW_THREE; Player " + targetPlayerIndex + " draws 3 and is skipped");
                break;
            case DRAW_TWO:
                this.currentColor = currentCard.getColor();
                if ("SpeedRuleset".equals(activeExtension)) {
                    logCommand(baseLog + "DRAW_TWO; Player " + targetPlayerIndex + " draws 1 and is skipped");
                } else {
                    logCommand(baseLog + "DRAW_TWO; Player " + targetPlayerIndex + " draws 2 and is skipped");
                }
                break;
            case WILD:
                logCommand(baseLog + "WILD (color will be chosen)");
                this.isWaitingForColor = true;
                break;
            case WILD_DRAW_FOUR:
                logCommand(baseLog + "WILD_DRAW_FOUR; Player " + targetPlayerIndex + " draws 4 and is skipped");
                this.isWaitingForColor = true;
                break;
            case SKIP:
                this.currentColor = currentCard.getColor();
                logCommand(baseLog + "SKIP");
                break;
            case REVERSE:
                this.currentColor = currentCard.getColor();
                logCommand(baseLog + "REVERSE");
                break;
            default:
                this.currentColor = currentCard.getColor();
                logCommand(baseLog + currentCard.toString());
                break;
        }

        currentCard.getEffect().execute(this);

        if (currentPlayer.getHand().getSize() == 0) {
            logCommand("EVENT GAME_END Player " + playerId + " wins\nEVENT WINNER player=" + playerId + "\nGAME_END");
            System.exit(0);
        }
    }
}