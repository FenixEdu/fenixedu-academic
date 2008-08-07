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
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class CloseAssiduousnessMonth extends Service {

    protected final Duration DAY_HOUR_LIMIT = Hours.TWO.toStandardDuration();
    protected final Duration hourDuration = new Duration(3600000);
    protected final Duration midHourDuration = new Duration(1800000);

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
	ClosedMonth closedMonth = ClosedMonth.getOpenClosedMonth(new YearMonth(beginDate));
	if (closedMonth != null) {
	    closedMonth.setClosedForBalance(Boolean.TRUE);
	    return closedMonth;
	} else {
	    return new ClosedMonth(beginDate);
	}
    }

    private AssiduousnessClosedMonth getMonthAssiduousnessBalance(AssiduousnessStatusHistory assiduousnessStatusHistory,
	    List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousnessStatusHistory.getAssiduousness()
		.getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap, init,
		end);
	HashMap<LocalDate, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);
	Duration totalBalance = Duration.ZERO;
	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration holidayRest = Duration.ZERO;
	Duration totalBalanceToDiscount = Duration.ZERO;
	Duration totalWorkedTime = Duration.ZERO;
	HashMap<WorkScheduleType, Duration> extra125Map = new HashMap<WorkScheduleType, Duration>();
	HashMap<WorkScheduleType, Duration> extra150Map = new HashMap<WorkScheduleType, Duration>();
	HashMap<WorkScheduleType, Duration> extra150WithLimitsMap = new HashMap<WorkScheduleType, Duration>();
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
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
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
				    Assiduousness.defaultStartNightWorkDay.toLocalTime()), workDaySheet.getDate().toDateTime(
				    Assiduousness.defaultEndNightWorkDay.toLocalTime()).plusDays(1));
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
		    setExtraWork(workDaySheet, thisDayBalance, extra125Map, extra150Map, extra150WithLimitsMap);
		    setUnjustified(workDaySheet, unjustifiedMap);
		    if (!timeLeaveToDiscount.equals(Duration.ZERO)) {
			thisDayWorkedTime = thisDayWorkedTime.minus(timeLeaveToDiscount);
		    }
		    totalWorkedTime = totalWorkedTime.plus(thisDayWorkedTime);
		} else {
		    totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance.plus(new Duration(Math.min(
			    roundToHalfHour(workDaySheet.getComplementaryWeeklyRest()).getMillis(),
			    Assiduousness.normalWorkDayDuration.getMillis())));
		    totalWeeklyRestBalance = totalWeeklyRestBalance.plus(new Duration(Math.min(roundToHalfHour(
			    workDaySheet.getWeeklyRest()).getMillis(), Assiduousness.normalWorkDayDuration.getMillis())));
		    holidayRest = holidayRest.plus(new Duration(Math.min(roundToHalfHour(workDaySheet.getHolidayRest())
			    .getMillis(), Assiduousness.normalWorkDayDuration.getMillis())));
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
	workScheduleTypeSet.addAll(extra150WithLimitsMap.keySet());
	workScheduleTypeSet.addAll(unjustifiedMap.keySet());

	for (WorkScheduleType workScheduleType : workScheduleTypeSet) {
	    Duration totalExtra25 = extra25Map.get(workScheduleType);
	    Duration totalExtra125 = extra125Map.get(workScheduleType);
	    Duration totalExtra150 = extra150Map.get(workScheduleType);
	    Duration totalExtra150WithLimit = extra150WithLimitsMap.get(workScheduleType);
	    Duration totalUnjustified = unjustifiedMap.get(workScheduleType);
	    new AssiduousnessExtraWork(assiduousnessClosedMonth, workScheduleType, totalExtra25, totalExtra125, totalExtra150,
		    totalExtra150WithLimit, totalUnjustified);
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

    private void setNightExtraWork(WorkDaySheet workDaySheet, HashMap<WorkScheduleType, Duration> extra25Map) {
	if (!workDaySheet.getIrregular() && workDaySheet.getTimeline() != null) {

	    Duration extraWorkDuration = roundToHalfHour(new Duration(workDaySheet.getTimeline()
		    .calculateWorkPeriodDurationBetweenDates(
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay.toLocalTime()),
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay.toLocalTime()).plusDays(1))));
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
	DateTime end = endDate.toDateTime(Assiduousness.defaultEndWorkDay.toLocalTime());
	WorkSchedule endWorkSchedule = workScheduleMap.get(endDate);
	if (endWorkSchedule != null) {
	    end = endDate.toDateTime(endWorkSchedule.getWorkScheduleType().getWorkTime().toLocalTime()).plus(
		    endWorkSchedule.getWorkScheduleType().getWorkTimeDuration());
	    if (endWorkSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
		end = end.plusDays(2);
	    }
	}
	return end;
    }

    private DateTime getInit(LocalDate lowerBeginDate, HashMap<LocalDate, WorkSchedule> workScheduleMap) {
	DateTime init = lowerBeginDate.toDateTime(Assiduousness.defaultStartWorkDay.toLocalTime());
	WorkSchedule beginWorkSchedule = workScheduleMap.get(lowerBeginDate);
	if (beginWorkSchedule != null) {
	    init = lowerBeginDate.toDateTime(beginWorkSchedule.getWorkScheduleType().getWorkTime().toLocalTime());
	}
	return init;
    }

}