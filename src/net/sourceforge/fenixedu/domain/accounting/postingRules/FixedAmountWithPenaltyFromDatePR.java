package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class FixedAmountWithPenaltyFromDatePR extends FixedAmountWithPenaltyFromDatePR_Base {

    protected FixedAmountWithPenaltyFromDatePR() {
	super();
    }

    public FixedAmountWithPenaltyFromDatePR(EntryType entryType, EventType eventType,
	    DateTime startDate, DateTime endDate, ServiceAgreementTemplate serviceAgreementTemplate,
	    Money fixedAmount, Money fixedAmountPenalty, YearMonthDay whenToApplyFixedAmountPenalty) {
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount,
		fixedAmountPenalty, whenToApplyFixedAmountPenalty);

    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty, YearMonthDay whenToApplyFixedAmountPenalty) {

	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount,
		fixedAmountPenalty);

	checkParameters(whenToApplyFixedAmountPenalty);

	super.setWhenToApplyFixedAmountPenalty(whenToApplyFixedAmountPenalty);
    }

    private void checkParameters(YearMonthDay whenToApplyFixedAmountPenalty) {
	if (whenToApplyFixedAmountPenalty == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.FixedAmountWithPenaltyFromDatePR.whenToApplyFixedAmountPenalty.cannot.be.null");
	}

    }

    @Override
    public void setWhenToApplyFixedAmountPenalty(YearMonthDay whenToApplyFixedAmountPenalty) {
	throw new DomainException(
		"error.accounting.postingRules.FixedAmountWithPenaltyFromDatePR.cannot.modify.whenToApplyFixedAmountPenalty");
    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	return when.toYearMonthDay().isAfter(getWhenToApplyFixedAmountPenalty());
    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyFromDatePR edit(Money fixedAmount, Money penaltyAmount,
	    YearMonthDay whenToApplyFixedAmountPenalty) {

	deactivate();

	return new FixedAmountWithPenaltyFromDatePR(getEntryType(), getEventType(), new DateTime()
		.minus(1000), null, getServiceAgreementTemplate(), fixedAmount, penaltyAmount,
		whenToApplyFixedAmountPenalty);

    }

}
