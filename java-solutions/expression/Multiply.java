package expression;

import expression.generic.OpType;
import expression.Exceptions.*;

public class Multiply<T extends Number> extends BinOperation<T> {
    public Multiply(ExpressionDetail<T> left, ExpressionDetail<T> right, OpType<T> op) {
        super.operations = op;
        super.left = left;
        super.right = right;
    }

    @Override
    protected T implEvaluate(T x, T y) throws ParserExceptions {
        return operations.OMultiply(x, y);
    }

}
