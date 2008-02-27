package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessMonthlyResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class ReadMonthResume extends Service {

    public List<AssiduousnessMonthlyResume> run(AssiduousnessExportChoices assiduousnessExportChoices) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(assiduousnessExportChoices.getYearMonth());
	List<AssiduousnessMonthlyResume> assiduousnessMonthlyResumeList = new ArrayList<AssiduousnessMonthlyResume>();
	if (closedMonth == null) {
	    HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords = getAssiduousnessRecord(
		    assiduousnessExportChoices.getBeginDate(), assiduousnessExportChoices.getEndDate().plusDays(1));
	    for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {
		if (assiduousness.isStatusActive(assiduousnessExportChoices.getBeginDate(), assiduousnessExportChoices
			.getEndDate())) {
		    assiduousnessMonthlyResumeList.add(getMonthAssiduousnessBalance(assiduousness, assiduousnessRecords
			    .get(assiduousness), assiduousnessExportChoices.getBeginDate(), assiduousnessExportChoices
			    .getEndDate()));
		}

	    }
	} else {
	    for (Assiduousness assiduousness : assiduousnessExportChoices.getAssiduousnesses()) {
		for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths(assiduousness)) {
		    if (assiduousnessClosedMonth != null) {
			AssiduousnessMonthlyResume assiduousnessMonthlyResume = new AssiduousnessMonthlyResume(
				assiduousnessClosedMonth);
			assiduousnessMonthlyResumeList.add(assiduousnessMonthlyResume);
		    }
		}
	    }
	}

	Collections.sort(assiduousnessMonthlyResumeList, new BeanComparator("employee.employeeNumber"));
	return assiduousnessMonthlyResumeList;
    }

    private AssiduousnessMonthlyResume getMonthAssiduousnessBalance(Assiduousness assiduousness,
	    List<AssiduousnessRecord> assiduousnessRecords, YearMonthDay beginDate, YearMonthDay endDate) {
	YearMonthDay lowerBeginDate = beginDate.minusDays(8);
	HashMap<YearMonthDay, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap,
		init, end);
	HashMap<YearMonthDay, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);
	Duration totalBalance = Duration.ZERO;
	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration holidayRest = Duration.ZERO;
	Duration extra125 = Duration.ZERO;
	Duration extra150 = Duration.ZERO;
	Duration nightWork = Duration.ZERO;
	Duration unjustified = Duration.ZERO;
	YearMonthDay today = new YearMonthDay();
	for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    if (thisDay.isBefore(today)) {
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
		    workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday, true);
		    if (workSchedule != null && !isDayHoliday) {
			Duration thisDayBalance = workDaySheet.getBalanceTime().toDurationFrom(new DateMidnight());
			if (!workSchedule.getWorkScheduleType().getScheduleClockingType().equals(
				ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
			    totalBalance = totalBalance.plus(thisDayBalance);
			}
			nightWork = nightWork.plus(setNightExtraWork(workDaySheet));
			Duration[] extraWork = setExtraWork(workDaySheet, thisDayBalance);
			extra125 = extra125.plus(extraWork[0]);
			extra150 = extra150.plus(extraWork[1]);
			unjustified = unjustified.plus(setUnjustified(workDaySheet));
		    } else {
			totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance.plus(workDaySheet
				.getComplementaryWeeklyRest());
			totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet.getWeeklyRest());
			holidayRest = holidayRest.plus(workDaySheet.getHolidayRest());
		    }
		}
	    }
	}

	AssiduousnessMonthlyResume assiduousnessMonthlyResume = new AssiduousnessMonthlyResume(assiduousness.getEmployee(),
		totalBalance, totalWeeklyRestBalance, totalComplementaryWeeklyRestBalance, holidayRest, nightWork, unjustified);
	return assiduousnessMonthlyResume;
    }

    private HashMap<YearMonthDay, List<Leave>> getLeavesMap(List<AssiduousnessRecord> assiduousnessRecords,
	    YearMonthDay beginDate, YearMonthDay endDate) {
	HashMap<YearMonthDay, List<Leave>> leavesMap = new HashMap<YearMonthDay, List<Leave>>();
	if (assiduousnessRecords != null) {
	    for (AssiduousnessRecord record : assiduousnessRecords) {
		if (record.isLeave() && !record.isAnulated()) {
		    YearMonthDay endLeaveDay = record.getDate().toYearMonthDay().plusDays(1);
		    if (((Leave) record).getEndYearMonthDay() != null) {
			endLeaveDay = ((Leave) record).getEndYearMonthDay().plusDays(1);
		    }
		    for (YearMonthDay leaveDay = record.getDate().toYearMonthDay(); leaveDay.isBefore(endLeaveDay); leaveDay = leaveDay
			    .plusDays(1)) {
			if (((Leave) record).getAplicableWeekDays() == null
				|| ((Leave) record).getAplicableWeekDays().contains(leaveDay.toDateTimeAtMidnight())) {
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

    private HashMap<YearMonthDay, List<AssiduousnessRecord>> getClockingsMap(List<AssiduousnessRecord> assiduousnessRecords,
	    HashMap<YearMonthDay, WorkSchedule> workScheduleMap, DateTime init, DateTime end) {
	HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = new HashMap<YearMonthDay, List<AssiduousnessRecord>>();
	if (assiduousnessRecords != null) {
	    final List<AssiduousnessRecord> clockings = new ArrayList<AssiduousnessRecord>(assiduousnessRecords);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    for (AssiduousnessRecord record : clockings) {
		if (record.isClocking() || record.isMissingClocking()) {
		    YearMonthDay clockDay = record.getDate().toYearMonthDay();
		    if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) == 0) {
			if (clockingsMap.get(clockDay.minusDays(1)) != null
				&& clockingsMap.get(clockDay.minusDays(1)).size() % 2 != 0) {
			    clockDay = clockDay.minusDays(1);
			}
		    } else if (WorkSchedule.overlapsSchedule(record.getDate(), workScheduleMap) < 0) {
			clockDay = clockDay.minusDays(1);
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

    private Duration setUnjustified(WorkDaySheet workDaySheet) {
	Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	if (thisDayUnjustified == null) {
	    thisDayUnjustified = Duration.ZERO;
	}
	return thisDayUnjustified;
    }

    private Duration[] setExtraWork(WorkDaySheet workDaySheet, Duration thisDayBalance) {
	Duration[] result = new Duration[] { Duration.ZERO, Duration.ZERO };
	Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	if (thisDayUnjustified == null) {
	    thisDayUnjustified = Duration.ZERO;
	}
	Duration hourDuration = new Duration(3600000);
	// TODO Duration thisDayExtraWork = thisDayBalance; ignore unjustified
	Duration thisDayExtraWork = thisDayBalance.plus(thisDayUnjustified);
	if (!thisDayExtraWork.equals(Duration.ZERO)) {
	    if (thisDayExtraWork.isLongerThan(hourDuration)) {
		result[1] = thisDayExtraWork.minus(hourDuration);
		result[0] = hourDuration;
	    } else if (thisDayExtraWork.isLongerThan(Duration.ZERO)) {
		result[0] = thisDayExtraWork;
	    }
	}
	return result;
    }

    private Duration setNightExtraWork(WorkDaySheet workDaySheet) {
	if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null) {
	    final Duration midHour = new Duration(1800000);
	    Duration extraWorkDuration = workDaySheet.getTimeline().calculateWorkPeriodDurationBetweenDates(
		    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
		    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1));
	    if (!extraWorkDuration.equals(Duration.ZERO)) {
		if (!extraWorkDuration.isShorterThan(midHour)) {
		    return extraWorkDuration;
		}
	    }
	}
	return Duration.ZERO;
    }

    private List<Leave> getDayLeaves(HashMap<YearMonthDay, List<Leave>> leavesMap, YearMonthDay thisDay) {
	List<Leave> leavesList = leavesMap.get(thisDay);
	if (leavesList == null) {
	    leavesList = new ArrayList<Leave>();
	}
	Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
	return leavesList;
    }

    private List<AssiduousnessRecord> getDayClockings(HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap,
	    YearMonthDay thisDay) {
	List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
	if (clockingsList == null) {
	    clockingsList = new ArrayList<AssiduousnessRecord>();
	}
	Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
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

    private DateTime getInit(YearMonthDay lowerBeginDate, HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

    private HashMap<Assiduousness, List<AssiduousnessRecord>> getAssiduousnessRecord(YearMonthDay beginDate, YearMonthDay endDate) {
	HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessLeaves = new HashMap<Assiduousness, List<AssiduousnessRecord>>();
	Interval interval = new Interval(beginDate.toDateTimeAtMidnight(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : rootDomainObject.getAssiduousnessRecords()) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), ((Leave) assiduousnessRecord).getEndDate()
			.plusSeconds(1));
		if (leaveInterval.overlaps(interval)) {
		    List<AssiduousnessRecord> leavesList = assiduousnessLeaves.get(assiduousnessRecord.getAssiduousness());
		    if (leavesList == null) {
			leavesList = new ArrayList<AssiduousnessRecord>();
		    }
		    leavesList.add(assiduousnessRecord);
		    assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
		}
	    } else if ((assiduousnessRecord.isClocking() || assiduousnessRecord.isMissingClocking())
		    && (!assiduousnessRecord.isAnulated())) {
		if (interval.contains(assiduousnessRecord.getDate().getMillis())) {
		    List<AssiduousnessRecord> list = assiduousnessLeaves.get(assiduousnessRecord.getAssiduousness());
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