package net.sourceforge.fenixedu.domain.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public abstract class PaymentPlan extends PaymentPlan_Base {

    protected PaymentPlan() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final ExecutionYear executionYear, final Boolean defaultPlan) {

	checkParameters(executionYear, defaultPlan);

	super.setDefaultPlan(defaultPlan);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(final ExecutionYear executionYear, final Boolean defaultPlan) {
	if (executionYear == null) {
	    throw new DomainException("error.accounting.PaymentPlan.executionYear.cannot.be.null");
	}

	if (defaultPlan == null) {
	    throw new DomainException("error.accounting.PaymentPlan.defaultPlan.cannot.be.null");
	}

    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException("error.accounting.PaymentCondition.cannot.modify.executionYear");
    }

    @Override
    public void setDefaultPlan(Boolean defaultPlan) {
	throw new DomainException("error.domain.accounting.PaymentPlan.cannot.modify.defaultPlan");
    }

    public List<Installment> getInstallmentsSortedByEndDate() {
	final List<Installment> result = new ArrayList<Installment>(getInstallmentsSet());
	Collections.sort(result, Installment.COMPARATOR_BY_END_DATE);

	return result;
    }

    public Installment getLastInstallment() {
	return (getInstallmentsCount() == 0) ? null : Collections.max(getInstallmentsSet(), Installment.COMPARATOR_BY_ORDER);
    }

    public Installment getFirstInstallment() {
	return (getInstallmentsCount() == 0) ? null : Collections.min(getInstallmentsSet(), Installment.COMPARATOR_BY_ORDER);
    }

    public int getLastInstallmentOrder() {
	final Installment installment = getLastInstallment();
	return installment == null ? 0 : installment.getOrder();
    }

    @Override
    public void addInstallments(Installment installment) {
	throw new DomainException("error.accounting.PaymentPlan.cannot.add.installment");
    }

    @Override
    public List<Installment> getInstallments() {
	return Collections.unmodifiableList(super.getInstallments());
    }

    @Override
    public Set<Installment> getInstallmentsSet() {
	return Collections.unmodifiableSet(super.getInstallmentsSet());
    }

    @Override
    public Iterator<Installment> getInstallmentsIterator() {
	return getInstallmentsSet().iterator();
    }

    @Override
    public void removeInstallments(Installment installment) {
	throw new DomainException("error.accounting.PaymentPlan.cannot.remove.installment");
    }

    public boolean isDefault() {
	return getDefaultPlan().booleanValue();
    }

    public Money calculateOriginalTotalAmount() {
	Money result = Money.ZERO;
	for (final Installment installment : getInstallmentsSet()) {
	    result = result.add(installment.getAmount());
	}

	return result;
    }

    public Money calculateTotalAmount(final Event event, final DateTime when, final BigDecimal discountPercentage) {

	Money result = Money.ZERO;
	for (final Money amount : calculateInstallmentTotalAmounts(event, when, discountPercentage).values()) {
	    result = result.add(amount);
	}

	return result;
    }

    private Map<Installment, Money> calculateInstallmentTotalAmounts(final Event event, final DateTime when,
	    final BigDecimal discountPercentage) {

	final Map<Installment, Money> result = new HashMap<Installment, Money>();
	final List<AccountingTransaction> transactions = event.getSortedNonAdjustingTransactions();

	for (final Installment installment : getInstallmentsSortedByEndDate()) {
	    final Iterator<AccountingTransaction> iterator = transactions.iterator();
	    boolean installmentPayed = false;
	    Money payedAmount = Money.ZERO;
	    while (iterator.hasNext()) {
		final AccountingTransaction transaction = iterator.next();
		iterator.remove();
		final Money installmentAmount = installment.calculateAmount(transaction.getWhenRegistered(), discountPercentage,
			isToApplyPenalty(event, installment));
		payedAmount = payedAmount.add(transaction.getAmountWithAdjustment());
		if (payedAmount.greaterOrEqualThan(installmentAmount)) {
		    installmentPayed = true;
		    result.put(installment, installmentAmount);
		    break;
		}
	    }

	    if (!installmentPayed) {
		final Money amountToPay = installment.calculateAmount(when, discountPercentage, isToApplyPenalty(event,
			installment));

		result.put(installment, amountToPay);

	    }
	}

	return result;

    }

    public Map<Installment, Money> calculateInstallmentRemainingAmounts(final Event event, final DateTime when,
	    final BigDecimal discountPercentage) {

	final Map<Installment, Money> result = new HashMap<Installment, Money>();
	final List<AccountingTransaction> transactions = event.getSortedNonAdjustingTransactions();

	Money payedAmount = Money.ZERO;
	for (final Installment installment : getInstallmentsSortedByEndDate()) {
	    final Iterator<AccountingTransaction> iterator = transactions.iterator();
	    boolean installmentPayed = false;
	    while (iterator.hasNext()) {
		final AccountingTransaction transaction = iterator.next();
		iterator.remove();
		final Money installmentAmount = installment.calculateAmount(transaction.getWhenRegistered(), discountPercentage,
			isToApplyPenalty(event, installment));
		payedAmount = payedAmount.add(transaction.getAmountWithAdjustment());
		if (payedAmount.greaterOrEqualThan(installmentAmount)) {
		    payedAmount = payedAmount.subtract(installmentAmount);
		    installmentPayed = true;
		    break;
		}
	    }

	    if (!installmentPayed) {
		final Money amountToPay = installment.calculateAmount(when, discountPercentage, isToApplyPenalty(event,
			installment));

		result.put(installment, amountToPay.subtract(payedAmount));

		payedAmount = Money.ZERO;
	    }
	}

	return result;

    }

    public Money calculateRemainingAmountFor(final Installment installment, final Event event, final DateTime when,
	    final BigDecimal discountPercentage) {

	final Map<Installment, Money> amountsByInstallment = calculateInstallmentRemainingAmounts(event, when, discountPercentage);
	final Money installmentAmount = amountsByInstallment.get(installment);

	return (installmentAmount != null) ? installmentAmount : Money.ZERO;
    }

    public boolean isInstallmentInDebt(final Installment installment, final Event event, final DateTime when,
	    final BigDecimal discountPercentage) {

	return calculateRemainingAmountFor(installment, event, when, discountPercentage).isPositive();

    }

    public Installment getInstallmentByOrder(int order) {
	for (final Installment installment : getInstallments()) {
	    if (installment.getInstallmentOrder() == order) {
		return installment;
	    }
	}

	return null;
    }

    protected boolean isToApplyPenalty(final Event event, final Installment installment) {
	return true;
    }

    protected void removeParameters() {
	super.setExecutionYear(null);
    }

    public boolean isGratuityPaymentPlan() {
	return false;
    }

    public boolean isCustomGratuityPaymentPlan() {
	return false;
    }

    public boolean isAppliableFor(final StudentCurricularPlan studentCurricularPlan, final ExecutionYear executionYear) {
	return false;
    }

    public boolean isFor(final ExecutionYear executionYear) {
	return hasExecutionYear() && getExecutionYear().equals(executionYear);
    }

    abstract public ServiceAgreementTemplate getServiceAgreementTemplate();

}