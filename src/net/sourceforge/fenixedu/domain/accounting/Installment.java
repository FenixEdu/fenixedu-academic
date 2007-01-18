package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class Installment extends Installment_Base {

    public static Comparator<Installment> COMPARATOR_BY_END_DATE = new Comparator<Installment>() {
	public int compare(Installment leftInstallment, Installment rightInstallment) {
	    int comparationResult = leftInstallment.getEndDate()
		    .compareTo(rightInstallment.getEndDate());
	    return (comparationResult == 0) ? leftInstallment.getIdInternal().compareTo(
		    rightInstallment.getIdInternal()) : comparationResult;
	}
    };

    public static Comparator<Installment> COMPARATOR_BY_ORDER = new Comparator<Installment>() {
	public int compare(Installment leftInstallment, Installment rightInstallment) {
	    int comparationResult = leftInstallment.getInstallmentOrder().compareTo(
		    rightInstallment.getInstallmentOrder());
	    return (comparationResult == 0) ? leftInstallment.getIdInternal().compareTo(
		    rightInstallment.getIdInternal()) : comparationResult;
	}
    };

    protected Installment() {
	super();
	super.setOjbConcreteClass(getClass().getName());
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final PaymentPlan paymentPlan, final Money amount, YearMonthDay startDate,
	    YearMonthDay endDate) {

	checkParameters(paymentPlan, amount, startDate, endDate);

	super.setInstallmentOrder(paymentPlan.getLastInstallmentOrder() + 1);
	super.setPaymentPlan(paymentPlan);
	super.setAmount(amount);
	super.setStartDate(startDate);
	super.setEndDate(endDate);
    }

    private void checkParameters(PaymentPlan paymentPlan, Money amount, YearMonthDay startDate,
	    YearMonthDay endDate) {

	if (paymentPlan == null) {
	    throw new DomainException("error.accounting.Installment.paymentCondition.cannot.be.null");
	}

	if (amount == null) {
	    throw new DomainException("error.accounting.Installment.amount.cannot.be.null");
	}

	if (startDate == null) {
	    throw new DomainException("error.accounting.enclosing_type.startDate.cannot.be.null");
	}

	if (endDate == null) {
	    throw new DomainException("error.accounting.Installment.endDate.cannot.be.null");
	}

    }

    @Override
    public void setPaymentPlan(PaymentPlan paymentPlan) {
	throw new DomainException("error.accounting.Installment.cannot.modify.paymentPlan");
    }

    @Override
    public void setAmount(Money amount) {
	throw new DomainException("error.accounting.Installment.cannot.modify.amount");
    }

    @Override
    public void setStartDate(YearMonthDay startDate) {
	throw new DomainException("error.accounting.Installment.cannot.modify.startDate");
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
	throw new DomainException("error.accounting.Installment.cannot.modify.endDate");
    }

    @Override
    public void setWhenCreated(DateTime whenCreated) {
	throw new DomainException(
		"error.accounting.installments.InstallmentWithMonthlyPenalty.cannot.modify.whenCreated");
    }

    @Override
    public void setInstallmentOrder(Integer order) {
	throw new DomainException("error.accounting.Installment.cannot.modify.installmentOrder");
    }

    public Money calculateAmount(DateTime when, BigDecimal discountPercentage, boolean applyPenalty) {
	return calculateAmountWithDiscount(discountPercentage);
    }

    protected Money calculateAmountWithDiscount(BigDecimal discountPercentage) {
	return getAmount().multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    public int getOrder() {
	return getInstallmentOrder().intValue();
    }

    public LabelFormatter getDescription() {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel("label.installment", "application").appendLabel(" ").appendLabel(
		getInstallmentOrder().toString()).appendLabel(" (").appendLabel(
		getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT)).appendLabel(" - ")
		.appendLabel(getEndDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT)).appendLabel(")");

	return labelFormatter;

    }
}
