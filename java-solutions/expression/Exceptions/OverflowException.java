package expression.Exceptions;

public class OverflowException extends ArithmeticExpressionException {
    //private final int leftOperand;
    //private final int rightOperand;

    public OverflowException(String message) {
        super(message);
    }
}
