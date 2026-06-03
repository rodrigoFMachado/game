package uno.engine;

import uno.api.*;

public class WildDrawFourEffect implements CardEffect {

    /**
     * * @param ctx
     */
    public void execute(GameContext ctx) {
        ctx.broadcastCardEffect();
        ctx.forceDraw(4);
        ctx.skipNextPlayer();
    }
    
}
