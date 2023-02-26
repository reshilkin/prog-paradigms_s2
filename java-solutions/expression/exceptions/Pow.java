package expression.exceptions;

import expression.BinOp;
import expression.MyExpression;

public class Pow extends BinOp {

    protected Pow(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "**";
    }

    private int pow(int p, int q) {
        if (q == 1) {
            return p;
        } else if (q == 0) {
            return 1;
        }
        if (q % 2 == 0) {
            int x = pow(p, q / 2);
            int t = x * x;
            if (x != 0 && t / x != x) {
                throw new OverflowException("Integer overflow");
            }
            return t;
        } else {
            int x = pow(p, q - 1);
            int t = x * p;
            if (x != 0 && t / x != p || p != 0 && t / p != x) {
                throw new OverflowException("Integer overflow");
            }
            return t;
        }
    }

    @Override
    protected int makeOperation(int x, int y) {
        if (x == 0 && y == 0) {
            throw new CalculationException("0 ^ 0");
        } else if (y < 0) {
            throw new CalculationException("Negative pow");
        }
        return pow(x, y);
    }

    @Override
    protected int getLeftPriority() {
        return 59;
    }

    @Override
    protected int getRightPriority() {
        return 60;
    }

    @Override
    public int getPriority() {
        return 59;
    }
}
