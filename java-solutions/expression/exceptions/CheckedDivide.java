package expression.exceptions;

import expression.Divide;
import expression.MyExpression;

public class CheckedDivide extends Divide {

    public CheckedDivide(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int x, int y) {
        if (y == 0) {
            throw new DBZException("Division by zero");
        } else {
            int z = x / y;
            if (z != 0 && (z ^ x ^ y) < 0) {
                throw new OverflowException("Integer overflow");
            } else {
                return z;
            }
        }
    }
}
