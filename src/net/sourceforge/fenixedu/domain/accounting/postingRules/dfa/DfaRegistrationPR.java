package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DfaRegistrationPR extends DfaRegistrationPR_Base {

    private DfaRegistrationPR() {
	super();
    }

    public DfaRegistrationPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(EntryType.REGISTRATION_FEE, EventType.DFA_REGISTRATION, startDate, endDate,
		serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	return hasPenaltyForRegistration((DfaRegistrationEvent) event)
		&& !hasPayedPenaltyForCandidacy((DfaRegistrationEvent) event);
    }

    private boolean hasPenaltyForRegistration(final DfaRegistrationEvent dfaRegistrationEvent) {
	return dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()
		&& !dfaRegistrationEvent.getRegistrationPeriodInDegreeCurricularPlan().containsDate(
			dfaRegistrationEvent.getRegistrationDate());
    }

    private boolean hasPayedPenaltyForCandidacy(final DfaRegistrationEvent dfaRegistrationEvent) {
	return dfaRegistrationEvent.hasCandidacyPeriodInDegreeCurricularPlan()
		&& !dfaRegistrationEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
			dfaRegistrationEvent.getCandidacyDate());
    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {
	
	deactivate();
	
	return new DfaRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount, penaltyAmount);
    }

}
