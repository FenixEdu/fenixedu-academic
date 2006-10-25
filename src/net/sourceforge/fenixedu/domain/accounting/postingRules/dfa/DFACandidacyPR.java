package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;

import org.joda.time.DateTime;

public class DFACandidacyPR extends DFACandidacyPR_Base {

    private DFACandidacyPR() {
	super();
    }

    public DFACandidacyPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal fixedAmount,
	    BigDecimal fixedAmountPenalty) {
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

    @Override
    protected FixedAmountWithPenaltyPR createNewVersion(BigDecimal fixedAmount, BigDecimal penaltyAmount) {
	return new DFACandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount, penaltyAmount);
    }

}
