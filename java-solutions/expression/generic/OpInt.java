package expression.generic;

import expression.Exceptions.*;

public class OpInt implements OpType<Integer> {

    private final boolean checked;

    public OpInt(boolean ch) {
        checked = ch;
    }

    @Override
    public Integer OAdd(Integer left, Integer right) throws ParserExceptions {
        if (checked && ((right > 0 && left > Integer.MAX_VALUE - right) || (right < 0 && left < Integer.MIN_VALUE - right))) {
            throw new OverflowException("overflow in add");
        }
        return left + right;
    }

    @Override
    public Integer OSubtract(Integer left, Integer right) {
        if (checked && ((right < 0 && left > Integer.MAX_VALUE + right) || (right > 0  && ((left < 0 && left < Integer.MIN_VALUE + right))))) {
            System.out.println(left + " " + right + " " + (1 - Integer.MAX_VALUE));
            throw new OverflowException("overflow in subtract");
        }
        return left - right;
    }

    @Override
    public Integer ODivide(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero");
        } else {
            if (checked && (left == Integer.MIN_VALUE && right == -1)) {
                throw new OverflowException("overflow in divide");
            }
            return left / right;
        }
    }

    @Override
    public Integer OMultiply(Integer left, Integer right) {
        if (
            checked &&
            ((right > 0 && left > Integer.MAX_VALUE / right) || (right < 0 && left < Integer.MAX_VALUE / right)
            || (right > 0 && left < -Integer.MAX_VALUE / right) || (right < 0 && left > -Integer.MAX_VALUE  / right))
        ) {
            System.out.println(left + "  " + right + " " + Integer.MIN_VALUE);
            throw new OverflowException("overflow in multiply");
        }
        return left * right;
    }

    @Override
    public Integer ONegate(Integer left) {
        if (checked && left == Integer.MIN_VALUE) {
            throw new OverflowException("overflow in negate");
        }
        return -left;
    }

    @Override
    public Integer parseString(String a) {
        return Integer.parseInt(a);
    }

    @Override
    public Integer OGetAbs(Integer arg) {
        if (arg >= 0) {
            return arg;
        }
        return -arg;
    }

    @Override
    public Integer OGetMod(Integer left, Integer right) {
        if (right == 0) {
            throw new DivisionByZeroException("division by zero in mod");
        }
        return left % right;
    }
}
