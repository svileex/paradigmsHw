package expression.generic;

import java.math.BigInteger;
import expression.Exceptions.*;

public class OpBigInteger implements OpType<BigInteger> {

    @Override
    public BigInteger OAdd(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    @Override
    public BigInteger OSubtract(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    @Override
    public BigInteger ODivide(BigInteger left, BigInteger right) throws ParserExceptions {
        if (right.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("division by zero in bi");
        }
        return left.divide(right);
    }

    @Override
    public BigInteger ONegate(BigInteger left) {
        return left.negate();
    }

    @Override
    public BigInteger OMultiply(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    @Override
    public BigInteger parseString(String a) {
        return new BigInteger(a);
    }

    @Override
    public BigInteger OGetAbs(BigInteger arg) {
        return arg.abs();
    }

    @Override
    public BigInteger OGetMod(BigInteger left, BigInteger right) {
        try {
            return left.mod(right);
        } catch (ArithmeticException e) {
            throw new DivisionByZeroException("mod division by zero");
        }
    }
}
