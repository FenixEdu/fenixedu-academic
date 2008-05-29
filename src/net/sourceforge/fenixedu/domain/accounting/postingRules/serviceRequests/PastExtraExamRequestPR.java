package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.util.Collections;
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
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PastEquivalencePlanRevisionRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PastExtraExamRequestPR extends PastExtraExamRequestPR_Base {

    protected PastExtraExamRequestPR() {
	super();
    }

    public PastExtraExamRequestPR(final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	super.init(EntryType.EXTRA_EXAM_REQUEST_FEE, EventType.PAST_EXTRA_EXAM_REQUEST, startDate, endDate,
		serviceAgreementTemplate);
    }

    @Override
    public Money calculateTotalAmountToPay(final Event event, final DateTime when, final boolean applyDiscount) {
	return ((PastEquivalencePlanRevisionRequestEvent) event).getPastAmount();
    }

    @Override
    public List<EntryDTO> calculateEntries(final Event event, final DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
		.getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
		.calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(final User user, final List<EntryDTO> entryDTOs, final Event event,
	    final Account fromAccount, final Account toAccount, final AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.accounting.postingRules.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.get(0).getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
		.get(0).getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(final Money amountToPay, final Event event, final DateTime when) {
	if (event.calculateAmountToPay(when).greaterThan(amountToPay)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.amount.being.payed.must.be.equal.to.amout.in.debt", event
			    .getDescriptionForEntryType(getEntryType()));
	}
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
