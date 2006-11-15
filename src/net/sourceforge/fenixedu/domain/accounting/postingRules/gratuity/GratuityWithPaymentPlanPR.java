package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryWithInstallmentDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.PaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.accountingTransactions.InstallmentAccountingTransaction;
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
    public Money calculateTotalAmountToPay(Event event, DateTime when) {

	Money result = Money.ZERO;
	for (final Installment installment : getPaymentPlan(event).getInstallmentsSortedByEndDate()) {
	    final InstallmentAccountingTransaction installmentAccountingTransaction = event
		    .getAccountingTransactionFor(installment);
	    final DateTime whenToCalculate = (installmentAccountingTransaction != null) ? installmentAccountingTransaction
		    .getWhenRegistered()
		    : when;
	    result = result.add(installment.calculateAmount(whenToCalculate, getDiscountPercentage()));
	}

	return result;
    }

    private BigDecimal getDiscountPercentage() {
	// TODO: add logic to read exemptions
	return BigDecimal.ZERO;
    }

    @Override
    // TODO: correct method to use discount percentage
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {

	final List<EntryDTO> result = new ArrayList<EntryDTO>();

	Money totalAmount = Money.ZERO;

	for (final Installment installment : getPaymentPlan(event).getInstallmentsSortedByEndDate()) {
	    if (!event.hasAccountingTransactionFor(installment)) {
		final Money amount = installment.calculateAmount(when);
		result.add(new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, event, installment
			.calculateAmount(when), event.getDescriptionForEntryType(getEntryType()),
			installment));
		totalAmount = totalAmount.add(amount);
	    }
	}

	if (needsTotalAmountEntry(result)) {
	    result.add(new EntryDTO(EntryType.GRATUITY_FEE, event, totalAmount, event
		    .calculatePayedAmount(), event.calculateAmountToPay(when), event
		    .getDescriptionForEntryType(getEntryType()), event.calculateAmountToPay(when)));
	}

	return result;
    }

    private boolean needsTotalAmountEntry(List<EntryDTO> result) {
	return result.size() > 1;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
	    Account toAccount) {

	if (entryDTOs.size() != 1) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.GratuityWithPaymentConditionPR.invalid.number.of.entryDTOs",
		    event.getDescriptionForEntryType(getEntryType()));
	}

	final EntryDTO entryDTO = entryDTOs.get(0);
	final GratuityEventWithPaymentPlan gratuityEventWithPaymentPlan = (GratuityEventWithPaymentPlan) event;
	final AccountingTransaction accountingTransaction;
	if (entryDTO instanceof EntryWithInstallmentDTO) {
	    accountingTransaction = internalProcessInstallment(user, paymentMode, whenRegistered,
		    fromAccount, toAccount, entryDTO, gratuityEventWithPaymentPlan);
	} else {
	    accountingTransaction = internalProcessTotal(user, paymentMode, whenRegistered, fromAccount,
		    toAccount, entryDTO, gratuityEventWithPaymentPlan);
	}

	return Collections.singleton(accountingTransaction);

    }

    private AccountingTransaction internalProcessTotal(User user, PaymentMode paymentMode,
	    DateTime whenRegistered, Account fromAccount, Account toAccount, final EntryDTO entryDTO,
	    final GratuityEventWithPaymentPlan event) {

	event.cancelGratuityTotalPaymentCode();
	checkIfCanAddAmount(entryDTO, whenRegistered, event);

	return super.makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(),
		entryDTO.getAmountToPay(), paymentMode, whenRegistered);

    }

    private AccountingTransaction internalProcessInstallment(User user, PaymentMode paymentMode,
	    DateTime whenRegistered, Account fromAccount, Account toAccount, final EntryDTO entryDTO,
	    final GratuityEventWithPaymentPlan event) {

	final EntryWithInstallmentDTO entryWithInstallmentDTO = (EntryWithInstallmentDTO) entryDTO;
	checkIfCanAddAmountForInstallment(entryWithInstallmentDTO, whenRegistered, event);
	event.cancelInstallmentPaymentCode(entryWithInstallmentDTO.getInstallment());

	return makeAccountingTransactionForInstallment(user, event, fromAccount, toAccount,
		getEntryType(), entryDTO.getAmountToPay(), paymentMode, whenRegistered,
		(entryWithInstallmentDTO).getInstallment());

    }

    private void checkIfCanAddAmount(EntryDTO entryDTO, DateTime whenRegistered, Event event) {
	if (entryDTO.getAmountToPay().compareTo(event.calculateAmountToPay(whenRegistered)) != 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.GratuityWithPaymentPlanPR.amount.to.pay.must.match.value",
		    event.getDescriptionForEntryType(getEntryType()));
	}
    }

    private void checkIfCanAddAmountForInstallment(EntryWithInstallmentDTO entryDTO, DateTime when,
	    Event event) {
	if (entryDTO.getAmountToPay().compareTo(entryDTO.getInstallment().calculateAmount(when)) != 0) {
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
	    PaymentMode paymentMode, DateTime whenRegistered, Installment installment) {
	return new InstallmentAccountingTransaction(responsibleUser, event, makeEntry(entryType, amount
		.negate(), from), makeEntry(entryType, amount, to), paymentMode, whenRegistered,
		installment);
    }

}
