package net.sourceforge.fenixedu.domain.accounting.postingRules.candidacy;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR extends SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR_Base {
    
    public  SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR() {
        super();
    }
    
    public SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR(final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
	this();
	init(EntryType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_FEE, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, startDate, endDate,
		serviceAgreementTemplate, fixedAmount);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR edit(final Money fixedAmount) {
	deactivate();
	return new SecondCycleIndividualCandidacyFixedValueMultipleDegreesPR(new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	return getFixedAmount();
    }

}
