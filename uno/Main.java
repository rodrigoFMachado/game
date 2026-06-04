package uno;

import uno.io.DeckLoader;
import uno.io.EventLogger;
import uno.engine.GameContext;
import uno.model.Deck;
import uno.io.ScriptParser;
import java.io.FileReader;
import java.io.Reader;

public class Main {
    public static void main(String[] args) {
        // 1. Validação dos Argumentos da Linha de Comandos
        if (args.length < 3) {
            System.err.println("Usage: java uno.Main <deckFile> <scriptFile> <playerCount> [<cardsPerPlayer>]");
            System.exit(1);
        }

        String deckFile = args[0];
        String scriptFile = args[1];
        int playerCount = Integer.parseInt(args[2]);
        int cardsPerPlayer = (args.length >= 4) ? Integer.parseInt(args[3]) : 7; // Default exigido é 7

        // 2. Cabeçalho Inicial Obrigatório 
        System.out.println("Running uno.Main with:");
        System.out.println("   Deckfile: " + deckFile);
        System.out.println("   Script file: " + scriptFile);
        System.out.println("   Nb players: " + playerCount);
        System.out.println("   Nb cards per player: " + cardsPerPlayer);
        System.out.println("");

        try (Reader deckReader = new FileReader(deckFile)) {
            
            // 3. Constroi o deck a partir do ficheiro usando o DeckLoader
            DeckLoader loader = new DeckLoader();
            Deck loadedDeck = loader.loadDeck(deckReader);

            // 4. Chama construtor de game context com o deck carregado e o número de jogadores
            GameContext engine = new GameContext(playerCount, loadedDeck);

            // 5. Ligar o monitor de saída (EventLogger) ao CPU
            EventLogger logger = new EventLogger();
            engine.addObserver(logger);

            // 6. Iniciar a sequência de arranque (Distribuir cartas e virar a primeira)
            engine.setupGame(cardsPerPlayer);
            
            // 7. LER O SCRIPT E EXECUTAR AS JOGADAS AUTOMATICAMENTE
            try (Reader scriptReader = new FileReader(scriptFile)) {
                ScriptParser parser = new ScriptParser(scriptReader, engine);
                parser.parseAll();
            }

            

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}