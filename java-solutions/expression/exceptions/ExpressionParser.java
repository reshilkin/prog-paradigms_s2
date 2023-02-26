package expression.exceptions;

import expression.*;

import java.util.*;

public class ExpressionParser implements TripleParser {
    private static final char END = '\0';
    private char ch = 0xffff;
    private StringSource source;
    private static final Map<String, Variable> VARIABLE_MAP = Map.of(
            "x", new Variable("x"),
            "y", new Variable("y"),
            "z", new Variable("z")
    );

    @Override
    public MyExpression parse(final String expression) throws ParseException {
        source = new StringSource(expression);
        take();
        final MyExpression ans = parseExpression();
        skipWhitespace();
        if (ch != 0) {
            throw new ParseException("Unknown operation at the position " + source.getPos());
        }
        return ans;
    }

    public MyExpression parseExpression() throws ParseException {
        MyExpression first = parseSum();
        while (true) {
            if (take("<")) {
                expect('<');
                first = new ShiftLeft(first, parseSum());
            } else if (take(">")) {
                expect('>');
                if (take(">")) {
                    first = new ArithmeticShiftRight(first, parseSum());
                } else {
                    first = new ShiftRight(first, parseSum());
                }
            } else {
                break;
            }
            skipWhitespace();
        }
        return first;
    }

    private MyExpression parseSum() throws ParseException {
        MyExpression first = parseTerm();
        while (true) {
            if (take("+")) {
                first = new CheckedAdd(first, parseTerm());
            } else if (take("-")) {
                first = new CheckedSubtract(first, parseTerm());
            } else {
                break;
            }
            skipWhitespace();
        }
        return first;
    }

    private MyExpression parseTerm() throws ParseException {
        MyExpression first = parseFactor();
        skipWhitespace();
        while (true) {
            if (take("*")) {
                first = new CheckedMultiply(first, parseFactor());
            } else if (take("/")) {
                first = new CheckedDivide(first, parseFactor());
            } else {
                break;
            }
            skipWhitespace();
        }
        return first;
    }

    private MyExpression parseFactor() throws ParseException {
        MyExpression first = parsePowLog();
        skipWhitespace();
        while (true) {
            if (take("**")) {
                first = new Pow(first, parsePowLog());
            } else if (take("//")) {
                first = new Log(first, parsePowLog());
            } else {
                break;
            }
            skipWhitespace();
        }
        return first;
    }

    private MyExpression parsePowLog() throws ParseException {
        skipWhitespace();
        if (take("(")) {
            final MyExpression temp = parseExpression();
            expect(')');
            return temp;
        } else if (take("-")) {
            if (between('0', '9')) {
                return parseNumber("-");
            } else {
                return new CheckedNegate(parsePowLog());
            }
        } else if (between('0', '9')) {
            return parseNumber("+");
        } else if (between('a', 'z') || between('A', 'Z')) {
            StringBuilder tokenSB = new StringBuilder();
            do {
                tokenSB.append(take());
            } while (between('a', 'z') || between('0', '9') || between('A', 'Z'));
            String token = tokenSB.toString();
            MyExpression res = VARIABLE_MAP.get(token);
            if (res != null) {
                return res;
            }
            if (token.equals("abs")) {
                return new Abs(parsePowLog());
            }
            // :NOTE: Unknown identifier
        }
        throw new ParseException("Expected argument at the position " + source.getPos());
    }

    private Const parseNumber(final String sign) {
        final StringBuilder res = new StringBuilder(sign);
        while (between('0', '9')) {
            res.append(take());
        }
        return new Const(Integer.parseInt(res.toString()));
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            take();
        }
    }

    private char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : ExpressionParser.END;
        return result;
    }

    private void expect(final char expected) throws ParseException {
        if (test(expected)) {
            take();
        } else {
            throw new ParseException("Expected " + expected + " at the position " + source.getPos());
        }
    }

    private boolean test(final char expected) {
        return ch == expected;
    }

    private boolean test(final String expected) {
        for (int i = 1; i < expected.length(); i++) {
            if (expected.charAt(i) != source.next(i - 1)) {
                return false;
            }
        }
        return test(expected.charAt(0));
    }

    private boolean take(final String expected) {
        if (!test(expected)) {
            return false;
        }
        for (int i = 0; i < expected.length(); i++) {
            take();
        }
        return true;
    }

    private boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
