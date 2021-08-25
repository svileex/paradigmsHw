package expression.generic;

public class OpDouble implements OpType<Double> {

    @Override
    public Double OAdd(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double OSubtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double ODivide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double ONegate(Double left) {
        return -left;
    }

    @Override
    public Double OMultiply(Double left, Double right) {
        return left * right;
    }

    public Double parseString(String a) {
        return Double.parseDouble(a);
    }

    @Override
    public Double OGetAbs(Double arg) {
        if (arg >= 0) {
            return arg;
        } else {
            return -arg;
        }
    }

    @Override
    public Double OGetMod(Double left, Double right) {
        return left % right;
    }
}
