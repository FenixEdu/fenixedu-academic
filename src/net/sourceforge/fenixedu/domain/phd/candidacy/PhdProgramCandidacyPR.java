package net.sourceforge.fenixedu.domain.phd.candidacy;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PhdProgramCandidacyPR extends PhdProgramCandidacyPR_Base {

    protected PhdProgramCandidacyPR() {
	super();
    }

    public PhdProgramCandidacyPR(DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
	    Money fixedAmount, Money fixedAmountPenalty) {
	super.init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate,
		serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	return false;
    }

}
