package uno.api;

import uno.engine.*;

public interface CardEffect {

	/**
	 * 
	 * @param ctx
	 */
	void execute(GameContext ctx);

}