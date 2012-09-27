package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class CertificateRequestWithoutBasePR extends CertificateRequestWithoutBasePR_Base {

    protected CertificateRequestWithoutBasePR() {
	super();
    }

    public CertificateRequestWithoutBasePR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
	    Money maximumAmount) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage,
		maximumAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final CertificateRequestEvent certificateRequestEvent = (CertificateRequestEvent) event;
	Money amountForUnits = getAmountForUnits(event);

	if (isUrgent(certificateRequestEvent)) {
	    amountForUnits = amountForUnits.multiply(2);
	}

	return amountForUnits;
    }

    @Checked("PostingRulePredicates.editPredicate")
    public CertificateRequestPR edit(final Money baseAmount, final Money amountPerUnit, final Money amountPerPage,
	    final Money maximumAmount) {
	deactivate();
	return new CertificateRequestWithoutBasePR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
