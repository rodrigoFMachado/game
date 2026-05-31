package uno.model;

import uno.api.*;

public class Card {

	private Color color;
	private Rank rank;
	private CardEffect effect;


	/**
	 * 
	 * @param color
	 * @param rank
	 * @param effect
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