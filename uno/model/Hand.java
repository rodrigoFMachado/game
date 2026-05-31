package uno.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {

	private List<Card> cards;
	
	public Hand() {
		this.cards = new ArrayList<>();
	}

	/**
	 * 
	 * @param c
	 */
	public void addCard(Card c) {
		this.cards.add(c);
	}

	/**
	 * 
	 * @param index
	 */
	public Card removeCard(int index) {
		return this.cards.remove(index);
	}

	/**
	 * 
	 * @param index
	 */
	public Card getCard(int index) {
		return this.cards.get(index);
	}

	public int getSize() {
		return this.cards.size();
	}
}