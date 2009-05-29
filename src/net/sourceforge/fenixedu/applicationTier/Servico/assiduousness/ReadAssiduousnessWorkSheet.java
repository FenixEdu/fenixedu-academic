package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ReadAssiduousnessWorkSheet extends FenixService {

    @Service
    public static EmployeeWorkSheet run(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate) {
	return run(assiduousness, beginDate, endDate, false);
    }

    @Service
    public static EmployeeWorkSheet run(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate, Boolean extraWork) {
	if (assiduousness == null) {
	    return null;
	}

	ClosedMonth closedMonth = ClosedMonth.getClosedMonth(beginDate);
	if (closedMonth != null && closedMonth.getClosedForBalance()) {
	    return closedMonth.getEmployeeWorkSheet(assiduousness, beginDate, endDate);
	} else {
	    LocalDate lastActiveStatus = assiduousness.getLastActiveStatusBetween(beginDate, endDate);
	    if (lastActiveStatus == null) {
		return new EmployeeWorkSheet(assiduousness.getEmployee(), beginDate, endDate);
	    }
	    endDate = lastActiveStatus;
	    LocalDate lowerBeginDate = beginDate.minusDays(8);
	    HashMap<LocalDate, WorkSchedule> workScheduleMap = assiduousness.getWorkSchedulesBetweenDates(lowerBeginDate, endDate
		    .plusDays(2));
	    DateTime init = getInit(lowerBeginDate, workScheduleMap);
	    DateTime end = getEnd(endDate, workScheduleMap);
	    HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = assiduousness.getClockingsMap(workScheduleMap, init, end
		    .plusDays(1));
	    HashMap<LocalDate, List<Leave>> leavesMap = assiduousness.getLeavesMap(beginDate, endDate);

	    return getEmployeeWorkSheet(assiduousness, workScheduleMap, clockingsMap, leavesMap, beginDate, endDate,
		    new LocalDate(), extraWork);
	}
    }

    @Service
    public static EmployeeWorkSheet run(Employee employee, LocalDate beginDate, LocalDate endDate) {
	if (employee.getAssiduousness() == null) {
	    return null;
	}

	LocalDate lastActiveStatus = employee.getAssiduousness().getLastActiveStatusBetween(beginDate, endDate);
	if (lastActiveStatus == null) {
	    return new EmployeeWorkSheet(employee, beginDate, endDate);
	}
	endDate = lastActiveStatus;
	LocalDate lowerBeginDate = beginDate.minusDays(8);
	HashMap<LocalDate, WorkSchedule> workScheduleMap = employee.getAssiduousness().getWorkSchedulesBetweenDates(
		lowerBeginDate, endDate.plusDays(2));
	DateTime init = getInit(lowerBeginDate, workScheduleMap);
	DateTime end = getEnd(endDate, workScheduleMap);
	HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap = employee.getAssiduousness().getClockingsMap(workScheduleMap,
		init, end.plusDays(1));
	HashMap<LocalDate, List<Leave>> leavesMap = employee.getAssiduousness().getLeavesMap(beginDate, endDate);

	return getEmployeeWorkSheet(employee.getAssiduousness(), workScheduleMap, clockingsMap, leavesMap, beginDate, endDate,
		new LocalDate(), true);

    }

    private static EmployeeWorkSheet getEmployeeWorkSheet(Assiduousness assiduousness,
	    HashMap<LocalDate, WorkSchedule> workScheduleMap, HashMap<LocalDate, List<AssiduousnessRecord>> clockingsMap,
	    HashMap<LocalDate, List<Leave>> leavesMap, LocalDate beginDate, LocalDate endDate, LocalDate today, Boolean extraWork) {
	final List<WorkDaySheet> workSheet = new ArrayList<WorkDaySheet>();
	Duration totalBalance = Duration.ZERO;
	Duration totalUnjustified = Duration.ZERO;

	Duration totalComplementaryWeeklyRestBalance = Duration.ZERO;
	Duration totalWeeklyRestBalance = Duration.ZERO;
	Duration totalHolidayBalance = Duration.ZERO;
	Duration totalBalanceToCompensate = Duration.ZERO;

	for (LocalDate thisDay = beginDate; thisDay.isBefore(endDate.plusDays(1)); thisDay = thisDay.plusDays(1)) {
	    WorkDaySheet workDaySheet = new WorkDaySheet();
	    workDaySheet.setDate(thisDay);

	    final Schedule schedule = assiduousness.getSchedule(thisDay);

	    if (schedule == null || !assiduousness.isStatusActive(thisDay, thisDay)) {
		if (workSheet.size() != 0) {
		    workDaySheet.setBalanceTime(new Period(0));
		    workDaySheet.setUnjustifiedTime(Duration.ZERO);
		    workSheet.add(workDaySheet);
		}
	    } else {
		final StringBuilder notes = new StringBuilder();
		final boolean isDayHoliday = assiduousness.isHoliday(thisDay);
		final WorkSchedule workSchedule = workScheduleMap.get(thisDay);
		workDaySheet.setWorkSchedule(workSchedule);
		List<AssiduousnessRecord> clockingsList = clockingsMap.get(thisDay);
		if (clockingsList == null) {
		    clockingsList = new ArrayList<AssiduousnessRecord>();
		}
		Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
		workDaySheet.setAssiduousnessRecords(clockingsList);
		List<Leave> leavesList = leavesMap.get(thisDay);
		if (leavesList == null) {
		    leavesList = new ArrayList<Leave>();
		}
		Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
		workDaySheet.setLeaves(leavesList);
		if (workSchedule != null && !isDayHoliday) {
		    for (final Leave leave : leavesList) {
			if (notes.length() != 0) {
			    notes.append(" / ");
			}
			notes.append(leave.getJustificationMotive().getAcronym());
		    }
		    workDaySheet.setNotes(notes.toString());

		    if (thisDay.isBefore(today)) {
			workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
			Duration balance = workDaySheet.getBalanceTime().toDurationFrom(new DateMidnight());
			totalBalance = totalBalance.plus(balance);
			totalUnjustified = totalUnjustified.plus(workDaySheet.getUnjustifiedTime());
			setExtraWork(workDaySheet, balance, extraWork);
		    }
		    workDaySheet.setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
		    workSheet.add(workDaySheet);
		} else {
		    if (!thisDay.equals(today)) {
			workDaySheet = assiduousness.calculateDailyBalance(workDaySheet, isDayHoliday);
			totalComplementaryWeeklyRestBalance = totalComplementaryWeeklyRestBalance.plus(workDaySheet
				.getComplementaryWeeklyRest());
			totalWeeklyRestBalance = totalWeeklyRestBalance.plus(workDaySheet.getWeeklyRest());
			totalHolidayBalance = totalHolidayBalance.plus(workDaySheet.getHolidayRest());
		    }
		    for (final Leave leave : leavesList) {
			if (leave.getJustificationMotive().getJustificationType() == JustificationType.OCCURRENCE
				&& leave.getJustificationMotive().getDayType() != DayType.WORKDAY
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.CURRENT_YEAR_HOLIDAYS
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.LAST_YEAR_HOLIDAYS
				&& leave.getJustificationMotive().getJustificationGroup() != JustificationGroup.NEXT_YEAR_HOLIDAYS) {
			    if (notes.length() != 0) {
				notes.append(" / ");
			    }
			    notes.append(leave.getJustificationMotive().getAcronym());
			}
		    }
		    workDaySheet.setNotes(notes.toString());
		    if (isDayHoliday) {
			ResourceBundle bundle = ResourceBundle
				.getBundle("resources.AssiduousnessResources", Language.getLocale());
			workDaySheet.setWorkScheduleAcronym(bundle.getString("label.holiday"));
		    }
		    workDaySheet.setUnjustifiedTime(Duration.ZERO);
		    workSheet.add(workDaySheet);
		}
		totalBalanceToCompensate = totalBalanceToCompensate.plus(workDaySheet.getBalanceToCompensate());
	    }
	}
	EmployeeWorkSheet employeeWorkSheet = new EmployeeWorkSheet(assiduousness.getEmployee(), beginDate, endDate);
	employeeWorkSheet.setWorkDaySheetList(workSheet);

	employeeWorkSheet.setTotalBalance(totalBalance);
	employeeWorkSheet.setUnjustifiedBalance(totalUnjustified);
	employeeWorkSheet.setComplementaryWeeklyRest(totalComplementaryWeeklyRestBalance);
	employeeWorkSheet.setWeeklyRest(totalWeeklyRestBalance);
	employeeWorkSheet.setHolidayRest(totalHolidayBalance);
	employeeWorkSheet.setBalanceToCompensate(totalBalanceToCompensate);
	return employeeWorkSheet;
    }

    private static void setExtraWork(WorkDaySheet workDaySheet, Duration balance, Boolean extraWork) {
	if (balance.isLongerThan(Duration.ZERO) && extraWork && !workDaySheet.getIrregular()
		&& workDaySheet.getTimeline() != null) {
	    Duration nightExtraWorkDuration = roundToHalfHour(new Duration(workDaySheet.getTimeline()
		    .calculateWorkPeriodDurationBetweenDates(
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
			    workDaySheet.getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1))));

	    if (workDaySheet.getWorkSchedule().getWorkScheduleType().canDoExtraWorkInWeekDays()) {
		Duration thisDayUnjustified = workDaySheet.getUnjustifiedTime();
		if (thisDayUnjustified == null) {
		    thisDayUnjustified = Duration.ZERO;
		}
		// TODO Duration thisDayExtraWork = thisDayBalance; ignore
		// unjustified
		// TE DIURNO
		Duration thisDayExtraWork = roundToHalfHour(new Duration(balance.minus(thisDayUnjustified)));
		if (!thisDayExtraWork.equals(Duration.ZERO)) {
		    if (thisDayExtraWork.isLongerThan(CloseAssiduousnessMonth.hourDuration)) {
			workDaySheet.setExtraWorkFirstLevel(CloseAssiduousnessMonth.hourDuration);
			workDaySheet.setExtraWorkSecondLevel(thisDayExtraWork.minus(CloseAssiduousnessMonth.hourDuration));
			workDaySheet.setExtraWorkSecondLevelWithLimit(new Duration(Math.min(thisDayExtraWork.minus(
				CloseAssiduousnessMonth.hourDuration).getMillis(), CloseAssiduousnessMonth.DAY_HOUR_LIMIT.minus(
				CloseAssiduousnessMonth.hourDuration).getMillis())));

		    } else if (thisDayExtraWork.isLongerThan(Duration.ZERO)) {
			workDaySheet.setExtraWorkFirstLevel(thisDayExtraWork);
		    }
		}

		// TE NOCTURNO
		if ((!thisDayExtraWork.equals(Duration.ZERO)) && (!nightExtraWorkDuration.equals(Duration.ZERO))) {
		    if (nightExtraWorkDuration.isShorterThan(thisDayExtraWork)) {
			if (nightExtraWorkDuration.isLongerThan(CloseAssiduousnessMonth.hourDuration)) {
			    workDaySheet.setNightExtraWorkFirstLevel(CloseAssiduousnessMonth.hourDuration);
			    workDaySheet.setNightExtraWorkSecondLevel(nightExtraWorkDuration
				    .minus(CloseAssiduousnessMonth.hourDuration));
			    workDaySheet.setNightExtraWorkSecondLevelWithLimit(new Duration(Math.min(nightExtraWorkDuration
				    .minus(CloseAssiduousnessMonth.hourDuration).getMillis(),
				    CloseAssiduousnessMonth.DAY_HOUR_LIMIT.minus(CloseAssiduousnessMonth.hourDuration)
					    .getMillis())));
			} else {
			    workDaySheet.setNightExtraWorkFirstLevel(nightExtraWorkDuration);
			}
		    } else {
			if (thisDayExtraWork.isLongerThan(CloseAssiduousnessMonth.hourDuration)) {
			    workDaySheet.setNightExtraWorkFirstLevel(CloseAssiduousnessMonth.hourDuration);
			    workDaySheet.setNightExtraWorkSecondLevel(thisDayExtraWork
				    .minus(CloseAssiduousnessMonth.hourDuration));
			    workDaySheet.setNightExtraWorkSecondLevelWithLimit(new Duration(Math.min(thisDayExtraWork.minus(
				    CloseAssiduousnessMonth.hourDuration).getMillis(), CloseAssiduousnessMonth.DAY_HOUR_LIMIT
				    .minus(CloseAssiduousnessMonth.hourDuration).getMillis())));
			} else {
			    workDaySheet.setNightExtraWorkFirstLevel(thisDayExtraWork);
			}
		    }
		}
		if (!nightExtraWorkDuration.equals(Duration.ZERO)
			&& workDaySheet.getWorkSchedule().getWorkScheduleType().isNocturnal()) {
		    workDaySheet.setNightWorkBalance(nightExtraWorkDuration);
		}
	    }
	}
    }

    private static Duration roundToHalfHour(Duration duration) {
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() >= 30) {
	    return new Duration(Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours() + 1).toStandardDuration());
	}
	Duration result = Hours.hours(duration.toPeriod(PeriodType.dayTime()).getHours()).toStandardDuration();
	if (duration.toPeriod(PeriodType.dayTime()).getMinutes() != 0) {
	    result = result.plus(CloseAssiduousnessMonth.midHourDuration);
	}
	return result;
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

}