package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

public class PercentageGratuityExemption extends PercentageGratuityExemption_Base {

    public PercentageGratuityExemption(final Employee employee, final GratuityEvent gratuityEvent,
	    final GratuityExemptionJustificationType gratuityExemptionJustificationType,
	    final String reason, final YearMonthDay dispatchDate, final BigDecimal percentage) {
	super();
	init(employee, gratuityExemptionJustificationType, reason, dispatchDate, gratuityEvent,
		percentage);
    }

    protected void init(Employee employee, GratuityExemptionJustificationType exemptionType,
	    String reason, YearMonthDay dispatchDate, GratuityEvent gratuityEvent, BigDecimal percentage) {

	checkParameters(percentage);
	super.setPercentage(percentage);

	super.init(employee, gratuityEvent, exemptionType, reason, dispatchDate);
    }

    private void checkParameters(BigDecimal percentage) {
	if (percentage == null) {
	    throw new DomainException(
		    "error.accounting.events.gratuity.PercentageGratuityExemption.percentage.cannot.be.null");
	}
    }

    @Override
    public void setPercentage(BigDecimal percentage) {
	throw new DomainException(
		"error.accounting.events.gratuity.PercentageGratuityExemption.cannot.modify.percentage");
    }

    @Override
    public BigDecimal calculateDiscountPercentage(Money amount) {
	return getPercentage();
    }

    public String getFormattedPercentage() {
	return getPercentage().multiply(BigDecimal.valueOf(100)).toPlainString();
    }

}
