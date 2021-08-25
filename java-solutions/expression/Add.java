package expression;

import expression.generic.OpType;
import expression.Exceptions.*;

public class Add<T extends Number> extends BinOperation<T> {

    public Add(ExpressionDetail<T> left, ExpressionDetail<T> right, OpType<T> op) {
        operations = op;
        super.left = left;
        super.right = right;
    }

    @Override
    public T implEvaluate(T x, T y) throws ParserExceptions {
        return super.operations.OAdd(x, y);
    }
}
