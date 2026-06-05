package uno.engine;

import uno.api.*;

/**
 * Realization of the CardEffect interface for the Skip card in Uno. When executed, it skips the next player's turn in the game.
 */
public class SkipEffect implements CardEffect {

	/**
	 * Executes the effect of the Skip card, which skips the next player's turn in the game.
	 * @param ctx the game context to call the necessary methods to apply the effect
	 */
	public void execute(GameContext ctx) {
		ctx.skipNextPlayer();
	}

}