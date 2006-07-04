package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Assiduousness extends Assiduousness_Base {

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
                    DateTime scheduleEndDate = schedule.getBeginDate().isEqual(schedule.getEndDate()) ? schedule
                            .getEndDate().toDateTimeAtMidnight().plus(3600)
                            : schedule.getEndDate().toDateTimeAtMidnight();
                    Interval scheduleInterval = new Interval(schedule.getBeginDate()
                            .toDateTimeAtMidnight(), scheduleEndDate);
                    if (scheduleInterval.contains(now.toDateTimeAtMidnight())) {
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
            // if there are more than one, it's beacause there was an exception schedule in that
            // specified date
            for (Schedule schedule : scheduleList) {
                if (schedule.getException()) {
                    return schedule;
                }
            }
            return null;
        }
    }

    public DailyBalance calculateDailyBalance(YearMonthDay day) {
        Schedule schedule = getSchedule(day);
        DailyBalance dailyBalance = null;
        List<Leave> leaves = getLeaves(day);
        Collections.sort(leaves, new BeanComparator("date"));

        Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator();
        if (schedule != null) {
            WorkSchedule workSchedule = schedule.workScheduleWithDate(day);
            boolean isDayHoliday = isHoliday(day);
            if (workSchedule != null && !isDayHoliday) {
                Timeline timeline = new Timeline(workSchedule.getWorkScheduleType());

                DateTime init = day.toDateTime(workSchedule.getWorkScheduleType().getWorkTime());
                DateTime end = day.toDateTime(workSchedule.getWorkScheduleType().getWorkEndTime());

                if (workSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                    end = end.plusDays(1);
                }

                List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(init, end);
                Collections.sort(clockings, new BeanComparator("date"));
                List<Leave> dayOccurrences = getLeavesByType(leaves, JustificationType.OCCURRENCE);

                if (!dayOccurrences.isEmpty()) {
                    dailyBalance = new DailyBalance(day, workSchedule);
                    dailyBalance.setWorkedOnNormalWorkPeriod(workSchedule.getWorkScheduleType()
                            .getNormalWorkPeriod().getWorkPeriodDuration());
                    dailyBalance.setJustification(true); // ver o tipo de justificacao
                    dailyBalance.setComment(dayOccurrences.get(0).getJustificationMotive().getAcronym());
                } else {
                    timeline.plotListInTimeline(clockings, leaves, attributesIt, day);
                    List<Leave> timeLeaves = getLeavesByType(leaves, JustificationType.TIME);
                    if (!clockings.isEmpty() || !timeLeaves.isEmpty()) {
                        dailyBalance = workSchedule.calculateWorkingPeriods(day, timeline, timeLeaves);
                    }
                    if (dailyBalance == null) {
                        dailyBalance = new DailyBalance(day, workSchedule);
                    }
                    dailyBalance.discountBalanceLeaveInFixedPeriod(getLeavesByType(leaves,
                            JustificationType.BALANCE));
                }
            } else {
                dailyBalance = new DailyBalance(day, null);
                Duration worked = new Duration(0);
                day.toDateMidnight().getDayOfWeek();
                WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
                EnumSet<WeekDay> enumSetWeekDay = EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY);

                List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(day
                        .toDateTimeAtMidnight(), day.toDateTimeAtMidnight().plusDays(1));
                if (enumSetWeekDay.contains(dayOfWeek) && !isDayHoliday && !clockings.isEmpty()) {
                    Collections.sort(clockings, new BeanComparator("date"));
                    Timeline timeline = new Timeline(day);
                    timeline.plotListInTimeline(clockings, leaves, attributesIt, day);
                    worked = timeline.calculateDurationAllIntervalsByAttributes(
                            DomainConstants.WORKED_ATTRIBUTES, new TimePoint(new TimeOfDay(7, 30, 0, 0),
                                    AttributeType.NULL), new TimePoint(new TimeOfDay(23, 59, 59, 99),
                                    AttributeType.NULL));
                    dailyBalance.setTotalBalance(worked);
                } else {
                    dailyBalance.setTotalBalance(new Duration(0));
                }
            }
        } else {
            dailyBalance = new DailyBalance(day, null);
            dailyBalance.setTotalBalance(new Duration(0));
        }
        return dailyBalance;
    }

    private List<Leave> getLeavesByType(List<Leave> leaves, JustificationType justificationType) {
        List<Leave> leavesByType = new ArrayList<Leave>();
        for (Leave leave : leaves) {
            if (leave.getJustificationMotive().getJustificationType().equals(justificationType)) {
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
                            .getState().equals(AnulationState.INVALID))) {
                Leave leave = (Leave) assiduousnessRecord;
                if (leave.occuredInDate(day)) {
                    leaves.add((Leave) assiduousnessRecord);
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
                    && interval.contains(assiduousnessRecord.getDate())
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState().equals(AnulationState.INVALID))) {
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
                    && interval.containsDate(assiduousnessRecord.getDate())
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState().equals(AnulationState.INVALID))) {
                clockingsList.add((Clocking) assiduousnessRecord);
            }
        }
        return clockingsList;
    }

    public List<Leave> getLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
        Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), new TimeOfDay(23, 59, 59)
                .toDateTime(endDate.toDateMidnight()));
        List<Leave> leavesList = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && (assiduousnessRecord.getAnulation() == null || assiduousnessRecord.getAnulation()
                            .getState().equals(AnulationState.INVALID))) {
                Interval leaveInterval = new Interval(assiduousnessRecord.getDate(),
                        ((Leave) assiduousnessRecord).getEndDate());
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
        if (campus != null) {
            for (Holiday holiday : getRootDomainObject().getHolidays()) {
                if ((holiday.getLocality() == null || holiday.getLocality() == campus
                        .getSpaceInformation().getLocality())
                        && holiday.getDate().isMatch(thisDay.toDateMidnight())) {
                    return true;
                }
            }
        }
        return false;
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

}
