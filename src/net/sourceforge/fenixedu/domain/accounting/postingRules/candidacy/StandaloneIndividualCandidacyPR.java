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

import pt.ist.fenixWebFramework.security.accessControl.Checked;

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
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	if (!event.hasAnyExemptions()) {
	    return getFixedAmount();
	}

	Money amount = getFixedAmount();

	for (Exemption exemption : event.getExemptions()) {
	    if (exemption.isAcademicEventExemption()) {
		AcademicEventExemption academicEventExemption = (AcademicEventExemption) exemption;
		amount = amount.subtract(academicEventExemption.getValue());
	    }
	}

	if (amount.isNegative()) {
	    return Money.ZERO;
	}

	return amount;
    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountPR edit(final Money fixedAmount) {

	deactivate();
	return new FixedAmountPR(getEntryType(), getEventType(), new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount);
    }
    
}
