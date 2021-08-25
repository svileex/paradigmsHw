package expression.generic;

import expression.ExpressionDetail;
import expression.parser.ExpressionParser;
import expression.Exceptions.*;

import java.util.Map;

public class GenericTabulator implements expression.generic.Tabulator {

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {

        Map<String, OpType<?>> type = Map.of(
                "i", new OpInt(true),
                "d", new OpDouble(),
                "bi", new OpBigInteger(),
                "u", new OpInt(false),
                "l", new OpLong(),
                "s", new OpShort()
        );

        return getM(type.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] getM(OpType<T> operations, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        ExpressionParser<T> expParser = new ExpressionParser<>(expression, operations);
        ExpressionDetail<T> parsedExp = expParser.parse();
        Object[][][] answ = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];

        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        answ[i][j][k] = parsedExp.evaluate(
                                operations.parseString(Integer.toString(x1 + i)),
                                operations.parseString(Integer.toString(y1 + j)),
                                operations.parseString(Integer.toString(z1 + k))
                        );
                    } catch (ParserExceptions e) {
                        answ[i][j][k] = null;
                    }
                }
            }
        }

        return answ;
    }
}
