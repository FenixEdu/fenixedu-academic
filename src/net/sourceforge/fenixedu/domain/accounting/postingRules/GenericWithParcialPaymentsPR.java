package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class GenericWithParcialPaymentsPR extends GenericWithParcialPaymentsPR_Base {

    protected GenericWithParcialPaymentsPR() {
	super();
    }

    public GenericWithParcialPaymentsPR(EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, EntryType totalPaymentEntryType,
	    EntryType parcialPaymentEntryType, BigDecimal totalAmount) {
	init(eventType, startDate, endDate, serviceAgreementTemplate, totalPaymentEntryType,
		parcialPaymentEntryType, totalAmount);
    }

    protected void init(EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, EntryType totalPaymentEntryType,
	    EntryType partialPaymentEntryType, BigDecimal totalAmount) {
	super.init(eventType, startDate, endDate, serviceAgreementTemplate);
	checkParameters(totalPaymentEntryType, partialPaymentEntryType, totalAmount);
	super.setTotalPaymentEntryType(totalPaymentEntryType);
	super.setPartialPaymentEntryType(partialPaymentEntryType);
	super.setTotalAmount(totalAmount);
    }

    private void checkParameters(EntryType totalPaymentEntryType, EntryType parcialPaymentEntryType,
	    BigDecimal totalAmount) {
	if (totalPaymentEntryType == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.postingRules.GenericWithParcialPaymentsPR.totalPaymentEntryType.cannot.be.null");
	}

	if (parcialPaymentEntryType == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.postingRules.GenericWithParcialPaymentsPR.parcialPaymentEntryType.cannot.be.null");
	}

	if (totalAmount == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.postingRules.GenericWithParcialPaymentsPR.totalAmount.cannot.be.null");
	}
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	final EntryType entryType = getEntryTypeForPayment(calculateTotalAmountToPay(event, when));

	return Collections.singletonList(new EntryDTO(entryType, event, getTotalAmount(), event
		.calculatePayedAmount(), calculateTotalAmountToPay(event, when), event
		.getDescriptionForEntryType(entryType)));
    }

    protected EntryType getEntryTypeForPayment(BigDecimal amountToPay) {
	return isPartialPayment(amountToPay) ? getPartialPaymentEntryType() : getTotalPaymentEntryType();
    }

    protected boolean isPartialPayment(BigDecimal amountToPay) {
	return getTotalAmount().compareTo(amountToPay) != 0;
    }

    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
	return getTotalAmount().subtract(event.calculatePayedAmount());
    }

    @Override
    public void setTotalAmount(BigDecimal totalAmount) {
	throw new DomainException(
		"error.accounting.postingRules.GenericWithParcialPaymentsPR.cannot.modify.totalAmount");
    }

    @Override
    public void setPartialPaymentEntryType(EntryType partialPaymentEntryType) {
	throw new DomainException(
		"error.accounting.postingRules.GenericWithParcialPaymentsPR.cannot.modify.partialPaymentEntryType");
    }

    @Override
    public void setTotalPaymentEntryType(EntryType totalPaymentEntryType) {
	throw new DomainException(
		"error.accounting.postingRules.GenericWithParcialPaymentsPR.cannot.modify.totalPaymentEntryType");
    }

}
