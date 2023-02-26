package expression.generic;

public class Divide<T> extends BinOp<T> {
    public Divide(MyExpression<T> left, MyExpression<T> right, Operator<T> operator) {
        super(left, right, operator);
    }
    @Override
    protected String getOp() {
        return "/";
    }

    @Override
    protected T makeOperation(T x, T y) {
        return operator.divide(x, y);
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        if (topPriority >= getPriority()) {
            res.append('(');
        }
        left.toMiniString(res, getLeftPriority());
        res.append(" ").append(getOp()).append(" ");
        right.toMiniString(res, getRightPriority());
        if (topPriority >= getPriority()) {
            res.append(')');
        }
    }

    @Override
    protected int getLeftPriority() {
        return 49;
    }

    @Override
    protected int getRightPriority() {
        return 51;
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
