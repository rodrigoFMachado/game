package uno.model;

public class Player {

	/** The player's unique ID. */
	private int id;

	/** The player's hand of cards. */
	private Hand hand;

	/**
	 * Creates a new player with the specified ID.
	 * @param id the player's ID
	 */
	public Player(int id) {
		this.id = id;
		this.hand = new Hand(); // Gives the player an empty hand when created
	}


	public int getId() {
		return this.id;
	}

	public Hand getHand() {
		return this.hand;
	}

}