package uno.io;

import uno.engine.GameContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


/**
 * ScriptParser is responsible for reading a script file containing a sequence of player commands and executing them in the context of the game engine. It parses each line of the script, validates the format, and translates it into corresponding method calls on the GameContext. The parser also handles logging of commands and errors as specified in the project requirements, ensuring that any invalid commands are reported and that the game state is updated accordingly after each valid command.
 */
public class ScriptParser implements AutoCloseable {

    /** The reader used to read the script file. */
    private final BufferedReader reader;

        /** A reference to the game engine's context, allowing the parser to execute commands and log events directly through the engine. This tight coupling is necessary for the parser to interact with the game state and ensure that all actions are properly recorded and executed according to the rules of the game. */
    private final GameContext engine;

    /**
     * Constructs a ScriptParser with the given Reader and GameContext, initializing the necessary fields for reading the script and interacting with the game engine. The constructor sets up the BufferedReader for efficient reading of the script file and stores a reference to the GameContext for executing commands and logging events as they are parsed from the script.
     * @param reader
     * @param engine
     */
    public ScriptParser(Reader reader, GameContext engine) {
        this.reader = new BufferedReader(reader);
        this.engine = engine;
    }

    /**
     * Parses all lines from the script file and executes the corresponding commands in the game engine.
     * @throws IOException If an I/O error occurs while reading the script file.
     */
    public void parseAll() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) continue;

            String[] parts = cleaned.split("\\s+");
            if (parts.length < 3 || !"PLAYER".equals(parts[0])) {
                throw new IllegalArgumentException("Invalid script line: " + line);
            }

            // O PDF exige que todos os comandos lidos sejam imprimidos no ecrã (Secção 1.4.3)
            engine.logCommand("EVENT COMMAND line=\"" + line.trim() + "\"");

            int playerId = Integer.parseInt(parts[1]);
            String cmd = parts[2];

            try {
                if ("PLAY".equalsIgnoreCase(cmd)) {
                    int idx = Integer.parseInt(parts[3]);
                    engine.playCard(playerId, idx); // O engine trata da jogada E do output!

                } else if ("DRAW".equalsIgnoreCase(cmd)) {
                    engine.drawCard(playerId); 

                } else if ("COLOR".equalsIgnoreCase(cmd)) {
                    String colorCode = parts[3];
                    engine.chooseColor(playerId, colorCode); 

                } else {
                    throw new IllegalArgumentException("Unknown command: " + cmd);
                }
                
                // Se a jogada foi válida e não atirou erro, o turno avança!
                engine.advanceTurn();
                
            } catch (Exception e) {
                // PDF 1.4.5: Imprimir o erro exato e terminar imediatamente!
                engine.logCommand("EVENT ERROR " + e.getMessage());
                System.exit(1); // 1 = Termina a execução com código de erro. O jogo morre aqui.
            }
        }
    }


    private String cleanLine(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) return null;
        if (trimmed.indexOf('#') >= 0) {
            throw new IllegalArgumentException("Inline comments are not allowed in script lines: " + line);
        }
        return trimmed;
    }

    /**
     * Closes the underlying reader when done, ensuring that all resources are properly released. This method is called automatically when using try-with-resources or can be called manually to clean up resources after parsing is complete.
     * @throws IOException If an I/O error occurs while closing the reader.
    */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}