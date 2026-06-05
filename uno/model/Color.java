package uno.model;

/**
 * Enum representing the color of a card in Uno.
 */
public enum Color {
    RED("R"),
    YELLOW("Y"),
    GREEN("G"),
    BLUE("B"),
    WILD("W");

    /** The code representing the color. */
    private final String code;

    Color(String code) {
        this.code = code;
    }

    /**
     * Returns the code representing the color.
     * @return the code representing the color
     */
    public String getCode() {
        return this.code;
    }
}