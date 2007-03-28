package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriod;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInImprovementOfApprovedEnrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentPenaltyExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ImprovementOfApprovedEnrolmentPR extends ImprovementOfApprovedEnrolmentPR_Base {

    protected ImprovementOfApprovedEnrolmentPR() {
	super();
    }

    public ImprovementOfApprovedEnrolmentPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(EntryType.IMPROVEMENT_OF_APPROVED_ENROLMENT_FEE, EventType.IMPROVEMENT_OF_APPROVED_ENROLMENT, startDate, endDate, serviceAgreementTemplate);
	checkParameters(fixedAmount, fixedAmountPenalty);
	super.setFixedAmount(fixedAmount);
	super.setFixedAmountPenalty(fixedAmountPenalty);
    }

    private void checkParameters(Money fixedAmount, Money fixedAmountPenalty) {
	if (fixedAmount == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmount.cannot.be.null");
	}
	if (fixedAmountPenalty == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.fixedAmountPenalty.cannot.be.null");
	}
    }

    @Override
    public void setFixedAmountPenalty(Money fixedAmountPenalty) {
	throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.cannot.modify.fixedAmountPenalty");
    }

    public ImprovementOfApprovedEnrolmentPR edit(final Money fixedAmount, final Money fixedAmountPenalty, final YearMonthDay whenToApplyFixedAmountPenalty) {
	deactivate();
	return new ImprovementOfApprovedEnrolmentPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), fixedAmount, fixedAmountPenalty);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent = (ImprovementOfApprovedEnrolmentEvent) event;
	final boolean hasPenalty = hasPenalty(event, when); 
	
	Money result = Money.ZERO;
	for (int i = 0; i < improvementOfApprovedEnrolmentEvent.getImprovementEnrolmentEvaluationsCount(); i++) {
	    result = result.add(hasPenalty ? getFixedAmountPenalty() : getFixedAmount());
	}
	
	return result;
    }

    private boolean hasPenalty(Event event, DateTime when) {
	if (event.hasAnyPenaltyExemptionsFor(ImprovementOfApprovedEnrolmentPenaltyExemption.class)) {
	    return false;
	} else {
	    return !getEnrolmentPeriodInImprovementOfApprovedEnrolment(event).containsDate(when); 
	}
    }

    private EnrolmentPeriodInImprovementOfApprovedEnrolment getEnrolmentPeriodInImprovementOfApprovedEnrolment(Event event) {
	final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent = (ImprovementOfApprovedEnrolmentEvent) event;
	final DegreeCurricularPlan degreeCurricularPlan = improvementOfApprovedEnrolmentEvent.getImprovementEnrolmentEvaluations().get(0).getDegreeCurricularPlan();
	
	final EnrolmentPeriod enrolmentPeriodInImprovementOfApprovedEnrolment = ExecutionPeriod.readActualExecutionPeriod().getEnrolmentPeriod(EnrolmentPeriodInImprovementOfApprovedEnrolment.class, degreeCurricularPlan);
	if (enrolmentPeriodInImprovementOfApprovedEnrolment == null) {
	    throw new DomainException("error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.enrolmentPeriodInImprovementOfApprovedEnrolment.must.not.be.null");
	}
	
	return (EnrolmentPeriodInImprovementOfApprovedEnrolment) enrolmentPeriodInImprovementOfApprovedEnrolment;
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	final Money totalAmountToPay = calculateTotalAmountToPay(event, when);
	return Collections.singletonList(new EntryDTO(getEntryType(), event, totalAmountToPay,
		Money.ZERO, totalAmountToPay, event.getDescriptionForEntryType(getEntryType()),
		totalAmountToPay));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.invalid.number.of.entryDTOs");
	}

	final EntryDTO entryDTO = entryDTOs.get(0);
	checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		entryDTO.getEntryType(), entryDTO.getAmountToPay(), transactionDetail));
    }
    
    private void checkIfCanAddAmount(Money amountToPay, final Event event, final DateTime when) {
	if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) < 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.ImprovementOfApprovedEnrolmentPR.amount.being.payed.must.match.amount.to.pay",
		    event.getDescriptionForEntryType(getEntryType()));
	}
    }

}
