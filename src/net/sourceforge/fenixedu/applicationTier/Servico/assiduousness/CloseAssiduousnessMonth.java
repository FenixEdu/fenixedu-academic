package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeBalanceResume;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class CloseAssiduousnessMonth extends Service {

    public List<AssiduousnessClosedMonth> run(YearMonthDay beginDate, YearMonthDay endDate) {
	HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords = getAssiduousnessRecord(beginDate, endDate
		.plusDays(1));
	ClosedMonth closedMonth = getClosedMonth(beginDate);
	System.out.println("Vou fechar o mes: " + new DateTime());
	List<AssiduousnessClosedMonth> negativeAssiduousnessClosedMonths = new ArrayList<AssiduousnessClosedMonth>();
	for (Assiduousness assiduousness : rootDomainObject.getAssiduousnesss()) {
	    List<AssiduousnessStatusHistory> assiduousnessStatusHistoryList = assiduousness.getStatusBetween(beginDate, endDate);
	    for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistoryList) {
		if (assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		    YearMonthDay thisBeginDate = beginDate;
		    if (assiduousnessStatusHistory.getBeginDate().isAfter(beginDate)) {
			thisBeginDate = assiduousnessStatusHistory.getBeginDate();
		    }
		    YearMonthDay thisEndDate = endDate;
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

    private ClosedMonth getClosedMonth(YearMonthDay beginDate) {
	ClosedMonth closedMonth = ClosedMonth.getOpenClosedMonth(new YearMonth(beginDate));
	if (closedMonth != null) {
	    closedMonth.setClosedForBalance(Boolean.TRUE);
	    return closedMonth;
	} else {
	    return new ClosedMonth(beginDate);
	}
    }

    private AssiduousnessClosedMonth getMonthAssiduousnessBalance(AssiduousnessStatusHistory assiduousnessStatusHistory,
	    List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth, YearMonthDay beginDate, YearMonthDay endDate) {
	YearMonthDay lowerBeginDate = beginDate.minusDays(8);
	HashMap<YearMonthDay, WorkSchedule> workScheduleMap = assiduousnessStatusHistory.getAssiduousness()
		.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<YearMonthDay, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap,
		init, end);
	HashMap<YearMonthDay, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);
	Duration totalBalance = Duration.ZERO;
	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration holidayRest = Duration.ZERO;
	Duration totalBalanceToDiscount = Duration.ZERO;
	Duration totalWorkedTime = Duration.ZERO;
	HashMap<WorkScheduleType, Duration> extra125Map = new HashMap<WorkScheduleType, Duration>();
	HashMap<WorkScheduleType, Duration> extra150Map = new HashMap<WorkScheduleType, Duration>();
	HashMap<WorkScheduleType, Duration> extra25Map = new HashMap<WorkScheduleType, Duration>();
	HashMap<WorkScheduleType, Duration> unjustifiedMap = new HashMap<WorkScheduleType, Duration>();
	HashMap<JustificationMotive, Duration> justificationsDuration = new HashMap<JustificationMotive, Duration>();
	double vacations = 0;
	double tolerance = 0;
	double article17 = 0;
	double article66 = 0;
	int maximumWorkingDays = 0;
	Integer workedDaysWithBonusDaysDiscount = 0;
	Integer workedDaysWithA17VacationsDaysDiscount = 0;
	for (YearMonthDay thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    int thisBonusDiscount = 0, thisA17Discount = 0;
	    WorkDaySheet workDaySheet = new WorkDaySheet();
	    workDaySheet.setDate(thisDay);
	    final Schedule schedule = assiduousnessStatusHistory.getAssiduousness().getSchedule(thisDay);
	    if (schedule != null && assiduousnessStatusHistory.getAssiduousness().isStatusActive(thisDay, thisDay)) {
		final boolean isDayHoliday = assiduousnessStatusHistory.getAssiduousness().isHoliday(thisDay);
		final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
		workDaySheet.setWorkSchedule(workSchedule);
		workDaySheet.setAssiduousnessRecords(getDayClockings(clockingsMap, thisDay));
		List<Leave> leavesList = getDayLeaves(leavesMap, thisDay);
		workDaySheet.setLeaves(leavesList);
		workDaySheet = assiduousnessStatusHistory.getAssiduousness().calculateDailyBalance(workDaySheet, isDayHoliday,
			true);
		Duration thisDayWorkedTime = Duration.ZERO;
		Duration timeLeaveToDiscount = Duration.ZERO;
		if (workSchedule != null && !isDayHoliday) {
		    maximumWorkingDays += 1;
		    for (Leave leave : leavesList) {
			if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
			    Duration justificationDuration = justificationsDuration.get(leave.getJustificationMotive());
			    if (justificationDuration == null) {
				justificationDuration = Duration.ZERO;
			    }
			    justificationDuration = justificationDuration.plus(workDaySheet.getLeaveDuration(thisDay,
				    workSchedule, leave));
			    justificationsDuration.put(leave.getJustificationMotive(), justificationDuration);
			} else if ((leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE) || leave
				.getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE))
				&& leave.getJustificationMotive().getActualWorkTime()) {
			    Interval nightInterval = new Interval(workDaySheet.getDate().toDateTime(
				    Assiduousness.defaultStartNightWorkDay), workDaySheet.getDate().toDateTime(
				    Assiduousness.defaultEndNightWorkDay).plusDays(1));
			    setNightExtraWorkMap(workDaySheet, extra25Map, workSchedule.getWorkScheduleType()
				    .getNormalWorkPeriod().getNormalNigthWorkPeriod(nightInterval));
			}
			if (leave.getJustificationMotive().getAcronym().equals("1/2 FÉR")
				|| leave.getJustificationMotive().getAcronym().equals("FHE")
				|| leave.getJustificationMotive().getAcronym().equals("FHEA")
				|| leave.getJustificationMotive().getAcronym().equals("FTRANS")
				|| leave.getJustificationMotive().getAcronym().equals("FA17")
				|| leave.getJustificationMotive().getAcronym().equals("FEB")
				|| leave.getJustificationMotive().getAcronym().equals("FER")
				|| leave.getJustificationMotive().getAcronym().equals("A42")
				|| leave.getJustificationMotive().getAcronym().equals("FA42")
				|| leave.getJustificationMotive().getAcronym().equals("FA42T")) {
			    if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
				vacations = vacations + 0.5;
			    } else {
				vacations = vacations + 1;
			    }
			} else if (leave.getJustificationMotive().getJustificationGroup() != null
				&& leave.getJustificationMotive().getJustificationGroup().equals(JustificationGroup.TOLERANCES)) {
			    tolerance = tolerance + 1;
			} else if (leave.getJustificationMotive().getAcronym().equals("A17")) {
			    article17 = article17 + 1;
			} else if (leave.getJustificationMotive().getAcronym().equals("A66")) {
			    article66 = article66 + 1;
			} else if (leave.getJustificationMotive().getAcronym().equals("1/2A66")) {
			    article66 = article66 + 0.5;
			}

			if (leave.getJustificationMotive().getJustificationType()
				.equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
			    totalBalanceToDiscount = totalBalanceToDiscount.plus(workDaySheet.getWorkSchedule()
				    .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
			} else if (leave.getJustificationMotive().getJustificationType().equals(
				JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {
			    totalBalanceToDiscount = totalBalanceToDiscount.plus(workDaySheet.getWorkSchedule()
				    .getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration().getMillis() / 2);
			}

			if (!leave.getJustificationMotive().getDiscountBonus()
				|| !leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
			    thisBonusDiscount = 1;
			}
			if (!leave.getJustificationMotive().getDiscountA17Vacations()
				|| !leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
			    thisA17Discount = 1;
			}
			if (!leave.getJustificationMotive().getDiscountA17Vacations()) {
			    if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
				thisDayWorkedTime = workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
					.getWorkPeriodDuration();
			    }
			} else {
			    if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)
				    && leave.getJustificationMotive().getJustificationType() != null) {
				timeLeaveToDiscount = timeLeaveToDiscount.plus(leave.getDuration());
			    }
			}
		    }
		    Duration thisDayBalance = workDaySheet.getBalanceTime().toDurationFrom(new DateMidnight());

		    if (workSchedule.getWorkScheduleType().getScheduleClockingType().equals(
			    ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
			thisDayWorkedTime = workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
				.getWorkPeriodDuration();
			if (leavesList.isEmpty()) {
			    thisBonusDiscount = 1;
			    thisA17Discount = 1;
			}
		    } else if (!workDaySheet.getAssiduousnessRecords().isEmpty()) {
			thisDayWorkedTime = workDaySheet.getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod()
				.getWorkPeriodDuration().plus(thisDayBalance);
			if (leavesList.isEmpty()) {
			    thisBonusDiscount = 1;
			    thisA17Discount = 1;
			}
		    }

		    if (!workSchedule.getWorkScheduleType().getScheduleClockingType().equals(
			    ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
			totalBalance = totalBalance.plus(thisDayBalance);
		    }
		    setNightExtraWork(workDaySheet, extra25Map);
		    setExtraWork(workDaySheet, thisDayBalance, extra125Map, extra150Map);
		    setUnjustified(workDaySheet, unjustifiedMap);
		    if (!timeLeaveToDiscount.equals(Duration.ZERO)) {
			thisDayWorkedTime = thisDayWorkedTime.minus(timeLeaveToDiscount);
		    }
		    totalWorkedTime = totalWorkedTime.plus(thisDayWorkedTime);
		} else {
		    totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance.plus(workDaySheet
			    .getComplementaryWeeklyRest());
		    totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet.getWeeklyRest());
		    holidayRest = holidayRest.plus(workDaySheet.getHolidayRest());
		}
	    }
	    workedDaysWithBonusDaysDiscount += thisBonusDiscount;
	    workedDaysWithA17VacationsDaysDiscount += thisA17Discount;
	}

	EmployeeBalanceResume employeeBalanceResume = new EmployeeBalanceResume(totalBalance, totalBalanceToDiscount, closedMonth
		.getClosedYearMonth(), assiduousnessStatusHistory);
	AssiduousnessClosedMonth assiduousnessClosedMonth = new AssiduousnessClosedMonth(assiduousnessStatusHistory, closedMonth,
		totalBalance, totalComplementaryWeeklyRestBalance, totalWeeklyRestBalance, holidayRest, totalBalanceToDiscount,
		vacations, tolerance, article17, article66, maximumWorkingDays, workedDaysWithBonusDaysDiscount,
		workedDaysWithA17VacationsDaysDiscount, employeeBalanceResume.getFinalAnualBalance(), employeeBalanceResume
			.getFutureBalanceToCompensate(), beginDate, endDate, totalWorkedTime);

	for (JustificationMotive justificationMotive : justificationsDuration.keySet()) {
	    Duration duration = justificationsDuration.get(justificationMotive);
	    new ClosedMonthJustification(assiduousnessClosedMonth, justificationMotive, duration);
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
	    new AssiduousnessExtraWork(assiduousnessClosedMonth, workScheduleType, totalExtra25, totalExtra125, totalExtra150,
		    totalUnjustified);
	}

	assiduousnessClosedMonth.setAllUnjustifiedAndAccumulatedArticle66();

	int unjustifiedDays = 0;
	int article66Days = 0;
	double unjustified = assiduousnessClosedMonth.getAccumulatedUnjustified();
	double accumulatedArticle66 = assiduousnessClosedMonth.getAccumulatedArticle66();
	AssiduousnessClosedMonth previousAssiduousnessClosedMonth = assiduousnessClosedMonth
		.getPreviousAssiduousnessClosedMonth();
	if (previousAssiduousnessClosedMonth != null) {
	    unjustifiedDays = (int) Math.floor(unjustified
		    - Math.floor(previousAssiduousnessClosedMonth.getAccumulatedUnjustified()));
	    article66Days = (int) Math.floor(accumulatedArticle66
		    - Math.floor(previousAssiduousnessClosedMonth.getAccumulatedArticle66()));
	} else {
	    unjustifiedDays = (int) Math.floor(unjustified);
	    article66Days = (int) Math.floor(accumulatedArticle66);
	}
	assiduousnessClosedMonth
		.setWorkedDaysWithBonusDaysDiscount(workedDaysWithBonusDaysDiscount - unjustifiedDays >= 0 ? workedDaysWithBonusDaysDiscount
			- unjustifiedDays
			: 0);
	assiduousnessClosedMonth.setWorkedDaysWithA17VacationsDaysDiscount(workedDaysWithA17VacationsDaysDiscount
		- unjustifiedDays >= 0 ? workedDaysWithA17VacationsDaysDiscount - unjustifiedDays : 0);

	if (unjustifiedDays > 0 || article66Days > 0 || assiduousnessClosedMonth.getArticle66().doubleValue() > 0) {
	    return assiduousnessClosedMonth;
	}
	return null;
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
	    HashMap<WorkScheduleType, Duration> extra125Map, HashMap<WorkScheduleType, Duration> extra150Map) {
	Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
	if (thisDayUnjustified == null) {
	    thisDayUnjustified = Duration.ZERO;
	}
	Duration hourDuration = new Duration(3600000);
	// TODO Duration thisDayExtraWork = thisDayBalance; ignore unjustified
	Duration thisDayExtraWork = thisDayBalance.plus(thisDayUnjustified);
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
		extra150 = extra150.plus(thisDayExtraWork.minus(hourDuration));
		extra150Map.put(workDaySheet.getWorkSchedule().getWorkScheduleType(), extra150);
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

    private void setNightExtraWork(WorkDaySheet workDaySheet, HashMap<WorkScheduleType, Duration> extra25Map) {
	if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null) {
	    final Duration midHour = new Duration(1800000);
	    Duration extraWorkDuration = workDaySheet.getTimeline().calculateWorkPeriodDurationBetweenDates(
		    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
		    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1));
	    if (!extraWorkDuration.equals(Duration.ZERO)) {
		if (!extraWorkDuration.isShorterThan(midHour)) {
		    setNightExtraWorkMap(workDaySheet, extra25Map, extraWorkDuration);
		}
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

    public HashMap<Assiduousness, List<AssiduousnessRecord>> getAssiduousnessRecord(YearMonthDay beginDate, YearMonthDay endDate) {
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