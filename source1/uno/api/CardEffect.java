package uno.api;

import uno.engine.GameContext;

public interface CardEffect {

	/** 
	 * Executes the effect of the card.
	 * @param ctx the game context
	 */
    void execute(GameContext ctx);
}