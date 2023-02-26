package expression.generic;

import expression.exceptions.DBZException;

public class UncheckedIntegerOperator extends IntegerOperator {
    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        try {
            return x / y;
        } catch (ArithmeticException e) {
            throw new DBZException("Division by zero");
        }
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer negate(Integer x) {
        return -x;
    }
}
