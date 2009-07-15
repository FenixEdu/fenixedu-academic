package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.organizationalStructure.AccountabilityTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.EmployeeContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.Month;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.YearMonthDay;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class EmployeeWorkSheet implements Serializable {

    private static final DecimalFormat UNIT_FORMAT = new DecimalFormat("0000");

    Employee employee;

    String unitCode;

    Unit unit;

    List<WorkDaySheet> workDaySheetList;

    Duration totalBalance;

    Duration unjustifiedBalance;

    Duration weeklyRest;

    Duration complementaryWeeklyRest;

    Duration holidayRest;

    Duration nightWork;

    Duration balanceToCompensate;

    AssiduousnessStatusHistory lastAssiduousnessStatusHistory;

    Integer unjustifiedDays;

    Integer accumulatedUnjustifiedDays;

    Integer accumulatedArticle66Days;

    public EmployeeWorkSheet() {
    }

    public EmployeeWorkSheet(Employee employee, LocalDate beginDate, LocalDate endDate) {
	setEmployee(employee);
	setLastAssiduousnessStatusHistory(beginDate, endDate);
	Unit unit = employee.getLastWorkingPlace(new YearMonthDay(beginDate), new YearMonthDay(endDate));
	EmployeeContract lastMailingContract = (EmployeeContract) employee
		.getLastContractByContractType(AccountabilityTypeEnum.MAILING_CONTRACT);
	if (lastMailingContract != null && lastMailingContract.getMailingUnit() != null) {
	    unit = lastMailingContract.getMailingUnit();
	}
	setUnit(unit);
	if (unit != null) {
	    setUnitCode(UNIT_FORMAT.format(unit.getCostCenterCode()));
	} else {
	    setUnitCode("");
	}
    }

    public String getFormattedDate() {
	ResourceBundle bundleEnumeration = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
	WorkDaySheet workDaySheet = workDaySheetList.get(0);
	StringBuilder stringBuilder = new StringBuilder();
	if (workDaySheet != null) {
	    String month = bundleEnumeration.getString(Month.values()[workDaySheet.getDate().getMonthOfYear() - 1].getName());
	    stringBuilder.append(month).append(" ").append(workDaySheet.getDate().getYear());
	}
	return stringBuilder.toString();
    }

    public String getTotalBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalTotalBalance = new MutablePeriod(getTotalBalance().getMillis(), PeriodType.time());
	if (getTotalBalance().toPeriod().getMinutes() < 0) {
	    finalTotalBalance.setMinutes(-getTotalBalance().toPeriod().getMinutes());
	    if (getTotalBalance().toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours().appendSeparator(":")
			.minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalTotalBalance);
    }

    public String getUnjustifiedBalanceString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalUnjustifiedBalance = new MutablePeriod(getUnjustifiedBalance().getMillis(), PeriodType.time());
	return fmt.print(finalUnjustifiedBalance);
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public String getUnitCode() {
	return unitCode;
    }

    public void setUnitCode(String unitCode) {
	this.unitCode = unitCode;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public Duration getTotalBalance() {
	return totalBalance == null ? Duration.ZERO : totalBalance;
    }

    public void setTotalBalance(Duration totalBalance) {
	this.totalBalance = totalBalance;
    }

    public void addTotalBalance(Duration balance) {
	this.totalBalance = this.totalBalance == null ? balance : this.totalBalance.plus(balance);
    }

    public Duration getUnjustifiedBalance() {
	return unjustifiedBalance == null ? Duration.ZERO : unjustifiedBalance;
    }

    public void setUnjustifiedBalance(Duration unjustifiedBalance) {
	this.unjustifiedBalance = unjustifiedBalance;
    }

    public void addUnjustifiedBalance(Duration balance) {
	this.unjustifiedBalance = this.unjustifiedBalance == null ? balance : this.unjustifiedBalance.plus(balance);
    }

    public List<WorkDaySheet> getWorkDaySheetList() {
	if (workDaySheetList == null) {
	    workDaySheetList = new ArrayList<WorkDaySheet>();
	}
	return workDaySheetList;
    }

    public void setWorkDaySheetList(List<WorkDaySheet> workDaySheetList) {
	this.workDaySheetList = workDaySheetList;
    }

    public Duration getComplementaryWeeklyRest() {
	return complementaryWeeklyRest == null ? Duration.ZERO : complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
	this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public void addComplementaryWeeklyRest(Duration balance) {
	this.complementaryWeeklyRest = this.complementaryWeeklyRest == null ? balance : this.complementaryWeeklyRest
		.plus(balance);
    }

    public Duration getWeeklyRest() {
	return weeklyRest == null ? Duration.ZERO : weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
	this.weeklyRest = weeklyRest;
    }

    public void addWeeklyRest(Duration balance) {
	this.weeklyRest = this.weeklyRest == null ? balance : this.weeklyRest.plus(balance);
    }

    public Duration getNightWork() {
	return nightWork;
    }

    public void setNightWork(Duration nightWork) {
	this.nightWork = nightWork;
    }

    public String getComplementaryWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalComplementaryWeeklyRestWork = new MutablePeriod(getComplementaryWeeklyRest().getMillis(), PeriodType
		.time());
	return fmt.print(finalComplementaryWeeklyRestWork);
    }

    public String getWeeklyRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalWeeklyRestExtraWork = new MutablePeriod(getWeeklyRest().getMillis(), PeriodType.time());
	return fmt.print(finalWeeklyRestExtraWork);
    }

    public String getHolidayRestString() {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalHolidayRestExtraWork = new MutablePeriod(getHolidayRest().getMillis(), PeriodType.time());
	return fmt.print(finalHolidayRestExtraWork);
    }

    public Duration getHolidayRest() {
	return holidayRest == null ? Duration.ZERO : holidayRest;
    }

    public void setHolidayRest(Duration holidayWork) {
	this.holidayRest = holidayWork;
    }

    public void addHolidayRest(Duration balance) {
	this.holidayRest = this.holidayRest == null ? balance : this.holidayRest.plus(balance);
    }

    public Duration getBalanceToCompensate() {
	return balanceToCompensate;
    }

    public void setBalanceToCompensate(Duration balanceToCompensate) {
	this.balanceToCompensate = balanceToCompensate;
    }

    public AssiduousnessStatusHistory getLastAssiduousnessStatusHistory() {
	return lastAssiduousnessStatusHistory;
    }

    public void setLastAssiduousnessStatusHistory(LocalDate beginDate, LocalDate endDate) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : getEmployee().getAssiduousness().getStatusBetween(beginDate,
		endDate)) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		if (lastAssiduousnessStatusHistory == null
			|| assiduousnessStatusHistory.getEndDate().isAfter(lastAssiduousnessStatusHistory.getEndDate())) {
		    this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		}
	    } else {
		this.lastAssiduousnessStatusHistory = assiduousnessStatusHistory;
		break;
	    }
	}
    }

    public void addWorkDaySheets(AssiduousnessClosedMonth assiduousnessClosedMonth, LocalDate beginDate, LocalDate endDate) {
	if (assiduousnessClosedMonth != null) {
	    Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusMillis(1));
	    for (AssiduousnessClosedDay assiduousnessClosedDay : assiduousnessClosedMonth.getAssiduousnessClosedDays()) {
		if (interval.contains(assiduousnessClosedDay.getDay().toDateTimeAtStartOfDay())) {
		    getWorkDaySheetList().add(new WorkDaySheet(assiduousnessClosedDay));
		}
	    }
	    Collections.sort(getWorkDaySheetList(), new BeanComparator("date"));
	    addTotalBalance(assiduousnessClosedMonth.getBalance());
	    addUnjustifiedBalance(assiduousnessClosedMonth.getTotalUnjustifiedBalance());
	    addComplementaryWeeklyRest(assiduousnessClosedMonth.getSaturdayBalance());
	    addWeeklyRest(assiduousnessClosedMonth.getSundayBalance());
	    addHolidayRest(assiduousnessClosedMonth.getHolidayBalance());
	    setUnjustifiedDays(assiduousnessClosedMonth.getUnjustifiedDays());
	    setAccumulatedUnjustifiedDays(assiduousnessClosedMonth.getAccumulatedUnjustifiedDays());
	    setAccumulatedArticle66Days(assiduousnessClosedMonth.getAccumulatedArticle66Days());
	}
    }

    public Integer getUnjustifiedDays() {
	return unjustifiedDays;
    }

    public void setUnjustifiedDays(Integer unjustifiedDays) {
	this.unjustifiedDays = unjustifiedDays;
    }

    public Integer getAccumulatedUnjustifiedDays() {
	return accumulatedUnjustifiedDays;
    }

    public void setAccumulatedUnjustifiedDays(Integer accumulatedUnjustifiedDays) {
	this.accumulatedUnjustifiedDays = accumulatedUnjustifiedDays;
    }

    public Integer getAccumulatedArticle66Days() {
	return accumulatedArticle66Days;
    }

    public void setAccumulatedArticle66Days(Integer accumulatedArticle66Days) {
	this.accumulatedArticle66Days = accumulatedArticle66Days;
    }

}
