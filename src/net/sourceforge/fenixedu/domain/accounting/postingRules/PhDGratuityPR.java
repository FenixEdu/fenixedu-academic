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
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.PhDGratuityEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.Months;

public class PhDGratuityPR extends PhDGratuityPR_Base {
    
    private PhDGratuityPR() {
        super();
    }

    public PhDGratuityPR(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money gratuityAmount, Money initialPayment, BigDecimal penaltyPercentage) {
	super();
	super.init(EventType.GRATUITY, startDate, endDate,serviceAgreementTemplate);
	setPhdGratuityAmount(gratuityAmount);
	setInitialPayment(initialPayment);
	
    }
    
    public Money getInitialPayment() {
	return getInitialPayment();
    }
    
  
    public boolean hasPenalty(Event event, DateTime when) {
	return ((PhDGratuityEvent)event).hasToBePayedUntil().isBefore(when.toYearMonthDay());
    }
     
    @Override
    public List<EntryDTO> calculateEntries(Event event, DateTime when) {
	return Collections.singletonList(new EntryDTO(EntryType.GRATUITY_FEE, event, calculateTotalAmountToPay(
		event, when), event.getPayedAmount(when), event.calculateAmountToPay(when), event
		.getDescriptionForEntryType(EntryType.GRATUITY_FEE), event.calculateAmountToPay(when)));
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when, boolean applyDiscount) {
	PhDGratuityEvent gratuityEvent = (PhDGratuityEvent) event;
	Money ammount = getPhdGratuityAmount();
	
	if(applyDiscount) {
	   ammount= ammount.subtract(ammount.multiply(gratuityEvent.calculateDiscountPercentage(ammount)));
	}
	if(hasPenalty(event, when)) {
	    ammount = ammount.add(
		    ammount.multiply(
			    new BigDecimal(
				    Months.monthsBetween(
					    ((PhDGratuityEvent)event).hasToBePayedUntil(), when.toYearMonthDay()).size()* getPenaltyPercentage().floatValue()))); 
	}
	
	return ammount;
    }

    @Override
    protected Set<AccountingTransaction> internalProcess(User user, List<EntryDTO> entryDTOs, Event event, Account fromAccount, Account toAccount, AccountingTransactionDetailDTO transactionDetail) {
	
	if (entryDTOs.size() != 1) {
	    throw new DomainException(
		    "error.accounting.postingRules.gratuity.phDGratuityPR.invalid.number.of.entryDTOs");
	}

	checkIfCanAddAmount(entryDTOs.get(0).getAmountToPay(), (PhDGratuityEvent)event, transactionDetail
		.getWhenRegistered());
	    
	return Collections.singleton(makeAccountingTransaction(user, event, fromAccount, toAccount,
		EntryType.GRATUITY_FEE, entryDTOs.get(0).getAmountToPay(), transactionDetail));
	}

    private void checkIfCanAddAmount(Money amountToPay, PhDGratuityEvent event, DateTime whenRegistered) {
	if(!event.isFirstYearOfPhD(whenRegistered)) {
	    if(!amountToPay.equals(getPhdGratuityAmount())) {
		throw new DomainException( "error.accounting.postingRules.gratuity.phDGratuityPR.has.to.be.total.payment");
	    }
	}
	else {
	    if(!(amountToPay.equals(getInitialPayment()) || amountToPay.equals(getPhdGratuityAmount().subtract(getInitialPayment())))) {
		throw new DomainException( "error.accounting.postingRules.gratuity.phDGratuityPR.invalid.ammount.for.first.year");
	    }
	}
    }
    
    public void edit(DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money gratuityAmount, Money initialPayment, BigDecimal penaltyPercentage) {
	
	deactivate();
	new PhDGratuityPR(startDate, endDate, serviceAgreementTemplate, gratuityAmount, initialPayment,penaltyPercentage);
    }
}
