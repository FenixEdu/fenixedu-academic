package org.fenixedu.academic.domain.accounting;

import static org.fenixedu.academic.domain.InterestRateLog.createLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

import org.fenixedu.academic.domain.accounting.calculator.InterestRateBean;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;

public class InterestRate extends InterestRate_Base {

    public InterestRate() {
        super();
    }

    private InterestRate(LocalDate start, LocalDate end, BigDecimal value) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setStart(start);
        setEnd(end);
        setValue(value);
        createLog(Bundle.MESSAGING, "log.interestRate.created", start.toString(), end.toString(), value.toString());
    }

    public void edit(LocalDate start, LocalDate end, BigDecimal value) {
        createLog(Bundle.MESSAGING, "log.interestRate.edited", getStart().toString(), start.toString(), getEnd().toString(), end.toString(), getValue().toString(), value.toString());
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
        return overlap == null ? 0 : Days.daysBetween(overlap.getStart().toLocalDate(), overlap.getEnd().toLocalDate()).getDays();
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
        return absoluteTaxValue.multiply(numberOfDays).divide(InterestRateBean.NUMBER_OF_DAYS_PER_YEAR, 5, RoundingMode.UP);
    }

    /**
     * Returns the interest applied to the specified amount and interest
     * @param amount
     * @param interval
     * @return the interest amount to apply, it can be 0 if no days overlap
     */
    public BigDecimal getInterest(BigDecimal amount, Interval interval) {
        return amount.multiply(getTax(interval));
    }

    /**
     * Sums to the specified amount the calculated interest within the interval
     * @param amount the amount
     * @param interval the interval
     * @return the original value with interest, returns the same specified amount if no interest applies.
     */

    public BigDecimal getAmountWithInterest(BigDecimal amount, Interval interval) {
        return amount.add(getInterest(amount, interval));
    }

    private boolean matches(LocalDate start, LocalDate end, BigDecimal value) {
        return getStart().equals(start) && getEnd().equals(end) && getValue().equals(value);
    }

    private static boolean isValidRange(LocalDate start, LocalDate end) {
        return start.isBefore(end);
    }

    private static boolean isValidValue(BigDecimal value) {
        return value.compareTo(BigDecimal.valueOf(0)) >= 0 && value.compareTo(BigDecimal.valueOf(100)) <= 0;
    }

    private boolean overlap(LocalDate start, LocalDate end) {
        return !(getStart().isAfter(end) || getEnd().isBefore(start));
    }

    public static InterestRate getOrCreate(LocalDate start, LocalDate end, BigDecimal value) throws FenixActionException {
        if(isValidRange(start, end)) {
            if (isValidValue(value)) {
                return Bennu.getInstance().getInterestRateSet().stream().filter(i -> i.overlap(start, end)).findAny().orElseGet(() -> {
                    try {
                        return FenixFramework.atomic(() -> new InterestRate(start, end, value));
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                });
            }
            else {
                throw new InvalidValueException(value);
            }
        }
        else {
            throw new InvalidDateRangeException(start, end);
        }
    }

    public void editIfValid(LocalDate start, LocalDate end, BigDecimal value) throws FenixActionException {
        if (isValidRange(start, end)) {
            if (isValidValue(value)) {
                if (Bennu.getInstance().getInterestRateSet().stream().noneMatch(i -> !i.equals(this) && i.overlap(start, end)))
                    FenixFramework.atomic(() -> {
                        edit(start, end, value);
                    });
            }
            else {
                throw new InvalidValueException(value);
            }
        }
        else {
            throw new InvalidDateRangeException(start, end);
        }
    }

    private static LocalDate getLastDueDate(Map<LocalDate, BigDecimal> entries) {
        return entries.keySet().stream().min(Comparator.reverseOrder()).get();
    }

    public static Optional<InterestRateBean> getInterestBean(LocalDate start, LocalDate end, BigDecimal amount) {

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

    public static BigDecimal getInterest(LocalDate dueDate, LocalDate when, BigDecimal amount) {
        return getInterestBean(dueDate, when, amount).map(InterestRateBean::getInterest).orElse(BigDecimal.ZERO);
    }


    @Override
    public String toString() {
        return "InterestRate{" + "start=" + getStart() + ", end=" + getEnd() + ", value=" + getValue() + ", externalId='" + getExternalId() + '\'' + "} " + super.toString();
    }

    public static class InvalidDateRangeException extends FenixActionException {
        public InvalidDateRangeException(LocalDate start, LocalDate end) {
            super("error.endDateMustBeGreaterThanStartDate", start, end);
        }
    }

    public static class InvalidValueException extends FenixActionException {
        public InvalidValueException(BigDecimal value) {
            super("error.valueMustBeBetween0And100", value);
        }
    }

}

