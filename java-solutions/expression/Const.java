package expression;

public class Const implements MyExpression {
    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public int evaluate(int x) {
        return (int) value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (int) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public void toString(StringBuilder res) {
        res.append(value.toString());
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        res.append(value.toString());
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Const) {
            return ((Const) object).getValue().equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
