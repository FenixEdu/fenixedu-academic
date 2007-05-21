package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.util.report.StyledExcelSpreadsheet;

import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class AssiduousnessMonthlyResume implements Serializable {

    Employee employee;

    Duration totalBalance;

    Duration unjustifiedBalance;

    Duration saturdaysBalance;

    Duration sundaysBalance;

    Duration holidayBalance;

    Duration nightlyBalance;

    public AssiduousnessMonthlyResume() {
    }

    public AssiduousnessMonthlyResume(Employee employee, Duration totalBalance,
	    Duration totalComplementaryWeeklyRestBalance, Duration totalWeeklyRestBalance,
	    Duration holidayRest, Duration nightWork, Duration unjustified) {
	setEmployee(employee);
	setTotalBalance(totalBalance);
	setUnjustifiedBalance(unjustified);
	setSaturdaysBalance(totalWeeklyRestBalance);
	setSundaysBalance(totalComplementaryWeeklyRestBalance);
	setHolidayBalance(holidayRest);
	setNightlyBalance(nightWork);
    }

    public AssiduousnessMonthlyResume(AssiduousnessClosedMonth assiduousnessClosedMonth) {
	setEmployee(assiduousnessClosedMonth.getAssiduousness().getEmployee());
	setTotalBalance(assiduousnessClosedMonth.getBalance());
	setUnjustifiedBalance(assiduousnessClosedMonth.getTotalUnjustifiedBalance());
	setSaturdaysBalance(assiduousnessClosedMonth.getSaturdayBalance());
	setSundaysBalance(assiduousnessClosedMonth.getSundayBalance());
	setHolidayBalance(assiduousnessClosedMonth.getHolidayBalance());
	setNightlyBalance(assiduousnessClosedMonth.getTotalNightBalance());
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public Duration getTotalBalance() {

	return totalBalance;
    }

    public void setTotalBalance(Duration totalBalance) {
	this.totalBalance = totalBalance;
    }

    public Duration getUnjustifiedBalance() {
	return unjustifiedBalance;
    }

    public void setUnjustifiedBalance(Duration unjustifiedBalance) {
	this.unjustifiedBalance = unjustifiedBalance;
    }

    public Duration getSaturdaysBalance() {
	return saturdaysBalance;
    }

    public void setSaturdaysBalance(Duration saturdaysBalance) {
	this.saturdaysBalance = saturdaysBalance;
    }

    public Duration getSundaysBalance() {
	return sundaysBalance;
    }

    public void setSundaysBalance(Duration sundaysRest) {
	this.sundaysBalance = sundaysRest;
    }

    public Duration getNightlyBalance() {
	return nightlyBalance;
    }

    public void setNightlyBalance(Duration nightlyBalance) {
	this.nightlyBalance = nightlyBalance;
    }

    public Duration getHolidayBalance() {
	return holidayBalance;
    }

    public void setHolidayBalance(Duration holidayWork) {
	this.holidayBalance = holidayWork;
    }

    public static void getExcelHeader(StyledExcelSpreadsheet spreadsheet, ResourceBundle bundle) {
	spreadsheet.newHeaderRow();
	spreadsheet.addHeader(bundle.getString("label.employeeNumber"));
	spreadsheet.addHeader(bundle.getString("label.totalBalance"));
	spreadsheet.addHeader(bundle.getString("label.totalUnjustified"));
	spreadsheet.addHeader(bundle.getString("label.totalSaturday"));
	spreadsheet.addHeader(bundle.getString("label.totalSunday"));
	spreadsheet.addHeader(bundle.getString("label.totalHoliday"));
	spreadsheet.addHeader(bundle.getString("label.totalNight"));
    }

    public void getExcelRow(StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newRow();
	spreadsheet.addCell(getEmployee().getEmployeeNumber().toString());
	spreadsheet.addCell(getTotalBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getTotalBalance()));
	spreadsheet.addCell(getUnjustifiedBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getUnjustifiedBalance()));
	spreadsheet.addCell(getSaturdaysBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getSaturdaysBalance()));
	spreadsheet.addCell(getSundaysBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getSundaysBalance()));
	spreadsheet.addCell(getHolidayBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getHolidayBalance()));
	spreadsheet.addCell(getNightlyBalance().equals(Duration.ZERO) ? ""
		: getDurationString(getNightlyBalance()));
    }

    public String getDurationString(Duration duration) {
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalDuration = new MutablePeriod(duration.getMillis(), PeriodType.time());
	if (duration.toPeriod().getMinutes() < 0) {
	    finalDuration.setMinutes(-duration.toPeriod().getMinutes());
	    if (duration.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
			.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalDuration);
    }

}
