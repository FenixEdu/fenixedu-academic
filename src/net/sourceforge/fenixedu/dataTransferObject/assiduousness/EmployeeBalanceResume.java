package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.Partial;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class EmployeeBalanceResume implements Serializable {

    Employee employee;

    Duration monthlyBalance = Duration.ZERO;

    Duration monthlyBalanceToCompensate = Duration.ZERO;

    Duration finalMonthlyBalance = Duration.ZERO;

    Duration anualBalance = Duration.ZERO;

    Duration anualBalanceToCompensate = Duration.ZERO;

    Duration finalAnualBalance = Duration.ZERO;

    Duration futureBalanceToCompensate = Duration.ZERO;

    public EmployeeBalanceResume(Employee employee) {
	setEmployee(employee);
    }

    public void setEmployeeBalanceResume(Duration thisBalance, Duration thisBalanceToCompensate,
	    Partial thisMonth) {
	setEmployee(employee);
	setMonthlyBalance(thisBalance.plus(thisBalanceToCompensate));
	setMonthlyBalanceToCompensate(thisBalanceToCompensate);
	Duration remainingBalanceToCompansate = Duration.ZERO;
	if (!thisBalance.isShorterThan(Duration.ZERO)) {
	    setFinalMonthlyBalance(thisBalance);
	} else {
	    if (getMonthlyBalance().isLongerThan(Duration.ZERO)) {
		setFinalMonthlyBalance(Duration.ZERO);
		remainingBalanceToCompansate = thisBalanceToCompensate.minus(getMonthlyBalance());
	    } else {
		setFinalMonthlyBalance(getMonthlyBalance());
		remainingBalanceToCompansate = thisBalanceToCompensate;
	    }
	}

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : employee.getAssiduousness()
		.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.year()) == thisMonth.get(DateTimeFieldType.year())
		    && assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
			    DateTimeFieldType.monthOfYear()) < thisMonth.get(DateTimeFieldType
			    .monthOfYear())) {
		if (!assiduousnessClosedMonth.getBalance().isShorterThan(Duration.ZERO)) {
		    anualBalance = anualBalance.plus(assiduousnessClosedMonth.getBalance());
		} else {
		    if (Duration.ZERO.minus(assiduousnessClosedMonth.getBalanceToDiscount())
			    .isLongerThan(assiduousnessClosedMonth.getBalance())) {
			anualBalance = anualBalance
				.plus(assiduousnessClosedMonth.getBalanceToDiscount());
		    } else {
			anualBalance = anualBalance.plus(new Duration(Math.abs(assiduousnessClosedMonth
				.getBalance().getMillis())));
		    }
		}
	    }

	    if (!anualBalance.isShorterThan(Duration.ZERO)) {
		setAnualBalanceToCompensate(remainingBalanceToCompansate);
	    } else {
		setAnualBalanceToCompensate(anualBalance.plus(remainingBalanceToCompansate));
	    }
	    Duration anualBalanceMinusCompensation = anualBalance.plus(getFinalMonthlyBalance()).minus(
		    getAnualBalanceToCompensate());
	    setFinalAnualBalance(anualBalanceMinusCompensation);

	    Duration futureBalanceToCompensate = new Duration(Math.abs(anualBalanceMinusCompensation
		    .getMillis()));
	    if (!anualBalanceMinusCompensation.isShorterThan(Duration.ZERO)) {
		futureBalanceToCompensate = Duration.ZERO;
	    }
	    setFutureBalanceToCompensate(futureBalanceToCompensate);
	}
    }

    public Duration getAnualBalance() {
	return anualBalance;
    }

    public void setAnualBalance(Duration anualBalance) {
	this.anualBalance = anualBalance;
    }

    public Duration getAnualBalanceToCompensate() {
	return anualBalanceToCompensate;
    }

    public void setAnualBalanceToCompensate(Duration anualBalanceToCompensate) {
	this.anualBalanceToCompensate = anualBalanceToCompensate;
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public Duration getFinalAnualBalance() {
	return finalAnualBalance;
    }

    public void setFinalAnualBalance(Duration finalAnualBalance) {
	this.finalAnualBalance = finalAnualBalance;
    }

    public Duration getFinalMonthlyBalance() {
	return finalMonthlyBalance;
    }

    public void setFinalMonthlyBalance(Duration finalMonthlyBalance) {
	this.finalMonthlyBalance = finalMonthlyBalance;
    }

    public Duration getFutureBalanceToCompensate() {
	return futureBalanceToCompensate;
    }

    public void setFutureBalanceToCompensate(Duration futureBalanceToCompensate) {
	this.futureBalanceToCompensate = futureBalanceToCompensate;
    }

    public Duration getMonthlyBalance() {
	return monthlyBalance;
    }

    public void setMonthlyBalance(Duration monthlyBalance) {
	this.monthlyBalance = monthlyBalance;
    }

    public Duration getMonthlyBalanceToCompensate() {
	return monthlyBalanceToCompensate;
    }

    public void setMonthlyBalanceToCompensate(Duration monthlyBalanceToCompensate) {
	this.monthlyBalanceToCompensate = monthlyBalanceToCompensate;
    }

    public String getAnualBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(anualBalance.getMillis(), PeriodType.time());
	if (anualBalance.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-anualBalance.toPeriod().getMinutes());
	    if (anualBalance.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);

    }

    public String getAnualBalanceToCompensateString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(anualBalanceToCompensate.getMillis(), PeriodType
		.time());
	if (anualBalanceToCompensate.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-anualBalanceToCompensate.toPeriod().getMinutes());
	    if (anualBalanceToCompensate.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);

    }

    public String getFinalAnualBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(finalAnualBalance.getMillis(), PeriodType.time());
	if (finalAnualBalance.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-finalAnualBalance.toPeriod().getMinutes());
	    if (finalAnualBalance.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

    public String getFinalMonthlyBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(finalMonthlyBalance.getMillis(), PeriodType.time());
	if (finalMonthlyBalance.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-finalMonthlyBalance.toPeriod().getMinutes());
	    if (finalMonthlyBalance.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

    public String getFutureBalanceToCompensateString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(futureBalanceToCompensate.getMillis(), PeriodType
		.time());
	if (futureBalanceToCompensate.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-futureBalanceToCompensate.toPeriod().getMinutes());
	    if (futureBalanceToCompensate.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

    public String getMonthlyBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(monthlyBalance.getMillis(), PeriodType.time());
	if (monthlyBalance.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-monthlyBalance.toPeriod().getMinutes());
	    if (monthlyBalance.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

    public String getMonthlyBalanceToCompensateString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(monthlyBalanceToCompensate.getMillis(), PeriodType
		.time());
	if (monthlyBalanceToCompensate.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-monthlyBalanceToCompensate.toPeriod().getMinutes());
	    if (monthlyBalanceToCompensate.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

}
