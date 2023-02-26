package expression;

public class Multiply extends BinOp {

    public Multiply(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "*";
    }

    @Override
    protected int makeOperation(int x, int y) {
        return x * y;
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
