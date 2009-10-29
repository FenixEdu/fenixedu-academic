package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.CalculateDailyWorkSheetStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys.ICalculateDailyWorkSheetStrategy;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessExportChoices;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssiduousnessMonthlyResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecordMonthIndex;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ReadMonthResume extends FenixService {

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static List<AssiduousnessMonthlyResume> run(AssiduousnessExportChoices assiduousnessExportChoices) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(assiduousnessExportChoices.getYearMonth());
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

	Collections.sort(assiduousnessMonthlyResumeList, AssiduousnessMonthlyResume.COMPARATOR_BY_EMPLOYEE_NUMBER);
	return assiduousnessMonthlyResumeList;
    }

    private static AssiduousnessMonthlyResume getMonthAssiduousnessBalance(Assiduousness assiduousness,
	    List<AssiduousnessRecord> assiduousnessRecords, LocalDate beginDate, LocalDate endDate) {
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap, init,
		end);
	HashMap<LocalDate, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);
	Duration totalBalance = Duration.ZERO;
	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration holidayRest = Duration.ZERO;
	Duration extra125 = Duration.ZERO;
	Duration extra150 = Duration.ZERO;
	Duration nightWork = Duration.ZERO;
	Duration unjustified = Duration.ZERO;
	LocalDate today = new LocalDate();
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    if (thisDay.isBefore(today)) {
		final Schedule schedule = assiduousness.getSchedule(thisDay);
		if (schedule != null && assiduousness.isStatusActive(thisDay, thisDay)) {
		    final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
		    final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
		    WorkDaySheet workDaySheet = new WorkDaySheet(assiduousness, thisDay, workSchedule, getDayClockings(
			    clockingsMap, thisDay), getDayLeaves(leavesMap, thisDay));
		    ICalculateDailyWorkSheetStrategy calculateDailyWorkSheetStrategy = CalculateDailyWorkSheetStrategyFactory
			    .getInstance().getCalculateDailyWorkSheetStrategy(thisDay);
		    workDaySheet = calculateDailyWorkSheetStrategy.calculateDailyBalance(assiduousness, workDaySheet,
			    isDayHoliday);

		    if (workSchedule != null && !isDayHoliday) {
			Duration thisDayBalance = workDaySheet.getBalanceTime();
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

    private static HashMap<LocalDate, List<Leave>> getLeavesMap(List<AssiduousnessRecord> assiduousnessRecords,
	    LocalDate beginDate, LocalDate endDate) {
	HashMap<LocalDate, List<Leave>> leavesMap = new HashMap<LocalDate, List<Leave>>();
	if (assiduousnessRecords != null) {
	    for (AssiduousnessRecord record : assiduousnessRecords) {
		if (record.isLeave() && !record.isAnulated()) {
		    LocalDate endLeaveDay = record.getDate().toLocalDate().plusDays(1);
		    if (((Leave) record).getEndLocalDate() != null) {
			endLeaveDay = ((Leave) record).getEndLocalDate().plusDays(1);
		    }
		    for (LocalDate leaveDay = record.getDate().toLocalDate(); leaveDay.isBefore(endLeaveDay); leaveDay = leaveDay
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

    private static HashMap<LocalDate, List<AssiduousnessRecord>> getClockingsMap(List<AssiduousnessRecord> assiduousnessRecords,
	    HashMap<LocalDate, WorkSchedule> workScheduleMap, DateTime init, DateTime end) {
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = new HashMap<LocalDate, List<AssiduousnessRecord>>();
	if (assiduousnessRecords != null) {
	    final List<AssiduousnessRecord> clockings = new ArrayList<AssiduousnessRecord>(assiduousnessRecords);
	    Collections.sort(clockings, AssiduousnessRecord.COMPARATOR_BY_DATE);
	    for (AssiduousnessRecord record : clockings) {
		if (record.isClocking() || record.isMissingClocking()) {
		    LocalDate clockDay = record.getDate().toLocalDate();
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

    private static Duration setUnjustified(WorkDaySheet workDaySheet) {
	Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	if (thisDayUnjustified == null) {
	    thisDayUnjustified = Duration.ZERO;
	}
	return thisDayUnjustified;
    }

    private static Duration[] setExtraWork(WorkDaySheet workDaySheet, Duration thisDayBalance) {
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

    private static Duration setNightExtraWork(WorkDaySheet workDaySheet) {
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

    private static List<Leave> getDayLeaves(HashMap<LocalDate, List<Leave>> leavesMap, LocalDate thisDay) {
	List<Leave> leavesList = leavesMap.get(thisDay);
	if (leavesList == null) {
	    leavesList = new ArrayList<Leave>();
	}
	Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
	return leavesList;
    }

    private static List<AssiduousnessRecord> getDayClockings(HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap,
	    LocalDate thisDay) {
	List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
	if (clockingsList == null) {
	    clockingsList = new ArrayList<AssiduousnessRecord>();
	}
	Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
	return clockingsList;
    }

    private static DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
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

    private static DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

    private static HashMap<Assiduousness, List<AssiduousnessRecord>> getAssiduousnessRecord(LocalDate beginDate, LocalDate endDate) {
	HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessLeaves = new HashMap<Assiduousness, List<AssiduousnessRecord>>();
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		.toDateMidnight()));
	Set<AssiduousnessRecord> assiduousnessRecordList = AssiduousnessRecordMonthIndex.getAssiduousnessRecordBetweenDates(
		interval.getStart(), interval.getEnd());
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecordList) {
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