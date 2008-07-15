package net.sourceforge.fenixedu.domain.accounting.postingRules.candidacy;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.SecondCycleIndividualCandidacyEvent;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class SecondCycleIndividualCandidacyPR extends SecondCycleIndividualCandidacyPR_Base {

    private SecondCycleIndividualCandidacyPR() {
	super();
    }

    public SecondCycleIndividualCandidacyPR(final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount) {
	this();
	init(EntryType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_FEE, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, startDate, endDate,
		serviceAgreementTemplate, fixedAmount);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public SecondCycleIndividualCandidacyPR edit(final Money fixedAmount) {

	deactivate();
	return new SecondCycleIndividualCandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final SecondCycleIndividualCandidacyEvent candidacyEvent = (SecondCycleIndividualCandidacyEvent) event;
	return candidacyEvent.hasSecondCycleIndividualCandidacyExemption() ? Money.ZERO : super.calculateTotalAmountToPay(event,
		when, applyDiscount);
    }
}
