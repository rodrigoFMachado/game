package uno.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	/** List of cards in the hand. */
	private List<Card> cards;
	
	/**
	 * Creates an empty hand for a player.
	 */
	public Hand() {
		this.cards = new ArrayList<>();
	}

	/**
	 * Adds a card to the player's hand.
	 * @param c the card to add
	 */
	public void addCard(Card c) {
		this.cards.add(c);
	}

	/**
	 * Removes a card from the player's hand.
	 * @param index the index of the card to remove
	 * @return the removed card
	 */
	public Card removeCard(int index) {
		return this.cards.remove(index);
	}


	public Card getCard(int index) {
		return this.cards.get(index);
	}


	public int getSize() {
		return this.cards.size();
	}
}