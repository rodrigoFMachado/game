package uno.engine;

import uno.api.*;

public class WildEffect implements CardEffect {

    /**
     * * @param ctx
     */
    public void execute(GameContext ctx) {
        ctx.waitForColor();
    }
    
}
