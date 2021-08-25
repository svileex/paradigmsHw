package expression;

public class Const<T extends Number> implements ExpressionDetail<T> {
    private final T ourConst;

    public Const(T in) {
        ourConst = in;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return ourConst;
    }
}
