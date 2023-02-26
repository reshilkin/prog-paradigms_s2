package expression;

public class Negate extends UnOp {

    public Negate(MyExpression expression) {
        super(expression);
    }

    @Override
    protected int makeOperation(int x) {
        return -x;
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
