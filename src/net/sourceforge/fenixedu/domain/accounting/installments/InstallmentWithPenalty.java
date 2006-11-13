package net.sourceforge.fenixedu.domain.accounting.installments;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class InstallmentWithPenalty extends InstallmentWithPenalty_Base {

    protected InstallmentWithPenalty() {
	super();
    }

    protected void init(final PaymentPlan paymentCondition, final Money amount,
	    YearMonthDay startDate, YearMonthDay endDate, final BigDecimal penaltyPercentage) {

	super.init(paymentCondition, amount, startDate, endDate);
	checkParameters(penaltyPercentage);

	super.setPenaltyPercentage(penaltyPercentage);

    }

    private void checkParameters(BigDecimal penaltyPercentage) {
	if (penaltyPercentage == null) {
	    throw new DomainException(
		    "error.accounting.installments.InstallmentWithPenalty.penaltyPercentage.cannot.be.null");
	}
    }

    @Override
    public Money calculateAmount(DateTime when, BigDecimal discountPercentage) {
	return super.calculateAmount(when, discountPercentage).add(
		calculatePenaltyAmount(when, discountPercentage));
    }

    abstract protected Money calculatePenaltyAmount(DateTime when, BigDecimal discountPercentage);
}
