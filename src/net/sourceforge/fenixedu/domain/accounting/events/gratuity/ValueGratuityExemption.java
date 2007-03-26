package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

public class ValueGratuityExemption extends ValueGratuityExemption_Base {

    public ValueGratuityExemption(final Employee employee, final GratuityEvent gratuityEvent,
	    final GratuityExemptionJustificationType gratuityExemptionType, final String reason,
	    final YearMonthDay dispatchDate, final Money value) {
	super();
	init(employee, gratuityEvent, gratuityExemptionType, reason, dispatchDate, value);
    }

    protected void init(Employee employee, GratuityEvent gratuityEvent,
	    GratuityExemptionJustificationType exemptionType, String reason, YearMonthDay dispatchDate,
	    Money value) {

	checkParameters(value);
	super.setValue(value);

	super.init(employee, gratuityEvent, exemptionType, reason, dispatchDate);
    }

    private void checkParameters(Money value) {
	if (value == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.ValueGratuityExemption.value.cannot.be.null");
	}
    }

    @Override
    public void setValue(Money value) {
	throw new DomainException(
		"error.accounting.events.gratuity.ValueGratuityExemption.cannot.modify.value");
    }

    @Override
    public BigDecimal calculateDiscountPercentage(Money amount) {
	final BigDecimal amountToDiscount = new BigDecimal(getValue().toString());
	return amountToDiscount.divide(amount.getAmount(), 10, RoundingMode.HALF_EVEN);
    }

}
