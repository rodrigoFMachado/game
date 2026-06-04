package uno.v2;

import uno.api.*;
import uno.engine.GameContext;

public class SpeedDrawTwoEffect implements CardEffect {

    /**
     * * @param ctx
     */
    public void execute(GameContext ctx) {
        ctx.forceDraw(1);
        ctx.skipNextPlayer();
    }
    
}
