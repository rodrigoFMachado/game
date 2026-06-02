package uno;

import uno.io.DeckLoader;
import uno.model.Deck;
import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {
        // 1. Parse Command Line Arguments
        if (args.length < 3) {
            System.err.println("Usage: java uno.Main <deckFile> <scriptFile> <playerCount> [<cardsPerPlayer>]");
            System.exit(1);
        }

        String deckFile = args[0];
        String scriptFile = args[1];
        int playerCount = Integer.parseInt(args[2]);
        int cardsPerPlayer = (args.length >= 4) ? Integer.parseInt(args[3]) : 7; // Default is 7

        // 2. Print Startup Header (PDF Requirement 1.4.1)
        System.out.println("Running uno.Main with:");
        System.out.println("Deck file: " + deckFile);
        System.out.println("Script file: " + scriptFile);
        System.out.println("Nb players: " + playerCount);
        System.out.println("Nb cards per player: " + cardsPerPlayer);

        // 3. Load the Deck into Memory
        Deck loadedDeck = null;
        try (Reader reader = new FileReader(deckFile)) {
            DeckLoader loader = new DeckLoader();
            loadedDeck = loader.loadDeck(reader);
        } catch (Exception e) {
            System.err.println("Error loading deck: " + e.getMessage());
            System.exit(1);
        }

        // TODO: Next we will boot the GameContext here using loadedDeck and playerCount
    }
}