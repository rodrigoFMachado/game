package uno.engine;

import uno.api.*;

/**
 * Realization of the CardEffect interface for the Wild Draw Four card in Uno. When executed, it forces the next player to draw four cards, skip their turn, and allows the current player to choose a new color for the game.
 */
public class WildDrawFourEffect implements CardEffect {

    /**
     * Executes the effect of the Wild Draw Four card, which forces the next player to draw four cards, skip their turn, and allows the current player to choose a new color for the game.
     * @param ctx the game context to call the necessary methods to apply the effect
     */
    public void execute(GameContext ctx) {
        ctx.waitForColor();
        ctx.forceDraw(4);
        ctx.skipNextPlayer();
    }
    
}
