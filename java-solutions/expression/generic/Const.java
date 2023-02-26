package expression.generic;

public class Const<T> implements MyExpression<T> {
    private final T value;
    private final Operator<T> operator;

    public Const(T value, Operator<T> operator) {
        this.value = value;
        this.operator = operator;
    }

    public T getValue() {
        return value;
    }

    @Override
    public T evaluate(int x) {
        return evaluate(operator.castFromInt(x));
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return evaluate(operator.castFromInt(x), operator.castFromInt(y), operator.castFromInt(z));
    }

    @Override
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
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
    public int getPriority() {
        return 60;
    }
}
