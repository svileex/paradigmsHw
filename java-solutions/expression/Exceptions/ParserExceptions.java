package expression.Exceptions;

public class ParserExceptions extends RuntimeException {
    private final String message;

    public ParserExceptions(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
