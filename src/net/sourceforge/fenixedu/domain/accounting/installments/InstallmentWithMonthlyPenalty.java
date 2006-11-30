package net.sourceforge.fenixedu.domain.accounting.installments;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class InstallmentWithMonthlyPenalty extends InstallmentWithMonthlyPenalty_Base {

    protected InstallmentWithMonthlyPenalty() {
	super();
    }

    public InstallmentWithMonthlyPenalty(final PaymentPlan paymentPlan, final Money amount,
	    final YearMonthDay startDate, final YearMonthDay endDate,
	    final BigDecimal penaltyPercentage, final YearMonthDay whenStartToApplyPenalty,
	    final Integer maxMonthsToApplyPenalty) {
	this();
	init(paymentPlan, amount, startDate, endDate, penaltyPercentage, whenStartToApplyPenalty,
		maxMonthsToApplyPenalty);
    }

    protected void init(PaymentPlan paymentPlan, Money amount, YearMonthDay startDate,
	    YearMonthDay endDate, BigDecimal penaltyPercentage, YearMonthDay whenStartToApplyPenalty,
	    Integer maxMonthsToApplyPenalty) {

	super.init(paymentPlan, amount, startDate, endDate, penaltyPercentage);
	checkParameters(whenStartToApplyPenalty, maxMonthsToApplyPenalty);

	super.setWhenStartToApplyPenalty(whenStartToApplyPenalty);
	super.setMaxMonthsToApplyPenalty(maxMonthsToApplyPenalty);

    }

    private void checkParameters(YearMonthDay whenStartToApplyPenalty, Integer maxMonthsToApplyPenalty) {
	if (whenStartToApplyPenalty == null) {
	    throw new DomainException(
		    "error.accounting.installments.InstallmentWithMonthlyPenalty.whenStartToApplyPenalty.cannot.be.null");
	}

	if (maxMonthsToApplyPenalty == null) {
	    throw new DomainException(
		    "error.accounting.installments.InstallmentWithMonthlyPenalty.maxMonthsToApplyPenalty.cannot.be.null");
	}

	if (maxMonthsToApplyPenalty <= 0) {
	    throw new DomainException(
		    "error.accounting.installments.InstallmentWithMonthlyPenalty.maxMonthsToApplyPenalty.must.be.greater.than.zero");
	}

    }

    @Override
    protected Money calculatePenaltyAmount(DateTime when, BigDecimal discountPercentage) {
	if (when.toDateMidnight().compareTo(getWhenStartToApplyPenalty().toDateMidnight()) >= 0) {
	    return new Money(calculateMonthPenalty(discountPercentage).multiply(
		    new BigDecimal(getNumberOfMonthsToChargePenalty(when))));
	} else {
	    return Money.ZERO;
	}
    }

    private BigDecimal calculateMonthPenalty(BigDecimal discountPercentage) {
	final BigDecimal amount = calculateAmountWithDiscount(discountPercentage).getAmount();
	amount.setScale(10);
	return amount.multiply(getPenaltyPercentage());
    }

    private int getNumberOfMonthsToChargePenalty(DateTime when) {
	final int numberOfMonths = (new Period(getWhenStartToApplyPenalty().toDateMidnight(), when
		.toDateMidnight()).getMonths() + 1);
	return numberOfMonths < getMaxMonthsToApplyPenalty() ? numberOfMonths
		: getMaxMonthsToApplyPenalty();
    }

}
