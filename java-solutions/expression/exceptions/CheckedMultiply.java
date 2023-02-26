package expression.exceptions;

import expression.Multiply;
import expression.MyExpression;

public class CheckedMultiply extends Multiply {

    public CheckedMultiply(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int x, int y) {
        int z = x * y;
        if (x != 0 && z / x != y || y != 0 && z / y != x) {
            throw new OverflowException("Integer overflow");
        } else {
            return z;
        }
    }
}
