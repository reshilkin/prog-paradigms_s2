package expression.generic;

public class Subtract<T> extends BinOp<T> {
    public Subtract(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        super(left, right, operator);
    }
    @Override
    protected String getOp() {
        return "-";
    }

    @Override
    protected T makeOperation(T x, T y) {
        return operator.subtract(x, y);
    }

    @Override
    protected int getLeftPriority() {
        return 20;
    }

    @Override
    protected int getRightPriority() {
        return 21;
    }

    @Override
    public int getPriority() {
        return 20;
    }
}
