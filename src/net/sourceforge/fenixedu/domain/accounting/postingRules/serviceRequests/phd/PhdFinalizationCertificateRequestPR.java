package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.phd;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdFinalizationCertificateRequestPR extends PhdFinalizationCertificateRequestPR_Base {
    
    protected PhdFinalizationCertificateRequestPR() {
        super();
    }
    
    public PhdFinalizationCertificateRequestPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate,
	    Money fixedAmount) {
	this();
	init(EntryType.PHD_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.PHD_FINALIZATION_CERTIFICATE_REQUEST, startDate,
		endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	AcademicServiceRequestEvent academicServiceRequestEvent = (AcademicServiceRequestEvent) event;
	AcademicServiceRequest academicServiceRequest = academicServiceRequestEvent.getAcademicServiceRequest();

	return super.doCalculationForAmountToPay(academicServiceRequestEvent, when, applyDiscount).multiply(
		academicServiceRequest.isUrgentRequest() ? 2 : 1);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public PhdFinalizationCertificateRequestPR edit(final Money fixedAmount) {

	deactivate();
	return new PhdFinalizationCertificateRequestPR(new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount);
    }

    public Money getUrgentAmount() {
	return super.getFixedAmount();
    }
}
