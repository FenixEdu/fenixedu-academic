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

    public GenericWithParcialPaymentsPR(EventType eventType, EntryType entryType, DateTime startDate,
	    DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal totalAmount) {
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, totalAmount);
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal totalAmount) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
	checkParameters(totalAmount);
	super.setTotalAmount(totalAmount);
    }

    private void checkParameters(BigDecimal totalAmount) {
	if (totalAmount == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.GenericWithParcialPaymentsPR.totalAmount.cannot.be.null");
	}
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, getTotalAmount(), event
		.calculatePayedAmount(), calculateTotalAmountToPay(event, when), event
		.getDescriptionForEntryType(getEntryType())));
    }

    //FIXME: this method should be in superclass. subclasses should only reimplement variable part...
    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
	return getTotalAmount().subtract(event.calculatePayedAmount());
    }

    @Override
    public void setTotalAmount(BigDecimal totalAmount) {
	throw new DomainException(
		"error.accounting.postingRules.GenericWithParcialPaymentsPR.cannot.modify.totalAmount");
    }

}
