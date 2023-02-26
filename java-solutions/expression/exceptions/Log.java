package expression.exceptions;

import expression.BinOp;
import expression.MyExpression;

public class Log extends BinOp {

    protected Log(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected String getOp() {
        return "//";
    }

    @Override
    protected int makeOperation(int x, int y) {
        if (y <= 1) {
            throw new CalculationException("Wrong base");
        } else if (x <= 0) {
            throw new CalculationException("Wrong argument");
        }
        int ans = 0;
        int t = 1;
        while (true) {
            int z = t * y;
            if (z / y != t || z > x) {
                break;
            }
            t = z;
            ans++;
        }
        return ans;
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        super.toMiniString(res, topPriority);
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
