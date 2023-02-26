package expression.generic;

public class Min<T> extends BinOp<T> {
    protected Min(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        super(left, right, operator);
    }

    @Override
    protected String getOp() {
        return "min";
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
        return operator.min(x, y);
    }
}
