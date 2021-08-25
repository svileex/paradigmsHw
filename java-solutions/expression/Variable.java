package expression;

import expression.Exceptions.ParserExceptions;

public class Variable<T extends Number> implements ExpressionDetail<T> {
    private final String name;

    public Variable(String in) {
        name = in;
    }

    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x" :
              return x;
            case "y" :
                return y;
            case "z" :
                return z;
            default:
                throw new ParserExceptions("wrong variable name");
        }
    }
}
