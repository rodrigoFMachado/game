package uno.engine;

import uno.api.*;

public class NumberEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		ctx.setColor();
		ctx.broadcastCardEffect();
	}

}