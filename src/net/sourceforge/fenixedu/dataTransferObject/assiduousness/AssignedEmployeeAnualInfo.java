package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTime;

public class AssignedEmployeeAnualInfo implements Serializable {

    private Employee employee;

    private YearMonth currentYearMonth;

    private Map<Month, AssignedEmployeeMonthInfo> anualInfo;
    
    private Double totalVacations;
    
    public AssignedEmployeeAnualInfo(Employee employee, YearMonth yearMonth) {
        anualInfo = new HashMap<Month, AssignedEmployeeMonthInfo>();
        setEmployee(employee);
        setCurrentYearMonth(new YearMonth(yearMonth.getYear(),yearMonth.getMonth()));
        buildMap(yearMonth);
    }

    private void buildMap(YearMonth yearMonth) {
        for (int iter = Month.DECEMBER.getNumberOfMonth(); iter > 0; iter--) {
            Month month = Month.fromDateTime(new DateTime(yearMonth.getYear(), iter, 1, 0, 0, 0, 0));
            anualInfo.put(month, new AssignedEmployeeMonthInfo(this, new YearMonth(yearMonth.getYear(),
                    month)));
        }
    }

    public AssignedEmployeeMonthInfo getJanuaryInfo() {
        return anualInfo.get(Month.JANUARY);
    }

    public AssignedEmployeeMonthInfo getFebruaryInfo() {
        return anualInfo.get(Month.FEBRUARY);
    }

    public AssignedEmployeeMonthInfo getMarchInfo() {
        return anualInfo.get(Month.MARCH);
    }

    public AssignedEmployeeMonthInfo getAprilInfo() {
        return anualInfo.get(Month.APRIL);
    }

    public AssignedEmployeeMonthInfo getMayInfo() {
        return anualInfo.get(Month.MAY);
    }

    public AssignedEmployeeMonthInfo getJuneInfo() {
        return anualInfo.get(Month.JUNE);
    }

    public AssignedEmployeeMonthInfo getJulyInfo() {
        return anualInfo.get(Month.JULY);
    }

    public AssignedEmployeeMonthInfo getAugustInfo() {
        return anualInfo.get(Month.AUGUST);
    }

    public AssignedEmployeeMonthInfo getSeptemberInfo() {
        return anualInfo.get(Month.SEPTEMBER);
    }

    public AssignedEmployeeMonthInfo getOctoberInfo() {
        return anualInfo.get(Month.OCTOBER);
    }

    public AssignedEmployeeMonthInfo getNovemberInfo() {
        return anualInfo.get(Month.NOVEMBER);
    }

    public AssignedEmployeeMonthInfo getDecemberInfo() {
        return anualInfo.get(Month.DECEMBER);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public AssignedEmployeeMonthInfo getCurrentMonthInfo() {
        return anualInfo.get(getCurrentYearMonth().getMonth());
    }

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }

    public void setCurrentYearMonth(YearMonth currentYearMonth) {
        this.currentYearMonth = currentYearMonth;
    }

    public void decreaseCurrentMonth() {
        getCurrentYearMonth().setMonth(Month.values()[getCurrentYearMonth().getNumberOfMonth() - 1 - 1]);
    }

    public Double getTotalVacations() {
        return totalVacations;
    }

    public void setTotalVacations(Double totalVacations) {
        this.totalVacations = totalVacations;
    }
}
