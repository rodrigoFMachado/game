package uno.v2;

import uno.api.*;
import uno.engine.GameContext;

/** 
 * Effect for drawing two cards quickly.
 */
public class SpeedDrawTwoEffect implements CardEffect {

    /**
     * Executes the speed draw two effect.
     * @param ctx the game context
     */
    public void execute(GameContext ctx) {
        ctx.forceDraw(1);
        ctx.skipNextPlayer();
    }
    
}
