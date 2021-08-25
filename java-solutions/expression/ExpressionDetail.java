package expression;

public interface ExpressionDetail<T extends Number> {
    T evaluate(T x, T y, T z);
}
