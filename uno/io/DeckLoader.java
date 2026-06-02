package uno.io;

import uno.model.*;
import uno.engine.*;
import uno.api.CardEffect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckLoader {

    public Deck loadDeck(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String line;
        List<Card> tempStorage = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String cleaned = cleanLine(line);
            if (cleaned == null) {
                continue;
            }

            String[] parts = cleaned.split("-");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid deck line: " + line);
            }

            // Translate text into Enums and Memory Objects
            Color color = parseColor(parts[0]);
            Rank rank = parseRank(parts[1]);
            CardEffect effect = assignEffect(rank);

            tempStorage.add(new Card(color, rank, effect));
        }

        // Reverse the list so the first line of the file becomes the "top" (end) of the deck
        Collections.reverse(tempStorage);

        Deck finalDeck = new Deck();
        for (Card c : tempStorage) {
            finalDeck.addTop(c);
        }

        return finalDeck;
    }

    private String cleanLine(String line) {
        String trimmed = line.trim();

        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return null;
        }
        
        int commentIndex = trimmed.indexOf('#');
        if (commentIndex >= 0) {
            trimmed = trimmed.substring(0, commentIndex).trim();
            if (trimmed.isEmpty()) {
                return null;
            }
        }

        return trimmed;
    }

    // --- Translators ---

    private Color parseColor(String c) {
        switch (c.toUpperCase()) {
            case "R": return Color.RED;
            case "Y": return Color.YELLOW;
            case "G": return Color.GREEN;
            case "B": return Color.BLUE;
            case "W": return Color.WILD;
            default: throw new IllegalArgumentException("Unknown color: " + c);
        }
    }

    private Rank parseRank(String r) {
        switch (r.toUpperCase()) {
            case "0": return Rank.ZERO;
            case "1": return Rank.ONE;
            case "2": return Rank.TWO;
            case "3": return Rank.THREE;
            case "4": return Rank.FOUR;
            case "5": return Rank.FIVE;
            case "6": return Rank.SIX;
            case "7": return Rank.SEVEN;
            case "8": return Rank.EIGHT;
            case "9": return Rank.NINE;
            case "SKIP": return Rank.SKIP;
            case "REVERSE": return Rank.REVERSE;
            case "DRAW_TWO": return Rank.DRAW_TWO;
            case "WILD": return Rank.WILD;
            case "WILD_DRAW_FOUR": return Rank.WILD_DRAW_FOUR;
            default: throw new IllegalArgumentException("Unknown rank: " + r);
        }
    }

    private CardEffect assignEffect(Rank rank) {
        // Strategy Pattern applied: Assigning the logic block based on the rank
        switch (rank) {
            case SKIP: return new SkipEffect();
            case REVERSE: return new ReverseEffect();
            case DRAW_TWO: return new DrawTwoEffect();
            default: return new NumberEffect(); // Wilds and Numbers don't inherently shift turn order
        }
    }
}