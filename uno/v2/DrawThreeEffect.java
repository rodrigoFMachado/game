package uno.v2;

import uno.api.*;
import uno.engine.GameContext;

/* 
 * Effect for drawing three cards.
 */
public class DrawThreeEffect implements CardEffect {

    /**
     * Executes the draw three effect.
     * @param ctx the game context
     */
    public void execute(GameContext ctx) {
        ctx.forceDraw(3);
        ctx.skipNextPlayer();
    }
    
}
