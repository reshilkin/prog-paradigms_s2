package expression;

import java.util.InputMismatchException;

public class Variable implements MyExpression {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (name.equals("x")) {
            return x;
        } else if (name.equals("y")) {
            return y;
        } else if (name.equals("z")) {
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
    public boolean equals(Object object) {
        if (object instanceof Variable) {
            return ((Variable) object).getName().equals(name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
