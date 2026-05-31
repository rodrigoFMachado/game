package uno.engine;

import uno.api.*;

public class SkipEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		// Tells the CPU to jump over the next person in the array
		ctx.skipNextPlayer();
	}

}