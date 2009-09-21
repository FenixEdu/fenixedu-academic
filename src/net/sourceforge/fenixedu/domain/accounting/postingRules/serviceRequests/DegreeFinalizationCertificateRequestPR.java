package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DegreeFinalizationCertificateRequestEvent;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class DegreeFinalizationCertificateRequestPR extends DegreeFinalizationCertificateRequestPR_Base {

    private DegreeFinalizationCertificateRequestPR() {
	super();
    }

    public DegreeFinalizationCertificateRequestPR(final DateTime startDate, DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
	    final Money amountPerPage) {
	this();
	init(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, startDate,
		endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage);
    }
    
    
    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when) {
        final Money total = super.calculateTotalAmountToPay(event, when);
        
        final DegreeFinalizationCertificateRequestEvent requestEvent = (DegreeFinalizationCertificateRequestEvent) event; 

        if (requestEvent.hasDegreeFinalizationCertificateRequestExemption()) {
            return total.subtract(requestEvent.getDegreeFinalizationCertificateRequestExemption().getValue());
        }
        
        return total;
    }
    

    @Checked("PostingRulePredicates.editPredicate")
    public DegreeFinalizationCertificateRequestPR edit(Money baseAmount, Money amountPerUnit, Money amountPerPage) {

	deactivate();

	return new DegreeFinalizationCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		baseAmount, amountPerUnit, amountPerPage);
    }

}
