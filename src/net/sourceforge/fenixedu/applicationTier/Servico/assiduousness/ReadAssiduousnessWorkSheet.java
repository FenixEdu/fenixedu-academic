package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
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
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.ContractType;
import net.sourceforge.fenixedu.util.LanguageUtils;

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

    private EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness, YearMonthDay beginDate,
            YearMonthDay endDate, YearMonthDay today) {
        final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
        YearMonthDay lowerBeginDate = beginDate.minusDays(8);
        Duration totalBalance = Duration.ZERO;
        Duration totalUnjustified = Duration.ZERO;
        // TODO remove comment in 2007
        // Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
        // Duration totalWeeklyRestBalance = Duration.ZERO;

        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = new HashMap<YearMonthDay, WorkSchedule>();
        for (YearMonthDay thisDay = lowerBeginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            final Schedule schedule = assiduousness.getSchedule(thisDay);
            if (schedule != null) {
                workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
            } else {
                workScheduleMap.put(thisDay, null);
            }
        }

        DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
        if (beginWorkSchedule != null) {
            init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }

        DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay);
        WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
        if (endWorkSchedule != null) {
            end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
                    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
            if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                end = end.plusDays(2);
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
                if (record.getAplicableWeekDays() == null
                        || record.getAplicableWeekDays().contains(leaveDay.toDateTimeAtMidnight())) {
                    List<Leave> leaveList = leavesMap.get(leaveDay);
                    if (leaveList == null) {
                        leaveList = new ArrayList<Leave>();
                    }
                    leaveList.add(record);
                    leavesMap.put(leaveDay, leaveList);
                }
            }
        }

        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);

            final Schedule schedule = assiduousness.getSchedule(thisDay);

            if (schedule == null || !assiduousness.isStatusActive(thisDay, thisDay)) {
                workDaySheet.setNotes("");
                workDaySheet.setBalanceTime(new Period(0));
                workDaySheet.setUnjustifiedTime(Duration.ZERO);
                workSheet.add(workDaySheet);
            } else {
                final StringBuilder notes = new StringBuilder();
                final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
                final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
                workDaySheet.setWorkSchedule(workSchedule);
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
                if (workSchedule != null && !isDayHoliday) {
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
                    // TODO remove comment in 2007
                    // if (!thisDay.equals(today)) {
                    // workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, thisDay,
                    // isDayHoliday);
                    // totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance
                    // .plus(workDaySheet.getComplementaryWeeklyRest());
                    // totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet
                    // .getWeeklyRest());
                    // }
                    for (final Leave leave : leavesList) {
                        if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE
                                && leave.getJustificationMotive().getDayType() != DayType.WORKDAY
                                && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.CURRENT_YEAR_HOLIDAYS
                                && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.LAST_YEAR_HOLIDAYS) {
                            if (notes.length() != 0) {
                                notes.append(" / ");
                            }
                            notes.append(leave.getJustificationMotive().getAcronym());
                        }
                    }
                    workDaySheet.setNotes(notes.toString());
                    if (isDayHoliday) {
                        ResourceBundle bundle = ResourceBundle
                                .getBundle("resources.AssiduousnessResources", LanguageUtils.getLocale());
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
        if (assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING) != null
                && assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING).getMailingUnit() != null) {
            unit = assiduousness.getEmployee().getLastContractByContractType(ContractType.MAILING).getMailingUnit();
        }
        employeeWorkSheet.setUnit(unit);
        if (unit != null) {
            employeeWorkSheet.setUnitCode((new DecimalFormat("0000")).format(unit.getCostCenterCode()));
        } else {
            employeeWorkSheet.setUnitCode("");
        }
        employeeWorkSheet.setTotalBalance(totalBalance);
        employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);

        // TODO remove comment in 2007
        // employeeWorkSheet.setComplementaryWeeklyRest(totalComplementaryWeeklyRestBalance);
        // employeeWorkSheet.setWeeklyRest(totalWeeklyRestBalance);
        return employeeWorkSheet;
    } // if returns false the clocking belongs to the clocking date // if returns true it may

    // belong to the clocking date or the day before
    private boolean overlapsSchedule(DateTime clocking,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        WorkSchedule thisDaySchedule = workScheduleMap.get(clocking.toYearMonthDay());
        WorkSchedule dayBeforeSchedule = workScheduleMap.get(clocking.toYearMonthDay().minusDays(1));

        Interval thisDayWorkTimeInterval = WorkScheduleType
                .getDefaultWorkTime(clocking.toYearMonthDay());
        if (thisDaySchedule != null) {
            DateTime beginThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkTime());
            DateTime endThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkEndTime());
            if (thisDaySchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endThisDayWorkTime = endThisDayWorkTime.plusDays(1);
            }
            thisDayWorkTimeInterval = new Interval(beginThisDayWorkTime, endThisDayWorkTime);
        }
        Interval dayBeforeWorkTimeInterval = WorkScheduleType.getDefaultWorkTime(clocking
                .toYearMonthDay().minusDays(1));
        if (dayBeforeSchedule != null) {
            DateTime beginDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkTime()).minusDays(1);
            DateTime endDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkEndTime()).minusDays(1);
            if (dayBeforeSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endDayBeforeWorkTime = endDayBeforeWorkTime.plusDays(1);
            }

            dayBeforeWorkTimeInterval = new Interval(beginDayBeforeWorkTime, endDayBeforeWorkTime);
        }
        Interval overlapResult = thisDayWorkTimeInterval.overlap(dayBeforeWorkTimeInterval);
        if (overlapResult == null) {
            Interval gapResult = dayBeforeWorkTimeInterval.gap(thisDayWorkTimeInterval);
            if (gapResult != null) {
                if (!gapResult.contains(clocking)) {
                    return dayBeforeWorkTimeInterval.contains(clocking);
                }
            } else {
                return dayBeforeWorkTimeInterval.contains(clocking);
            }
        } else if (!overlapResult.contains(clocking) && clocking.isAfter(overlapResult.getStart())) {
            return false;
        }
        return true;
    }
}
