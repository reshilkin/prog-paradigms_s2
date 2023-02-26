package expression;

public class ArithmeticShiftRight extends BinOp {
    public ArithmeticShiftRight(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x >>> y;
    }

    @Override
    protected int getLeftPriority() {
        return 10;
    }

    @Override
    protected int getRightPriority() {
        return 11;
    }

    @Override
    protected String getOp() {
        return ">>>";
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
