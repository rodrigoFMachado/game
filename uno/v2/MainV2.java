package uno.v2;

import uno.io.EventLogger;
import uno.io.ScriptParser;
import uno.model.*;
import java.io.FileReader;
import java.io.Reader;

/* 
 * Main class for running the enhanced Uno game.
 */
public class MainV2 {

    /**
     * Main method to run the Uno game with the specified parameters.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 6 || !args[0].equals("-p2")) {
            System.err.println("Usage: java -cp ... uno.v2.MainV2 -p2 <type> <name> <deckFile> <scriptFile> <playerCount> [<cardsPerPlayer>]");
            System.exit(1);
        }

        String extensionName = args[2]; // Ex: "DRAW_THREE" ou "SpeedRuleset"
        String deckFile = args[3];
        String scriptFile = args[4];
        int playerCount = Integer.parseInt(args[5]);
        int cardsPerPlayer = args.length > 6 ? Integer.parseInt(args[6]) : 7;


        // 2. Cabeçalho Inicial Obrigatório 
        System.out.println("Running uno.Main with:");
        System.out.println("   Deckfile: " + deckFile);
        System.out.println("   Script file: " + scriptFile);
        System.out.println("   Nb players: " + playerCount);
        System.out.println("   Nb cards per player: " + cardsPerPlayer);
        System.out.println("");

        try (Reader deckReader = new FileReader(deckFile)) {
            
            DeckLoaderV2 loader = new DeckLoaderV2(extensionName);
            Deck loadedDeck = loader.loadDeck(deckReader);

            GameContextV2 engine = new GameContextV2(playerCount, loadedDeck, extensionName);
            engine.addObserver(new EventLogger());
            engine.setupGame(cardsPerPlayer);

            try (Reader scriptReader = new FileReader(scriptFile);
                ScriptParser parser = new ScriptParser(scriptReader, engine)) {
                parser.parseAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}