package net.sourceforge.fenixedu.domain.accounting.postingRules;

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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class FixedAmountPR extends FixedAmountPR_Base {

    protected FixedAmountPR() {
	super();
    }

    public FixedAmountPR(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
	this();
	init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, fixedAmount);
    }

    private void checkParameters(Money fixedAmount) {
	if (fixedAmount == null) {
	    throw new DomainException(
		    "error.accounting.postingRules.FixedAmountPR.fixedAmount.cannot.be.null");
	}

    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money fixedAmount) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
	checkParameters(fixedAmount);
	super.setFixedAmount(fixedAmount);
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs,
	    Event event, Account fromAccount, Account toAccount,
	    AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.FixedAmountPR.invalid.number.of.entryDTOs");
	}

	final EntryDTO entryDTO = entryDTOs.get(0);
	checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		entryDTO.getEntryType(), entryDTO.getAmountToPay(), transactionDetail));

    }

    @Override
    public void setFixedAmount(Money fixedAmount) {
	throw new DomainException(
		"error.accounting.postingRules.FixedAmountPR.cannot.modify.fixedAmount");
    }

    private void checkIfCanAddAmount(Money amountToPay, final Event event, final DateTime when) {
	if (amountToPay.compareTo(calculateTotalAmountToPay(event, when)) != 0) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.FixedAmountPR.amount.being.payed.must.match.amount.to.pay",
		    event.getDescriptionForEntryType(getEntryType()));
	}
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	final Money totalAmountToPay = calculateTotalAmountToPay(event, when);
	return Collections.singletonList(new EntryDTO(getEntryType(), event, totalAmountToPay,
		Money.ZERO, totalAmountToPay, event.getDescriptionForEntryType(getEntryType()),
		totalAmountToPay));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	return getFixedAmount();
    }

    @Checked("PostingRulePredicates.editPredicate")
    public FixedAmountPR edit(final Money fixedAmount) {

	deactivate();
	return new FixedAmountPR(getEntryType(), getEventType(), new DateTime().minus(1000), null,
		getServiceAgreementTemplate(), fixedAmount);
    }

}
