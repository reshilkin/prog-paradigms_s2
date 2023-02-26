package expression.generic;

public abstract class IntegerOperator implements Operator<Integer> {

    @Override
    public Integer parseString(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public Integer castFromInt(int i) {
        return i;
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return Math.min(x, y);
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return Math.max(x, y);
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }
}
