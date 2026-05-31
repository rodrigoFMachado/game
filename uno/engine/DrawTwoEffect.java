package uno.engine;

import uno.api.*;

public class DrawTwoEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		// Standard UNO rules: Next player draws 2 cards AND loses their turn.
		ctx.forceDraw(2);
		ctx.skipNextPlayer();
	}

}