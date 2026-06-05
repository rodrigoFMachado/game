package uno.api;

/**
 * Represents an observer that can react to events in the Uno game.
 */
public interface GameObserver {

	/**
	 * Logs an event in the game.
	 * @param message the message to log
	 */
    void logEvent(String message);
}