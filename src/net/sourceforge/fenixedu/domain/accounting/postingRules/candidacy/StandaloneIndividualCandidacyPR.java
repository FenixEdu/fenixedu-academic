package net.sourceforge.fenixedu.domain.accounting.postingRules.candidacy;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountPR;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class StandaloneIndividualCandidacyPR extends StandaloneIndividualCandidacyPR_Base {

	protected StandaloneIndividualCandidacyPR() {
		super();
	}

	public StandaloneIndividualCandidacyPR(DateTime startDate, DateTime endDate,
			ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
		this();
		init(EntryType.STANDALONE_INDIVIDUAL_CANDIDACY_FEE, EventType.STANDALONE_INDIVIDUAL_CANDIDACY, startDate, endDate,
				serviceAgreementTemplate, fixedAmount);
	}

	@Override
	protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
		if (!event.hasAnyExemptions()) {
			return amountToPay;
		}

		for (Exemption exemption : event.getExemptions()) {
			if (exemption.isAcademicEventExemption()) {
				AcademicEventExemption academicEventExemption = (AcademicEventExemption) exemption;
				amountToPay = amountToPay.subtract(academicEventExemption.getValue());
			}
		}

		if (amountToPay.isNegative()) {
			return Money.ZERO;
		}

		return amountToPay;
	}

	@Override
	public FixedAmountPR edit(final Money fixedAmount) {

		deactivate();
		return new FixedAmountPR(getEntryType(), getEventType(), new DateTime().minus(1000), null, getServiceAgreementTemplate(),
				fixedAmount);
	}

}
