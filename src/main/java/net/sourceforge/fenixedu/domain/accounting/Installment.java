package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.Comparator;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class Installment extends Installment_Base {

    public static Comparator<Installment> COMPARATOR_BY_END_DATE = new Comparator<Installment>() {
        @Override
        public int compare(Installment leftInstallment, Installment rightInstallment) {
            int comparationResult = leftInstallment.getEndDate().compareTo(rightInstallment.getEndDate());
            return (comparationResult == 0) ? leftInstallment.getExternalId().compareTo(rightInstallment.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<Installment> COMPARATOR_BY_ORDER = new Comparator<Installment>() {
        @Override
        public int compare(Installment leftInstallment, Installment rightInstallment) {
            int comparationResult = leftInstallment.getInstallmentOrder().compareTo(rightInstallment.getInstallmentOrder());
            return (comparationResult == 0) ? leftInstallment.getExternalId().compareTo(rightInstallment.getExternalId()) : comparationResult;
        }
    };

    protected Installment() {
        super();
        super.setRootDomainObject(RootDomainObject.getInstance());
        super.setWhenCreated(new DateTime());
    }

    public Installment(final PaymentPlan paymentPlan, final Money amount, YearMonthDay startDate, YearMonthDay endDate) {
        this();
        init(paymentPlan, amount, startDate, endDate);
    }

    protected void init(final PaymentPlan paymentPlan, final Money amount, YearMonthDay startDate, YearMonthDay endDate) {

        checkParameters(paymentPlan, amount, startDate, endDate);

        super.setInstallmentOrder(paymentPlan.getLastInstallmentOrder() + 1);
        super.setPaymentPlan(paymentPlan);
        super.setAmount(amount);
        super.setStartDate(startDate);
        super.setEndDate(endDate);
    }

    protected void checkParameters(PaymentPlan paymentPlan, Money amount, YearMonthDay startDate, YearMonthDay endDate) {

        if (paymentPlan == null) {
            throw new DomainException("error.accounting.Installment.paymentCondition.cannot.be.null");
        }

        checkParameters(amount, startDate, endDate);
    }

    private void checkParameters(Money amount, YearMonthDay startDate, YearMonthDay endDate) {
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
        throw new DomainException("error.accounting.installments.InstallmentWithMonthlyPenalty.cannot.modify.whenCreated");
    }

    @Override
    public void setInstallmentOrder(Integer order) {
        throw new DomainException("error.accounting.Installment.cannot.modify.installmentOrder");
    }

    public Money calculateAmount(Event event, DateTime when, BigDecimal discountPercentage, boolean applyPenalty) {
        return calculateAmountWithDiscount(event, discountPercentage);
    }

    protected Money calculateAmountWithDiscount(Event event, BigDecimal discountPercentage) {
        return calculateBaseAmount(event).multiply(BigDecimal.ONE.subtract(discountPercentage));
    }

    protected Money calculateBaseAmount(Event event) {
        return getAmount();
    }

    public int getOrder() {
        return getInstallmentOrder().intValue();
    }

    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel("application", "label.Installment.description", getInstallmentOrder().toString(),
                getStartDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT),
                getEndDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT));

        return labelFormatter;

    }

    public boolean isWithMonthlyPenalty() {
        return false;
    }

    public boolean isForFirstTimeStudents() {
        return false;
    }

    public void delete() {
        super.setPaymentPlan(null);
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public LocalDate getEndDate(final Event event) {
        return super.getEndDate().toLocalDate();
    }

    public void edit(final InstallmentBean bean) {
        Money amount = bean.getAmount();
        YearMonthDay startDate = bean.getStartDate();
        YearMonthDay endDate = bean.getEndDate();

        checkParameters(amount, startDate, endDate);

        super.setStartDate(startDate);
        super.setEndDate(endDate);
    }

}
