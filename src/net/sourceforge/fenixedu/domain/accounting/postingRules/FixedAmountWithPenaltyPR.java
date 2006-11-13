package net.sourceforge.fenixedu.domain.accounting.postingRules;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public abstract class FixedAmountWithPenaltyPR extends FixedAmountWithPenaltyPR_Base {

    protected FixedAmountWithPenaltyPR() {
	super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
	checkParameters(fixedAmountPenalty);
	super.setFixedAmountPenalty(fixedAmountPenalty);
    }

    private void checkParameters(Money fixedAmountPenalty) {
	if (fixedAmountPenalty == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.FixedAmountWithPenaltyPR.fixedAmountPenalty.cannot.be.null");
	}
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when) {
	return super.calculateTotalAmountToPay(event, when).add(
		hasPenalty(event, when) ? getFixedAmountPenalty() : Money.ZERO);
    }

    @Override
    public void setFixedAmountPenalty(Money fixedAmountPenalty) {
	throw new DomainException(
		"error.accounting.postingRules.FixedAmountWithPenaltyPR.cannot.modify.fixedAmountPenalty");
    }


    abstract protected boolean hasPenalty(Event event, DateTime when);

}
