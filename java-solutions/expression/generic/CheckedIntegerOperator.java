package expression.generic;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;

public class CheckedIntegerOperator extends IntegerOperator {
    @Override
    public Integer add(Integer x, Integer y) {
        if (x >= 0 && y >= 0 && Integer.MAX_VALUE - x < y || x <= 0 && y <= 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException("Overflow");
        }
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        int z = x - y;
        if (((x ^ z) & (x ^ y)) < 0) {
            throw new OverflowException("Overflow");
        }
        return x - y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new DBZException("Division by zero");
        }
        if(x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("Overflow");
        }
        return x / y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        int z = x * y;
        if(x != 0 && z / x != y || y != 0 && z / y != x) {
            throw new OverflowException("Overflow");
        }
        return x * y;
    }

    @Override
    public Integer negate(Integer x) {
        if(x == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow");
        }
        return -x;
    }
}
