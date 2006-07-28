package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.EnumSet;
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

    public WorkDaySheet calculateDailyBalance(WorkDaySheet workDaySheet, YearMonthDay day,
            boolean isDayHoliday) {
        if (workDaySheet.getWorkSchedule() != null && !isDayHoliday) {
            Timeline timeline = new Timeline(workDaySheet.getWorkSchedule().getWorkScheduleType());

            List<Leave> dayOccurrences = getLeavesByType(workDaySheet.getLeaves(),
                    JustificationType.OCCURRENCE);

            if (dayOccurrences.isEmpty()) {
                List<Leave> timeLeaves = getLeavesByType(workDaySheet.getLeaves(),
                        JustificationType.TIME);
                List<Leave> balanceLeaves = getLeavesByType(workDaySheet.getLeaves(),
                        JustificationType.BALANCE);
                if (!workDaySheet.getAssiduousnessRecords().isEmpty() || !timeLeaves.isEmpty()) {
                    Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES
                            .getAttributes().iterator();
                    timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), workDaySheet
                            .getLeaves(), attributesIt, day);
                    workDaySheet = workDaySheet.getWorkSchedule().calculateWorkingPeriods(workDaySheet,
                            day, timeline, timeLeaves);
                } else {
                    workDaySheet.setBalanceTime(Duration.ZERO.minus(
                            workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
                                    .getWorkPeriodDuration()).toPeriod());
                    if (balanceLeaves.isEmpty()) {
                        workDaySheet.setNotes(workDaySheet.getNotes().concat("FALTA INJ"));
                    }
                }
                workDaySheet.discountBalanceLeaveInFixedPeriod(balanceLeaves);
            }
        } else {
            final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(day.toDateTimeAtMidnight());
            final EnumSet<WeekDay> enumSetWeekDay = EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY);

            if (!isDayHoliday && !workDaySheet.getAssiduousnessRecords().isEmpty()
                    && enumSetWeekDay.contains(dayOfWeek)) {
                final Timeline timeline = new Timeline(day);
                Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                        .iterator();
                timeline.plotListInTimeline(workDaySheet.getAssiduousnessRecords(), workDaySheet
                        .getLeaves(), attributesIt, day);
                final Duration worked = timeline.calculateWorkPeriodDuration(null, timeline
                        .getTimePoints().iterator().next(), new TimePoint(defaultStartWorkDay,
                        AttributeType.NULL), new TimePoint(defaultStartWorkDay, AttributeType.NULL),
                        null);
                workDaySheet.setBalanceTime(worked.toPeriod());
            }
        }
        return workDaySheet;
    }

    private List<Leave> getLeavesByType(List<Leave> leaves, JustificationType justificationType) {
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

    public boolean isStatusActive(YearMonthDay beginDate, YearMonthDay endDate) {
        for (AssiduousnessStatusHistory assiduousnessStatusHistory : getAssiduousnessStatusHistories()) {
            if (assiduousnessStatusHistory.getEndDate() != null) {
                Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate()
                        .toDateMidnight(), assiduousnessStatusHistory.getEndDate().toDateMidnight()
                        .plus(3600));
                Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight()
                        .plus(3600));
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

}
