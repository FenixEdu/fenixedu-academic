package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DeclarationRequestPR extends DeclarationRequestPR_Base {
    
    protected DeclarationRequestPR() {
	super();
    }

    public DeclarationRequestPR(EntryType entryType, EventType eventType, DateTime startDate,
	    DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public DeclarationRequestPR edit(final Money fixedAmount) {
	deactivate();
	return new DeclarationRequestPR(getEntryType(), getEventType(), new DateTime().minus(1000),
		null, getServiceAgreementTemplate(), fixedAmount);

    }

}
