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
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.YearMonthDay;

public class ReadAssiduousnessWorkSheet extends Service {

    public EmployeeWorkSheet run(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate) {

        if (assiduousness == null) {
            return null;
        }
        return getEmployeeWorkSheet(assiduousness, beginDate, endDate, new YearMonthDay());
    }

    public List<EmployeeWorkSheet> run(YearMonthDay beginDate, YearMonthDay endDate) {
        final List<EmployeeWorkSheet> employeeWorkSheetList = new ArrayList<EmployeeWorkSheet>();
        final YearMonthDay today = new YearMonthDay();

        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {
                EmployeeWorkSheet employeeWorkSheet = getEmployeeWorkSheet(assiduousness, beginDate,
                        endDate, today);
                employeeWorkSheetList.add(employeeWorkSheet);
            }
        }
        return employeeWorkSheetList;
    }

    private EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate, YearMonthDay today) {
        final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();

        Duration totalBalance = Duration.ZERO;
        Duration totalUnjustified = Duration.ZERO;

        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = new HashMap<YearMonthDay, WorkSchedule>();
        for (YearMonthDay thisDay = beginDate.minusDays(1); thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            final Schedule schedule = assiduousness.getSchedule(thisDay);
            if (schedule != null) {
                workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
            } else {
                workScheduleMap.put(thisDay, null);
            }
        }

        DateTime init = beginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(beginDate);
        if (beginWorkSchedule != null) {
            init = beginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }

        DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay);
        WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
        if (endWorkSchedule != null) {
            end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkEndTime());
            if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                end = end.plusDays(1);
            }
        }

        final List<AssiduousnessRecord> clockings = assiduousness.getClockingsAndMissingClockings(init
                .minusDays(1), end);
        Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
        for (AssiduousnessRecord record : clockings) {
            YearMonthDay clockDay = record.getDate().toYearMonthDay();
            if (overlapsSchedule(record.getDate(), workScheduleMap)) {
                if (clockingsMap.get(clockDay.minusDays(1)) != null
                        && clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
                    clockDay = clockDay.minusDays(1);
                }
            }

            List<AssiduousnessRecord> clocks = clockingsMap.get(clockDay);
            if (clocks == null) {
                clocks = new ArrayList<AssiduousnessRecord>();
            }
            clocks.add(record);
            clockingsMap.put(clockDay, clocks);
        }

        final List<Leave> leaves = assiduousness.getLeaves(beginDate, endDate);
        HashMap<YearMonthDay, List<Leave>> leavesMap = new HashMap<YearMonthDay, List<Leave>>();
        for (Leave record : leaves) {
            YearMonthDay endLeaveDay = record.getDate().toYearMonthDay().plusDays(1);
            if (record.getEndYearMonthDay() != null) {
                endLeaveDay = record.getEndYearMonthDay().plusDays(1);
            }
            for (YearMonthDay leaveDay = record.getDate().toYearMonthDay(); leaveDay
                    .isBefore(endLeaveDay); leaveDay = leaveDay.plusDays(1)) {
                List<Leave> leaveList = leavesMap.get(leaveDay);
                if (leaveList == null) {
                    leaveList = new ArrayList<Leave>();
                }
                leaveList.add(record);
                leavesMap.put(leaveDay, leaveList);
            }
        }

        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);

            final Schedule schedule = assiduousness.getSchedule(thisDay);

            if (schedule == null) {
                workDaySheet.setNotes("");
                workDaySheet.setBalanceTime(new Period(0));
                workDaySheet.setUnjustifiedTime(Duration.ZERO);
                workSheet.add(workDaySheet);
            } else {
                final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
                final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
                workDaySheet.setWorkSchedule(workSchedule);
                if (workSchedule != null && !isDayHoliday) {

                    List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
                    if (clockingsList == null) {
                        clockingsList = new ArrayList<AssiduousnessRecord>();
                    }
                    Collections.sort(clockingsList, AssiduousnessRecord.COMPARATORY_BY_DATE);
                    workDaySheet.setAssiduousnessRecords(clockingsList);
                    List<Leave> leavesList = leavesMap.get(thisDay);
                    if (leavesList == null) {
                        leavesList = new ArrayList<Leave>();
                    }
                    Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
                    workDaySheet.setLeaves(leavesList);

                    final StringBuilder notes = new StringBuilder();
                    for (final Leave leave : leavesList) {
                        if (notes.length() != 0) {
                            notes.append(" / ");
                        }
                        notes.append(leave.getJustificationMotive().getAcronym());
                    }
                    workDaySheet.setNotes(notes.toString());
                    if (!thisDay.equals(today)) {
                        workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, thisDay,
                                isDayHoliday);
                        totalBalance = totalBalance.plus(workDaySheet.getBalanceTime().toDurationFrom(
                                new DateMidnight()));
                        totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
                    }
                    workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
                    workSheet.add(workDaySheet);
                } else {
                    List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
                    if (clockingsList == null) {
                        clockingsList = new ArrayList<AssiduousnessRecord>();
                    }
                    Collections.sort(clockingsList, AssiduousnessRecord.COMPARATORY_BY_DATE);
                    workDaySheet.setAssiduousnessRecords(clockingsList);
                    workDaySheet.setLeaves(new ArrayList<Leave>());
                    if (!thisDay.equals(today)) {
                        workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, thisDay,
                                isDayHoliday);
                    }
                    workDaySheet.setNotes("");
                    if (isDayHoliday) {
                        ResourceBundle bundle = ResourceBundle
                                .getBundle("resources.AssiduousnessResources");
                        workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
                    }
                    workDaySheet.setUnjustifiedTime(Duration.ZERO);
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

        return employeeWorkSheet;
    }

    private Boolean overlapsSchedule(DateTime clocking,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        WorkSchedule thisDaySchedule = workScheduleMap.get(clocking.toYearMonthDay());
        WorkSchedule dayBeforeSchedule = workScheduleMap.get(clocking.toYearMonthDay().minusDays(1));
        if (thisDaySchedule == null) {
            DateTime beginDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkTime()).minusDays(1);
            DateTime endDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkEndTime()).minusDays(1);
            if (dayBeforeSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endDayBeforeWorkTime = endDayBeforeWorkTime.plusDays(1);
            }

            Interval dayBeforeWorkTimeInterval = new Interval(beginDayBeforeWorkTime,
                    endDayBeforeWorkTime);
            if (dayBeforeWorkTimeInterval.contains(clocking)) {
                return true;
            }
        } else if (dayBeforeSchedule != null) {
            DateTime beginThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkTime());
            DateTime endThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkEndTime());
            if (thisDaySchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endThisDayWorkTime = endThisDayWorkTime.plusDays(1);
            }
            Interval thisDayWorkTimeInterval = new Interval(beginThisDayWorkTime, endThisDayWorkTime);

            DateTime beginDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkTime()).minusDays(1);
            DateTime endDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkEndTime()).minusDays(1);
            if (dayBeforeSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endDayBeforeWorkTime = endDayBeforeWorkTime.plusDays(1);
            }

            Interval nextDayWorkTimeInterval = new Interval(beginDayBeforeWorkTime, endDayBeforeWorkTime);

            if (thisDayWorkTimeInterval.overlap(nextDayWorkTimeInterval) != null) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

}
