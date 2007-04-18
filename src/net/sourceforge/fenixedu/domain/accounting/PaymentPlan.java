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
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.InstallmentAccountingTransaction;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public abstract class PaymentPlan extends PaymentPlan_Base {

    static {
	ServiceAgreementTemplatePaymentPlan
		.addListener(new RelationAdapter<PaymentPlan, ServiceAgreementTemplate>() {
		    @Override
		    public void beforeAdd(PaymentPlan paymentPlanToAdd,
			    ServiceAgreementTemplate serviceAgreementTemplate) {

			if (paymentPlanToAdd != null) {
			    if (paymentPlanToAdd.isDefault()
				    && serviceAgreementTemplate.hasDefaultPaymentPlan(paymentPlanToAdd
					    .getExecutionYear())) {
				throw new DomainException(
					"error.domain.accounting.ServiceAgreementTemplate.already.has.a.default.payment.plan.for.execution.year");
			    }

			}
		    }
		});
    }

    protected PaymentPlan() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setOjbConcreteClass(getClass().getName());
	super.setWhenCreated(new DateTime());
    }

    protected void init(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {

	checkParameters(executionYear, serviceAgreementTemplate, defaultPlan);

	super.setDefaultPlan(defaultPlan);
	super.setExecutionYear(executionYear);
	super.setServiceAgreementTemplate(serviceAgreementTemplate);
    }

    private void checkParameters(final ExecutionYear executionYear,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Boolean defaultPlan) {
	if (executionYear == null) {
	    throw new DomainException("error.accounting.PaymentPlan.executionYear.cannot.be.null");
	}

	if (serviceAgreementTemplate == null) {
	    throw new DomainException(
		    "error.accounting.PaymentPlan.serviceAgreementTemplate.cannot.be.null");
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
    public void setServiceAgreementTemplate(ServiceAgreementTemplate serviceAgreementTemplate) {
	throw new DomainException(
		"error.accounting.PaymentCondition.cannot.modify.serviceAgreementTemplate");
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
	return (getInstallmentsCount() == 0) ? null : Collections.max(getInstallmentsSet(),
		Installment.COMPARATOR_BY_ORDER);
    }

    public Installment getFirstInstallment() {
	return (getInstallmentsCount() == 0) ? null : Collections.min(getInstallmentsSet(),
		Installment.COMPARATOR_BY_ORDER);
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

    public Map<Installment, Money> calculateInstallmentRemainingAmounts(final Event event,
	    final DateTime when, final BigDecimal discountPercentage,
	    final Set<Installment> installmentsWithoutPenalty) {

	final Map<Installment, Money> result = new HashMap<Installment, Money>();
	Money totalPayedAmount = event.getPayedAmount();
	Money remainingAmountAfterPayed = Money.ZERO;
	Installment lastInstallmentToBePayed = null;

	for (final Installment installment : getInstallmentsSortedByEndDate()) {

	    final InstallmentAccountingTransaction accountingTransaction = event
		    .getAccountingTransactionFor(installment);
	    final Money installmentAmount = calculateInstallmentAmount(when, discountPercentage,
		    installment, accountingTransaction, !installmentsWithoutPenalty
			    .contains(installment));
	    final Money amountToDiscount = calculateTransportAmountToDiscount(totalPayedAmount,
		    installmentAmount);
	    final Money installmentFinalAmount = installmentAmount.subtract(amountToDiscount);

	    if (accountingTransaction == null) {
		result.put(installment, installmentFinalAmount);
		lastInstallmentToBePayed = installment;

	    } else if (installmentFinalAmount.isPositive()) {
		remainingAmountAfterPayed = remainingAmountAfterPayed.add(installmentFinalAmount);
	    }

	    totalPayedAmount = totalPayedAmount.subtract(amountToDiscount);

	}

	if (lastInstallmentToBePayed != null && remainingAmountAfterPayed.isPositive()) {
	    result.put(lastInstallmentToBePayed, result.get(lastInstallmentToBePayed).add(
		    remainingAmountAfterPayed));
	}

	return result;

    }

    private Money calculateInstallmentAmount(final DateTime when, final BigDecimal discountPercentage,
	    final Installment installment,
	    final InstallmentAccountingTransaction installmentAccountingTransaction,
	    final boolean applyPenalty) {
	final DateTime whenToCalculate = (installmentAccountingTransaction != null) ? installmentAccountingTransaction
		.getWhenRegistered()
		: when;
	return installment.calculateAmount(whenToCalculate, discountPercentage, applyPenalty);
    }

    private Money calculateTransportAmountToDiscount(Money installmentTransportAmount,
	    final Money installmentAmount) {
	return installmentTransportAmount.lessOrEqualThan(installmentAmount) ? installmentTransportAmount
		: installmentAmount;
    }

    public Money calculateRemainingAmountFor(final Installment installment, final Event event,
	    final DateTime when, final BigDecimal discountPercentage,
	    final Set<Installment> installmentsWithoutPenalty) {

	final Map<Installment, Money> amountsByInstallment = calculateInstallmentRemainingAmounts(event,
		when, discountPercentage, installmentsWithoutPenalty);
	final Money installmentAmount = amountsByInstallment.get(installment);

	return (installmentAmount != null) ? installmentAmount : Money.ZERO;
    }

    public boolean isInstallmentInDebt(final Installment installment, final Event event,
	    final DateTime when, final BigDecimal discountPercentage,
	    final Set<Installment> installmentsWithoutPenalty) {

	return calculateRemainingAmountFor(installment, event, when, discountPercentage,
		installmentsWithoutPenalty).isPositive();

    }

    public Installment getInstallmentByOrder(int order) {
	for (final Installment installment : getInstallments()) {
	    if (installment.getInstallmentOrder() == order) {
		return installment;
	    }
	}

	return null;

    }

}