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
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class Assiduousness extends Assiduousness_Base {

    public Assiduousness(Employee employee) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        //setEmployee(employee);
    }

    public List calculateIntervalBalance(DateInterval interval) {
        System.out.println("Calculate interval balance");
        List<DailyBalance> dailyBalanceList = new ArrayList<DailyBalance>();
        List<Schedule> scheduleList = getSchedulesWithInterval(interval); // todos os Schedules dentro
        // do intervalo validInterval
        // para cada dia do intervalo ver q horario esta definido nesse dia
        for (YearMonthDay date = interval.getStartDate(); isDateInInterval(date, interval); date = date
                .plusDays(1)) {
            // percorrer os schedules (tipicamente ha' 1 mas pode haver varios...)
            // System.out.println("percorrer os dias do intervalo");
            for (Schedule schedule : scheduleList) { // para cada schedule...
                WorkSchedule workSchedule = schedule.workScheduleWithDate(date); // ...ve que Work
                // Schedule esta'
                // definido nesse dia
                DailyBalance dailyBalance = new DailyBalance(date, workSchedule);
                if (workSchedule != null) { // ha horario neste dia
                    System.out.println("horario:" + workSchedule.getWorkScheduleType().getAcronym());
                    dailyBalance.setWorkSchedule(workSchedule);
                    dailyBalance = calculateDailyBalance(date);
                    dailyBalanceList.add(dailyBalance);
                }
            }
        }
        return dailyBalanceList;
    }

    public boolean isDateInInterval(YearMonthDay date, DateInterval interval) {
        return (date.isBefore(interval.getEndDate()) || date.isEqual(interval.getEndDate()) || date
                .isEqual(interval.getStartDate()));
    }

    // Returns a list with the schedules valid in a DateInterval
    public List<Schedule> getSchedulesWithInterval(DateInterval interval) {
        List<Schedule> scheduleList = new ArrayList<Schedule>();
        Iterator<Schedule> schedulesIt = getSchedulesIterator();
        while (schedulesIt.hasNext()) {
            Schedule schedule = schedulesIt.next();
            if (schedule.isDefinedInInterval(interval)) {
                scheduleList.add(schedule);
            }
        }
        return scheduleList;
    }

    // returns the schedule used on the specified date
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

    // Returns a list of clockings made on date
    public List<Clocking> validClockingsWithDate(YearMonthDay date) {
        List<Clocking> clockingsList = new ArrayList<Clocking>();
        Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
        while (itAssidRecord.hasNext()) {
            AssiduousnessRecord assidRecord = itAssidRecord.next();
            if (assidRecord instanceof Clocking) {
                Clocking clocking = (Clocking) assidRecord;
                if (clocking.getDate().toYearMonthDay().equals(date)) {
                    clockingsList.add(clocking);
                }
            }
        }
        return clockingsList;
    }

    // Returns a list of leaves valid on a date
    public List<Leave> leavesWithDate(YearMonthDay date) {
        List<Leave> leavesList = new ArrayList<Leave>();
        Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
        while (itAssidRecord.hasNext()) {
            AssiduousnessRecord assidRecord = itAssidRecord.next();
            if (assidRecord instanceof Leave) {
                Leave leave = (Leave) assidRecord;
                if (leave.occuredInDate(date)) {
                    leavesList.add(leave);
                }
            }
        }
        return leavesList;
    }

    // Returns a list of missing clockings valid on a date
    public List<MissingClocking> missingClockingsWithDate(YearMonthDay date) {
        List<MissingClocking> missingClockingList = new ArrayList<MissingClocking>();
        Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
        while (itAssidRecord.hasNext()) {
            AssiduousnessRecord assidRecord = itAssidRecord.next();
            if (assidRecord instanceof MissingClocking) {
                MissingClocking missingClocking = (MissingClocking) assidRecord;
                if (missingClocking.occuredInDate(date)) {
                    missingClockingList.add(missingClocking);
                }
            }
        }
        return missingClockingList;
    }

    public DailyBalance calculateDailyBalance(YearMonthDay day) {
        WorkSchedule workSchedule = getSchedule(day).workScheduleWithDate(day);
        Timeline timeline = new Timeline(workSchedule.getWorkScheduleType());
        Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator();

        List<Leave> leaves = getLeaves(day, day);
        Collections.sort(leaves, new BeanComparator("date"));
        
        DateTime init = day.toDateTime(workSchedule.getWorkScheduleType().getWorkTime());
        DateTime end = day.toDateTime(workSchedule.getWorkScheduleType().getWorkEndTime());
        if (workSchedule.getWorkScheduleType().isNextDay()) {
            end = end.plusDays(1);
        }
        List<AssiduousnessRecord> clockings = getClockingsAndMissingClockings(init, end);
        Collections.sort(clockings, new BeanComparator("date"));
        // for (AssiduousnessRecord assiduousnessRecord : clockings) {
        // assiduousnessRecord.setDate(assiduousnessRecord.getDate().minusSeconds(
        // assiduousnessRecord.getDate().getSecondOfMinute()));
        // }

        DailyBalance dailyBalance = null;
        if (clockings.size() > 0) {
            timeline.plotListInTimeline(clockings, leaves, attributesIt, day);
            timeline.print();
            DateTime firstClockingDate = null;
            firstClockingDate = (clockings.iterator().next()).getDate();
            DateTime lastClockingDate = null;
            lastClockingDate = (clockings.get(clockings.size() - 1)).getDate();
            dailyBalance = workSchedule.calculateWorkingPeriods(day, firstClockingDate, lastClockingDate,
                timeline);
            List<Leave> balanceLeavesList = getBalanceLeaves(day, day); // descontar as compensacoes no periodo fixo
            if (balanceLeavesList.size() > 0) {
                dailyBalance.discountBalanceLeaveInFixedPeriod(balanceLeavesList);
            }
        } else { // nao ha marcacoes
            System.out.println("nao ha marcacoes");
            Leave occurrence = null;
            if (leaves.size() > 0) { // ha leaves associadas ao dia
                if ((leaves.get(0).getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) && (leaves.get(0).justificationForDay(day))) {
                    occurrence = leaves.get(0); // pode haver mais q 1 justificacao por dia?
                }
            } else { // nao ha leaves associadas ao dia, mas o dia esta dentro da duracao da leave
                occurrence = getOccurrenceLeaveContainsDate(day);
            }
            if (occurrence != null) {
                dailyBalance = new DailyBalance(day, workSchedule);
                dailyBalance.setWorkedOnNormalWorkPeriod(workSchedule.getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
                if (workSchedule.getWorkScheduleType().definedFixedPeriod()) {
                    dailyBalance.setFixedPeriodAbsence(workSchedule.getWorkScheduleType().getFixedWorkPeriod().getWorkPeriodDuration());
                }
                dailyBalance.setJustification(true); // ver o tipo de justificacao
                dailyBalance.setComment(occurrence.getJustificationMotive().getAcronym()); // observacao com o tipo de justificacao utilizada.
            }
        }
        return dailyBalance;
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
    
    // Returns all the Balance Leaves (Compensacao) for a particular begin-end date
    public List<Leave> getBalanceLeaves(YearMonthDay beginDate, YearMonthDay endDate) {
        DateInterval interval = new DateInterval(beginDate, endDate);
        List<Leave> balanceLeaveList = new ArrayList<Leave>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave
                    && interval.containsDate(assiduousnessRecord.getDate()) && 
                            ((Leave)assiduousnessRecord).getJustificationMotive().getJustificationType() == JustificationType.BALANCE) {
                balanceLeaveList.add((Leave) assiduousnessRecord);
            }
        }
        return balanceLeaveList;
    }

    // Returns the leave for the date
    public Leave getOccurrenceLeaveContainsDate(YearMonthDay date) {
        Leave occurrence = null;
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Leave) {
                occurrence = (Leave)assiduousnessRecord;
                if ((occurrence.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE) && (occurrence.occuredInDate(date))) {
                    return occurrence;
                }
            }
        }
        return occurrence;
    }

    
}
