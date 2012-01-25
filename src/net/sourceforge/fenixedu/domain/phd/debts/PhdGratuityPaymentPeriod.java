package net.sourceforge.fenixedu.domain.phd.debts;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;

import pt.ist.fenixWebFramework.services.Service;

public class PhdGratuityPaymentPeriod extends PhdGratuityPaymentPeriod_Base {

    public PhdGratuityPaymentPeriod(int dayStart, int monthStart, int dayEnd, int monthEnd, int dayLastPayment,
	    int monthLastPayment) {
	super();
	init(new Partial().with(DateTimeFieldType.monthOfYear(), monthStart).with(DateTimeFieldType.dayOfYear(), dayStart),
		new Partial().with(DateTimeFieldType.monthOfYear(), monthEnd).with(DateTimeFieldType.dayOfYear(), dayEnd),
		new Partial().with(DateTimeFieldType.monthOfYear(), monthLastPayment).with(DateTimeFieldType.dayOfYear(),
			dayLastPayment));
    }

    protected void init(Partial start, Partial end, Partial lastPaymentDay) {
	setStart(start);
	setEnd(end);
	setLastPayment(lastPaymentDay);
    }

    public PhdGratuityPaymentPeriod(Partial start, Partial end, Partial lastPaymentDay) {
	super();
	init(start,end,lastPaymentDay);
    }

    public int getMonthStart() {
	return getStart().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayStart() {
	return getStart().get(DateTimeFieldType.dayOfMonth());
    }

    public int getMonthEnd() {
	return getEnd().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayEnd() {
	return getEnd().get(DateTimeFieldType.dayOfMonth());
    }

    public int getMonthLastPayment() {
	return getLastPayment().get(DateTimeFieldType.monthOfYear());
    }

    public int getDayLastPayment() {
	return getLastPayment().get(DateTimeFieldType.dayOfMonth());
    }

    public boolean contains(LocalDate date) {
	LocalDate start = new LocalDate(date.getYear(), getMonthStart(), getDayStart());
	LocalDate end = new LocalDate(date.getYear(), getMonthEnd(), getDayEnd());

	if ((date.equals(start) || date.isAfter(start)) && (date.equals(end) || date.isBefore(end))) {
	    return true;
	} else {
	    return false;
	}
    }

    public Money fine(double fineRate, Money amount, DateTime when) {
	LocalDate whenPaying = new LocalDate(when.getYear(), when.monthOfYear().get(), when.dayOfMonth().get());
	LocalDate lastPaymentDay = new LocalDate(when.getYear(), getMonthLastPayment(), getDayLastPayment());

	if (whenPaying.isAfter(lastPaymentDay)) {
	    int monthsOut = when.getMonthOfYear() - lastPaymentDay.getMonthOfYear();

	    // if is in the same month, and a day has passed, at least it
	    // counts for one month
	    if (monthsOut == 0) {
		monthsOut = 1;
	    }
	    return new Money(amount.getAmount().multiply(new BigDecimal(fineRate * monthsOut)));
	} else {
	    return new Money(0);
	}
    }

    /*
     * @Service public static final PhdGratuityPaymentPeriod
     * makeNewFirstStandardPeriod() { return new PhdGratuityPaymentPeriod(1, 1,
     * 30, 6, 31, 8); }
     * 
     * @Service public static final PhdGratuityPaymentPeriod
     * makeNewSecondStandardPeriod() { return new PhdGratuityPaymentPeriod(1, 7,
     * 31, 12, 28, 2); }
     */

    public void delete() {
	removeRootDomainObject();
	removePostingRule();
	super.deleteDomainObject();
    }
}
