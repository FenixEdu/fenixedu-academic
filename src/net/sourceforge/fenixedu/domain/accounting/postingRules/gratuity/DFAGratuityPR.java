package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.joda.time.DateTime;

public class DFAGratuityPR extends DFAGratuityPR_Base {

    protected DFAGratuityPR() {
	super();
    }

    public DFAGratuityPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate,BigDecimal totalAmount,
	    BigDecimal partialAcceptedPercentage) {
	super();
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, totalAmount, partialAcceptedPercentage);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal totalAmount,
	    BigDecimal partialAcceptedPercentage) {

	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, totalAmount);
	checkParameters(partialAcceptedPercentage);
	super.setPartialAcceptedPercentage(partialAcceptedPercentage);

    }

    private void checkParameters(BigDecimal parcialAcceptedPercentage) {
	if (parcialAcceptedPercentage == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.DFAGratuityPR.parcialAcceptedPercentage.cannot.be.null");
	}

    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    PaymentMode paymentMode, DateTime whenRegistered, Event event, Account fromAccount,
	    Account toAccount) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.number.of.entryDTOs");
	}
	final EntryDTO entryDTO = entryDTOs.get(0);

	checkIfCanAddAmount(entryDTO.getAmountToPay(), event, whenRegistered);

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		getEntryType(), entryDTO.getAmountToPay(),
		paymentMode, whenRegistered));
    }

    private void checkIfCanAddAmount(BigDecimal amountToAdd, Event event, DateTime when) {
	final BigDecimal amountToPay = calculateTotalAmountToPay(event, when);
	if (getTotalAmount().compareTo(amountToPay) != 0 && amountToAdd.compareTo(amountToPay) != 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));
	}

	if (amountToPay.compareTo(amountToAdd) != 0
		&& amountToAdd.compareTo(getPartialPaymentAmount()) != 0) {
	    final LabelFormatter percentageLabelFormatter = new LabelFormatter();
	    percentageLabelFormatter.appendLabel(getPartialAcceptedPercentage().multiply(
		    BigDecimal.valueOf(100)).toString());

	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.DFAGratuityPR.invalid.partial.payment.value",
		    event.getDescriptionForEntryType(getEntryType()), percentageLabelFormatter);
	}

    }

    private BigDecimal getPartialPaymentAmount() {
	return getTotalAmount().multiply(getPartialAcceptedPercentage());
    }

    @Override
    public void setPartialAcceptedPercentage(BigDecimal partialAcceptedPercentage) {
	throw new DomainException(
		"error.accounting.postingRules.gratuity.enclosing_type.cannot.modify.partialAcceptedPercentage");
    }

}
