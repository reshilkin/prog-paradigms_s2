package expression.exceptions;

import expression.MyExpression;
import expression.Negate;

public class CheckedNegate extends Negate {

    public CheckedNegate(MyExpression expression) {
        super(expression);
    }

    @Override
    protected int makeOperation(int x) {
        int y = -x;
        if ((y & x) < 0) {
            throw new OverflowException("Integer Overflow");
        } else {
            return y;
        }
    }
}
