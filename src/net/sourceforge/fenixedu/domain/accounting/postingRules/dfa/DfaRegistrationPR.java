package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;

import org.joda.time.DateTime;

public class DfaRegistrationPR extends DfaRegistrationPR_Base {

    private DfaRegistrationPR() {
	super();
    }

    public DfaRegistrationPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal fixedAmount,
	    BigDecimal fixedAmountPenalty) {
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

    @Override
    protected FixedAmountWithPenaltyPR createNewVersion(BigDecimal fixedAmount, BigDecimal penaltyAmount) {
	return new DfaRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount, penaltyAmount);
    }
}
