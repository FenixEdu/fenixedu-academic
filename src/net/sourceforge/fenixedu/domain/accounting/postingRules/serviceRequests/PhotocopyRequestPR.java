package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PhotocopyRequestEvent;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PhotocopyRequestPR extends PhotocopyRequestPR_Base {

    protected PhotocopyRequestPR() {
	super();
    }

    public PhotocopyRequestPR(final ServiceAgreementTemplate serviceAgreementTemplate, final DateTime startDate,
	    final DateTime endDate, final Money baseAmount, final Money amountPerPage) {
	this();
	super.init(EntryType.PHOTOCOPY_REQUEST_FEE, EventType.PHOTOCOPY_REQUEST, startDate, endDate, serviceAgreementTemplate,
		baseAmount, amountPerPage);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public PhotocopyRequestPR edit(final Money baseAmount, final Money amountPerUnit) {
	deactivate();
	return new PhotocopyRequestPR(getServiceAgreementTemplate(), new DateTime().minus(1000), null, baseAmount, amountPerUnit);
    }

    @Override
    protected Money getAmountForPages(final Event event) {
	final PhotocopyRequestEvent requestEvent = (PhotocopyRequestEvent) event;
	return getAmountPerPage().multiply(BigDecimal.valueOf(requestEvent.getNumberOfPages()));
    }

    @Override
    protected boolean isUrgent(final Event event) {
	return ((PhotocopyRequestEvent) event).isUrgentRequest();
    }
}
