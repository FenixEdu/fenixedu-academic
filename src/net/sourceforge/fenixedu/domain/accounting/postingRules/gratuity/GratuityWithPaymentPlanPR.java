package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryWithInstallmentDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.InstallmentAccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEventWithPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class GratuityWithPaymentPlanPR extends GratuityWithPaymentPlanPR_Base {

    public GratuityWithPaymentPlanPR() {
	super();
    }

    public GratuityWithPaymentPlanPR(EntryType entryType, EventType eventType, DateTime startDate,
	    DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {

	final BigDecimal discountPercentage = applyDiscount ? getDiscountPercentage(event)
		: BigDecimal.ZERO;
	Money result = Money.ZERO;
	for (final Installment installment : getPaymentPlan(event).getInstallmentsSortedByEndDate()) {
	    final InstallmentAccountingTransaction installmentAccountingTransaction = event
		    .getAccountingTransactionFor(installment);
	    final DateTime whenToCalculate = (installmentAccountingTransaction != null) ? installmentAccountingTransaction
		    .getWhenRegistered()
		    : when;
	    result = result.add(installment.calculateAmount(whenToCalculate, discountPercentage,
		    isToApplyPenaltyFor(event, installment)));
	}

	return result;
    }

    private boolean isToApplyPenaltyFor(final Event event, final Installment installment) {
	return !((GratuityEventWithPaymentPlan) event).hasPenaltyExemptionFor(installment);

    }

    private BigDecimal getDiscountPercentage(final Event event) {
	final GratuityEvent gratuityEvent = (GratuityEvent) event;
	return gratuityEvent.hasGratuityExemption() ? gratuityEvent.getGratuityExemption()
		.calculateDiscountPercentage(getPaymentPlan(event).calculateOriginalTotalAmount())
		: BigDecimal.ZERO;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	final List<EntryDTO> result = new ArrayList<EntryDTO>();
	final Map<Installment, Money> amountsByInstallment = getPaymentPlan(event)
		.calculateInstallmentRemainingAmounts(event, when, getDiscountPercentage(event),
			getInstallmentsWithoutPenalty(event));
	Money totalAmount = Money.ZERO;

	for (final Installment installment : getPaymentPlan(event).getInstallmentsSortedByEndDate()) {
	    final Money installmentAmount = amountsByInstallment.get(installment);

	    if (installmentAmount == null || !installmentAmount.isPositive()) {
		continue;
	    }

	    result.add(new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, event, installmentAmount,
		    event.getDescriptionForEntryType(getEntryType()), installment));

	    totalAmount = totalAmount.add(installmentAmount);
	}

	if (needsTotalAmountEntry(getPaymentPlan(event), result)) {
	    final Money amountToPay = event.calculateAmountToPay(when);
	    result.add(new EntryDTO(EntryType.GRATUITY_FEE, event, totalAmount, event.getPayedAmount(),
		    amountToPay, event.getDescriptionForEntryType(getEntryType()), amountToPay));
	}

	return result;
    }

    private Set<Installment> getInstallmentsWithoutPenalty(Event event) {
	return ((GratuityEventWithPaymentPlan) event).getInstallmentsWithoutPenalty();
    }

    private boolean needsTotalAmountEntry(final PaymentPlan paymentPlan, List<EntryDTO> result) {
	return paymentPlan.getInstallmentsCount() == result.size();
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    Event event, Account fromAccount, Account toAccount,
	    AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.GratuityWithPaymentConditionPR.invalid.number.of.entryDTOs",
		    event.getDescriptionForEntryType(getEntryType()));
	}

	final EntryDTO entryDTO = entryDTOs.get(0);
	final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan = (GratuityEventWithPaymentPlan) event;

	final AccountingTransaction accountingTransaction;
	if (entryDTO instanceof EntryWithInstallmentDTO) {
	    accountingTransaction = internalProcessInstallment(user, fromAccount, toAccount, entryDTO,
		    gratuityEventWithPaymentPlan, transactionDetail);
	} else {
	    accountingTransaction = internalProcessTotal(user, fromAccount, toAccount, entryDTO,
		    gratuityEventWithPaymentPlan, transactionDetail);
	}

	return Collections.singleton(accountingTransaction);

    }

    private AccountingTransaction internalProcessTotal(User user, Account fromAccount,
	    Account toAccount, EntryDTO entryDTO, GratuityEventWithPaymentPlan event,
	    AccountingTransactionDetailDTO transactionDetail) {

	event.changeGratuityTotalPaymentCodeState(event.getPaymentCodeStateFor(transactionDetail
		.getPaymentMode()));
	checkIfCanAddAmount(entryDTO, transactionDetail.getWhenRegistered(), event);

	return super.makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(),
		entryDTO.getAmountToPay(), transactionDetail);

    }

    private AccountingTransaction internalProcessInstallment(User user, Account fromAccount,
	    Account toAccount, EntryDTO entryDTO, GratuityEventWithPaymentPlan event,
	    AccountingTransactionDetailDTO transactionDetail) {

	final EntryWithInstallmentDTO entryWithInstallmentDTO = (EntryWithInstallmentDTO) entryDTO;

	checkIfCanAddAmountForInstallment(entryWithInstallmentDTO,
		transactionDetail.getWhenRegistered(), event);
	event.changeInstallmentPaymentCodeState(entryWithInstallmentDTO.getInstallment(), event
		.getPaymentCodeStateFor(transactionDetail.getPaymentMode()));

	return makeAccountingTransactionForInstallment(user, event, fromAccount, toAccount,
		getEntryType(), entryDTO.getAmountToPay(), (entryWithInstallmentDTO).getInstallment(),
		transactionDetail);

    }

    private void checkIfCanAddAmount(EntryDTO entryDTO, DateTime whenRegistered, Event event) {
	if (entryDTO.getAmountToPay().compareTo(event.calculateAmountToPay(whenRegistered)) < 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.amount.to.pay.must.match.value",
		    event.getDescriptionForEntryType(getEntryType()));
	}
    }

    private void checkIfCanAddAmountForInstallment(EntryWithInstallmentDTO entryDTO,
	    DateTime whenRegistered, Event event) {
	final Money installmentAmount = getPaymentPlan(event).calculateRemainingAmountFor(
		entryDTO.getInstallment(), event, whenRegistered, getDiscountPercentage(event),
		getInstallmentsWithoutPenalty(event));
	if (entryDTO.getAmountToPay().compareTo(installmentAmount) < 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.amount.to.pay.must.match.value",
		    event.getDescriptionForEntryType(getEntryType()));
	}

    }

    private PaymentPlan getPaymentPlan(Event event) {
	return ((GratuityEventWithPaymentPlan) event).getGratuityPaymentPlan();
    }

    @Override
    public DegreeCurricularPlanServiceAgreementTemplate getServiceAgreementTemplate() {
	return (DegreeCurricularPlanServiceAgreementTemplate) super.getServiceAgreementTemplate();
    }

    protected AccountingTransaction makeAccountingTransactionForInstallment(User responsibleUser,
	    Event event, Account from, Account to, EntryType entryType, Money amount,
	    Installment installment, AccountingTransactionDetailDTO transactionDetail) {
	return new InstallmentAccountingTransaction(responsibleUser, event, makeEntry(entryType, amount
		.negate(), from), makeEntry(entryType, amount, to), installment,
		makeAccountingTransactionDetail(transactionDetail));
    }

    @Override
    public boolean isOtherPartiesPaymentsSupported() {
	return true;
    }

}
