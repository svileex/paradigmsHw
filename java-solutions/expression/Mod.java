package expression;

import expression.Exceptions.ArithmeticExpressionException;
import expression.generic.OpType;

public class Mod<T extends Number> extends BinOperation<T> {
    public Mod(ExpressionDetail<T> left, ExpressionDetail<T> right, OpType<T> op) {
        super.operations = op;
        super.left = left;
        super.right = right;
    }

    @Override
    protected T implEvaluate(T x, T y) throws ArithmeticExpressionException {
        return operations.OGetMod(x, y);
    }
}
