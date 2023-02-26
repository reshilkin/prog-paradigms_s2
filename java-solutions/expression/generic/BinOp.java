package expression.generic;

public abstract class BinOp<T> implements MyExpression<T> {
    protected final MyExpression<T> left;
    protected final MyExpression<T> right;
    protected final Operator<T> operator;

    protected BinOp(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        toString(res);
        return res.toString();
    }

    public void toString(StringBuilder res) {
        res.append('(');
        left.toString(res);
        res.append(' ').append(this.getOp()).append(' ');
        right.toString(res);
        res.append(')');
    }

    protected abstract String getOp();

    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res, -1);
        return res.toString();
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        if (topPriority > getPriority()) {
            res.append('(');
        }
        left.toMiniString(res, getLeftPriority());
        res.append(" ").append(getOp()).append(" ");
        right.toMiniString(res, getRightPriority());
        if (topPriority > getPriority()) {
            res.append(')');
        }
    }

    protected abstract int getLeftPriority();

    protected abstract int getRightPriority();

    protected abstract T makeOperation(T x, T y);

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
        return this.makeOperation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return this.makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }
}
