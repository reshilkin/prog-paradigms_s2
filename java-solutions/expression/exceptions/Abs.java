package expression.exceptions;

import expression.MyExpression;
import expression.UnOp;

public class Abs extends UnOp {

    public Abs(MyExpression expression) {
        super(expression);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        res.append("abs");
        if (getPriority() <= expression.getPriority()) {
            res.append(' ');
        }
        expression.toMiniString(res, getPriority());
    }

    @Override
    public void toString(StringBuilder res) {
        res.append("abs(").append(expression.toString()).append(")");
    }

    @Override
    protected int makeOperation(int x) {
        if (x >= 0) {
            return x;
        } else {
            int y = -x;
            if ((y & x) < 0) {
                throw new OverflowException("Integer overflow");
            } else {
                return y;
            }
        }
    }
}
