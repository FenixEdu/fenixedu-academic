package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.accessControl.Checked;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class FixedAmountWithPenaltyPR extends FixedAmountWithPenaltyPR_Base {

    protected FixedAmountWithPenaltyPR() {
	super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, BigDecimal fixedAmount,
	    BigDecimal fixedAmountPenalty) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
	checkParameters(fixedAmountPenalty);
	super.setFixedAmountPenalty(fixedAmountPenalty);
    }

    private void checkParameters(BigDecimal fixedAmountPenalty) {
	if (fixedAmountPenalty == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.FixedAmountWithPenaltyPR.fixedAmountPenalty.cannot.be.null");
	}
    }

    @Override
    public BigDecimal calculateTotalAmountToPay(Event event, DateTime when) {
	return super.calculateTotalAmountToPay(event, when).add(
		hasPenalty(event, when) ? getFixedAmountPenalty() : BigDecimal.ZERO);
    }

    @Override
    public void setFixedAmountPenalty(BigDecimal fixedAmountPenalty) {
	throw new DomainException(
		"error.net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR.cannot.modify.fixedAmountPenalty");
    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyPR edit(final BigDecimal fixedAmount, final BigDecimal penaltyAmount) {

	deactivate();

	return createNewVersion(fixedAmount, penaltyAmount);
    }

    abstract protected FixedAmountWithPenaltyPR createNewVersion(BigDecimal fixedAmount,
	    BigDecimal penaltyAmount);

    abstract protected boolean hasPenalty(Event event, DateTime when);

}
