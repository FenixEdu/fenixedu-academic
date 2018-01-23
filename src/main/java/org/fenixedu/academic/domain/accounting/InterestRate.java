package org.fenixedu.academic.domain.accounting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import edu.emory.mathcs.backport.java.util.Collections;
import pt.ist.fenixframework.FenixFramework;

public class InterestRate extends InterestRate_Base {

    private static final BigDecimal NUMBER_OF_DAYS_PER_YEAR = BigDecimal.valueOf(365);

    public InterestRate() {
        super();
    }

    private InterestRate(LocalDate start, LocalDate end, BigDecimal value) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setStart(start);
        setEnd(end);
        setValue(value);
    }

    public Interval getInterval() {
        return new Interval(getStart().toDateTimeAtStartOfDay(), getEnd().toDateTimeAtStartOfDay().plusDays(1));
    }

    /**
     * returns whether this interest rate applies to the specified interval
     * @param interval
     * @return
     */
    public boolean isActive(Interval interval) {
        return getNumberOfDays(interval) != 0;
    }

    /**
     * Returns the number of days that the interval overlaps in this interest rate, 0 if it does not overlaps.
     * @param interval
     * @return
     */
    public Integer getNumberOfDays(Interval interval) {
        Interval overlap = getInterval().overlap(interval);
        return overlap == null ? 0 : Days.daysBetween(overlap.getStart(), overlap.getEnd()).getDays();
    }

    /**
     * Returns the absolute tax value for the specified interval
     *  getValue() / 100 * numberOfOverlapDays / NUMBER_OF_DAYS_PER_YEAR
     * @param interval the interval to overlap with
     * @return the absolute tax it can be 0 if there is no overlap
     */
    public BigDecimal getTax(Interval interval) {
        BigDecimal absoluteTaxValue = getValue().divide(BigDecimal.valueOf(100), 5, RoundingMode.UNNECESSARY);
        BigDecimal numberOfDays = new BigDecimal(getNumberOfDays(interval));
        return absoluteTaxValue.multiply(numberOfDays).divide(NUMBER_OF_DAYS_PER_YEAR, 5, RoundingMode.UP);
    }

    /**
     * Returns the interest applied to the specified amount and interest
     * @param amount
     * @param interval
     * @return the interest amount to apply, it can be 0 if no days overlap
     */
    public Money getInterest(Money amount, Interval interval) {
        return amount.multiply(getTax(interval));
    }

    /**
     * Sums to the specified amount the calculated interest within the interval
     * @param amount the amount
     * @param interval the interval
     * @return the original value with interest, returns the same specified amount if no interest applies.
     */
    
    public Money getAmountWithInterest(Money amount, Interval interval) {
        return amount.add(getInterest(amount, interval));
    }

    private boolean matches(LocalDate start, LocalDate end, BigDecimal value) {
        return getStart().equals(start) && getEnd().equals(end) && getValue().equals(value);
    }

    public static InterestRate getOrCreate(LocalDate start, LocalDate end, BigDecimal value) {
        return Bennu.getInstance().getInterestRateSet().stream().filter(i -> i.matches(start, end, value)).findAny().orElseGet(() -> {
            try {
                return FenixFramework.atomic(() -> new InterestRate(start, end, value));
            } catch (Exception e) {
                throw new Error(e);
            }
        });
    }


    private static LocalDate getLastDueDate(Map<LocalDate, Money> entries) {
        return entries.keySet().stream().min(Comparator.reverseOrder()).get();
    }

    public static class InterestRateBean {

        public static String toString(Interval interval) {
            final String start = interval.getStart().toLocalDate().toString("yyyy-MM-dd");
            final String end = interval.getEnd().toLocalDate().toString("yyyy-MM-dd");
            return String.format("[%s,%s[", start, end);
        }

        public class PartialInterestRateBean {
            private final Interval overlap;
            private final int numberOfDays;
            private final BigDecimal rate;
            private Money result;

            public PartialInterestRateBean(Interval overlap, BigDecimal rate) {
                this.overlap = overlap;
                this.rate = rate;
                this.numberOfDays = Days.daysBetween(overlap.getStart(), overlap.getEnd()).getDays();
                this.result = calculateResult();
            }

            private Money calculateResult() {
                BigDecimal absoluteTaxValue = rate.divide(BigDecimal.valueOf(100), 5, RoundingMode.UNNECESSARY);
                BigDecimal partial = absoluteTaxValue.multiply(BigDecimal.valueOf(this.numberOfDays)).divide(NUMBER_OF_DAYS_PER_YEAR, 5, RoundingMode.UP);
                return amount.multiply(partial);
            }

            public BigDecimal getRate() {
                return rate;
            }

            public Money getResult() {
                return result;
            }

            @Override
            public String toString() {
                return String.format("%s (%s)/365 x %s %% x %s = %s", InterestRateBean.toString(overlap), numberOfDays, rate.toPlainString(), amount.toPlainString(), result.toPlainString());
            }
        }

        private Money amount;
        private Interval interval;
        private List<PartialInterestRateBean> partials;

        public InterestRateBean(LocalDate start, LocalDate end, Money amount) {
            this.amount = amount;
            this.interval = new Interval(start.plusDays(1).toDateTimeAtStartOfDay(), end.plusDays(1).toDateTimeAtStartOfDay());
            this.partials = new ArrayList<>();
        }

        public Interval getInterval() {
            return interval;
        }
        
        public Money getInterest() {
            return partials.stream().map(PartialInterestRateBean::getResult).reduce(Money.ZERO, Money::add);
        }

        public void addPartial(Interval overlap, BigDecimal rate) {
            partials.add(new PartialInterestRateBean(overlap, rate));
        }

        public List<PartialInterestRateBean> getPartials() {
            return Collections.singletonList(partials);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            Money interest = getInterest();
            builder.append(String.format("%s - %s + %s = %s%n", InterestRateBean.toString(interval), amount.toPlainString(), interest.toPlainString(), amount.add(interest).toPlainString()));
            partials.stream().sorted(Comparator.comparing(p -> p.overlap.getStart())).forEach(p -> {
                builder.append(String.format("\t%s%n", p.toString()));
            });

            return builder.toString();
        }
    }

    public static Optional<InterestRateBean> getInterestBean(LocalDate start, LocalDate end, Money amount) {

        if (end.isAfter(start)) {
            InterestRateBean bean = new InterestRateBean(start, end, amount);
            Interval dueInterval = bean.getInterval();
            for (InterestRate interestRate : Bennu.getInstance().getInterestRateSet()) {
                if (interestRate.isActive(dueInterval)) {
                    Interval overlap = interestRate.getInterval().overlap(dueInterval);
                    bean.addPartial(overlap, interestRate.getValue());
                }
            }
            return Optional.of(bean);
        }

        return Optional.empty();
    }

    public static Money getInterest(LocalDate dueDate, Money amount, LocalDate when) {
        return getInterestBean(dueDate, when, amount).map(InterestRateBean::getInterest).orElse(Money.ZERO);
    }


    @Override
    public String toString() {
        return "InterestRate{" + "start=" + getStart() + ", end=" + getEnd() + ", value=" + getValue() + ", externalId='" + getExternalId() + '\'' + "} " + super.toString();
    }
}
