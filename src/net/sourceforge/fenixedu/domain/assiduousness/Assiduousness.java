package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Assiduousness extends Assiduousness_Base {

    public static final TimeOfDay defaultStartWorkDay = new TimeOfDay(7, 30, 0, 0);

    public static final TimeOfDay defaultEndWorkDay = new TimeOfDay(23, 59, 59, 99);

    public static final TimeOfDay defaultStartWeeklyRestDay = new TimeOfDay(7, 0, 0, 0);

    public static final TimeOfDay defaultStartNightWorkDay = new TimeOfDay(20, 0, 0, 0);

    public static final TimeOfDay defaultEndNightWorkDay = new TimeOfDay(7, 0, 0, 0);

    public static final Duration normalWorkDayDuration = new Duration(25200000); // 7

    // hours

    public Assiduousness(Employee employee) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setEmployee(employee);
    }

    public Schedule getCurrentSchedule() {
        YearMonthDay now = new YearMonthDay();
        for (final Schedule schedule : getSchedules()) {
            if (!schedule.getException()) {
                if (schedule.getEndDate() == null) {
                    if (schedule.getBeginDate().isBefore(now) || schedule.getBeginDate().isEqual(now)) {
                        return schedule;
                    }
                } else {
                    DateInterval dateInterval = new DateInterval(schedule.getBeginDate(), schedule
                            .getEndDate());
                    if (dateInterval.containsDate(now)) {
                        return schedule;
                    }
                }
            }
        }
        return null;
    }

    public Schedule getSchedule(YearMonthDay date) {
        List<Schedule> scheduleList = new ArrayList<Schedule>();
        for (Schedule schedule : getSchedules()) {
            if (schedule.isDefinedInDate(date)) {
                scheduleList.add(schedule);
            }
        }
        if (scheduleList.size() == 1) {
            return scheduleList.iterator().next();
        } else {
            // if there are more than one, it's beacause there was an
            // exception schedule in that specified date
            for (Schedule schedule : scheduleList) {
                if (schedule.getException()) {
                    return schedule;
                }
            }
            return null;
        }
    }

    public HashMap<YearMonthDay, WorkSchedule> getWorkSchedulesBetweenDates(YearMonthDay beginDate,
            YearMonthDay endDate) {
        HashMap<YearMonthDay, WorkSchedule> workScheduleMap = new HashMap<YearMonthDay, WorkSchedule>();
        for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay
                .plusDays(1)) {
            final Schedule schedule = getSchedule(thisDay);
            if (schedule != null) {
                workScheduleMap.put(thisDay, schedule.workScheduleWithDate(thisDay));
            } else {
                workScheduleMap.put(thisDay, null);
            }
        }
        return workScheduleMap;
    }

    public HashMap<YearMonthDay, List<AssiduousnessRecord>> getClockingsMap(
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap, DateTime init, DateTime end) {
        HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
        final List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(init.minusDays(1),
                end);
        Collections.sort(clockings, AssiduousnessRecord.COMPARATORY_BY_DATE);
        for (AssiduousnessRecord record : clockings) {
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
        return clockingsMap;
    }

    public HashMap<YearMonthDay, List<Leave>> getLeavesMap(YearMonthDay beginDate, YearMonthDay endDate) {
        HashMap<YearMonthDay, List<Leave>> leavesMap = new HashMap<YearMonthDay, List<Leave>>();
        final List<Leave> leaves = getLeaves(beginDate, endDate);
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
        return leavesMap;
    }

    private Timeline getTimeline(WorkDaySheet workDaySheet, List<Leave> timeLeaves) {
        Timeline timeline = new Timeline(workDaySheet.getWorkSchedule().getWorkScheduleType());
        Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator();
        timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), timeLeaves, attributesIt,
                workDaySheet.getDate());
        return timeline;
    }

    public WorkDaySheet calculateDailyBalance(WorkDaySheet workDaySheet, boolean isDayHoliday) {
        return calculateDailyBalance(workDaySheet, isDayHoliday, false);
    }

    public WorkDaySheet calculateDailyBalance(WorkDaySheet workDaySheet, boolean isDayHoliday,
            boolean closingMonth) {
        if (workDaySheet.getWorkSchedule() != null
                && !isDayHoliday
                && (!workDaySheet.getWorkSchedule().getWorkScheduleType().getScheduleClockingType()
                        .equals(ScheduleClockingType.NOT_MANDATORY_CLOCKING) || closingMonth)) {

            List<Leave> dayOccurrences = getLeavesByType(workDaySheet.getLeaves(),
                    JustificationType.OCCURRENCE);

            if (dayOccurrences.isEmpty() || !workDaySheet.getAssiduousnessRecords().isEmpty()) {
                List<Leave> timeLeaves = getLeavesByType(workDaySheet.getLeaves(),
                        JustificationType.TIME);
                List<Leave> balanceLeaves = getLeavesByType(workDaySheet.getLeaves(),
                        JustificationType.BALANCE);
                List<Leave> balanceOcurrenceLeaves = getLeavesByType(workDaySheet.getLeaves(),
                        JustificationType.MULTIPLE_MONTH_BALANCE);
                if (!workDaySheet.getAssiduousnessRecords().isEmpty() || !timeLeaves.isEmpty()) {
                    workDaySheet.setTimeline(getTimeline(workDaySheet, timeLeaves));
                    workDaySheet = workDaySheet.getWorkSchedule().calculateWorkingPeriods(workDaySheet,
                            timeLeaves);
                    if (!dayOccurrences.isEmpty()) {
                        workDaySheet.setIrregular(true);
                    }
                } else {
                    workDaySheet.setBalanceTime(Duration.ZERO.minus(
                            workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
                                    .getWorkPeriodDuration()).toPeriod());
                    if (workDaySheet.getWorkSchedule().getWorkScheduleType().getFixedWorkPeriod() != null) {
                        workDaySheet.setUnjustifiedTime(workDaySheet.getWorkSchedule()
                                .getWorkScheduleType().getFixedWorkPeriod().getWorkPeriodDuration());
                    }
                    if (balanceLeaves.isEmpty() && balanceOcurrenceLeaves.isEmpty()) {
                        workDaySheet.addNote("FALTA");
                    }
                }
                workDaySheet.discountBalanceLeaveInFixedPeriod(balanceLeaves);
                workDaySheet.discountBalanceOcurrenceLeaveInFixedPeriod(balanceOcurrenceLeaves);
            }
        } else {
            if (!workDaySheet.getAssiduousnessRecords().isEmpty()) {
                DateTime firstClocking = workDaySheet.getAssiduousnessRecords().get(0).getDate();
                DateTime lastClocking = workDaySheet.getAssiduousnessRecords().get(
                        workDaySheet.getAssiduousnessRecords().size() - 1).getDate();
                final Timeline timeline = new Timeline(workDaySheet.getDate(), firstClocking,
                        lastClocking);
                Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                        .iterator();
                timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), workDaySheet
                        .getLeaves(), attributesIt, workDaySheet.getDate());
                Duration worked = timeline.calculateWorkPeriodDuration(null, timeline.getTimePoints()
                        .iterator().next(),
                        new TimePoint(defaultStartWeeklyRestDay, AttributeType.NULL), new TimePoint(
                                lastClocking.toTimeOfDay(), lastClocking.toYearMonthDay().equals(
                                        workDaySheet.getDate()) ? false : true, AttributeType.NULL),
                        null, null);
                Duration weeklyRestDuration = worked;
                // TODO Remove coments in 2007
                // if (worked.isLongerThan(normalWorkDayDuration)) {
                // weeklyRestDuration = normalWorkDayDuration;
                // }
                final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(workDaySheet.getDate()
                        .toDateTimeAtMidnight());
                if (dayOfWeek.equals(WeekDay.SATURDAY)) {
                    workDaySheet.setComplementaryWeeklyRest(weeklyRestDuration);
                } else if (dayOfWeek.equals(WeekDay.SUNDAY)) {
                    workDaySheet.setWeeklyRest(weeklyRestDuration);
                } else if (isDayHoliday) {
                    workDaySheet.setHolidayRest(weeklyRestDuration);
                }
                // TODO Remove coment in 2007
                // workDaySheet.setBalanceTime(worked.toPeriod());
            }
        }
        return workDaySheet;
    }

    public List<Leave> getLeavesByType(List<Leave> leaves, JustificationType justificationType) {
        List<Leave> leavesByType = new ArrayList<Leave>();
        for (Leave leave : leaves) {
            if (leave.getJustificationMotive().getJustificationType() == justificationType) {
                leavesByType.add(leave);
            }
        }
        return leavesByType;
    }

    public List<Leave> getLeaves(YearMonthDay day) {
        List<Leave> leaves = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)) {
                Leave leave = (Leave) assiduousnessRecord;
                if (leave.occuredInDate(day)) {
                    leaves.add(leave);
                }
            }
        }
        return leaves;
    }

    public List<AssiduousnessRecord> getClockingsAndMissingClockings(DateTime beginDate, DateTime endDate) {
        Interval interval = new Interval(beginDate, endDate);
        List<AssiduousnessRecord> clockingsList = new ArrayList<AssiduousnessRecord>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if ((assiduousnessRecord instanceof Clocking || assiduousnessRecord instanceof MissingClocking)
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)
                    && interval.contains(assiduousnessRecord.getDate())) {
                clockingsList.add(assiduousnessRecord);
            }
        }
        return clockingsList;
    }

    public List<Clocking> getClockings(YearMonthDay beginDate, YearMonthDay endDate) {
        DateInterval interval = new DateInterval(beginDate, endDate);
        List<Clocking> clockingsList = new ArrayList<Clocking>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Clocking
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)
                    && interval.containsDate(assiduousnessRecord.getDate())) {
                clockingsList.add((Clocking) assiduousnessRecord);
            }
        }
        return clockingsList;
    }

    public List<Clocking> getClockingsAndAnulatedClockings(YearMonthDay beginDate, YearMonthDay endDate) {
        DateInterval interval = new DateInterval(beginDate, endDate);
        List<Clocking> clockingsList = new ArrayList<Clocking>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Clocking
                    && interval.containsDate(assiduousnessRecord.getDate())) {
                if (assiduousnessRecord.getAnulation() == null
                        || assiduousnessRecord.getAnulation().getState() == AnulationState.INVALID) {
                    clockingsList.add((Clocking) assiduousnessRecord);
                } else {
                    clockingsList.add((Clocking) assiduousnessRecord.getAnulation()
                            .getAnulatedAssiduousnessRecord());
                }
            }
        }
        return clockingsList;
    }

    public List<Leave> getLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
        Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), defaultEndWorkDay
                .toDateTime(endDate.toDateMidnight()));
        List<Leave> leavesList = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState() == AnulationState.INVALID)) {
                Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
                        ((Leave) assiduousnessRecord).getEndDate().plusSeconds(1));
                if (leaveInterval.overlaps(interval)) {
                    leavesList.add((Leave) assiduousnessRecord);
                }
            }
        }
        return leavesList;
    }

    public List<MissingClocking> getMissingClockings(YearMonthDay beginDate, YearMonthDay endDate) {
        DateInterval interval = new DateInterval(beginDate, endDate);
        List<MissingClocking> missingClockingsList = new ArrayList<MissingClocking>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof MissingClocking
                    && interval.containsDate(assiduousnessRecord.getDate())
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState().equals(AnulationState.INVALID))) {
                missingClockingsList.add((MissingClocking) assiduousnessRecord);
            }
        }
        return missingClockingsList;
    }

    public boolean isHoliday(YearMonthDay thisDay) {
        Campus campus = getAssiduousnessCampus(thisDay);
        return Holiday.isHoliday(thisDay, campus);
    }

    private Campus getAssiduousnessCampus(YearMonthDay thisDay) {
        for (AssiduousnessCampusHistory assiduousnessCampusHistory : getAssiduousnessCampusHistories()) {
            DateInterval dateInterval = new DateInterval(assiduousnessCampusHistory.getBeginDate(),
                    assiduousnessCampusHistory.getEndDate());
            if (dateInterval.containsDate(thisDay)) {
                return assiduousnessCampusHistory.getCampus();
            }
        }
        return null;
    }

    public YearMonthDay getLastActiveStatusBetween(YearMonthDay beginDate, YearMonthDay endDate) {
        YearMonthDay lastActiveStatus = null;
        for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
            if (assiduousnessStatusHistory.getEndDate() != null) {
                Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate()
                        .toDateMidnight(), assiduousnessStatusHistory.getEndDate().toDateMidnight()
                        .plusDays(1));
                Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight()
                        .plusDays(1));
                if (interval.overlaps(statusInterval)
                        && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
                    if (lastActiveStatus == null
                            || !lastActiveStatus.isAfter(assiduousnessStatusHistory.getEndDate())) {
                        lastActiveStatus = assiduousnessStatusHistory.getEndDate();
                    }
                }
            } else {
                if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory
                        .getBeginDate().isEqual(endDate))
                        && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
                    lastActiveStatus = endDate;
                }
            }

        }
        if (lastActiveStatus != null && lastActiveStatus.isAfter(endDate)) {
            lastActiveStatus = endDate;
        }
        return lastActiveStatus;
    }

    public List<AssiduousnessStatusHistory> getStatusBetween(YearMonthDay beginDate, YearMonthDay endDate) {
        List<AssiduousnessStatusHistory> assiduousnessStatusList = new ArrayList<AssiduousnessStatusHistory>();
        for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
            if (assiduousnessStatusHistory.getEndDate() != null) {
                DateInterval statusInterval = new DateInterval(
                        assiduousnessStatusHistory.getBeginDate(), assiduousnessStatusHistory
                                .getEndDate());
                DateInterval interval = new DateInterval(beginDate,endDate);
                if (interval.containsInterval(statusInterval) || statusInterval.containsInterval(interval)) {
                    assiduousnessStatusList.add(assiduousnessStatusHistory);
                }
            } else {
                if (assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory
                        .getBeginDate().isEqual(endDate)) {
                    assiduousnessStatusList.add(assiduousnessStatusHistory);
                }
            }
        }
        return assiduousnessStatusList;
    }

    public boolean isStatusActive(YearMonthDay beginDate, YearMonthDay endDate) {
        for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
            if (assiduousnessStatusHistory.getEndDate() != null) {
                Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate()
                        .toDateMidnight(), assiduousnessStatusHistory.getEndDate().toDateMidnight()
                        .plusDays(1));
                Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight()
                        .plusDays(1));
                if (interval.overlaps(statusInterval)
                        && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
                    return true;
                }
            } else {
                if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory
                        .getBeginDate().isEqual(endDate))
                        && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
                    return true;
                }
            }

        }
        return false;
    }

    public List<Campus> getCampusForInterval(YearMonthDay begin, YearMonthDay end) {

        List<AssiduousnessCampusHistory> histories = this.getAssiduousnessCampusHistories();
        List<Campus> campus = new ArrayList<Campus>();
        DateInterval targetInterval = new DateInterval(begin, end);
        for (AssiduousnessCampusHistory history : histories) {
            DateInterval historyInterval = new DateInterval(history.getBeginDate(), history.getEndDate());
            if (historyInterval.containsInterval(targetInterval)) {
                campus.add(history.getCampus());
            }
        }
        return campus;
    }

    public boolean overlapsOtherSchedules(final Schedule schedule, YearMonthDay beginDate,
            YearMonthDay endDate) {
        DateInterval scheduleInterval = new DateInterval(beginDate, endDate);
        for (final Schedule otherSchedule : getSchedules()) {
            if (schedule != otherSchedule) {
                DateInterval otherScheduleInterval = new DateInterval(otherSchedule.getBeginDate(),
                        otherSchedule.getEndDate());
                if (scheduleInterval.containsInterval(otherScheduleInterval)
                        || otherScheduleInterval.containsInterval(scheduleInterval)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyRecordsBetweenDates(YearMonthDay begin, YearMonthDay end) {
        DateInterval dateInterval = new DateInterval(begin, end);
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (dateInterval.containsDate(assiduousnessRecord.getDate())
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState().equals(AnulationState.INVALID))) {
                return true;
            }
        }
        return false;
    }
}