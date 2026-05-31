package uno.engine;

import uno.api.*;

public class NumberEffect implements CardEffect {

	/**
	 * * @param ctx
	 */
	public void execute(GameContext ctx) {
		// Number cards don't modify the turn order or force draws.
		// The execute method stays empty because the normal turn cycle just continues.
	}

}