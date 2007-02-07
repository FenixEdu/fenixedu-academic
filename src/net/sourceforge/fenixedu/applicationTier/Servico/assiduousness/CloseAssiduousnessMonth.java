package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.MissingClocking;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class CloseAssiduousnessMonth extends Service {

    public ClosedMonth run(YearMonthDay beginDate, YearMonthDay endDate) {
        HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords = getAssiduousnessRecord(
                beginDate, endDate.plusDays(1));
        ClosedMonth closedMonth = new ClosedMonth(beginDate);
        for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
            if (assiduousness.isStatusActive(beginDate, endDate)) {
                getMonthAssiduousnessBalance(assiduousness, assiduousnessRecords.get(assiduousness),
                        closedMonth, beginDate, endDate);
            }
        }
        return closedMonth;
    }

    private void getMonthAssiduousnessBalance(Assiduousness assiduousness,
            List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth,
            YearMonthDay beginDate, YearMonthDay endDate) {
        YearMonthDay lowerBeginDate = beginDate.minusDays(8);
        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = assiduousness
                .getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
        DateTime init = getInit(lowerBeginDate, workScheduleMap);
        DateTime end = getEnd(endDate, workScheduleMap);
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(
                assiduousnessRecords, workScheduleMap, init, end);
        HashMap<YearMonthDay, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate,
                endDate);
        Duration totalBalance = Duration.ZERO;
        Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
        Duration totalWeeklyRestBalance = Duration.ZERO;
        Duration holidayRest = Duration.ZERO;
        HashMap<WorkScheduleType, Duration> extra125Map = new HashMap<WorkScheduleType, Duration>();
        HashMap<WorkScheduleType, Duration> extra150Map = new HashMap<WorkScheduleType, Duration>();
        HashMap<WorkScheduleType, Duration> extra25Map = new HashMap<WorkScheduleType, Duration>();
        HashMap<WorkScheduleType, Duration> unjustifiedMap = new HashMap<WorkScheduleType, Duration>();
        HashMap<JustificationMotive, Duration> justificationsDuration = new HashMap<JustificationMotive, Duration>();
        double vacations = 0;
        double tolerance = 0;
        double article17 = 0;
        double article66 = 0;
        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            WorkDaySheet workDaySheet = new WorkDaySheet();
            workDaySheet.setDate(thisDay);
            final Schedule schedule = assiduousness.getSchedule(thisDay);
            if (schedule != null && assiduousness.isStatusActive(thisDay, thisDay)) {
                final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
                final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
                workDaySheet.setWorkSchedule(workSchedule);
                workDaySheet.setAssiduousnessRecords(getDayClockings(clockingsMap, thisDay));
                List<Leave> leavesList = getDayLeaves(leavesMap, thisDay);
                workDaySheet.setLeaves(leavesList);
                workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
                if (workSchedule != null && !isDayHoliday) {
                    for (Leave leave : leavesList) {
                        if (leave.getJustificationMotive().getJustificationType().equals(
                                JustificationType.TIME)) {
                            Duration overlapsDuration = Duration.ZERO;
                            Duration justificationDuration = justificationsDuration.get(leave
                                    .getJustificationMotive());
                            if (justificationDuration == null) {
                                justificationDuration = Duration.ZERO;
                            }
                            if (!workDaySheet.getIrregular()) {
                                Interval interval = workSchedule.getWorkScheduleType()
                                        .getNormalWorkPeriod().getNotWorkingPeriod(thisDay);
                                if (interval != null) {
                                    Interval overlaps = interval.overlap(leave.getTotalInterval());
                                    if (overlaps != null) {
                                        overlapsDuration = overlaps.toDuration();
                                    }
                                }
                                justificationDuration = justificationDuration.plus(leave.getDuration()
                                        .minus(overlapsDuration));
                            }
                            justificationsDuration.put(leave.getJustificationMotive(),
                                    justificationDuration);
                        }
                        if (leave.getJustificationMotive().equals("1/2 FÉR")
                                || leave.getJustificationMotive().equals("FHE")
                                || leave.getJustificationMotive().equals("FHEA")
                                || leave.getJustificationMotive().equals("FTRANS")
                                || leave.getJustificationMotive().equals("FA17")
                                || leave.getJustificationMotive().equals("FEB")
                                || leave.getJustificationMotive().equals("FER")
                                || leave.getJustificationMotive().equals("A42")
                                || leave.getJustificationMotive().equals("FA42")
                                || leave.getJustificationMotive().equals("FA42T")) {
                            if (leave.getJustificationMotive().getJustificationType().equals(
                                    JustificationType.TIME)) {
                                vacations = vacations + 0.5;
                            } else {
                                vacations = vacations + 1;
                            }
                        } else if (leave.getJustificationMotive().equals("TOL")) {
                            tolerance = tolerance + 1;
                        } else if (leave.getJustificationMotive().equals("A17")) {
                            article17 = article17 + 1;
                        } else if (leave.getJustificationMotive().equals("A66")) {
                            article66 = article66 + 1;
                        } else if (leave.getJustificationMotive().equals("1/2A66")) {
                            article66 = article66 + 0.5;
                        }
                    }
                    Duration thisDayBalance = workDaySheet.getBalanceTime().toDurationFrom(
                            new DateMidnight());
                    totalBalance = totalBalance.plus(thisDayBalance);
                    setNightExtraWork(workDaySheet, extra25Map);
                    setExtraWork(workDaySheet, thisDayBalance, extra125Map, extra150Map);
                    setUnjustified(workDaySheet, unjustifiedMap);
                } else {
                    totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance
                            .plus(workDaySheet.getComplementaryWeeklyRest());
                    totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet.getWeeklyRest());
                    holidayRest = holidayRest.plus(workDaySheet.getHolidayRest());
                }
            }
        }

        AssiduousnessClosedMonth assiduousnessClosedMonth = new AssiduousnessClosedMonth(assiduousness,
                closedMonth, totalBalance, totalComplementaryWeeklyRestBalance, totalWeeklyRestBalance,
                holidayRest, vacations, tolerance, article17, article66);

        for (JustificationMotive justificationMotive : justificationsDuration.keySet()) {
            new ClosedMonthJustification(assiduousnessClosedMonth, justificationMotive,
                    justificationsDuration.get(justificationMotive));
        }

        Set<WorkScheduleType> workScheduleTypeSet = new HashSet<WorkScheduleType>(extra25Map.keySet());
        workScheduleTypeSet.addAll(extra125Map.keySet());
        workScheduleTypeSet.addAll(extra150Map.keySet());
        workScheduleTypeSet.addAll(unjustifiedMap.keySet());

        for (WorkScheduleType workScheduleType : workScheduleTypeSet) {
            Duration totalExtra25 = extra25Map.get(workScheduleType);
            Duration totalExtra125 = extra125Map.get(workScheduleType);
            Duration totalExtra150 = extra150Map.get(workScheduleType);
            Duration totalUnjustified = unjustifiedMap.get(workScheduleType);
            new AssiduousnessExtraWork(assiduousnessClosedMonth, workScheduleType, totalExtra25,
                    totalExtra125, totalExtra150, totalUnjustified);
        }
        return;
    }

    private HashMap<YearMonthDay, List<Leave>> getLeavesMap(
            List<AssiduousnessRecord> assiduousnessRecords, YearMonthDay beginDate, YearMonthDay endDate) {
        HashMap<YearMonthDay, List<Leave>> leavesMap = new HashMap<YearMonthDay, List<Leave>>();
        if (assiduousnessRecords != null) {
            for (AssiduousnessRecord record : assiduousnessRecords) {
                if (record instanceof Leave) {
                    YearMonthDay endLeaveDay = record.getDate().toYearMonthDay().plusDays(1);
                    if (((Leave) record).getEndYearMonthDay() != null) {
                        endLeaveDay = ((Leave) record).getEndYearMonthDay().plusDays(1);
                    }
                    for (YearMonthDay leaveDay = record.getDate().toYearMonthDay(); leaveDay
                            .isBefore(endLeaveDay); leaveDay = leaveDay.plusDays(1)) {
                        if (((Leave) record).getAplicableWeekDays() == null
                                || ((Leave) record).getAplicableWeekDays().contains(
                                        leaveDay.toDateTimeAtMidnight())) {
                            List<Leave> leaveList = leavesMap.get(leaveDay);
                            if (leaveList == null) {
                                leaveList = new ArrayList<Leave>();
                            }
                            leaveList.add((Leave) record);
                            leavesMap.put(leaveDay, leaveList);
                        }
                    }
                }
            }
        }
        return leavesMap;
    }

    private HashMap<YearMonthDay, List<AssiduousnessRecord>> getClockingsMap(
            List<AssiduousnessRecord> assiduousnessRecords,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap, DateTime init, DateTime end) {
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
        if (assiduousnessRecords != null) {
            final List<AssiduousnessRecord> clockings = new ArrayList<AssiduousnessRecord>(
                    assiduousnessRecords);
            Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
            for (AssiduousnessRecord record : clockings) {
                if (record instanceof Clocking || record instanceof MissingClocking) {
                    YearMonthDay clockDay = record.getDate().toYearMonthDay();
                    if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap)) {
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
            }
        }
        return clockingsMap;
    }

    private void setUnjustified(WorkDaySheet workDaySheet,
            HashMap<WorkScheduleType, Duration> unjustifiedMap) {
        Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
        if (thisDayUnjustified == null) {
            thisDayUnjustified = Duration.ZERO;
        }
        Duration totalUnjustified = unjustifiedMap.get(workDaySheet.getWorkSchedule()
                .getWorkScheduleType());
        if (totalUnjustified == null) {
            totalUnjustified = Duration.ZERO;
        }
        totalUnjustified = totalUnjustified.plus(thisDayUnjustified);
        unjustifiedMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), totalUnjustified);
    }

    private void setExtraWork(WorkDaySheet workDaySheet, Duration thisDayBalance,
            HashMap<WorkScheduleType, Duration> extra125Map,
            HashMap<WorkScheduleType, Duration> extra150Map) {
        Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
        if (thisDayUnjustified == null) {
            thisDayUnjustified = Duration.ZERO;
        }
        Duration hourDuration = new Duration(3600000);
        // TODO Duration thisDayExtraWork = thisDayBalance; ignore unjustified
        Duration thisDayExtraWork = thisDayBalance.plus(thisDayUnjustified);
        if (!thisDayExtraWork.equals(Duration.ZERO)) {
            if (thisDayExtraWork.isLongerThan(hourDuration)) {
                Duration extra125 = extra125Map
                        .get(workDaySheet.getWorkSchedule().getWorkScheduleType());
                if (extra125 == null) {
                    extra125 = Duration.ZERO;
                }
                Duration extra150 = extra150Map
                        .get(workDaySheet.getWorkSchedule().getWorkScheduleType());
                if (extra150 == null) {
                    extra150 = Duration.ZERO;
                }
                extra150 = extra150.plus(thisDayExtraWork.minus(hourDuration));
                extra150Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra150);
                extra125 = extra125.plus(hourDuration);
                extra125Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra125);
            } else if (thisDayExtraWork.isLongerThan(Duration.ZERO)) {
                Duration extra125 = extra125Map
                        .get(workDaySheet.getWorkSchedule().getWorkScheduleType());
                if (extra125 == null) {
                    extra125 = Duration.ZERO;
                }
                extra125 = extra125.plus(thisDayExtraWork);
                extra125Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra125);
            }
        }
    }

    private void setNightExtraWork(WorkDaySheet workDaySheet,
            HashMap<WorkScheduleType, Duration> extra25Map) {
        if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null) {
            final Duration midHour = new Duration(1800000);
            Duration extraWorkDuration = workDaySheet.getTimeline()
                    .calculateWorkPeriodDurationBetweenDates(
                            workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
                            workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay)
                                    .plusDays(1));
            if (!extraWorkDuration.equals(Duration.ZERO)) {
                if (!extraWorkDuration.isShorterThan(midHour)) {
                    Duration extra25 = extra25Map.get(workDaySheet.getWorkSchedule()
                            .getWorkScheduleType());
                    if (extra25 == null) {
                        extra25 = Duration.ZERO;
                    }
                    extra25 = extra25.plus(extraWorkDuration);
                    extra25Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra25);
                }
            }
        }
    }

    private List<Leave> getDayLeaves(HashMap<YearMonthDay, List<Leave>> leavesMap, YearMonthDay thisDay) {
        List<Leave> leavesList = leavesMap.get(thisDay);
        if (leavesList == null) {
            leavesList = new ArrayList<Leave>();
        }
        Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
        return leavesList;
    }

    private List<AssiduousnessRecord> getDayClockings(
            HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap, YearMonthDay thisDay) {
        List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
        if (clockingsList == null) {
            clockingsList = new ArrayList<AssiduousnessRecord>();
        }
        Collections.sort(clockingsList, AssiduousnessRecord.COMPARATORY_BY_DATE);
        return clockingsList;
    }

    private DateTime getEnd(YearMonthDay endDate, HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay);
        WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
        if (endWorkSchedule != null) {
            end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime()).plus(
                    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
            if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                end = end.plusDays(2);
            }
        }
        return end;
    }

    private DateTime getInit(YearMonthDay lowerBeginDate,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
        WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
        if (beginWorkSchedule != null) {
            init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
        }
        return init;
    }

    public HashMap<Assiduousness, List<AssiduousnessRecord>> getAssiduousnessRecord(
            YearMonthDay beginDate, YearMonthDay endDate) {
        HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessLeaves = new HashMap<Assiduousness, List<AssiduousnessRecord>>();
        Interval interval = new Interval(beginDate.toDateTimeAtMidnight(),
                Assiduousness.defaultEndWorkDay.toDateTime(endDate.toDateMidnight()));
        for (AssiduousnessRecord assiduousnessRecord : rootDomainObject.getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)) {
                Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
                        ((Leave) assiduousnessRecord).getEndDate().plusSeconds(1));
                if (leaveInterval.overlaps(interval)) {
                    List<AssiduousnessRecord> leavesList = assiduousnessLeaves.get(assiduousnessRecord
                            .getAssiduousness());
                    if (leavesList == null) {
                        leavesList = new ArrayList<AssiduousnessRecord>();
                    }
                    leavesList.add(assiduousnessRecord);
                    assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
                }
            } else if ((assiduousnessRecord instanceof Clocking || assiduousnessRecord instanceof MissingClocking)
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)) {
                if (interval.contains(assiduousnessRecord.getDate().getMillis())) {
                    List<AssiduousnessRecord> list = assiduousnessLeaves.get(assiduousnessRecord
                            .getAssiduousness());
                    if (list == null) {
                        list = new ArrayList<AssiduousnessRecord>();
                    }
                    list.add(assiduousnessRecord);
                    assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), list);
                }
            }
        }
        return assiduousnessLeaves;
    }
}