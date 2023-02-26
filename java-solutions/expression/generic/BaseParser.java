package expression.generic;

import expression.exceptions.ParseException;
import expression.exceptions.StringSource;

import java.util.Set;

public class BaseParser {
    private static final char END = '\0';
    private static final Set<Character> SINGLE_OP_CHARS = Set.of('-', '+');
    private static final Set<Character> MULTIPLE_OP_CHARS = Set.of('*', '/');
    protected char ch = 0xffff;
    protected StringSource source;
    protected String curToken;

    public BaseParser() {
        curToken = "";
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    protected void updateToken() {
        skipWhitespace();
        StringBuilder tokenSB = new StringBuilder();
        if (between('a', 'z') || between('A', 'Z')) {
            do {
                tokenSB.append(take());
            } while (between('a', 'z') || between('0', '9') || between('A', 'Z'));
        } else if (MULTIPLE_OP_CHARS.contains(ch)) {
            do {
                tokenSB.append(take());
            } while (MULTIPLE_OP_CHARS.contains(ch));
        } else if (SINGLE_OP_CHARS.contains(ch)) {
            tokenSB.append(take());
        }
        curToken = tokenSB.toString();
    }

    protected String checkToken() {
        if (curToken.isEmpty()) {
            updateToken();
        }
        return curToken;
    }

    protected String takeToken() {
        String res = checkToken();
        curToken = "";
        return res;
    }

    protected boolean takeToken(String expected) {
        if (checkToken().equals(expected)) {
            takeToken();
            return true;
        }
        return false;
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected void expect(final char expected) throws ParseException {
        if (test(expected)) {
            take();
        } else {
            throw new ParseException("Expected " + expected + " at the position " + source.getPos());
        }
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        for (int i = 1; i < expected.length(); i++) {
            if (expected.charAt(i) != source.next(i - 1)) {
                return false;
            }
        }
        return test(expected.charAt(0));
    }

    protected boolean take(final String expected) {
        if (!test(expected)) {
            return false;
        }
        for (int i = 0; i < expected.length(); i++) {
            take();
        }
        return true;
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
