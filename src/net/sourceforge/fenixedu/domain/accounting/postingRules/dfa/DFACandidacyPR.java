package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DFACandidacyPR extends DFACandidacyPR_Base {

    private DFACandidacyPR() {
	super();
    }

    public DFACandidacyPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate,
		serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	final DFACandidacyEvent dfaCandidacyEvent = (DFACandidacyEvent) event;
	return dfaCandidacyEvent.hasCandidacyPeriodInDegreeCurricularPlan()
		&& !dfaCandidacyEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
			dfaCandidacyEvent.getCandidacyDate());

    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

	deactivate();

	return new DFACandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount, penaltyAmount);
    }

}
