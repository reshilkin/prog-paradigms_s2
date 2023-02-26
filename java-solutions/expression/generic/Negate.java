package expression.generic;

public class Negate<T> extends UnOp<T> {

    public Negate(MyExpression<T> expression, Operator<T> operator) {
        super(expression, operator);
    }

    @Override
    protected T makeOperation(T x) {
        return operator.negate(x);
    }

    @Override
    public void toString(StringBuilder res) {
        res.append("-(");
        expression.toString(res);
        res.append(")");
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        res.append("-");
        if (getPriority() <= expression.getPriority()) {
            res.append(' ');
        }
        expression.toMiniString(res, getPriority());
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
