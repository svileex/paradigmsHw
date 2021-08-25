package expression;

import expression.generic.OpType;
import expression.Exceptions.*;

public class Subtract<T extends Number> extends BinOperation<T> {
    public Subtract(ExpressionDetail<T> left, ExpressionDetail<T> right, OpType<T> op) {
        operations = op;
        super.left = left;
        super.right = right;
    }

    @Override
    protected T implEvaluate(T x, T y) throws ParserExceptions {
        return operations.OSubtract(x, y);
    }
}
