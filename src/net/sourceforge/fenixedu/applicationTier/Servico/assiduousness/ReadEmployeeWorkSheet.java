package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.DailyBalance;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class ReadEmployeeWorkSheet extends Service {

    public EmployeeWorkSheet run(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate) {
        
        if (assiduousness == null) {
            return null;
        }

        List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
        YearMonthDay today = new YearMonthDay();
        Duration totalBalance = new Duration(0);
        Duration totalUnjustified = new Duration(0);
        HashMap<Integer, JustificationMotive> subtitles = new HashMap<Integer, JustificationMotive>();

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
                    DateTime init = thisDay.toDateTime(workSchedule.getWorkScheduleType().getWorkTime());
                    DateTime end = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                            .getWorkEndTime());
                    if (workSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                        end = end.plusDays(1);
                    }

                    List<AssiduousnessRecord> clockings = assiduousness.getClockingsAndMissingClockings(
                            init, end);
                    Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
                    workDaySheet.setAssiduousnessRecord(clockings);

                    List<Leave> leaves = new ArrayList<Leave>(assiduousness.getLeaves(thisDay));
                    Collections.sort(leaves, Leave.COMPARATORY_BY_DATE);
                    for (Leave leave : leaves) {
                        if (notes.length() != 0) {
                            notes = notes.concat(" / ");
                        }
                        notes = notes.concat(leave.getJustificationMotive().getAcronym());
                        putSubtitle(subtitles, leave.getJustificationMotive());
                    }
                    workDaySheet.setNotes(notes);
                    DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                    workDaySheet.setBalanceTime(dailyBalance.getNormalWorkPeriodBalance().toPeriod());
                    if (!thisDay.equals(today)) {
                        totalBalance = totalBalance.plus(dailyBalance.getNormalWorkPeriodBalance());
                    }
                    workDaySheet.setUnjustifiedTime(dailyBalance.getFixedPeriodAbsence());
                    totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                    workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
                    workSheet.add(workDaySheet);
                } else {
                    DateTime init = thisDay.toDateTime(new TimeOfDay(7, 30, 0, 0));
                    DateTime end = thisDay.toDateTime(new TimeOfDay(23, 59, 59, 99));
                    List<AssiduousnessRecord> clockings = assiduousness.getClockingsAndMissingClockings(
                            init, end);
                    Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
                    workDaySheet.setAssiduousnessRecord(clockings);
                    DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                    workDaySheet.setBalanceTime(dailyBalance.getTotalBalance().toPeriod());
                    workDaySheet.setNotes(notes);
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
        employeeWorkSheet.setTotalBalance(totalBalance);
        employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);
        employeeWorkSheet.setJustificationMotives(subtitles.values());
        return employeeWorkSheet;      
    }

    private void putSubtitle(HashMap<Integer, JustificationMotive> subtitles,
            JustificationMotive justificationMotive) {
        if (subtitles.get(justificationMotive.getIdInternal()) == null) {
            subtitles.put(justificationMotive.getIdInternal(), justificationMotive);
        }
    }
}
