package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DateInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.YearMonthDay;

public class Assiduousness extends Assiduousness_Base {

    public Assiduousness(Employee employee) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setEmployee(employee);
    }

    public List calculateIntervalBalance(DateInterval interval) {
        List<DailyBalance> dailyBalanceList = new ArrayList<DailyBalance>();
        List<Schedule> scheduleList = getSchedulesWithInterval(interval);
        // para cada dia do intervalo ver q horario esta definido nesse dia
        for (YearMonthDay date = interval.getStartDate(); ola(date, interval); date = date.plusDays(1)) {
            // percorrer os schedules (tipicamente ha' 1 mas pode haver varios...)
            System.out.println("percorrer os dias do intervalo");
            for (Schedule schedule : scheduleList) {
                System.out.println("percorrer os schedules");
                WorkSchedule workSchedule = schedule.workScheduleWithDate(date);
                DailyBalance dailyBalance = new DailyBalance(date);
                if (workSchedule != null) { // ha horario neste dia
                    System.out.println("horario:" + workSchedule.getWorkScheduleType().getAcronym());
                    dailyBalance.setWorkSchedule(workSchedule);
                    List<Clocking> clockingList = clockingsWithDate(date);
                    List<Leave> leaveList = leavesWithDate(date);
                    if (clockingList.size() > 0) {
                        dailyBalance.setClockingList(clockingList);
                    }
                    if (leaveList.size() > 0) {
                        dailyBalance.setLeaveList(leaveList);
                    }
                    dailyBalance = calculateDailyBalance(date, workSchedule, clockingList, leaveList);
                    System.out.println("horario added:"
                            + workSchedule.getWorkScheduleType().getAcronym());
                    dailyBalanceList.add(dailyBalance);
                } else { // nao ha horario neste dia => sabado ou domingo
                    // neste caso devia ter info de DSC e DS?
                    // TODO ver esta situacao
                }
            }
        }
        return dailyBalanceList;
    }

    public boolean ola(YearMonthDay date, DateInterval interval) {
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

    // Returns a list of clockings made on date
    public List<Clocking> clockingsWithDate(YearMonthDay date) {
        List<Clocking> clockingsList = new ArrayList<Clocking>();
        Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
        while (itAssidRecord.hasNext()) {
            AssiduousnessRecord assidRecord = itAssidRecord.next();
            if (assidRecord instanceof Clocking) {
                Clocking clocking = (Clocking) assidRecord;
                if (clocking.getDate().toYearMonthDay().equals(date)) { // TODO and clocking
                    // state valid?!
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

    public DailyBalance calculateDailyBalance(YearMonthDay day, WorkSchedule workSchedule,
            List<Clocking> clockingList, List<Leave> leaveList) {
        Timeline timeline = new Timeline();
        workSchedule.getWorkScheduleType().plotInTimeline(timeline);
        Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes()
                .iterator();
        Clocking.plotListInTimeline(clockingList, attributesIt, timeline);
        Leave.plotListInTimeline(leaveList, attributesIt, timeline);
        timeline.print();
        DailyBalance dailyBalance = workSchedule.calculateWorkingPeriods(day, clockingList, timeline);
        return dailyBalance;
    }

    public List<Clocking> getClockings() {
        List<Clocking> clockingsList = new ArrayList<Clocking>();
        for (AssiduousnessRecord assiduousnessRecord : getAssiduousnessRecords()) {
            if (assiduousnessRecord instanceof Clocking) {
                clockingsList.add((Clocking) assiduousnessRecord);
            }
        }
        return clockingsList;
    }
}
