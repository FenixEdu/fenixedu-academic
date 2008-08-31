package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessExtraWork;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ExportClosedMonth extends Service {

    private final static String durationZeroString = "00:00";

    public String run(ClosedMonth closedMonth, LocalDate beginDate, LocalDate endDate) {
	HashMap<Assiduousness, List<AssiduousnessRecord>> assiduousnessRecords = getAssiduousnessRecord(beginDate, endDate);
	StringBuilder result = new StringBuilder();
	// for (Assiduousness assiduousness :
	// rootDomainObject.getAssiduousnesss()) {
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE) {
		result.append(getMonthAssiduousnessBalance(assiduousnessClosedMonth, assiduousnessRecords
			.get(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()), closedMonth));
	    }
	}
	return result.toString();
    }

    private String getMonthAssiduousnessBalance(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    List<AssiduousnessRecord> assiduousnessRecords, ClosedMonth closedMonth) {
	LocalDate beginDate = assiduousnessClosedMonth.getBeginDate();
	LocalDate endDate = assiduousnessClosedMonth.getEndDate();
	StringBuilder result = new StringBuilder();
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousnessClosedMonth.getAssiduousnessStatusHistory()
		.getAssiduousness().getWorkSchedulesBetweenDates(lowerBeginDate, endDate);
	HashMap<LocalDate, List<Leave>> leavesMap = getLeavesMap(assiduousnessRecords, beginDate, endDate);
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = getClockingsMap(assiduousnessRecords, workScheduleMap, init,
		end);
	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    final Schedule schedule = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getSchedule(
		    thisDay);
	    if (schedule != null
		    && assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().isStatusActive(thisDay,
			    thisDay)) {
		final boolean isDayHoliday = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			.isHoliday(thisDay);
		final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
		HashMap<JustificationMotive, List<Leave>> leavesList = getDayLeaves(leavesMap, thisDay);
		String acronym = workSchedule == null ? "" : StringUtils.upperCase(workSchedule.getWorkScheduleType()
			.getAcronym());
		WorkDaySheet workDaySheet = null;
		if (workSchedule != null && !isDayHoliday) {
		    for (JustificationMotive justificationMotive : leavesList.keySet()) {
			List<Leave> list = leavesList.get(justificationMotive);
			Duration leaveDuration = Duration.ZERO;
			workDaySheet = getWorkDaySheet(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
				.getAssiduousness(), clockingsMap.get(thisDay), thisDay, isDayHoliday, workSchedule,
				workDaySheet, getListFromCollection(leavesList.values()));
			for (final Leave leave : list) {
			    if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE
				    || leave.getJustificationMotive().getJustificationType() == JustificationType.MULTIPLE_MONTH_BALANCE) {
				if (!workDaySheet.getIrregular()) {
				    leaveDuration = schedule.getEqualWorkPeriodDuration();
				}
			    } else if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.BALANCE)) {
				if (workSchedule.getWorkScheduleType().getFixedWorkPeriod() == null) {
				    leaveDuration = Duration.ZERO;
				} else if (leave.getDuration().isLongerThan(
					workDaySheet.getUnjustifiedTimeWithoutBalanceDiscount())) {
				    leaveDuration = workDaySheet.getUnjustifiedTimeWithoutBalanceDiscount();
				} else {
				    leaveDuration = leaveDuration.plus(leave.getDuration());
				}
			    } else if (leave.getJustificationMotive().getJustificationType().equals(
				    JustificationType.HALF_OCCURRENCE)) {
				leaveDuration = new Duration(schedule.getEqualWorkPeriodDuration().getMillis() / 2);
			    } else {
				leaveDuration = leaveDuration.plus(workDaySheet.getLeaveDuration(thisDay, workSchedule, leave));
			    }
			}
			result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
				.getEmployee().getEmployeeNumber(), justificationMotive.getAcronym(), thisDay, thisDay,
				leaveDuration, workSchedule.getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration(),
				acronym));
		    }
		} else {
		    for (JustificationMotive justificationMotive : leavesList.keySet()) {
			List<Leave> list = leavesList.get(justificationMotive);
			Duration leaveDuration = Duration.ZERO;
			boolean showDuration = false;
			for (final Leave leave : list) {
			    if ((leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE || leave
				    .getJustificationMotive().getJustificationType() == JustificationType.MULTIPLE_MONTH_BALANCE)
				    && leave.getJustificationMotive().getDayType() != DayType.WORKDAY
				    && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.CURRENT_YEAR_HOLIDAYS
				    && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.LAST_YEAR_HOLIDAYS
				    && leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.NEXT_YEAR_HOLIDAYS) {
				leaveDuration = schedule.getEqualWorkPeriodDuration();
				showDuration = true;
			    }
			}
			if (showDuration) {
			    String dayDescription = bundle.getString("label.holiday");
			    final WeekDay dayOfWeek = WeekDay.fromJodaTimeToWeekDay(thisDay.toDateTimeAtMidnight());
			    if (dayOfWeek.equals(WeekDay.SATURDAY)) {
				dayDescription = bundle.getString("label.weeklyRest");
			    } else if (dayOfWeek.equals(WeekDay.SUNDAY)) {
				dayDescription = bundle.getString("label.complementaryWeeklyRest");
			    }
			    result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
				    .getEmployee().getEmployeeNumber(), justificationMotive.getAcronym(), thisDay, thisDay,
				    getDurationString(leaveDuration), durationZeroString, StringUtils.upperCase(dayDescription)));
			}
		    }
		}
	    }
	}

	List<AssiduousnessExtraWork> assiduousnessExtraWorkList = new ArrayList<AssiduousnessExtraWork>(assiduousnessClosedMonth
		.getAssiduousnessExtraWorks());

	Collections.sort(assiduousnessExtraWorkList, AssiduousnessExtraWork.COMPARATOR_BY_WORK_SCHEDULE_TYPE_ACRONYM);
	StringBuilder totalExtra25Result = new StringBuilder();
	StringBuilder totalExtra125Result = new StringBuilder();
	StringBuilder totalExtra150Result = new StringBuilder();
	StringBuilder totalUnjustifiedResult = new StringBuilder();

	for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorkList) {
	    Duration totalExtra25 = assiduousnessExtraWork.getNightBalance();
	    Duration totalExtra125 = assiduousnessExtraWork.getFirstLevelBalance();
	    Duration totalExtra150 = assiduousnessExtraWork.getSecondLevelBalance();
	    Duration totalUnjustified = assiduousnessExtraWork.getUnjustified();

	    if (totalExtra25 != null && !totalExtra25.equals(Duration.ZERO)) {
		totalExtra25Result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			.getEmployee().getEmployeeNumber(), StringUtils.upperCase(bundle.getString("label.25.percent")),
			beginDate, endDate, getDurationString(totalExtra25), durationZeroString, StringUtils
				.upperCase(assiduousnessExtraWork.getWorkScheduleType().getAcronym())));
	    }
	    if (totalExtra125 != null && !totalExtra125.equals(Duration.ZERO)) {
		totalExtra125Result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			.getEmployee().getEmployeeNumber(), StringUtils.upperCase(bundle.getString("label.125.percent")),
			beginDate, endDate, getDurationString(totalExtra125), durationZeroString, StringUtils
				.upperCase(assiduousnessExtraWork.getWorkScheduleType().getAcronym())));
	    }
	    if (totalExtra150 != null && !totalExtra150.equals(Duration.ZERO)) {
		totalExtra150Result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			.getEmployee().getEmployeeNumber(), StringUtils.upperCase(bundle.getString("label.150.percent")),
			beginDate, endDate, getDurationString(totalExtra150), durationZeroString, StringUtils
				.upperCase(assiduousnessExtraWork.getWorkScheduleType().getAcronym())));
	    }
	    if (totalUnjustified != null && !totalUnjustified.equals(Duration.ZERO)) {
		totalUnjustifiedResult.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			.getEmployee().getEmployeeNumber(), StringUtils.upperCase(bundle.getString("label.unjustifieds")),
			beginDate, endDate, getDurationString(totalUnjustified), durationZeroString, StringUtils
				.upperCase(assiduousnessExtraWork.getWorkScheduleType().getAcronym())));
	    }
	}
	result.append(totalExtra25Result);
	result.append(totalExtra125Result);
	result.append(totalExtra150Result);
	result.append(totalUnjustifiedResult);
	if (assiduousnessClosedMonth.getHolidayBalance() != null
		&& !assiduousnessClosedMonth.getHolidayBalance().equals(Duration.ZERO)) {
	    result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getEmployeeNumber(), bundle.getString("label.200.percent"), beginDate, endDate,
		    getDurationString(assiduousnessClosedMonth.getHolidayBalance()), durationZeroString, StringUtils
			    .upperCase(bundle.getString("label.holiday"))));
	}
	if (assiduousnessClosedMonth.getSaturdayBalance() != null
		&& !assiduousnessClosedMonth.getSaturdayBalance().equals(Duration.ZERO)) {
	    result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getEmployeeNumber(), bundle.getString("label.200.percent"), beginDate, endDate,
		    getDurationString(assiduousnessClosedMonth.getSaturdayBalance()), durationZeroString, bundle
			    .getString("label.weeklyRest")));
	}
	if (assiduousnessClosedMonth.getSundayBalance() != null
		&& !assiduousnessClosedMonth.getSundayBalance().equals(Duration.ZERO)) {
	    result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		    .getEmployeeNumber(), bundle.getString("label.200.percent"), beginDate, endDate,
		    getDurationString(assiduousnessClosedMonth.getSundayBalance()), durationZeroString, bundle
			    .getString("label.complementaryWeeklyRest")));
	}
	result.append(getLine(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().getEmployee()
		.getEmployeeNumber(), StringUtils.upperCase(bundle.getString("label.balance")), beginDate, endDate,
		getDurationString(assiduousnessClosedMonth.getBalance()), durationZeroString, null));
	return result.toString();
    }

    private List<Leave> getListFromCollection(Collection<List<Leave>> list) {
	List<Leave> result = new ArrayList<Leave>();
	for (List<Leave> leaveList : list) {
	    result.addAll(leaveList);
	}
	return result;
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

    private WorkDaySheet getWorkDaySheet(Assiduousness assiduousness, List<AssiduousnessRecord> clockings, LocalDate thisDay,
	    final boolean isDayHoliday, final WorkSchedule workSchedule, WorkDaySheet workDaySheet, List<Leave> list) {
	if (workDaySheet == null) {
	    if (clockings == null) {
		clockings = new ArrayList<AssiduousnessRecord>();
	    }
	    workDaySheet = new WorkDaySheet(thisDay, workSchedule, clockings, list);
	    workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday, true);
	}
	return workDaySheet;
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

    private HashMap<JustificationMotive, List<Leave>> getDayLeaves(HashMap<LocalDate, List<Leave>> leavesMap, LocalDate thisDay) {
	HashMap<JustificationMotive, List<Leave>> leavesMapList = new HashMap<JustificationMotive, List<Leave>>();

	if (leavesMap != null && leavesMap.get(thisDay) != null) {
	    for (Leave leave : leavesMap.get(thisDay)) {
		List<Leave> leavesList = leavesMapList.get(leave.getJustificationMotive());
		if (leavesList == null) {
		    leavesList = new ArrayList<Leave>();
		}
		leavesList.add(leave);
		Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
		leavesMapList.put(leave.getJustificationMotive(), leavesList);
	    }
	}
	return leavesMapList;
    }

    public HashMap<Assiduousness, List<AssiduousnessRecord>> getAssiduousnessRecord(LocalDate beginDate, LocalDate endDate) {
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

    private StringBuilder getLine(Integer employeeNumber, String acronym, LocalDate beginDate, LocalDate endDate,
	    String duration1, String duration2, String scheduleAcronym) {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getTokens(employeeNumber.toString(), 8)).append(";");
	stringBuilder.append(getTokens(acronym, 8)).append(";");
	stringBuilder.append(getTokens(beginDate.toString(), 10)).append(";");
	stringBuilder.append(getTokens(endDate.toString(), 10)).append(";");
	stringBuilder.append(getTokens(duration1, 6)).append(";");
	stringBuilder.append(getTokens(duration2, 6)).append(";");
	if (!StringUtils.isEmpty(scheduleAcronym)) {
	    stringBuilder.append(getTokens(scheduleAcronym, 8));
	}
	stringBuilder.append("\r\n");
	return stringBuilder;
    }

    private String getTokens(String value, int size) {
	if (value.length() > size) {
	    return value.substring(0, size);
	}
	return StringUtils.rightPad(value, size);
    }

    private StringBuilder getLine(Integer employeeNumber, String acronym, LocalDate beginDate, LocalDate endDate,
	    Duration duration1, Duration duration2, String scheduleAcronym) {
	return getLine(employeeNumber, acronym, beginDate, endDate, getDurationString(duration1), getDurationString(duration2),
		scheduleAcronym);

    }

    public String getDurationString(Duration duration) {
	if (duration == null)
	    return durationZeroString;
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendHours()
		.appendSeparator(":").minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod finalTotalBalance = new MutablePeriod(duration.getMillis(), PeriodType.time());
	if (duration.toPeriod().getMinutes() < 0) {
	    finalTotalBalance.setMinutes(-duration.toPeriod().getMinutes());
	    if (duration.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().minimumPrintedDigits(2).appendLiteral("-")
			.minimumPrintedDigits(2).appendHours().appendSeparator(":").appendMinutes().toFormatter();
	    }
	}
	return fmt.print(finalTotalBalance);
    }
}