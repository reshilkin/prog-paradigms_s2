package expression.generic;

@FunctionalInterface
public interface Constructor<T> {
    BinOp<T> build(MyExpression<T> left, MyExpression<T> right, Operator<T> operator);
}
