package uno.engine;

import uno.api.*;

public class ReverseEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		ctx.reverseDirection();
	}

}