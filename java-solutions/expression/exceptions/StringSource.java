package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char next(int a) {
        if(pos + a >= data.length()) {
            return 0;
        } else {
            return data.charAt(pos + a);
        }
    }

    public int getPos() {
        return pos;
    }
}
