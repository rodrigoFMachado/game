package uno.api;

import uno.engine.GameContext;


/* 
 * Represents the effect of a card in the Uno game. Each card can have a unique effect that alters the game state when played.
 */
public interface CardEffect {

	/** 
	 * Executes the effect of the card.
	 * @param ctx the game context
	 */
    void execute(GameContext ctx);
}