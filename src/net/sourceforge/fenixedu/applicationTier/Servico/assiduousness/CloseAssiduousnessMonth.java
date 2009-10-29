package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class CloseAssiduousnessMonth extends FenixService {

    public final static Duration DAY_HOUR_LIMIT = Hours.TWO.toStandardDuration();
    public final static Duration hourDuration = new Duration(3600000);
    public final static Duration midHourDuration = new Duration(1800000);

    public List<AssiduousnessClosedMonth> run(HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords,
	    LocalDate beginDate, LocalDate endDate) {
	ClosedMonth closedMonth = getClosedMonth(beginDate);
	System.out.println("Vou fechar o mes: " + new DateTime());
	List<AssiduousnessClosedMonth> negativeAssiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>();
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    List<AssiduousnessStatusHistory> assiduousnessStatusHistoryList = assiduousness.getStatusBetween(beginDate, endDate);
	    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistoryList) {
		if (assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    LocalDate thisBeginDate = beginDate;
		    if (assiduousnessStatusHistory.getBeginDate().isAfter(beginDate)) {
			thisBeginDate = assiduousnessStatusHistory.getBeginDate();
		    }
		    LocalDate thisEndDate = endDate;
		    if (assiduousnessStatusHistory.getEndDate() != null
			    && assiduousnessStatusHistory.getEndDate().isBefore(endDate)) {
			thisEndDate = assiduousnessStatusHistory.getEndDate();
		    }
		    AssiduousnessClosedMonth assiduousnessClosedMonth = getMonthAssiduousnessBalance(assiduousnessStatusHistory,
			    assiduousnessRecords.get(assiduousness), closedMonth, thisBeginDate, thisEndDate);
		    if (assiduousnessClosedMonth != null) {
			negativeAssiduousnessClosedMonths.add(assiduousnessClosedMonth);
		    }
		}
	    }
	}
	return negativeAssiduousnessClosedMonths;
    }

    private ClosedMonth getClosedMonth(LocalDate beginDate) {
	ClosedMonth closedMonth = ClosedMonth.getOrCreateOpenClosedMonth(beginDate);
	if (closedMonth != null) {
	    closedMonth.setClosedForBalance(Boolean.TRUE);
	    closedMonth.setClosedForBalanceDate(new DateTime());
	}
	return closedMonth;
    }

    private AssiduousnessClosedMonth getMonthAssiduousnessBalance(AssiduousnessStatusHistory assiduousnessStatusHistory,
	    List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousnessStatusHistory.getAssiduousness()
		.getWorkSchedulesBetweenDates(lowerBeginDate, endDate.plusDays(2));
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap, init,
		end);
	HashMap<LocalDate, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);

	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(assiduousnessStatusHistory.getAssiduousness().getEmployee(),
		beginDate, endDate);
	employeeWorkSheet.calculateWorkSheet(workScheduleMap, clockingsMap, leavesMap, true);

	AssiduousnessClosedMonth assiduousnessClosedMonth = new AssiduousnessClosedMonth(assiduousnessStatusHistory, closedMonth,
		employeeWorkSheet, beginDate, endDate);

	for (WorkDaySheet workDaySheet : employeeWorkSheet.getWorkDaySheetList()) {
	    new AssiduousnessClosedDay(assiduousnessClosedMonth, workDaySheet);
	}
	for (JustificationMotive justificationMotive : employeeWorkSheet.getJustificationsDuration().keySet()) {
	    Duration duration = employeeWorkSheet.getJustificationsDuration().get(justificationMotive);
	    new ClosedMonthJustification(assiduousnessClosedMonth, justificationMotive, duration);
	}

	Set<WorkScheduleType> workScheduleTypeSet = new HashSet<WorkScheduleType>(employeeWorkSheet.getExtra25Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra125Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra150Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtra150WithLimitsMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight160Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight190Map().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraNight190WithLimitsMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getUnjustifiedMap().keySet());
	workScheduleTypeSet.addAll(employeeWorkSheet.getExtraWorkNightsMap().keySet());

	for (WorkScheduleType workScheduleType : workScheduleTypeSet) {
	    Duration totalExtra25 = employeeWorkSheet.getExtra25Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra25Map().get(workScheduleType);
	    Duration totalExtra125 = employeeWorkSheet.getExtra125Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra125Map().get(workScheduleType);
	    Duration totalExtra150 = employeeWorkSheet.getExtra150Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra150Map().get(workScheduleType);
	    Duration totalExtra150WithLimit = employeeWorkSheet.getExtra150WithLimitsMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtra150WithLimitsMap().get(workScheduleType);
	    Duration totalNightExtra160 = employeeWorkSheet.getExtraNight160Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight160Map().get(workScheduleType);
	    Duration totalNightExtra190 = employeeWorkSheet.getExtraNight190Map().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight190Map().get(workScheduleType);
	    Duration totalNightExtra190WithLimit = employeeWorkSheet.getExtraNight190WithLimitsMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getExtraNight190WithLimitsMap().get(workScheduleType);
	    Duration totalUnjustified = employeeWorkSheet.getUnjustifiedMap().get(workScheduleType) == null ? Duration.ZERO
		    : employeeWorkSheet.getUnjustifiedMap().get(workScheduleType);
	    Integer extraWorkNights = employeeWorkSheet.getExtraWorkNightsMap().get(workScheduleType) == null ? 0
		    : employeeWorkSheet.getExtraWorkNightsMap().get(workScheduleType);
	    new AssiduousnessExtraWork(assiduousnessClosedMonth, workScheduleType, totalExtra25, totalExtra125, totalExtra150,
		    totalExtra150WithLimit, totalNightExtra160, totalNightExtra190, totalNightExtra190WithLimit, extraWorkNights,
		    totalUnjustified);
	}

	if (employeeWorkSheet.getUnjustifiedDays() > 0 || assiduousnessClosedMonth.getAccumulatedUnjustifiedDays() > 0
		|| assiduousnessClosedMonth.getAccumulatedArticle66Days() > 0
		|| assiduousnessClosedMonth.getArticle66().doubleValue() > 0) {
	    return assiduousnessClosedMonth;
	}
	return null;
    }

    private Duration roundToHalfHour(Duration duration) {
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() >= 30) {
	    return new Duration(Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours() + 1).toStandardDuration());
	}
	Duration result = Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours()).toStandardDuration();
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() != 0) {
	    result = result.plus(midHourDuration);
	}
	return result;
    }

    private HashMap<LocalDate, List<Leave>> getLeavesMap(List<AssiduousnessRecord> assiduousnessRecords, LocalDate beginDate,
	    LocalDate endDate) {
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
				|| ((Leave) record).getAplicableWeekDays().contains(leaveDay.toDateTimeAtStartOfDay())) {
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

    private HashMap<LocalDate, List<AssiduousnessRecord>> getClockingsMap(List<AssiduousnessRecord> assiduousnessRecords,
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

    private void setUnjustified(WorkDaySheet workDaySheet, HashMap<WorkScheduleType, Duration> unjustifiedMap) {
	Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	if (thisDayUnjustified == null) {
	    thisDayUnjustified = Duration.ZERO;
	}
	Duration totalUnjustified = unjustifiedMap.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
	if (totalUnjustified == null) {
	    totalUnjustified = Duration.ZERO;
	}
	totalUnjustified = totalUnjustified.plus(thisDayUnjustified);
	unjustifiedMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), totalUnjustified);
    }

    private void setExtraWork(WorkDaySheet workDaySheet, Duration thisDayBalance,
	    HashMap<WorkScheduleType, Duration> extra125Map, HashMap<WorkScheduleType, Duration> extra150Map,
	    HashMap<WorkScheduleType, Duration> extra150WithLimitsMap) {

	if (workDaySheet.getWorkSchedule().getWorkScheduleType().canDoExtraWorkInWeekDays()) {
	    Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	    if (thisDayUnjustified == null) {
		thisDayUnjustified = Duration.ZERO;
	    }
	    // TODO Duration thisDayExtraWork = thisDayBalance; ignore
	    // unjustified
	    Duration thisDayExtraWork = roundToHalfHour(new Duration(thisDayBalance.minus(thisDayUnjustified)));
	    if (!thisDayExtraWork.equals(Duration.ZERO)) {
		if (thisDayExtraWork.isLongerThan(hourDuration)) {
		    Duration extra125 = extra125Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		    if (extra125 == null) {
			extra125 = Duration.ZERO;
		    }
		    Duration extra150 = extra150Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		    if (extra150 == null) {
			extra150 = Duration.ZERO;
		    }

		    Duration extra150WithLimits = extra150WithLimitsMap.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		    if (extra150WithLimits == null) {
			extra150WithLimits = Duration.ZERO;
		    }
		    extra150 = extra150.plus(thisDayExtraWork.minus(hourDuration));
		    extra150Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra150);

		    extra150WithLimits = new Duration(extra150WithLimits.plus(Math.min(thisDayExtraWork.minus(hourDuration)
			    .getMillis(), DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
		    extra150WithLimitsMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra150WithLimits);

		    extra125 = extra125.plus(hourDuration);
		    extra125Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra125);
		} else if (thisDayExtraWork.isLongerThan(Duration.ZERO)) {
		    Duration extra125 = extra125Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		    if (extra125 == null) {
			extra125 = Duration.ZERO;
		    }
		    extra125 = extra125.plus(thisDayExtraWork);
		    extra125Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra125);
		}
	    }
	}
    }

    private void setNightExtraWork(WorkDaySheet workDaySheet, Duration thisDayBalance,
	    HashMap<WorkScheduleType, Duration> extraNight160Map, HashMap<WorkScheduleType, Duration> extraNight190Map,
	    HashMap<WorkScheduleType, Duration> extraNight190WithLimitsMap, HashMap<WorkScheduleType, Integer> extraWorkNightsMap) {
	if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null
		&& workDaySheet.getWorkSchedule().getWorkScheduleType().canDoExtraWorkInWeekDays()) {
	    Duration nightExtraWorkDuration = roundToHalfHour(new Duration(workDaySheet.getTimeline()
		    .calculateWorkPeriodDurationBetweenDates(
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1))));

	    Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	    if (thisDayUnjustified == null) {
		thisDayUnjustified = Duration.ZERO;
	    }
	    // TODO Duration thisDayExtraWork = thisDayBalance; ignore
	    // unjustified
	    Duration thisDayExtraWork = roundToHalfHour(new Duration(thisDayBalance.minus(thisDayUnjustified)));
	    if ((!thisDayExtraWork.equals(Duration.ZERO)) && (!nightExtraWorkDuration.equals(Duration.ZERO))) {
		Duration extra160 = extraNight160Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		if (extra160 == null) {
		    extra160 = Duration.ZERO;
		}
		Duration extra190 = extraNight190Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		if (extra190 == null) {
		    extra190 = Duration.ZERO;
		}
		Duration extra190WithLimits = extraNight190WithLimitsMap
			.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
		if (extra190WithLimits == null) {
		    extra190WithLimits = Duration.ZERO;
		}
		if (nightExtraWorkDuration.isShorterThan(thisDayExtraWork)) {
		    if (nightExtraWorkDuration.isLongerThan(hourDuration)) {
			extra160 = extra160.plus(hourDuration);
			extra190 = extra190.plus(nightExtraWorkDuration.minus(hourDuration));
			extra190WithLimits = new Duration(extra190WithLimits.plus(Math.min(nightExtraWorkDuration.minus(
				hourDuration).getMillis(), DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
		    } else {
			extra160 = extra160.plus(nightExtraWorkDuration);
		    }
		} else {
		    if (thisDayExtraWork.isLongerThan(hourDuration)) {
			extra160 = extra160.plus(hourDuration);
			extra190 = extra190.plus(thisDayExtraWork.minus(hourDuration));
			extra190WithLimits = new Duration(extra190WithLimits.plus(Math.min(thisDayExtraWork.minus(hourDuration)
				.getMillis(), DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
		    } else {
			extra160 = extra160.plus(thisDayExtraWork);
		    }
		}
		extraNight160Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra160);
		extraNight190Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra190);
		extraNight190WithLimitsMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra190WithLimits);
	    }
	    Integer extraWorkNight = extraWorkNightsMap.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
	    if (extraWorkNight == null) {
		extraWorkNight = 0;
	    } else {
		extraWorkNight += 1;
	    }
	    extraWorkNightsMap.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extraWorkNight);
	}

    }

    private void setNightExtraWork(WorkDaySheet workDaySheet, HashMap<WorkScheduleType, Duration> extra25Map) {
	if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null) {

	    Duration extraWorkDuration = roundToHalfHour(new Duration(workDaySheet.getTimeline()
		    .calculateWorkPeriodDurationBetweenDates(
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1))));
	    if (!extraWorkDuration.equals(Duration.ZERO)) {
		setNightExtraWorkMap(workDaySheet, extra25Map, extraWorkDuration);
	    }
	}
    }

    private void setNightExtraWorkMap(WorkDaySheet workDaySheet, HashMap<WorkScheduleType, Duration> extra25Map,
	    Duration extraWorkDuration) {
	Duration extra25 = extra25Map.get(workDaySheet.getWorkSchedule().getWorkScheduleType());
	if (extra25 == null) {
	    extra25 = Duration.ZERO;
	}
	extra25 = extra25.plus(extraWorkDuration);
	extra25Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra25);
    }

    private List<Leave> getDayLeaves(HashMap<LocalDate, List<Leave>> leavesMap, LocalDate thisDay) {
	List<Leave> leavesList = leavesMap.get(thisDay);
	if (leavesList == null) {
	    leavesList = new ArrayList<Leave>();
	}
	Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
	return leavesList;
    }

    private List<AssiduousnessRecord> getDayClockings(HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap,
	    LocalDate thisDay) {
	List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
	if (clockingsList == null) {
	    clockingsList = new ArrayList<AssiduousnessRecord>();
	}
	Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
	return clockingsList;
    }

    private DateTime getEnd(LocalDate endDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
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

    private DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay);
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime());
	}
	return init;
    }

}