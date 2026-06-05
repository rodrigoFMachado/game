package uno.engine;

import uno.api.*;

/**
 * Realization of the CardEffect interface for the Wild card in Uno. When executed, it allows the current player to choose a new color for the game.
 */
public class WildEffect implements CardEffect {

    /**
     * Executes the effect of the Wild card, which allows the current player to choose a new color for the game.
     * * @param ctx the game context to call the necessary methods to apply the effect
     */
    public void execute(GameContext ctx) {
        ctx.waitForColor();
    }
}
