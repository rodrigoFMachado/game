package uno.io;

import uno.api.*;

public class EventLogger implements GameObserver {

	/**
	 * Logs an event to the console.
	 * @param message The message to log.
	 */
	public void logEvent(String message) {
		System.out.println(message); // Só isto!
	}
}