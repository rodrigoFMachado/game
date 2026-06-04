package uno.model;

/**
 * Enum representing the rank of a card in Uno.
 */
public enum Rank {
	ZERO("0"),
	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	NINE("9"),
	SKIP("SKIP"),
	REVERSE("REVERSE"),
	DRAW_TWO("DRAW_TWO"),
	WILD("WILD"),
	WILD_DRAW_FOUR("WILD_DRAW_FOUR");

	private final String label;

	// Construtor do Enum
	Rank(String label) {
		this.label = label;
	}

	// Quando o programa tentar imprimir o Rank, usa a label (o número) em vez do nome da variável!
	@Override
	public String toString() {
		return this.label;
	}
}