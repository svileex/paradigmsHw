package expression;

import expression.generic.OpType;

public class Abs<T extends Number> extends UnaryOperations<T> {

    public Abs(ExpressionDetail<T> exp, OpType<T> op) {
        super(exp, op);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return operations.OGetAbs(arg.evaluate(x, y, z));
    }
}
