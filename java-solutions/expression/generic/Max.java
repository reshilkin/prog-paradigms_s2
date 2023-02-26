package expression.generic;

public class Max<T> extends BinOp<T> {
    protected Max(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        super(left, right, operator);
    }

    @Override
    protected String getOp() {
        return "max";
    }

    @Override
    protected int getLeftPriority() {
        return 10;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    protected int getRightPriority() {
        return 10;
    }

    @Override
    protected T makeOperation(T x, T y) {
        return operator.max(x, y);
    }
}