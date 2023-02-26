package expression.generic;

public abstract class UnOp<T> implements MyExpression<T> {
    protected final MyExpression<T> expression;
    protected final Operator<T> operator;

    public UnOp(MyExpression<T> expression, Operator<T> operator) {
        this.expression = expression;
        this.operator = operator;
    }

    protected abstract T makeOperation(T x);

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
        return makeOperation(expression.evaluate(x));
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        toString(res);
        return res.toString();
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return makeOperation(expression.evaluate(x, y, z));
    }

    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res, getPriority());
        return res.toString();
    }
}
