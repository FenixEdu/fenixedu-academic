package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Assiduousness extends Assiduousness_Base {

    public Assiduousness(Employee employee) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setEmployee(employee);
    }

    public Schedule getCurrentSchedule() {
        for (final Schedule schedule : getSchedules()) {
            if (schedule.getEndDate() == null && !schedule.getException()) {
                return schedule;
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
        WorkSchedule workSchedule = getSchedule(day).workScheduleWithDate(day);
        Timeline timeline = new Timeline(workSchedule.getWorkScheduleType());
        Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator();

        List<Leave> leaves = getLeaves(day);
        Collections.sort(leaves, new BeanComparator("date"));

        DateTime init = day.toDateTime(workSchedule.getWorkScheduleType().getWorkTime());
        DateTime end = day.toDateTime(workSchedule.getWorkScheduleType().getWorkEndTime());
        if (workSchedule.getWorkScheduleType().isNextDay()) {
            end = end.plusDays(1);
        }
        List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(init, end);
        Collections.sort(clockings, new BeanComparator("date"));

        Leave dayOccurrence = getOccurrenceLeave(leaves);

        DailyBalance dailyBalance = null;
        if (dayOccurrence != null) {
            dailyBalance = new DailyBalance(day, workSchedule);
            dailyBalance.setWorkedOnNormalWorkPeriod(workSchedule.getWorkScheduleType()
                    .getNormalWorkPeriod().getWorkPeriodDuration());
            dailyBalance.setJustification(true); // ver o tipo de justificacao
            dailyBalance.setComment(dayOccurrence.getJustificationMotive().getAcronym());
        } else {
            timeline.plotListInTimeline(clockings, leaves, attributesIt, day);
            timeline.print();
            if (!clockings.isEmpty()) {
                DateTime firstClockingDate = (clockings.iterator().next()).getDate();
                DateTime lastClockingDate = (clockings.get(clockings.size() - 1)).getDate();
                dailyBalance = workSchedule.calculateWorkingPeriods(day, firstClockingDate,
                        lastClockingDate, timeline);
            }
            List<Leave> balanceLeavesList = getBalanceLeaves(leaves);
            if (dailyBalance == null) {
                dailyBalance = new DailyBalance(day, workSchedule);
            }
            dailyBalance.discountBalanceLeaveInFixedPeriod(balanceLeavesList);
        }
        return dailyBalance;
    }

    private List<Leave> getBalanceLeaves(List<Leave> leaves) {
        List<Leave> balanceLeaves = new ArrayList<Leave>();
        for (Leave leave : leaves) {
            if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.BALANCE)) {
                balanceLeaves.add(leave);
            }
        }
        return balanceLeaves;
    }

    private Leave getOccurrenceLeave(List<Leave> leaves) {
        for (Leave leave : leaves) {
            if (leave.getJustificationMotive().getJustificationType().equals(
                    JustificationType.OCCURRENCE)) {
                return leave;
            }
        }
        return null;
    }

    private List<Leave> getLeaves(YearMonthDay day) {
        List<Leave> leaves = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave) {
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
                    && interval.containsDate(assiduousnessRecord.getDate())) {
                clockingsList.add((Clocking) assiduousnessRecord);
            }
        }
        return clockingsList;
    }

    public List<Leave> getLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
        DateInterval interval = new DateInterval(beginDate, endDate);
        List<Leave> clockingsList = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && interval.containsDate(assiduousnessRecord.getDate())) {
                clockingsList.add((Leave) assiduousnessRecord);
            }
        }
        return clockingsList;
    }

}
