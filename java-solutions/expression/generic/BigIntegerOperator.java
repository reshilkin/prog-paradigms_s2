package expression.generic;

import expression.exceptions.DBZException;

import java.math.BigInteger;

public class BigIntegerOperator implements Operator<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        try {
            return x.divide(y);
        } catch (ArithmeticException e) {
            throw new DBZException("Division by zero");
        }
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger parseString(String s) {
        return BigInteger.valueOf(Long.parseLong(s));
    }

    @Override
    public BigInteger castFromInt(int i) {
        return BigInteger.valueOf(i);
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        return x.min(y);
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        return x.max(y);
    }

    @Override
    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf(x.bitCount());
    }
}
