package org.fenixedu.academic.domain.accounting.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class InterestRateBean {

    public static final BigDecimal NUMBER_OF_DAYS_PER_YEAR = BigDecimal.valueOf(365);

    public static String toString(Interval interval) {
        final String start = interval.getStart().toLocalDate().toString("yyyy-MM-dd");
        final String end = interval.getEnd().toLocalDate().minusDays(1).toString("yyyy-MM-dd");
        return String.format("[%s,%s]", start, end);
    }

    public class PartialInterestRateBean {
        private final Interval overlap;
        private final int numberOfDays;
        private final BigDecimal rate;
        private BigDecimal result;

        public PartialInterestRateBean(Interval overlap, BigDecimal rate) {
            this.overlap = overlap;
            this.rate = rate;
            this.numberOfDays = Days.daysBetween(overlap.getStart().toLocalDate(), overlap.getEnd().toLocalDate()).getDays();
            this.result = calculateResult();
        }

        private BigDecimal calculateResult() {
            BigDecimal absoluteTaxValue = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.UNNECESSARY);
            BigDecimal partial = absoluteTaxValue.multiply(BigDecimal.valueOf(this.numberOfDays))
                    .divide(NUMBER_OF_DAYS_PER_YEAR, 5, RoundingMode.UP);
            return amount.multiply(partial);
        }

        public BigDecimal getRate() {
            return rate;
        }

        public BigDecimal getResult() {
            return result;
        }

        @Override
        public String toString() {
            return String.format("%s (%s) / %s x %s %% x %s = %s", InterestRateBean.toString(overlap), numberOfDays,
                    NUMBER_OF_DAYS_PER_YEAR, rate.toPlainString(), amount.toPlainString(), result.toPlainString());
        }
    }

    private BigDecimal amount;
    private Interval interval;
    private List<PartialInterestRateBean> partials;

    public InterestRateBean(LocalDate start, LocalDate end, BigDecimal amount) {
        this.amount = amount;
        this.interval = new Interval(start.plusDays(1).toDateTimeAtStartOfDay(), end.plusDays(1).toDateTimeAtStartOfDay());
        this.partials = new ArrayList<>();
    }

    public Interval getInterval() {
        return interval;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterest() {
        return partials.stream().map(PartialInterestRateBean::getResult).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addPartial(Interval overlap, BigDecimal rate) {
        addPartial(new PartialInterestRateBean(overlap, rate));
    }

    public void addPartial(PartialInterestRateBean bean) {
        this.partials.add(bean);
    }

    public List<PartialInterestRateBean> getPartials() {
        return new ArrayList<>(this.partials);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        BigDecimal interest = getInterest();
        builder.append(String.format("%s - %s + %s = %s%n", InterestRateBean.toString(interval), amount.toPlainString(), interest.toPlainString(), amount.add(interest).toPlainString()));
        partials.stream().sorted(Comparator.comparing(p -> p.overlap.getEnd())).forEach(p -> {
            builder.append(String.format("\t%s%n", p.toString()));
        });

        return builder.toString();
    }
}
