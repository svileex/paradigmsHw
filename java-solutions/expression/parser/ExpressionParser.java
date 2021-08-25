package expression.parser;

import expression.*;
import expression.generic.*;
import expression.Exceptions.*;

public class ExpressionParser<T extends Number> extends BaseParser {
    private final OpType<T> Operations;

    public ExpressionParser(String a, OpType<T> op) {
        super(new StringSource(a));
        Operations = op;
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    private void skipWhitespace() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
            // skip
        }
    }

    public ExpressionDetail<T> parse() {
        skipWhitespace();
        return parseAddSub();
    }

    private ExpressionDetail<T> parseAddSub() {
        ExpressionDetail<T> left = parseMult();
        while (true) {
            skipWhitespace();
            if (test('+')) {
                ExpressionDetail<T> right = parseMult();
                left = new Add<>(left, right, Operations);
            } else if (test('-')) {
                ExpressionDetail<T> right = parseMult();
                left = new Subtract<>(left, right, Operations);
            } else {
                return left;
            }
        }
    }

    private ExpressionDetail<T> parseMult() {
        ExpressionDetail<T> left = parseValue();
        while (true) {
            skipWhitespace();
            if (test('*')) {
                ExpressionDetail<T> right = parseValue();
                left = new Multiply<>(left, right, Operations);
            } else if (test('/')) {
                ExpressionDetail<T> right = parseValue();
                left = new Divide<>(left, right, Operations);
            } else if (test('m') && test('o') && test('d')) {
                ExpressionDetail<T> right = parseValue();
                left = new Mod<>(left, right, Operations);
            } else {
                return left;
            }
        }
    }

    //map with op
    private ExpressionDetail<T> parseValue() {
        skipWhitespace();
        if (test('(')) {
            ExpressionDetail<T> a = parse();
            if (test(')')) {
                return a;
            } else {
                throw new ParserExceptions("something wrong in expression");
            }
        } else if ('0' <= ch && ch <= '9') {
            return new Const<>(getConst(false));
        } else if (ch == 'x' || ch == 'y' || ch == 'z') {
            char temp = ch;
            nextChar();
            return new Variable<>(Character.toString(temp));
        } else if (test('-')) {
            skipWhitespace();
            if ('0' <= ch && ch <= '9') {
                return new Const<>(getConst(true));
            }
            return new Negate<>(parseValue(), Operations);
        } else if (test('a') && test('b') && test('s')) {
            return new Abs<>(parseValue(), Operations);
        } else if (test('s') && test('q') && test('u') && test('a') && test('r') && test('e')) {
            return new Square<>(parseValue(), Operations);
        } else {
            throw new ParserExceptions("something wrong in expression");
        }
    }

    private T getConst(boolean flag) {
        StringBuilder res = new StringBuilder();
        if (flag) {
            res.append("-");
        }
        while ('0' <= ch && ch <= '9') {
            res.append(ch);
            nextChar();
        }
        String resToString = res.toString();
        return Operations.parseString(resToString);
    }
}