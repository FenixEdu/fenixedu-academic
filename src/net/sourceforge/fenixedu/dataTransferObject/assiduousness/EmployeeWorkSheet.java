package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.Duration;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class EmployeeWorkSheet implements Serializable {

    Employee employee;

    String unitCode;

    Unit unit;

    List<WorkDaySheet> workDaySheetList;

    Duration totalBalance;

    Duration unjustifiedBalance;

    Duration weeklyRest;

    Duration complementaryWeeklyRest;

    public String getTotalBalanceString() {
        PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
                .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
        MutablePeriod finalTotalBalance = new MutablePeriod(getTotalBalance().getMillis(), PeriodType
                .time());
        if (getTotalBalance().toPeriod().getMinutes() < 0) {
            finalTotalBalance.setMinutes(-getTotalBalance().toPeriod().getMinutes());
            if (getTotalBalance().toPeriod().getHours() == 0) {
                fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours()
                        .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
            }
        }
        return fmt.print(finalTotalBalance);
    }

    public String getUnjustifiedBalanceString() {
        PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
                .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
        MutablePeriod finalUnjustifiedBalance = new MutablePeriod(getUnjustifiedBalance().getMillis(),
                PeriodType.time());
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

    public List<WorkDaySheet> getWorkDaySheetList() {
        return workDaySheetList;
    }

    public void setWorkDaySheetList(List<WorkDaySheet> workDaySheetList) {
        this.workDaySheetList = workDaySheetList;
    }

    public Duration getComplementaryWeeklyRest() {
        return complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
        this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
        return weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
        this.weeklyRest = weeklyRest;
    }

    public String getComplementaryWeeklyRestString() {
        PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
                .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
        MutablePeriod finalComplementaryWeeklyRestWork = new MutablePeriod(getComplementaryWeeklyRest()
                .getMillis(), PeriodType.time());
        return fmt.print(finalComplementaryWeeklyRestWork);
    }

    public String getWeeklyRestString() {
        PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours()
                .appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
        MutablePeriod finalWeeklyRestExtraWork = new MutablePeriod(getWeeklyRest().getMillis(),
                PeriodType.time());
        return fmt.print(finalWeeklyRestExtraWork);
    }

}
