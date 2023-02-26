package expression;

public class Add extends BinOp {
    public Add(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "+";
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x + y;
    }

    @Override
    protected int getLeftPriority() {
        return 20;
    }

    @Override
    protected int getRightPriority() {
        return 20;
    }

    @Override
    public int getPriority() {
        return 20;
    }
}
