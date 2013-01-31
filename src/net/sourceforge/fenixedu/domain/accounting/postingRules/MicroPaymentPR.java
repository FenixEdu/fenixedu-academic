package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.Collection;
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
import net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class MicroPaymentPR extends MicroPaymentPR_Base {
	public MicroPaymentPR(ServiceAgreementTemplate template) {
		super();
		super.init(EntryType.MICRO_PAYMENT, EventType.MICRO_PAYMENT, new DateTime(), null, template);
	}

	@Override
	protected Money doCalculationForAmountToPay(Event event, DateTime when, boolean applyDiscount) {
		return ((MicroPaymentEvent) event).getAmount();
	}

	@Override
	protected Money subtractFromExemptions(Event event, DateTime when, boolean applyDiscount, Money amountToPay) {
		return amountToPay;
	}

	@Override
	public List<EntryDTO> calculateEntries(Event event, DateTime when) {
		Money amounToPay = event.calculateAmountToPay(when);
		return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), amounToPay,
				amounToPay, event.getDescriptionForEntryType(getEntryType()), amounToPay));
	}

	@Override
	protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
			Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
		if (entryDTOs.size() != 1) {
			throw new DomainException("error.accounting.postingRules.MicroPaymentPR.invalid.number.of.entryDTOs");
		}

		final EntryDTO entryDTO = entryDTOs.iterator().next();
		Money amount = entryDTO.getAmountToPay();

		checkBudget((MicroPaymentEvent) event, amount);
		return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, EntryType.MICRO_PAYMENT,
				amount, transactionDetail));
	}

	private void checkBudget(MicroPaymentEvent event, Money amountToPay) {
		Money totalCredit = calculateBalance(event);
		if (amountToPay.greaterThan(totalCredit)) {
			throw new DomainException("error.accounting.AccountingTransaction.amount.to.spend.exceeds.account");
		}
	}

	private Money calculateBalance(MicroPaymentEvent event) {
		return event.getAffiliationEvent().calculateBalance();
	}

}
