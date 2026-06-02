package uno.api;

import uno.engine.GameContext;

public interface CardEffect {

	/**
	 * 
	 * @param ctx
	 */
    void execute(GameContext ctx);
}