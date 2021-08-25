package expression.generic;

public interface OpType<T extends Number> {
    T OAdd(T left, T right);
    T OSubtract(T left, T right);
    T ODivide(T left, T right);
    T ONegate(T left);
    T OMultiply(T left, T right);
    T parseString(String in);
    T OGetAbs(T arg);
    T OGetMod(T left, T right);
}
