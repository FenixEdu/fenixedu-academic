package net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity;

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
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PastDegreeGratuityEvent;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.DegreeCurricularPlanServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class PastDegreeGratuityPR extends PastDegreeGratuityPR_Base {

    protected PastDegreeGratuityPR() {

    }

    public PastDegreeGratuityPR(DateTime startDate, DateTime endDate,
	    DegreeCurricularPlanServiceAgreementTemplate serviceAgreementTemplate) {
	super.init(EntryType.GRATUITY_FEE, EventType.GRATUITY, startDate, endDate, serviceAgreementTemplate);
    }

    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(getEntryType(), event, calculateTotalAmountToPay(event, when), event
		.getPayedAmount(), event.calculateAmountToPay(when), event.getDescriptionForEntryType(getEntryType()), event
		.calculateAmountToPay(when)));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	return ((PastDegreeGratuityEvent) event).getPastDegreeGratuityAmount();
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount,
	    Account toAccount, AccountingTransactionDetailDTO transactionDetail) {

	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.accounting.postingRules.gratuity.PastDegreeGratuityPR.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.get(0).getAmountToPay(), event, transactionDetail.getWhenRegistered());

	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount, getEntryType(), entryDTOs
		.get(0).getAmountToPay(), transactionDetail));
    }

    private void checkIfCanAddAmount(Money amountToPay, Event event, DateTime when) {
	if (event.calculateAmountToPay(when).greaterThan(amountToPay)) {
	    throw new DomainExceptionWithLabelFormatter(
		    "error.accounting.postingRules.gratuity.PastDegreeGratuityPR.amount.being.payed.must.be.equal.to.amout.in.debt",
		    event.getDescriptionForEntryType(getEntryType()));
	}

    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
