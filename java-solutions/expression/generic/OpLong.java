package expression.generic;

import expression.Exceptions.*;

public class OpLong implements OpType<Long> {
    
    @Override
    public Long OAdd(Long left, Long right) throws ParserExceptions {
        return left + right;
    }

    @Override
    public Long OSubtract(Long left, Long right) {
        return left - right;
    }

    @Override
    public Long ODivide(Long left, Long right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero");
        } else {
            return left / right;
        }
    }

    @Override
    public Long OMultiply(Long left, Long right) {
        return left * right;
    }

    @Override
    public Long ONegate(Long left) {
        return -left;
    }

    @Override
    public Long parseString(String a) {
        return Long.parseLong(a);
    }

    @Override
    public Long OGetAbs(Long arg) {
        if (arg >= 0) {
            return arg;
        }
        return -arg;
    }

    @Override
    public Long OGetMod(Long left, Long right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero in mod");
        }
        return left % right;
    }
}
