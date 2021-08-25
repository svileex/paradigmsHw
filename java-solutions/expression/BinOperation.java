package expression;

import expression.Exceptions.ArithmeticExpressionException;
import expression.generic.OpType;

public abstract class BinOperation<T extends Number> implements ExpressionDetail<T> {
    protected ExpressionDetail<T> left;
    protected ExpressionDetail<T> right;
    protected OpType<T> operations;

    protected abstract T implEvaluate(T x, T y) throws ArithmeticExpressionException;

    public T evaluate(T x, T y, T z) throws ArithmeticExpressionException {
        return implEvaluate(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
