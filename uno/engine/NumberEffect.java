package uno.engine;

import uno.api.*;

/**
 * Realization of the CardEffect interface for number cards in Uno. When executed, it has no effect on the game state, as number cards do not have any special actions associated with them.
 */
public class NumberEffect implements CardEffect {

	/**
	 * Executes the effect of a number card, which has no effect on the game state.
	 * @param ctx the game context to call the necessary methods to apply the effect
	 */
	public void execute(GameContext ctx) {
	}

}