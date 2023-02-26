package expression.generic;

public class Count<T> extends UnOp<T>{
    public Count(MyExpression<T> expression, Operator<T> operator) {
        super(expression, operator);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    protected T makeOperation(T x) {
        return operator.count(x);
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        res.append("count ");
        expression.toMiniString(res, getPriority());
    }

    @Override
    public void toString(StringBuilder res) {
        res.append("count(");
        expression.toString(res);
        res.append(")");
    }
}
