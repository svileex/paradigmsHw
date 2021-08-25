package expression.Exceptions;

public class DivisionByZeroException extends ArithmeticExpressionException {
    public DivisionByZeroException(String message) {
        super(message);
    }
}
