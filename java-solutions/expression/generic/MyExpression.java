package expression.generic;

public interface MyExpression<T> {
    int getPriority();

    void toMiniString(StringBuilder res, int topPriority);

    void toString(StringBuilder res);

    @Override
    String toString();

    String toMiniString();

    T evaluate(T x);

    T evaluate(int x);

    T evaluate(T x, T y, T z);

    T evaluate(int x, int y, int z);
}
