package uno.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {

	private List<Card> cards;

	/**
	 * Creates an empty deck.
	 */
	public Deck() {
		this.cards = new ArrayList<>();
	}

	/**
	 * Draws a card from the top of the deck.
	 * @return the drawn card, or null if the deck is empty
	 */
	public Card drawTop() {
		if (this.cards.isEmpty()) {
			return null;
		}
		// Remove and return the last card in the list
		return this.cards.remove(this.cards.size() - 1);
	}

	/**
	 * Adds a card to the top of the deck.
	 * @param c the card to add
	 */
	public void addTop(Card c) {
		this.cards.add(c);
	}

	/**
	 * Checks if the deck is empty.
	 * @return true if the deck is empty, false otherwise
	 */
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

}