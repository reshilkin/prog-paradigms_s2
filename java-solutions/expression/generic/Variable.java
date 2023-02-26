package expression.generic;

import java.util.InputMismatchException;

public class Variable<T> implements MyExpression<T> {
    private final String name;
    private final Operator<T> operator;

    public Variable(final String name, Operator<T> operator) {
        this.name = name;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    @Override
    public T evaluate(int x) {
        return evaluate(operator.castFromInt(x));
    }

    @Override
    public T evaluate(int x, int y, int z) {
        return evaluate(operator.castFromInt(x), operator.castFromInt(y), operator.castFromInt(z));
    }

    @Override
    public T evaluate(T x) {
        return x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
        throw new InputMismatchException("Wrong Variable name. Please, use \"x\",\"y\" or \"z\"");
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void toString(StringBuilder res) {
        res.append(name);
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public void toMiniString(StringBuilder res, int topPriority) {
        res.append(name);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
