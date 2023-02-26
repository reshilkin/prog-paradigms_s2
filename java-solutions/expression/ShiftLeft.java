package expression;

public class ShiftLeft extends BinOp {

    public ShiftLeft(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "<<";
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x << y;
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
    public int getPriority() {
        return 10;
    }
}
