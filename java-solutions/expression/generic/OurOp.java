package expression.generic;

public abstract class OurOp<T extends Number> implements OpType<T> {

    @Override
    public abstract T OAdd(T left, T right);

    @Override
    public abstract T OSubtract(T left, T right);

    @Override
    public abstract T ODivide(T left, T right);

    @Override
    public abstract T ONegate(T left);

    @Override
    public abstract T OMultiply(T left, T right);

    public abstract T parseString(String a);
}
