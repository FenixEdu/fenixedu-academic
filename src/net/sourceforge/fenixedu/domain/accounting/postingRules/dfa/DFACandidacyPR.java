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
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DFACandidacyEvent;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountWithPenaltyPR;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DFACandidacyPR extends DFACandidacyPR_Base {

    private DFACandidacyPR() {
	super();
    }

    public DFACandidacyPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount,
	    Money fixedAmountPenalty) {
	super.init(EntryType.CANDIDACY_ENROLMENT_FEE, EventType.CANDIDACY_ENROLMENT, startDate, endDate,
		serviceAgreementTemplate, fixedAmount, fixedAmountPenalty);

    }

    @Override
    protected boolean hasPenalty(Event event, DateTime when) {
	final DFACandidacyEvent dfaCandidacyEvent = (DFACandidacyEvent) event;
	return dfaCandidacyEvent.hasCandidacyPeriodInDegreeCurricularPlan()
		&& !dfaCandidacyEvent.getCandidacyPeriodInDegreeCurricularPlan().containsDate(
			dfaCandidacyEvent.getCandidacyDate());

    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountWithPenaltyPR edit(final Money fixedAmount, final Money penaltyAmount) {

	deactivate();

	return new DFACandidacyPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(),
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
	final DFACandidacyEvent dfaCandidacyEvent = (DFACandidacyEvent) event;
	if (!dfaCandidacyEvent.hasCandidacyPeriodInDegreeCurricularPlan()) {
	    throw new DomainException(
		    "error.accounting.postingRules.dfa.DFACandidacyPR.cannot.process.without.candidacy.period.defined");
	}
    }

}
