package uno.engine;

import uno.api.*;

public class ReverseEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		// Tells the CPU to flip the direction boolean
		ctx.reverseDirection();
	}

}