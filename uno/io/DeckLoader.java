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

/**
 * DeckLoader is responsible for reading a deck configuration from a text file and converting it into a Deck object that the game engine can use. It handles parsing each line of the file, translating color and rank codes into their corresponding enums, and assigning the correct effects to special cards. The loader also ensures that the order of cards in the file is preserved in the final deck, with the first line representing the bottom card and the last line representing the top card.
 */
public class DeckLoader {

    /**
     * Loads a deck from a given Reader, parsing each line according to the specified format and translating it into a Deck object. The method handles comments, empty lines, and validates the format of each card entry, throwing exceptions for any invalid lines. It also ensures that the order of cards is maintained as per the file's structure.
     * @param reader
     * @return
     * @throws IOException
     */
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

            // Traduz de array "parts" para variáveis de tipo enum, declaradas em model
            Color color = parseColor(parts[0]);
            Rank rank = parseRank(parts[1]);
            CardEffect effect = assignEffect(rank);

            tempStorage.add(new Card(color, rank, effect));
        }

        // Reverse the list so the first line of the file becomes the last item in array
        Collections.reverse(tempStorage);

        Deck finalDeck = new Deck();

        // Adiciona cartas do array para o topo do deck, comceçando pela primeira carta do array (agora última a ser lida), corrigindo a ordem

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
    /**
     * Traduz cores de letras para enum, usando enum definida em model
     * @param c
     * @return
     */
    protected Color parseColor(String c) {
        switch (c.toUpperCase()) {
            case "R": return Color.RED;
            case "Y": return Color.YELLOW;
            case "G": return Color.GREEN;
            case "B": return Color.BLUE;
            case "W": return Color.WILD;
            default: throw new IllegalArgumentException("Unknown color: " + c);
        }
    }


    /**
     * Traduz ranks de letras para enum, usando enum definida em model
     * @param r
     * @return
     */
    protected Rank parseRank(String r) {
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

    /**
     * Assigns the appropriate effect to a card based on its rank, to be later used by the game engine when the card is played. This method implements a simple strategy pattern, where the logic for determining the effect is encapsulated in one place, making it easier to manage and extend if new card types are added in the future.
     * @param rank
     * @return
     */
    protected CardEffect assignEffect(Rank rank) {
        // Strategy Pattern applied: Assigning the logic block based on the rank
        switch (rank) {
            case SKIP: return new SkipEffect();
            case REVERSE: return new ReverseEffect();
            case DRAW_TWO: return new DrawTwoEffect();
            case WILD: return new WildEffect();
            case WILD_DRAW_FOUR: return new WildDrawFourEffect();
            default: return new NumberEffect(); // Wilds and Numbers don't inherently shift turn order
        }
    }
}