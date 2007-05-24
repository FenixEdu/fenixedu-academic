package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DiplomaRequestPR extends DiplomaRequestPR_Base {

    protected DiplomaRequestPR() {
	super();
    }

    public DiplomaRequestPR(final EntryType entryType, final EventType eventType,
	    final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    @Override
    @Checked("PostingRulePredicates.editPredicate")
    final public DiplomaRequestPR edit(final Money fixedAmount) {
	deactivate();
	return new DiplomaRequestPR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount);
    }

}
