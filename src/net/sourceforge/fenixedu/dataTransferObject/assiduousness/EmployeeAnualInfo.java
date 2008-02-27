package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;

public class EmployeeAnualInfo implements Serializable {

    private Employee employee;

    private YearMonth currentYearMonth;

    private Map<Month, EmployeeMonthInfo> anualInfo;

    private Double totalVacations;

    public EmployeeAnualInfo(Employee employee, YearMonth yearMonth) {
	anualInfo = new HashMap<Month, EmployeeMonthInfo>();
	setEmployee(employee);
	setCurrentYearMonth(new YearMonth(yearMonth.getYear(), yearMonth.getMonth()));
	buildMap(yearMonth);
    }

    private void buildMap(YearMonth yearMonth) {
	for (int iter = Month.DECEMBER.getNumberOfMonth(); iter > 0; iter--) {
	    Month month = Month.fromDateTime(new DateTime(yearMonth.getYear(), iter, 1, 0, 0, 0, 0));
	    anualInfo.put(month, new EmployeeMonthInfo(this, new YearMonth(yearMonth.getYear(), month)));
	}
    }

    public EmployeeMonthInfo getJanuaryInfo() {
	return anualInfo.get(Month.JANUARY);
    }

    public EmployeeMonthInfo getFebruaryInfo() {
	return anualInfo.get(Month.FEBRUARY);
    }

    public EmployeeMonthInfo getMarchInfo() {
	return anualInfo.get(Month.MARCH);
    }

    public EmployeeMonthInfo getAprilInfo() {
	return anualInfo.get(Month.APRIL);
    }

    public EmployeeMonthInfo getMayInfo() {
	return anualInfo.get(Month.MAY);
    }

    public EmployeeMonthInfo getJuneInfo() {
	return anualInfo.get(Month.JUNE);
    }

    public EmployeeMonthInfo getJulyInfo() {
	return anualInfo.get(Month.JULY);
    }

    public EmployeeMonthInfo getAugustInfo() {
	return anualInfo.get(Month.AUGUST);
    }

    public EmployeeMonthInfo getSeptemberInfo() {
	return anualInfo.get(Month.SEPTEMBER);
    }

    public EmployeeMonthInfo getOctoberInfo() {
	return anualInfo.get(Month.OCTOBER);
    }

    public EmployeeMonthInfo getNovemberInfo() {
	return anualInfo.get(Month.NOVEMBER);
    }

    public EmployeeMonthInfo getDecemberInfo() {
	return anualInfo.get(Month.DECEMBER);
    }

    public Employee getEmployee() {
	return employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public EmployeeMonthInfo getCurrentMonthInfo() {
	return anualInfo.get(getCurrentYearMonth().getMonth());
    }

    public YearMonth getCurrentYearMonth() {
	return currentYearMonth;
    }

    public void setCurrentYearMonth(YearMonth currentYearMonth) {
	this.currentYearMonth = currentYearMonth;
    }

    public Double getTotalVacations() {
	return totalVacations;
    }

    public void setTotalVacations(Double totalVacations) {
	this.totalVacations = totalVacations;
    }
}
