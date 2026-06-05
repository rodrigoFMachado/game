package uno.v2;

import uno.io.DeckLoader;
import uno.model.Rank;
import uno.api.CardEffect;
import uno.engine.DrawTwoEffect; // caso nao esteja ativo o speed ruleset

/* 
 * Loader for creating decks with additional card types and effects.
 */
public class DeckLoaderV2 extends DeckLoader {

    /** The active extension for the deck loader. */
    private String activeExtension;

    /**
     * Creates a new DeckLoaderV2 with the specified active extension.
     * @param activeExtension
     */
    public DeckLoaderV2(String activeExtension) {
        this.activeExtension = activeExtension;
    }

    /**
     * Parses a rank from a string representation.
     * @param r the string representation of the rank
     * @return the corresponding Rank enum value
     */
    @Override
    protected Rank parseRank(String r) {
        // Se o texto for DRAW_THREE, este loader novo trata disso!
        if (r.toUpperCase().equals("DRAW_THREE")) {
            return Rank.DRAW_THREE;
        }
        
        // Para qualquer outra carta normal (R-3, WILD, etc), manda o pai (Fase 1) trabalhar!
        return super.parseRank(r); 
    }

    /**
     * Assigns an effect to a card based on its rank.
     * @param rank the rank of the card
     * @return the corresponding card effect
     */
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