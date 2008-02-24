package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PastDegreeDiplomaRequestEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PastDegreeDiplomaRequestPR extends PastDegreeDiplomaRequestPR_Base {

    protected PastDegreeDiplomaRequestPR() {
	super();
    }

    public PastDegreeDiplomaRequestPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate) {
	super.init(EntryType.DIPLOMA_REQUEST_FEE, EventType.PAST_DEGREE_DIPLOMA_REQUEST, startDate, endDate,
		serviceAgreementTemplate);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
		.getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
		.calculateAmountToPay(when)));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	return ((PastDegreeDiplomaRequestEvent) event).getPastAmount();
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
