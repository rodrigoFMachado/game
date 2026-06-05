package uno.engine;

import uno.api.*;

/* 
 * Realization of the CardEffect interface for the Draw Two card. When executed, it forces the next player to draw two cards and skip their turn.
 */
public class DrawTwoEffect implements CardEffect {

	/**
	 * Executes the effect of the Draw Two card.
	 * @param ctx the game context to call the necessary methods to apply the effect
	 */
	public void execute(GameContext ctx) {
		ctx.forceDraw(2);
		ctx.skipNextPlayer();
	}

}