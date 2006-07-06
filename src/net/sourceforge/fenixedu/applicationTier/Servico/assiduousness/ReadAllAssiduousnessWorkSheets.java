package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.DailyBalance;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class ReadAllAssiduousnessWorkSheets extends Service {

    public List<EmployeeWorkSheet> run(YearMonthDay beginDate, YearMonthDay endDate) {
        List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
        List<Assiduousness> assiduousnessList = getCurrentAssiduousness(beginDate, endDate);
        for (Assiduousness assiduousness : assiduousnessList) {
            List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
            YearMonthDay today = new YearMonthDay();
            Duration totalBalance = new Duration(0);
            Duration totalUnjustified = new Duration(0);

            for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                    .plusDays(1)) {
                WorkDaySheet workDaySheet = new WorkDaySheet();
                workDaySheet.setDate(thisDay);
                String notes = "";

                Schedule schedule = assiduousness.getSchedule(thisDay);
                boolean isDayHoliday = assiduousness.isHoliday(thisDay);

                if (schedule == null) {
                    workDaySheet.setNotes(notes);
                    workDaySheet.setBalanceTime(new Period(0));
                    workDaySheet.setUnjustifiedTime(new Duration(0));
                    workSheet.add(workDaySheet);
                } else {
                    WorkSchedule workSchedule = schedule.workScheduleWithDate(thisDay);
                    if (workSchedule != null && !isDayHoliday) {
                        DateTime init = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkTime());
                        DateTime end = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                .getWorkEndTime());
                        if (workSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                            end = end.plusDays(1);
                        }

                        List<AssiduousnessRecord> clockings = assiduousness
                                .getClockingsAndMissingClockings(init, end);

                        Collections.sort(clockings, new BeanComparator("date"));
                        for (AssiduousnessRecord assiduousnessRecord : (List<AssiduousnessRecord>) clockings) {
                            workDaySheet.addClockings(assiduousnessRecord.getDate().toTimeOfDay());
                        }

                        List<Leave> leaves = new ArrayList<Leave>(assiduousness.getLeaves(thisDay));
                        Collections.sort(leaves, new BeanComparator("date"));
                        for (Leave leave : leaves) {
                            if (notes.length() != 0) {
                                notes = notes.concat(" / ");
                            }
                            notes = notes.concat(leave.getJustificationMotive().getAcronym());
                        }
                        workDaySheet.setNotes(notes);
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet
                                .setBalanceTime(dailyBalance.getNormalWorkPeriodBalance().toPeriod());
                        if (!thisDay.equals(today)) {
                            totalBalance = totalBalance.plus(dailyBalance.getNormalWorkPeriodBalance());
                        }
                        workDaySheet.setUnjustifiedTime(dailyBalance.getFixedPeriodAbsence());
                        totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                        workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType()
                                .getAcronym());
                        workSheet.add(workDaySheet);
                    } else {
                        DateTime init = thisDay.toDateTime(new TimeOfDay(7, 30, 0, 0));
                        DateTime end = thisDay.toDateTime(new TimeOfDay(23, 59, 59, 99));
                        List<AssiduousnessRecord> clockings = assiduousness
                                .getClockingsAndMissingClockings(init, end);
                        Collections.sort(clockings, new BeanComparator("date"));
                        DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                        workDaySheet.setBalanceTime(dailyBalance.getTotalBalance().toPeriod());
                        workDaySheet.setNotes(notes);
                        for (AssiduousnessRecord assiduousnessRecord : clockings) {
                            workDaySheet.addClockings(assiduousnessRecord.getDate().toTimeOfDay());
                        }
                        if (isDayHoliday) {
                            ResourceBundle bundle = ResourceBundle
                                    .getBundle("resources.AssiduousnessResources");
                            workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                        }
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    }
                }
            }
            EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet();
            employeeWorkSheet.setWorkDaySheetList(workSheet);
            employeeWorkSheet.setEmployee(assiduousness.getEmployee());
            Unit unit = assiduousness.getEmployee().getLastWorkingPlaceByPeriod(beginDate, endDate);
            if (unit != null) {
                employeeWorkSheet.setUnitCode(unit.getCostCenterCode().toString());
            } else {
                employeeWorkSheet.setUnitCode("");
            }
            employeeWorkSheet.setTotalBalance(totalBalance);
            employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);
            employeeWorkSheetList.add(employeeWorkSheet);
        }

        return employeeWorkSheetList;
    }

    private List<Assiduousness> getCurrentAssiduousness(YearMonthDay beginDate, YearMonthDay endDate) {
        List<Assiduousness> assiduousnessList = new ArrayList<Assiduousness>();
        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {
                assiduousnessList.add(assiduousness);
            }
        }
        return assiduousnessList;
    }
}
