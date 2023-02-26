package expression.generic;

import expression.exceptions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpressionParser<T> extends BaseParser {
    private final static Set<String> VARIABLES = Set.of("x", "y", "z");
    private final Operator<T> operator;
    private final Map<String, Variable<T>> variableMap;
    private final Map<Integer, Map<String, Constructor<T>>> binaryOperations;

    public ExpressionParser(Operator<T> operator) {
        this.operator = operator;
        variableMap = new HashMap<>();
        for (String s : VARIABLES) {
            variableMap.put(s, new Variable<>(s, operator));
        }
        binaryOperations = Map.of(                                              //
                0, Map.of("min", Min::new, "max", Max::new),        //
                1, Map.of("*", Multiply::new, "/", Divide::new),    //
                2, Map.of("+", Add::new, "-", Subtract::new)        //
        );
    }

    public MyExpression<T> parse(final String expression) throws ParseException {
        source = new StringSource(expression);
        take();
        final MyExpression<T> ans = recParse(0);
        skipWhitespace();
        if (ch != 0) {
            throw new ParseException("Unknown operation at the position " + source.getPos());
        }
        return ans;
    }

    private MyExpression<T> recParse(int level) throws ParseException {
        if (level == binaryOperations.size()) {
            return parseToken();
        } else {
            MyExpression<T> first = recParse(level + 1);
            Constructor<T> constructor = binaryOperations.get(level).get(checkToken());
            if (constructor == null) {
                return first;
            } else {
                takeToken();
                return constructor.build(first, recParse(level + 1), operator);
            }
        }
    }

    private MyExpression<T> parseToken() throws ParseException {
        skipWhitespace();
        if (take("(")) {
            final MyExpression<T> temp = recParse(0);
            expect(')');
            return temp;
        } else if (take("-")) {
            if (between('0', '9')) {
                return parseNumber("-");
            } else {
                return new Negate<>(parseToken(), operator);
            }
        } else if (between('0', '9')) {
            return parseNumber("+");
        }
        MyExpression<T> res = variableMap.get(checkToken());
        if (res != null) {
            takeToken();
            return res;
        }
        if (takeToken("count")) {
            return new Count<>(parseToken(), operator);
        }
        throw new ParseException("Expected argument at the position " + source.getPos());
    }

    private Const<T> parseNumber(final String sign) {
        StringBuilder num = new StringBuilder(sign);
        while (between('0', '9')) {
            num.append(take());
        }
        return new Const<>(operator.parseString(num.toString()), operator);
    }
}
