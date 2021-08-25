package expression.parser;

public class StringSource implements CharSource {
    private String source;
    private int pointer;

    public StringSource(String source) {
        this.source = source;
        //pointer = 0;
    }

    @Override
    public boolean hasNext() {
        return pointer < source.length();
    }

    @Override
    public char getNext() {
        return source.charAt(pointer++);
    }
}
