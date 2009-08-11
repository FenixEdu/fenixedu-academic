package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class PhdRegistrationFeePR extends PhdRegistrationFeePR_Base {

    private PhdRegistrationFeePR() {
	super();
    }

    public PhdRegistrationFeePR(final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate, final Money fixedAmount, final Money fixedAmountPenalty) {
	this();
	super.init(EntryType.PHD_REGISTRATION_FEE, EventType.PHD_REGISTRATION_FEE, startDate, endDate, serviceAgreementTemplate,
		fixedAmount, fixedAmountPenalty);
    }

    @Checked("PostingRulePredicates.editPredicate")
    public PhdRegistrationFeePR edit(final Money fixedAmount, final Money penaltyAmount) {
	deactivate();
	return new PhdRegistrationFeePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount,
		penaltyAmount);
    }

    @Override
    protected boolean hasPenalty(final Event event, final DateTime when) {
	final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;

	if (phdEvent.hasPhdRegistrationFeePenaltyExemption()) {
	    return false;
	}

	final PhdIndividualProgramProcess process = phdEvent.getProcess();
	return PhdProgramCalendarUtil.countWorkDaysBetween(process.getCandidacyProcess().getWhenRatified(), process
		.getWhenFormalizedRegistration()) > 20;

    }

}
