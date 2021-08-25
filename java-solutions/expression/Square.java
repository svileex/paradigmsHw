package expression;

import expression.generic.OpType;

public class Square<T extends Number> extends UnaryOperations<T> {

    public Square(ExpressionDetail<T> exp, OpType<T> op) {
        super(exp, op);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return operations.OMultiply(arg.evaluate(x, y, z), arg.evaluate(x, y, z));
    }
}
