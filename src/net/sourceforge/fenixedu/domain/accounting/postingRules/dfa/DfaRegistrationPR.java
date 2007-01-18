package net.sourceforge.fenixedu.domain.accounting.postingRules.dfa;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DfaRegistrationPR extends DfaRegistrationPR_Base {

    private DfaRegistrationPR() {
	super();
    }

    public DfaRegistrationPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(EntryType.REGISTRATION_FEE, EventType.DFA_REGISTRATION, startDate, endDate,
		serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	return hasPenaltyForRegistration((DfaRegistrationEvent) event)
		&& !hasPayedPenaltyForCandidacy((DfaRegistrationEvent) event);
    }

    private boolean hasPenaltyForRegistration(final DfaRegistrationEvent dfaRegistrationEvent) {
	return dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()
		&& !dfaRegistrationEvent.getRegistrationPeriodInDegreeCurricularPlan().containsDate(
			dfaRegistrationEvent.getRegistrationDate());
    }

    private boolean hasPayedPenaltyForCandidacy(final DfaRegistrationEvent dfaRegistrationEvent) {
	return dfaRegistrationEvent.hasCandidacyPeriodInDegreeCurricularPlan()
		&& !dfaRegistrationEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
			dfaRegistrationEvent.getCandidacyDate());

    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

	deactivate();

	return new DfaRegistrationPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
		fixedAmount, penaltyAmount);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    Event event, Account fromAccount, Account toAccount,
	    AccountingTransactionDetailDTO transactionDetail) {
	checkPreconditionsToProcess(event);
	return super.internalProcess(user, entryDTOs, event, fromAccount, toAccount, transactionDetail);
    }

    private void checkPreconditionsToProcess(Event event) {
	final DfaRegistrationEvent dfaRegistrationEvent = (DfaRegistrationEvent) event;
	if (!dfaRegistrationEvent.hasRegistrationPeriodInDegreeCurricularPlan()) {
	    throw new DomainException(
		    "error.accounting.postingRules.dfa.DfaRegistrationPR.cannot.process.without.registration.period.defined");
	}
    }

}
