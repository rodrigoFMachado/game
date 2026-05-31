package uno.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

	private List<Card> cards;

	public Deck() {
		this.cards = new ArrayList<>();
	}

	public Card drawTop() {
		if (this.cards.isEmpty()) {
			return null;
		}
		// Remove and return the last card in the list
		return this.cards.remove(this.cards.size() - 1);
	}

	/**
	 * 
	 * @param c
	 */
	public void addTop(Card c) {
		this.cards.add(c);
	}

	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

}