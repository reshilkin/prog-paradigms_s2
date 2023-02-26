package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    char next(int a);
}
