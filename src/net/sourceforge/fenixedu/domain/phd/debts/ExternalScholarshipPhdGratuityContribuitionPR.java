package net.sourceforge.fenixedu.domain.phd.debts;

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
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class ExternalScholarshipPhdGratuityContribuitionPR extends ExternalScholarshipPhdGratuityContribuitionPR_Base {

    public ExternalScholarshipPhdGratuityContribuitionPR() {
	super();

    }

    public ExternalScholarshipPhdGratuityContribuitionPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate) {
	this();
	init(EventType.EXTERNAL_SCOLARSHIP, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	return ((ExternalScholarshipPhdGratuityContribuitionEvent) event).getPhdGratuityExternalScholarshipExemption().getValue();
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(EntryType.EXTERNAL_SCOLARSHIP_PAYMENT, event, calculateTotalAmountToPay(event,
		when), event.getPayedAmount(), event.calculateAmountToPay(when), event
		.getDescriptionForEntryType(EntryType.EXTERNAL_SCOLARSHIP_PAYMENT), event.calculateAmountToPay(when)));
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, Collection<EntryDTO> entryDTOs, Event event,
	    Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException("error.accounting.postingRules.gratuity.PhdGratuityPR.invalid.number.of.entryDTOs");
	}	

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		EntryType.EXTERNAL_SCOLARSHIP_PAYMENT, entryDTOs.iterator().next().getAmountToPay(), transactionDetail));
    }
    
    @Override
    public AccountingTransaction depositAmount(User responsibleUser, Event event, Account fromAcount, Account toAccount,
            Money amount, AccountingTransactionDetailDTO transactionDetailDTO) {
        return depositAmount(responsibleUser, event, fromAcount, toAccount, amount, EntryType.EXTERNAL_SCOLARSHIP_PAYMENT, transactionDetailDTO);
    }
}
