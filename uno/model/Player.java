package uno.model;

public class Player {

	private int id;
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