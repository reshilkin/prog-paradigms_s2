package expression.generic;

import expression.exceptions.DBZException;

public class TruncateOperator implements Operator<Integer> {
    @Override
    public Integer add(Integer x, Integer y) {
        return truncate(x + y);
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return truncate(x - y);
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        try {
            return truncate(x / y);
        } catch (ArithmeticException e) {
            throw new DBZException("Division by zero");
        }
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return truncate(x * y);
    }

    @Override
    public Integer negate(Integer x) {
        return truncate(-x);
    }

    @Override
    public Integer parseString(String s) {
        return truncate(Integer.parseInt(s));
    }

    @Override
    public Integer castFromInt(int i) {
        return truncate(i);
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return truncate(Math.min(x, y));
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return truncate(Math.max(x, y));
    }

    @Override
    public Integer count(Integer x) {
        return truncate(Integer.bitCount(x));
    }

    private Integer truncate(Integer x) {
        return x / 10 * 10;
    }
}
