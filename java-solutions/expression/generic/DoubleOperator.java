package expression.generic;

public class DoubleOperator implements Operator<Double>{
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double subtract(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double negate(Double x) {
        return -x;
    }

    @Override
    public Double parseString(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public Double castFromInt(int i) {
        return (double)i;
    }

    @Override
    public Double min(Double x, Double y) {
        return Math.min(x, y);
    }

    @Override
    public Double max(Double x, Double y) {
        return Math.max(x, y);
    }

    @Override
    public Double count(Double x) {
        return (double) Long.bitCount(Double.doubleToLongBits(x));
    }
}
