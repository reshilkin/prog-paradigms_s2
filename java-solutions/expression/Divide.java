package expression;

public class Divide extends BinOp {
    public Divide(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "/";
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x / y;
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
