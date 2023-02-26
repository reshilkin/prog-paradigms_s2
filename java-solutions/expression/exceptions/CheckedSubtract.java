package expression.exceptions;

import expression.MyExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {

    public CheckedSubtract(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int x, int y) {
        int z = x - y;
        if (((x ^ z) & (x ^ y)) < 0) {
            throw new OverflowException("Integer overflow");
        } else {
            return z;
        }
    }
}
