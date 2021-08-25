package expression.generic;

import expression.Exceptions.*;

public class OpShort implements OpType<Short> {
    @Override
    public Short OAdd(Short left, Short right) throws ParserExceptions {
        return (short) (left + right);
    }

    @Override
    public Short OSubtract(Short left, Short right) {
        return (short) (left - right);
    }

    @Override
    public Short ODivide(Short left, Short right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero");
        } else {
            return (short) (left / right);
        }
    }

    @Override
    public Short OMultiply(Short left, Short right) {
        return (short) (left * right);
    }

    @Override
    public Short ONegate(Short left) {
        return (short) -left;
    }

    @Override
    public Short parseString(String a) {
        return (short) Integer.parseInt(a);
    }

    @Override
    public Short OGetAbs(Short arg) {
        if (arg >= 0) {
            return arg;
        }
        return (short) -arg;
    }

    @Override
    public Short OGetMod(Short left, Short right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero in mod");
        }
        return (short) (left % right);
    }
}
