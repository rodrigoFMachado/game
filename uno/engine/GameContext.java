package uno.engine;

import uno.model.*;

public class GameContext {

	private List<Player> players;
	private Deck drawPile;
	private Deck discardPile;
	private int currentPlayerIndex;
	private boolean isClockwise;
	private Color currentColor;

	/**
	 * 
	 * @param playerId
	 * @param cardIndex
	 */
	public void playCard(int playerId, int cardIndex) {
		// TODO - implement GameContext.playCard
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param playerId
	 */
	public void drawCard(int playerId) {
		// TODO - implement GameContext.drawCard
		throw new UnsupportedOperationException();
	}

}