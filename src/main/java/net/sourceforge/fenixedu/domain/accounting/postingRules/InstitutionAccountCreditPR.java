package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountingTransaction;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.InstitutionAffiliationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.MicroPaymentEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

/**
 * Rule that does not register as a debt and when processed creates a
 * transaction with whatever value was payed.
 */
public class InstitutionAccountCreditPR extends InstitutionAccountCreditPR_Base {
    public InstitutionAccountCreditPR(ServiceAgreementTemplate template) {
        super();
        super.init(EntryType.INSTITUTION_ACCOUNT_CREDIT, EventType.INSTITUTION_AFFILIATION, new DateTime(), null, template);
    }

    @Override
    protected Money doCalculationForAmountToPay(final Event event, final DateTime when, final boolean applyDiscount) {
        Money result = Money.ZERO;
        final InstitutionAffiliationEvent iaEvent = (InstitutionAffiliationEvent) event;
        for (final MicroPaymentEvent otherEvent : iaEvent.getMicroPaymentEventSet()) {
            result = result.add(otherEvent.getAmount());
        }
        return result;
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
            throw new DomainException("error.accounting.postingRules.InstitutionAccountCreditPR.invalid.number.of.entryDTOs");
        }

        final EntryDTO entryDTO = entryDTOs.iterator().next();
        return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
                EntryType.INSTITUTION_ACCOUNT_CREDIT, entryDTO.getAmountToPay(), transactionDetail));
    }

}
