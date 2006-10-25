package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class DFAGratuityPR extends DFAGratuityPR_Base {

    protected DFAGratuityPR() {
	super();
    }

    public DFAGratuityPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal dfaTotalAmount,
	    BigDecimal partialAcceptedPercentage, BigDecimal dfaAmountPerEctsCredit) {
	super();
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, dfaTotalAmount,
		partialAcceptedPercentage, dfaAmountPerEctsCredit);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal dfaTotalAmount,
	    BigDecimal dfaPartialAcceptedPercentage, BigDecimal dfaAmountPerEctsCredit) {

	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);

	checkParameters(dfaTotalAmount, dfaPartialAcceptedPercentage, dfaAmountPerEctsCredit);

	super.setDfaTotalAmount(dfaTotalAmount);
	super.setDfaPartialAcceptedPercentage(dfaPartialAcceptedPercentage);
	super.setDfaAmountPerEctsCredit(dfaAmountPerEctsCredit);

    }

    private void checkParameters(BigDecimal dfaTotalAmount, BigDecimal dfaPartialAcceptedPercentage,
	    BigDecimal dfaAmountPerEctsCredit) {
	if (dfaTotalAmount == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaTotalAmount.cannot.be.null");
	}

	if (dfaPartialAcceptedPercentage == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaPartialAcceptedPercentage.cannot.be.null");
	}

	if (dfaAmountPerEctsCredit == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.dfaAmountPerEctsCredit.cannot.be.null");
	}

    }

    @Override
    public void setDfaTotalAmount(BigDecimal dfaTotalAmount) {
	throw new DomainException(
		"error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaTotalAmount");
    }

    @Override
    public void setDfaPartialAcceptedPercentage(BigDecimal dfaPartialAcceptedPercentage) {
	throw new DomainException(
		"error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaPartialAcceptedPercentage");
    }

    @Override
    public void setDfaAmountPerEctsCredit(BigDecimal dfaAmountPerEctsCredit) {
	throw new DomainException(
		"error.accounting.postingRules.gratuity.DFAGratuityPR.cannot.modify.dfaAmountPerEctsCredit");
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
	    Account toAccount) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.get(0).getAmountToPay(), event, whenRegistered);

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		getEntryType(), entryDTOs.get(0).getAmountToPay(), paymentMode, whenRegistered));
    }

    private void checkIfCanAddAmount(BigDecimal amountToAdd, Event event, DateTime when) {
	if (((GratuityEvent) event).isCustomEnrolmentModel()) {
	    checkIfCanAddAmountForCustomEnrolmentModel(amountToAdd, event, when);
	} else {
	    checkIfCanAddAmountForCompleteEnrolmentModel(amountToAdd, event, when);
	}

    }

    private void checkIfCanAddAmountForCustomEnrolmentModel(BigDecimal amountToAdd, Event event,
	    DateTime when) {
	final BigDecimal amountToPay = event.calculateAmountToPay(when);
	if (amountToPay.compareTo(amountToAdd) != 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));
	}

    }

    private void checkIfCanAddAmountForCompleteEnrolmentModel(BigDecimal amountToAdd, Event event,
	    DateTime when) {
	final BigDecimal amountToPay = event.calculateAmountToPay(when);
	if (getDfaTotalAmount().compareTo(amountToPay) != 0 && amountToAdd.compareTo(amountToPay) != 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));
	}

	if (amountToPay.compareTo(amountToAdd) != 0
		&& amountToAdd.compareTo(getPartialPaymentAmount()) != 0) {
	    final LabelFormatter percentageLabelFormatter = new LabelFormatter();
	    percentageLabelFormatter.appendLabel(getDfaPartialAcceptedPercentage().multiply(
		    BigDecimal.valueOf(100)).toString());

	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.partial.payment.value",
		    event.getDescriptionForEntryType(getEntryType()), percentageLabelFormatter);
	}
    }

    private BigDecimal getPartialPaymentAmount() {
	return getDfaTotalAmount().multiply(getDfaPartialAcceptedPercentage());
    }

    @Override
    public List<GenericPair<EntryType, BigDecimal>> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new GenericPair<EntryType, BigDecimal>(getEntryType(),
		calculateTotalAmountToPay(event, when)));
    }

    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
	if (((GratuityEvent) event).isCustomEnrolmentModel()) {
	    return ((GratuityEvent) event).getTotalEctsCreditsForRegistration().multiply(
		    getDfaAmountPerEctsCredit());
	} else {
	    return getDfaTotalAmount();
	}
    }
}
