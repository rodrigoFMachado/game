package uno.engine;

import uno.api.*;

/**
 * Realization of the CardEffect interface for the Reverse card in Uno. When executed, it reverses the direction of play in the game.
 */
public class ReverseEffect implements CardEffect {

	/** 
	 * Executes the effect of the Reverse card, which reverses the direction of play in the game.
	 * @param ctx the game context to call the necessary methods to apply the effect
	 */
	public void execute(GameContext ctx) {
		ctx.reverseDirection();
	}

}