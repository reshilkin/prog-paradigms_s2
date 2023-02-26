package expression.generic;

public interface Operator<T>{
    T add(T x, T y);

    T subtract(T x, T y);

    T divide(T x, T y);

    T multiply(T x, T y);

    T negate(T x);

    T parseString(String s);

    T castFromInt(int i);

    T min(T x, T y);

    T max(T x, T y);

    T count(T x);
}
