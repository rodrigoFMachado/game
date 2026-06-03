package uno.engine;

import uno.api.*;

public class SkipEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		ctx.skipNextPlayer();
	}

}