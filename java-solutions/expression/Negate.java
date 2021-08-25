package expression;

import expression.generic.OpType;
import expression.Exceptions.*;

public class Negate<T extends Number> extends UnaryOperations<T> {

    public Negate(ExpressionDetail<T> exp, OpType<T> op) {
        super(exp, op);
    }

    @Override
    public T evaluate(T x, T y, T z) throws ParserExceptions {
        return operations.ONegate(arg.evaluate(x, y, z));
    }
}
