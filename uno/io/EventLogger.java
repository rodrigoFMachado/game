package uno.io;

import uno.api.*;

public class EventLogger implements GameObserver {

	/**
	 * * @param message
	 */
	public void logEvent(String message) {
		// Imprime os eventos enviados pelo CPU diretamente para o terminal
		System.out.println(message);
	}
}