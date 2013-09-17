package net.sourceforge.fenixedu.domain.accounting.installments;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class InstallmentWithPenalty extends InstallmentWithPenalty_Base {

    protected InstallmentWithPenalty() {
        super();
    }

    protected void init(final PaymentPlan paymentCondition, final Money amount, YearMonthDay startDate, YearMonthDay endDate,
            final BigDecimal penaltyPercentage) {

        super.init(paymentCondition, amount, startDate, endDate);
        checkParameters(penaltyPercentage);

        super.setPenaltyPercentage(penaltyPercentage);

    }

    private void checkParameters(BigDecimal penaltyPercentage) {
        if (penaltyPercentage == null) {
            throw new DomainException("error.accounting.installments.InstallmentWithPenalty.penaltyPercentage.cannot.be.null");
        }
    }

    @Override
    public Money calculateAmount(Event event, DateTime when, BigDecimal discountPercentage, boolean applyPenalty) {
        return super.calculateAmount(event, when, discountPercentage, applyPenalty).add(
                (applyPenalty ? calculatePenaltyAmount(event, when, discountPercentage) : Money.ZERO));
    }

    abstract protected Money calculatePenaltyAmount(Event event, DateTime when, BigDecimal discountPercentage);

    @Deprecated
    public boolean hasPenaltyPercentage() {
        return getPenaltyPercentage() != null;
    }

}
