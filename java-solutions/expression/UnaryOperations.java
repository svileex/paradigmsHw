package expression;

import expression.generic.OpType;

public abstract class UnaryOperations<T extends Number> implements ExpressionDetail<T> {
    protected ExpressionDetail<T> arg;
    protected OpType<T> operations;

    protected UnaryOperations(ExpressionDetail<T> exp, OpType<T> op) {
        arg = exp;
        operations = op;
    }

    public abstract T evaluate(T x, T y, T z);
}
