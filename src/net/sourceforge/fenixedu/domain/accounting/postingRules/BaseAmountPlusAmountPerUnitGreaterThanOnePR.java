package net.sourceforge.fenixedu.domain.accounting.postingRules;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public abstract class BaseAmountPlusAmountPerUnitGreaterThanOnePR extends
	BaseAmountPlusAmountPerUnitGreaterThanOnePR_Base {

    protected BaseAmountPlusAmountPerUnitGreaterThanOnePR() {
	super();
    }

    protected void init(EntryType entryType, EventType eventType, DateTime startDate, DateTime endDate,
	    ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount,
	    Money amountPerUnit) {
	super.init(entryType, eventType, startDate, endDate, serviceAgreementTemplate, baseAmount,
		amountPerUnit);
    }

    @Override
    public Money calculateTotalAmountToPay(Event event, DateTime when) {
	return ((getNumberOfUnits(event) > 1) ? getBaseAmount().add(
		getAmountPerUnit().multiply(new BigDecimal(getNumberOfUnits(event) - 1)))
		: getBaseAmount());

    }

}
