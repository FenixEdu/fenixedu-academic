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

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class ReadAllAssiduousnessWorkSheets extends Service {

    public List<EmployeeWorkSheet> run(YearMonthDay beginDate, YearMonthDay endDate) {
        final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
        final YearMonthDay today = new YearMonthDay();

        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {

                final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
                
                Duration totalBalance = new Duration(0);
                Duration totalUnjustified = new Duration(0);

                for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                        .plusDays(1)) {
                    final WorkDaySheet workDaySheet = new WorkDaySheet();
                    workDaySheet.setDate(thisDay);

                    final Schedule schedule = assiduousness.getSchedule(thisDay);

                    if (schedule == null) {
                        workDaySheet.setNotes("");
                        workDaySheet.setBalanceTime(new Period(0));
                        workDaySheet.setUnjustifiedTime(new Duration(0));
                        workSheet.add(workDaySheet);
                    } else {
                        final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
                        final WorkSchedule workSchedule = schedule.workScheduleWithDate(thisDay);
                        if (workSchedule != null && !isDayHoliday) {
                            final DateTime init = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                    .getWorkTime());
                            DateTime end = thisDay.toDateTime(workSchedule.getWorkScheduleType()
                                    .getWorkEndTime());
                            if (workSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                                end = end.plusDays(1);
                            }

                            final List<AssiduousnessRecord> clockings = assiduousness
                                    .getClockingsAndMissingClockings(init, end);
                            Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
                            workDaySheet.setAssiduousnessRecord(clockings);

                            final StringBuilder notes = new StringBuilder();
                            for (final Leave leave : assiduousness.getLeaves(thisDay)) {
                                if (notes.length() != 0) {
                                    notes.append(" / ");
                                }
                                notes.append(leave.getJustificationMotive().getAcronym());
                            }
                            workDaySheet.setNotes(notes.toString());
                            final DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                            workDaySheet.setBalanceTime(dailyBalance.getNormalWorkPeriodBalance()
                                    .toPeriod());
                            if (!thisDay.equals(today)) {
                                totalBalance = totalBalance.plus(dailyBalance
                                        .getNormalWorkPeriodBalance());
                            }
                            workDaySheet.setUnjustifiedTime(dailyBalance.getFixedPeriodAbsence());
                            totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                            workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType()
                                    .getAcronym());
                            workSheet.add(workDaySheet);
                        } else {
                            DateTime init = thisDay.toDateTime(Assiduousness.startTimeOfDay);
                            DateTime end = thisDay.toDateTime(Assiduousness.endTimeOfDay);
                            List<AssiduousnessRecord> clockings = assiduousness.getClockingsAndMissingClockings(init, end);
                            Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
                            DailyBalance dailyBalance = assiduousness.calculateDailyBalance(thisDay);
                            workDaySheet.setBalanceTime(dailyBalance.getTotalBalance().toPeriod());
                            workDaySheet.setNotes("");
                            workDaySheet.setAssiduousnessRecord(clockings);
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
                employeeWorkSheet.setUnit(unit);
                if (unit != null) {
                    employeeWorkSheet.setUnitCode(unit.getCostCenterCode().toString());
                } else {
                    employeeWorkSheet.setUnitCode("");
                }
                employeeWorkSheet.setTotalBalance(totalBalance);
                employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);
                employeeWorkSheetList.add(employeeWorkSheet);
            }
        }

        return employeeWorkSheetList;
    }

}
