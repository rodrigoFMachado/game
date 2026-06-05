package uno.model;

import uno.api.*;

/**
 * Represents a card in the Uno game, which has a color, rank, and effect.
 */
public class Card {

	/** The color of the card. */
	private Color color;

	/** The rank of the card. */
	private Rank rank;

	/** The effect of the card, assigned by deckLoader*/
	private CardEffect effect;


	/**
	 * Creates a new card with the specified color, rank, and effect.
	 * @param color the color of the card
	 * @param rank the rank of the card
	 * @param effect the effect of the card
	 */
	public Card(Color color, Rank rank, CardEffect effect) {
		this.color = color;
		this.rank = rank;
		this.effect = effect;
	}


	public Color getColor() { 
		return this.color; 
	}

	public Rank getRank() { 
		return this.rank; 
	}

	public CardEffect getEffect() { 
		return this.effect; 
	}

	/**
	 * Returns a string representation of the card in the format "Color-Rank".
	 * For example, a red 5 would be represented as "R-5".
	 * @return a string representation of the card
	 */
    @Override
    public String toString() {
        return this.color.getCode() + "-" + this.rank.toString();
    }

}