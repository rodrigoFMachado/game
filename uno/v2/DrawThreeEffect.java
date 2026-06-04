package uno.v2;

import uno.api.*;
import uno.engine.GameContext;

public class DrawThreeEffect implements CardEffect {

    /**
     * * @param ctx
     */
    public void execute(GameContext ctx) {
        ctx.forceDraw(3);
        ctx.skipNextPlayer();
    }
    
}
