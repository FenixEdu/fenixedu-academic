package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;
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
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.Interval;

public class ResidencePR extends ResidencePR_Base {

    public ResidencePR(final EntryType entryType, final EventType eventType, final DateTime startDate, final DateTime endDate,
	    final ServiceAgreementTemplate serviceAgreementTemplate) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	Money amounToPay = event.calculateAmountToPay(when);
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), amounToPay,
		amounToPay, event.getDescriptionForEntryType(getEntryType()), amounToPay));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	ResidenceEvent residenceEvent = (ResidenceEvent) event;
	Money baseValue = residenceEvent.getRoomValue();
	if (residenceEvent.getLimitPaymentDate().isAfter(when)) {
	    return baseValue;
	}
	return baseValue.add(getPenaltyPerDay().multiply(
		BigDecimal.valueOf(new Interval(getEndDate(), when).toPeriod().getDays())));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
	
	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.accounting.postingRules.residencePR.invalid.number.of.entryDTOs");
	}

	final EntryDTO entryDTO = entryDTOs.iterator().next();

	checkIfCanAddAmount(entryDTO.getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, entryDTO.getEntryType(),
		entryDTO.getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToPay, Event event, DateTime whenRegistered) {
	if (!event.calculateAmountToPay(whenRegistered).equals(amountToPay)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.amount.being.payed.must.be.equal.to.amout.in.debt", event
			    .getDescriptionForEntryType(getEntryType()));
	}
    }

}
