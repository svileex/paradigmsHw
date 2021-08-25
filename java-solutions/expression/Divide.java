package expression;

import expression.generic.OpType;
import expression.Exceptions.*;

public class Divide<T extends Number> extends BinOperation<T> {
    public Divide(ExpressionDetail<T> left, ExpressionDetail<T> right, OpType<T> op) {
        operations = op;
        super.left = left;
        super.right = right;
    }

    @Override
    protected T implEvaluate(T x, T y) throws ParserExceptions {
        return operations.ODivide(x, y);
    }

}
