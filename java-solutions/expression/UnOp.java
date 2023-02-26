package expression;

public abstract class UnOp implements MyExpression {
    protected final MyExpression expression;

    protected abstract int makeOperation(int x);

    @Override
    public int evaluate(int x) {
        return makeOperation(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(expression.evaluate(x, y, z));
    }

    public UnOp(MyExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        toString(res);
        return res.toString();
    }

    @Override
    public String toMiniString() {
        StringBuilder res = new StringBuilder();
        toMiniString(res, getPriority());
        return res.toString();
    }
}
