package expression.generic;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final static Map<String, Operator<? extends Number>> OPERATORS = Map.of(    //
            "i", new CheckedIntegerOperator(),                                      //
            "d", new DoubleOperator(),                                              //
            "bi", new BigIntegerOperator(),                                         //
            "u", new UncheckedIntegerOperator(),                                    //
            "l", new UncheckedLongOperator(),                                       //
            "t", new TruncateOperator()                                             //
    );

    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        MyExpression<? extends Number> result = new ExpressionParser<>(OPERATORS.get(mode)).parse(expression);
        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i <= x2 - x1; i++) {
            for (int j = 0; j <= y2 - y1; j++) {
                for (int k = 0; k <= z2 - z1; k++) {
                    try {
                        ans[i][j][k] = result.evaluate(x1 + i, y1 + j, z1 + k);
                    } catch (ArithmeticException e) {
                        //System.out.println(e.getMessage());
                    }
                }
            }
        }
        return ans;
    }
}
