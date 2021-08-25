package expression.parser;

public abstract class BaseParser {
    protected CharSource source;
    protected char ch;
    public static final char END = '\0';

    public BaseParser(CharSource in) {
        source = in;
        nextChar();
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.getNext() : END;
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }
}
