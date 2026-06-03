package uno.model;

import uno.api.*;

public class Card {

	private Color color;
	private Rank rank;
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
}