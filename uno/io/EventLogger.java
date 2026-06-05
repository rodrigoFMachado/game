package uno.io;

import uno.api.*;

/**
 * EventLogger is a simple implementation of the GameObserver interface that logs game events to the console. It provides a method to log messages, which can be called by the game engine whenever an event occurs that needs to be recorded. This class serves as a basic observer for monitoring game events and can be extended or modified to include additional functionality, such as logging to a file or filtering specific types of events.
 */
public class EventLogger implements GameObserver {

	/**
	 * Logs an event to the console.
	 * @param message The message to log.
	 */
	public void logEvent(String message) {
		System.out.println(message); // Só isto!
	}
}