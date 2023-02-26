package expression.generic;

import expression.exceptions.DBZException;

public class UncheckedLongOperator implements Operator<Long> {
    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long subtract(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long divide(Long x, Long y) {
        try {
            return x / y;
        } catch (ArithmeticException e) {
            throw new DBZException("Division by zero");
        }
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long negate(Long x) {
        return -x;
    }

    @Override
    public Long parseString(String s) {
        return Long.parseLong(s);
    }

    @Override
    public Long castFromInt(int i) {
        return (long) i;
    }

    @Override
    public Long min(Long x, Long y) {
        return Math.min(x, y);
    }

    @Override
    public Long max(Long x, Long y) {
        return Math.max(x, y);
    }

    @Override
    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }
}
