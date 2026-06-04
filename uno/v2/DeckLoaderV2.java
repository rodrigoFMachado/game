package uno.v2;

import uno.io.DeckLoader;
import uno.model.Rank;
import uno.api.CardEffect;
import uno.engine.DrawTwoEffect; // caso nao esteja ativo o speed ruleset

public class DeckLoaderV2 extends DeckLoader {

    private String activeExtension;

    public DeckLoaderV2(String activeExtension) {
        this.activeExtension = activeExtension;
    }

    @Override
    protected Rank parseRank(String r) {
        // Se o texto for DRAW_THREE, este loader novo trata disso!
        if (r.toUpperCase().equals("DRAW_THREE")) {
            return Rank.DRAW_THREE;
        }
        
        // Para qualquer outra carta normal (R-3, WILD, etc), manda o pai (Fase 1) trabalhar!
        return super.parseRank(r); 
    }

    @Override
    protected CardEffect assignEffect(Rank rank) {
        // 1. Lógica do Novo Cartão (DRAW_THREE)
        if (rank == Rank.DRAW_THREE) {
            return new DrawThreeEffect();
        }
        
        // 2. Lógica da Variação de Regras (SpeedRuleset injeta um chip diferente no DRAW_TWO!)
        if (rank == Rank.DRAW_TWO && "SpeedRuleset".equals(this.activeExtension)) {
            return new SpeedDrawTwoEffect();
        }

        // Para tudo o resto, o motor da Fase 1 faz o trabalho sozinho!
        return super.assignEffect(rank);
    }
}