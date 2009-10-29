package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.Partial;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class EmployeeBalanceResume implements Serializable {

    private static final String NEGATIVE_HOUR = "-";

    private static final String HOUR_SEPARATOR = ":";

    AssiduousnessStatusHistory assiduousnessStatusHistory;

    Duration monthlyBalance = Duration.ZERO;

    Duration monthlyBalanceToCompensate = Duration.ZERO;

    Duration finalMonthlyBalance = Duration.ZERO;

    Duration previousAnualBalance = Duration.ZERO;

    Duration anualBalanceToCompensate = Duration.ZERO;

    Duration finalAnualBalance = Duration.ZERO;

    Duration futureBalanceToCompensate = Duration.ZERO;

    public EmployeeBalanceResume() {
    }

    public EmployeeBalanceResume(Duration thisBalance, Duration thisBalanceToCompensate, LocalDate thisMonth,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	Partial partial = new Partial().with(DateTimeFieldType.monthOfYear(), thisMonth.getMonthOfYear()).with(
		DateTimeFieldType.year(), thisMonth.getYear());
	setEmployeeBalanceResume(thisBalance, thisBalanceToCompensate, partial, assiduousnessStatusHistory);
    }

    public EmployeeBalanceResume(Duration thisBalance, Duration thisBalanceToCompensate, Partial thisMonth,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	setEmployeeBalanceResume(thisBalance, thisBalanceToCompensate, thisMonth, assiduousnessStatusHistory);
    }

    public void setEmployeeBalanceResume(AssiduousnessClosedMonth assiduosunessClosedMonth) {
	setEmployeeBalanceResume(assiduosunessClosedMonth.getBalance(), assiduosunessClosedMonth.getBalanceToDiscount(),
		assiduosunessClosedMonth.getClosedMonth().getClosedYearMonth(), assiduosunessClosedMonth
			.getAssiduousnessStatusHistory());
    }

    public void setEmployeeBalanceResume(Duration thisBalance, Duration thisBalanceToCompensate, Partial thisMonth,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	setAssiduousnessStatusHistory(assiduousnessStatusHistory);
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

	AssiduousnessClosedMonth previousAssiduousnessClosedMonth = getPreviousAssiduousnessClosedMonth(new YearMonth(thisMonth),
		assiduousnessStatusHistory);
	if (previousAssiduousnessClosedMonth != null) {
	    previousAnualBalance = previousAssiduousnessClosedMonth.getFinalBalance();
	    anualBalanceToCompensate = previousAssiduousnessClosedMonth.getFinalBalanceToCompensate();
	}
	if (thisBalanceToCompensate.isLongerThan(Duration.ZERO) && getMonthlyBalance().isShorterThan(thisBalanceToCompensate)) {
	    if (getMonthlyBalance().isLongerThan(Duration.ZERO)) {
		futureBalanceToCompensate = thisBalanceToCompensate.minus(getMonthlyBalance());
	    } else {
		futureBalanceToCompensate = thisBalanceToCompensate;
	    }
	}
	futureBalanceToCompensate = futureBalanceToCompensate.plus(anualBalanceToCompensate);

	finalAnualBalance = previousAnualBalance.plus(thisBalance).plus(remainingBalanceToCompansate);
	if (futureBalanceToCompensate.isLongerThan(Duration.ZERO) && finalAnualBalance.isLongerThan(Duration.ZERO)) {
	    long finalBalanceLong = Math.max(finalAnualBalance.minus(futureBalanceToCompensate).getMillis(), 0);
	    long balanceToCompensateInFutureLong = Math.max(futureBalanceToCompensate.minus(finalAnualBalance).getMillis(), 0);
	    finalAnualBalance = new Duration(finalBalanceLong);
	    futureBalanceToCompensate = new Duration(balanceToCompensateInFutureLong);
	}

    }

    private AssiduousnessClosedMonth getPreviousAssiduousnessClosedMonth(YearMonth yearMonth,
	    AssiduousnessStatusHistory assiduousnessStatusHistory) {
	if (yearMonth.getMonth().getNumberOfMonth() != 1) {
	    yearMonth.subtractMonth();
	    ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(yearMonth);
	    if (closedMonth != null) {
		return closedMonth.getAssiduousnessClosedMonth(assiduousnessStatusHistory);
	    }
	}
	return null;
    }

    public Duration getAnualBalance() {
	return previousAnualBalance;
    }

    public void setAnualBalance(Duration anualBalance) {
	this.previousAnualBalance = anualBalance;
    }

    public Duration getAnualBalanceToCompensate() {
	return anualBalanceToCompensate;
    }

    public void setAnualBalanceToCompensate(Duration anualBalanceToCompensate) {
	this.anualBalanceToCompensate = anualBalanceToCompensate;
    }

    public AssiduousnessStatusHistory getAssiduousnessStatusHistory() {
	return assiduousnessStatusHistory;
    }

    public void setAssiduousnessStatusHistory(AssiduousnessStatusHistory assiduousnessStatusHistory) {
	this.assiduousnessStatusHistory = assiduousnessStatusHistory;
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
	return getDurationString(previousAnualBalance);
    }

    public String getAnualBalanceToCompensateString() {
	return getDurationString(anualBalanceToCompensate);
    }

    public String getFinalAnualBalanceString() {
	return getDurationString(finalAnualBalance);
    }

    public String getFinalMonthlyBalanceString() {
	return getDurationString(finalMonthlyBalance);
    }

    public String getFutureBalanceToCompensateString() {
	return getDurationString(futureBalanceToCompensate);
    }

    public String getMonthlyBalanceString() {
	return getDurationString(monthlyBalance);
    }

    public String getMonthlyBalanceToCompensateString() {
	return getDurationString(monthlyBalanceToCompensate);
    }

    public String getDurationString(Duration duration) {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(HOUR_SEPARATOR)
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(duration.getMillis(), PeriodType.time());
	if (duration.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-duration.toPeriod().getMinutes());
	    if (duration.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral(NEGATIVE_HOUR).appendHours().appendSeparator(
			HOUR_SEPARATOR).minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }
}
