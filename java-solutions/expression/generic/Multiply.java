package expression.generic;

public class Multiply<T> extends BinOp<T> {

    public Multiply(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        super(left, right, operator);
    }
    @Override
    protected String getOp() {
        return "*";
    }

    @Override
    protected T makeOperation(T x, T y) {
        return operator.multiply(x, y);
    }

    @Override
    protected int getLeftPriority() {
        return 49;
    }

    @Override
    protected int getRightPriority() {
        return 50;
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
