package uno.model;

public enum Color {
    RED("R"),
    YELLOW("Y"),
    GREEN("G"),
    BLUE("B"),
    WILD("W");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}