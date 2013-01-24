package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

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

    public PhdRegistrationFeePR edit(final Money fixedAmount, final Money penaltyAmount) {
	deactivate();
	return new PhdRegistrationFeePR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount,
		penaltyAmount);
    }

    @Override
    protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	Money total = super.doCalculationForAmountToPay(event, when, applyDiscount).add(
		hasPenalty(event, when) ? getFixedAmountPenalty() : Money.ZERO);

	return total;
    }

    @Override
    protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
	final PhdRegistrationFee feeEvent = (PhdRegistrationFee) event;
	if (feeEvent.hasPhdEventExemption()) {
	    amountToPay = amountToPay.subtract(feeEvent.getPhdEventExemption().getValue());
	}

	return amountToPay.isPositive() ? amountToPay : Money.ZERO;
    }

    @Override
    protected boolean hasPenalty(final Event event, final DateTime when) {
	final PhdRegistrationFee phdEvent = (PhdRegistrationFee) event;

	if (phdEvent.hasPhdRegistrationFeePenaltyExemption()) {
	    return false;
	}

	final PhdIndividualProgramProcess process = phdEvent.getProcess();
	return PhdProgramCalendarUtil.countWorkDaysBetween(process.getCandidacyProcess().getWhenRatified(),
		process.getWhenFormalizedRegistration()) > 20;

    }

}
