package expression;

public class Subtract extends BinOp {
    public Subtract(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "-";
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x - y;
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
