package expression.exceptions;

import expression.*;

public class CheckedAdd extends Add {

    public CheckedAdd(MyExpression left, MyExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int x, int y) {
        int z = x + y;
        if (((x ^ z) & (y ^ z)) < 0) {
            throw new OverflowException("Integer overflow");
        } else {
            return z;
        }
    }
}
