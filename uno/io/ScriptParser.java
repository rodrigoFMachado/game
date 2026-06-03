package uno.io;

import uno.engine.GameContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ScriptParser implements AutoCloseable {

    private final BufferedReader reader;
    private final GameContext engine; // A ligação direta ao teu CPU

    public ScriptParser(Reader reader, GameContext engine) {
        this.reader = new BufferedReader(reader);
        this.engine = engine;
    }

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
                    engine.playCard(playerId, idx); // Envia para o CPU

                } else if ("DRAW".equalsIgnoreCase(cmd)) {
                    engine.drawCard(playerId); // Envia para o CPU

                } else if ("COLOR".equalsIgnoreCase(cmd)) {
                    String colorCode = parts[3];
                    engine.chooseColor(playerId, colorCode); // Envia para o CPU

                } else {
                    throw new IllegalArgumentException("Unknown command: " + cmd);
                }
                
                // Se a jogada foi válida e não atirou erro, avança o turno
                engine.advanceTurn();
                
            } catch (Exception e) {
                // Se o GameContext atirar um erro (InvalidMoveException), apanhamos aqui
                engine.logCommand("EVENT ERROR " + e.getMessage());
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

    @Override
    public void close() throws IOException {
        reader.close();
    }
}