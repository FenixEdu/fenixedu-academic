package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class BigDecimalUtil {

    public static BinaryOperator<BigDecimal> reducer = (a,b) -> (a.setScale(2, RoundingMode.HALF_EVEN).add(b.setScale(2, RoundingMode.HALF_EVEN)));

    public static <R extends Object> BigDecimal sum(Stream<R> stream, Function<R, BigDecimal> mapper) {
        return sum(stream.map(mapper));
    }

    public static <$ extends Object> BigDecimal sum(Stream<BigDecimal> stream) {
        return stream.reduce(BigDecimal.ZERO, reducer);
    }

    public static boolean isPositive(BigDecimal bigDecimal){
        return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
    }
}
