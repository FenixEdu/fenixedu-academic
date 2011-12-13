package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.LeaveBean;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecordMonthIndex;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.domain.space.Campus;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ExportClosedExtraWorkMonth extends FenixService {
    private static class StateWrapper {
	public List<LocalDate> unjustifiedDays;

	public JustificationMotive a66JustificationMotive;

	public JustificationMotive unjustifiedJustificationMotive;

	public JustificationMotive maternityJustificationMotive;

	public List<Assiduousness> maternityJustificationList;
    }

    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyyMMdd");

    private static final DecimalFormat monthFormat = new DecimalFormat("00");

    private static final DecimalFormat employeeNumberFormat = new DecimalFormat("000000");

    private static final String fieldSeparator = ("\t");

    private static String maternityMovementCode = "508";

    private static String nightExtraWorkMovementCode = "130";

    private static String extraWorkVacationDaysMovementCode = "209";

    public static String extraWorkSundayMovementCode = "207";

    public static String extraWorkSaturdayMovementCode = "210";

    public static String extraWorkHolidayMovementCode = "212";

    public static String extraWorkWeekDayFirstLevelMovementCode = "201";// "200";//
    // 201

    public static String extraWorkWeekDaySecondLevelMovementCode = "203";// "201";//
    // 203

    public static String extraNightWorkFirstLevelMovementCode = "202";

    public static String extraNightWorkSecondLevelMovementCode = "204";

    public static String extraNightWorkMealMovementCode = "123";

    private static Set<String> emptyCodes;

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static String run(ClosedMonth closedMonth) {
	return run(closedMonth, true, true);
    }

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static String run(ClosedMonth closedMonth, Boolean getWorkAbsences, Boolean getExtraWorkMovements) {
	emptyCodes = new HashSet<String>();
	emptyCodes.add("0");
	emptyCodes.add("000");

	LocalDate beginDate = new LocalDate()
		.withField(DateTimeFieldType.year(), closedMonth.getClosedYearMonth().get(DateTimeFieldType.year()))
		.withField(DateTimeFieldType.monthOfYear(), closedMonth.getClosedYearMonth().get(DateTimeFieldType.monthOfYear()))
		.withField(DateTimeFieldType.dayOfMonth(), 1);
	LocalDate endDate = beginDate.withField(DateTimeFieldType.dayOfMonth(), beginDate.dayOfMonth().getMaximumValue());
	LocalDate beginProcessingMonthDate = beginDate.plusMonths(1);
	LocalDate endProcessingMonthDate = beginProcessingMonthDate.withField(DateTimeFieldType.dayOfMonth(),
		beginProcessingMonthDate.dayOfMonth().getMaximumValue());

	Set<AssiduousnessRecord> allAssiduousnessRecord = AssiduousnessRecordMonthIndex.getAssiduousnessRecordBetweenDates(
		beginDate.toDateTimeAtStartOfDay(), endProcessingMonthDate.plusDays(1).toDateTimeAtStartOfDay());

	StringBuilder result = new StringBuilder();
	HashMap<Assiduousness, List<LeaveBean>> allLeaves = getLeaves(allAssiduousnessRecord, beginDate, endDate, false);

	HashMap<Assiduousness, List<LeaveBean>> allProcessingMonthLeaves = getLeaves(allAssiduousnessRecord,
		beginProcessingMonthDate, endProcessingMonthDate, true);
	StateWrapper state = new StateWrapper();
	state.a66JustificationMotive = JustificationMotive.getA66JustificationMotive();
	if (state.a66JustificationMotive == null) {
	    throw new InvalidGiafCodeException("errors.invalidA66JustificationMotive");
	}
	state.unjustifiedJustificationMotive = JustificationMotive.getUnjustifiedJustificationMotive();
	if (state.unjustifiedJustificationMotive == null) {
	    throw new InvalidGiafCodeException("errors.invalidUnjustifiedJustificationMotive");
	}
	state.maternityJustificationMotive = getMaternityJustificationMotive();
	state.maternityJustificationList = new ArrayList<Assiduousness>();

	StringBuilder extraWorkResult = new StringBuilder();
	List<Assiduousness> alreadyProcessedExtraWork = new ArrayList<Assiduousness>();

	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    state.unjustifiedDays = new ArrayList<LocalDate>();
	    if (getWorkAbsences) {
		String lineResult = getAssiduousnessMonthBalance(assiduousnessClosedMonth,
			allLeaves.get(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()), state);

		result.append(lineResult);
		List<LeaveBean> leaveBeanList = allProcessingMonthLeaves.get(assiduousnessClosedMonth
			.getAssiduousnessStatusHistory().getAssiduousness());
		if (leaveBeanList != null) {
		    for (LeaveBean leaveBean : leaveBeanList) {
			result.append(getLeaveLine(assiduousnessClosedMonth, leaveBean, beginProcessingMonthDate,
				endProcessingMonthDate, closedMonth.getClosedMonthFirstDay().plusMonths(1), state));
		    }
		}
	    }
	    if (getExtraWorkMovements) {
		if (!alreadyProcessedExtraWork.contains(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			.getAssiduousness())) {
		    extraWorkResult.append(getAssiduousnessExtraWork(assiduousnessClosedMonth));
		    alreadyProcessedExtraWork.add(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness());
		}
	    }
	}
	if (endDate.getDayOfMonth() != 30 && getExtraWorkMovements) {
	    StringBuilder maternityJustificationResult = new StringBuilder();
	    Integer daysNumber = endDate.getDayOfMonth() - 30;
	    for (Assiduousness assiduousness : state.maternityJustificationList) {
		maternityJustificationResult.append(getExtraWorkMovement(assiduousness, beginDate, endDate,
			maternityMovementCode, daysNumber));
	    }
	    extraWorkResult.append(maternityJustificationResult);
	}
	return result.append(extraWorkResult).toString();
    }

    private static String getExtraWorkMovement(Assiduousness assiduousness, LocalDate beginDate, LocalDate endDate,
	    String movementCode, Integer daysNumber) {
	return getExtraWorkMovement(assiduousness, beginDate.plusMonths(1).getYear(), beginDate.plusMonths(1).getMonthOfYear(),
		beginDate, endDate, movementCode, daysNumber);
    }

    private static String getExtraWorkMovement(Assiduousness assiduousness, int year, int month, LocalDate beginDate,
	    LocalDate endDate, String movementCode, Integer daysNumber) {
	StringBuilder result = new StringBuilder();
	result.append(year).append(fieldSeparator);
	result.append(monthFormat.format(month)).append(fieldSeparator);
	result.append(employeeNumberFormat.format(assiduousness.getEmployee().getEmployeeNumber())).append(fieldSeparator);
	result.append("M").append(fieldSeparator).append(movementCode).append(fieldSeparator);
	result.append(dateFormat.print(beginDate)).append(fieldSeparator);
	result.append(dateFormat.print(endDate)).append(fieldSeparator);
	result.append(employeeNumberFormat.format(daysNumber)).append("00").append(fieldSeparator);
	result.append(employeeNumberFormat.format(daysNumber)).append("00\r\n");
	return result.toString();
    }

    private static String getAssiduousnessExtraWork(AssiduousnessClosedMonth assiduousnessClosedMonth) {
	StringBuilder result = new StringBuilder();
	Map<WorkScheduleType, Duration> nightWorkByWorkScheduleType = assiduousnessClosedMonth.getNightWorkByWorkScheduleType();
	for (WorkScheduleType workScheduleType : nightWorkByWorkScheduleType.keySet()) {
	    Duration duration = nightWorkByWorkScheduleType.get(workScheduleType);
	    if (workScheduleType.isNocturnal() && duration.isLongerThan(Duration.ZERO)) {
		long hours = Math.round((double) duration.toPeriod(PeriodType.minutes()).getMinutes() / (double) 60);
		result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness(),
			assiduousnessClosedMonth.getBeginDate(), assiduousnessClosedMonth.getEndDate(),
			nightExtraWorkMovementCode, (int) hours));
	    }
	}

	List<ExtraWorkRequest> extraWorkRequests = assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
		.getExtraWorkRequests(assiduousnessClosedMonth.getBeginDate());

	for (ExtraWorkRequest extraWorkRequest : extraWorkRequests) {
	    if (extraWorkRequest != null) {
		LocalDate begin = new LocalDate(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()),
			extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), 1);
		LocalDate end = new LocalDate(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()),
			extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()), begin.dayOfMonth()
				.getMaximumValue());

		if (extraWorkRequest.getSundayHours() != null && extraWorkRequest.getSundayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkSundayMovementCode, extraWorkRequest.getSundayHours()));
		}
		Integer totalVacationDays = 0;
		if (extraWorkRequest.getNightVacationsDays() != null) {
		    totalVacationDays = totalVacationDays + extraWorkRequest.getNightVacationsDays();
		}
		if (extraWorkRequest.getNormalVacationsDays() != null) {
		    totalVacationDays = totalVacationDays + extraWorkRequest.getNormalVacationsDays();
		}

		if (totalVacationDays != 0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkVacationDaysMovementCode,
			    extraWorkRequest.getNightVacationsDays() + extraWorkRequest.getNormalVacationsDays()));
		}
		if (extraWorkRequest.getSaturdayHours() != null && extraWorkRequest.getSaturdayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkSaturdayMovementCode, extraWorkRequest.getSaturdayHours()));
		}
		if (extraWorkRequest.getHolidayHours() != null && extraWorkRequest.getHolidayHours() != 0.0) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkHolidayMovementCode, extraWorkRequest.getHolidayHours()));
		}

		if (extraWorkRequest.getWorkdayFirstLevelHours() != null && extraWorkRequest.getWorkdayFirstLevelHours() != 0.0
			&& (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkWeekDayFirstLevelMovementCode, extraWorkRequest.getWorkdayFirstLevelHours()));
		}

		if (extraWorkRequest.getWorkdaySecondLevelHours() != null && extraWorkRequest.getWorkdaySecondLevelHours() != 0.0
			&& (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraWorkWeekDaySecondLevelMovementCode, extraWorkRequest.getWorkdaySecondLevelHours()));
		}

		if (extraWorkRequest.getExtraNightHoursFirstLevel() != null
			&& extraWorkRequest.getExtraNightHoursFirstLevel() != 0.0 && (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraNightWorkFirstLevelMovementCode, extraWorkRequest.getExtraNightHoursFirstLevel()));
		}

		if (extraWorkRequest.getExtraNightHoursSecondLevel() != null
			&& extraWorkRequest.getExtraNightHoursSecondLevel() != 0.0 && (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraNightWorkSecondLevelMovementCode, extraWorkRequest.getExtraNightHoursSecondLevel()));
		}

		if (extraWorkRequest.getExtraNightDays() != null && extraWorkRequest.getExtraNightDays() != 0.0
			&& (!extraWorkRequest.getAddToVacations())) {
		    result.append(getExtraWorkMovement(assiduousnessClosedMonth.getAssiduousnessStatusHistory()
			    .getAssiduousness(), assiduousnessClosedMonth.getBeginDate().plusMonths(1).getYear(),
			    assiduousnessClosedMonth.getBeginDate().plusMonths(1).getMonthOfYear(), begin, end,
			    extraNightWorkMealMovementCode, extraWorkRequest.getExtraNightDays()));
		}

	    }
	}
	return result.toString();
    }

    private static String getAssiduousnessMonthBalance(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    List<LeaveBean> leavesBeans, StateWrapper state) {
	StringBuilder result = new StringBuilder();
	if (leavesBeans != null && !leavesBeans.isEmpty()) {
	    Collections.sort(leavesBeans, LeaveBean.COMPARATOR_BY_DATE);
	    for (LeaveBean leaveBean : leavesBeans) {
		if (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType()
				.equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    result.append(getLeaveLine(assiduousnessClosedMonth, leaveBean, state));
		} else if (leaveBean.getLeave().getJustificationMotive().getJustificationType()
			.equals(JustificationType.HALF_OCCURRENCE_TIME)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType()
				.equals(JustificationType.HALF_OCCURRENCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType()
				.equals(JustificationType.HALF_MULTIPLE_MONTH_BALANCE)
			|| (leaveBean.getLeave().getJustificationMotive().getJustificationType().equals(JustificationType.TIME) && !leaveBean
				.getLeave().getJustificationMotive().getAccumulate())) {
		    result.append(getHalfLeaveLine(assiduousnessClosedMonth, leaveBean.getLeave()));
		}
	    }
	}

	HashMap<String, Duration> pastJustificationsDurations = assiduousnessClosedMonth.getPastJustificationsDurations();
	for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth.getClosedMonthJustifications()) {
	    if (closedMonthJustification.getJustificationMotive().getAccumulate()) {
		int justificationDays = closedMonthJustification.getJustificationDays(pastJustificationsDurations);
		if (justificationDays != 0) {
		    result.append(getLeaveLine(assiduousnessClosedMonth, closedMonthJustification.getJustificationMotive(),
			    justificationDays, leavesBeans, state));
		}
	    }
	}

	int A66ToDiscount = assiduousnessClosedMonth.getAccumulatedArticle66Days();
	int unjustifiedToDiscount = assiduousnessClosedMonth.getAccumulatedUnjustifiedDays();

	if (A66ToDiscount != 0) {
	    result.append(getLeaveLine(assiduousnessClosedMonth, state.a66JustificationMotive, A66ToDiscount, leavesBeans, state));
	}

	for (AssiduousnessClosedDay assiduousnessClosedDay : assiduousnessClosedMonth.getAssiduousnessClosedDays()) {
	    if (assiduousnessClosedDay.getUnjustifiedDay()) {
		result.append(getLine(assiduousnessClosedMonth, state.unjustifiedJustificationMotive,
			assiduousnessClosedDay.getDay()));
		state.unjustifiedDays.add(assiduousnessClosedDay.getDay());
	    }
	}

	if (unjustifiedToDiscount != 0) {
	    result.append(getLeaveLine(assiduousnessClosedMonth, state.unjustifiedJustificationMotive, unjustifiedToDiscount,
		    leavesBeans, state));
	}

	return result.toString();
    }

    private static StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, int justificationDays, List<LeaveBean> leavesBeans, StateWrapper state) {
	List<LocalDate> daysToUnjustify = getJustificationDays(assiduousnessClosedMonth, justificationMotive, justificationDays,
		leavesBeans, state);
	StringBuilder line = new StringBuilder();
	for (LocalDate day : daysToUnjustify) {
	    line.append(getLine(assiduousnessClosedMonth, justificationMotive, day));
	}
	return line;
    }

    private static StringBuilder getLine(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, LocalDate day) {
	LocalDate nextMontDate = day.plusMonths(1);
	String code = justificationMotive.getGiafCode(assiduousnessClosedMonth.getAssiduousnessStatusHistory());
	StringBuilder line = new StringBuilder();
	if (!emptyCodes.contains(code)) {
	    line.append(nextMontDate.getYear()).append(fieldSeparator);
	    line.append(monthFormat.format(nextMontDate.getMonthOfYear())).append(fieldSeparator);
	    line.append(
		    employeeNumberFormat.format(assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			    .getEmployee().getEmployeeNumber())).append(fieldSeparator);
	    line.append("F").append(fieldSeparator);
	    line.append(code).append(fieldSeparator);
	    line.append(dateFormat.print(day)).append(fieldSeparator).append(dateFormat.print(day)).append(fieldSeparator)
		    .append("100").append(fieldSeparator).append("100\r\n");
	}
	return line;
    }

    private static List<LocalDate> getJustificationDays(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    JustificationMotive justificationMotive, int daysNumber, List<LeaveBean> leavesBeans, StateWrapper state) {

	List<LocalDate> days = new ArrayList<LocalDate>();
	LocalDate day = assiduousnessClosedMonth.getClosedMonth().getClosedMonthLastDay();
	HashMap<LocalDate, WorkSchedule> workSchedules = assiduousnessClosedMonth.getAssiduousnessStatusHistory()
		.getAssiduousness().getWorkSchedulesBetweenDates(assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth());

	while (daysNumber != 0) {
	    day = getPreviousWorkingDay(workSchedules, justificationMotive, assiduousnessClosedMonth
		    .getAssiduousnessStatusHistory().getAssiduousness(), day, true);
	    if (getDaysWithoutAnyLeave(state.unjustifiedDays, leavesBeans, assiduousnessClosedMonth.getClosedMonth()
		    .getClosedMonthFirstDay(), assiduousnessClosedMonth.getClosedMonth().getClosedMonthLastDay()) == 0
		    || (!state.unjustifiedDays.contains(day) && !existAnyLeaveForThisDay(leavesBeans, day))) {
		days.add(day);
		daysNumber--;
	    }
	    day = day.minusDays(1);
	}
	state.unjustifiedDays.addAll(days);
	return days;
    }

    private static boolean existAnyLeaveForThisDay(List<LeaveBean> leavesBeans, LocalDate day) {
	if (leavesBeans != null) {
	    for (LeaveBean leaveBean : leavesBeans) {
		if (leaveBean.getLeave().getJustificationMotive().getJustificationType()
			.equals(JustificationType.MULTIPLE_MONTH_BALANCE)
			|| leaveBean.getLeave().getJustificationMotive().getJustificationType()
				.equals(JustificationType.OCCURRENCE)) {
		    if (leaveBean.getLeave().getTotalInterval().contains(day.toDateTimeAtStartOfDay())) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    private static int getDaysWithoutAnyLeave(List<LocalDate> unjustifiedDays, List<LeaveBean> leavesBeans, LocalDate beginDate,
	    LocalDate endDate) {
	int totaldays = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.plusDays(1).toDateTimeAtStartOfDay()).toPeriod(
		PeriodType.days()).getDays();
	if (leavesBeans != null) {
	    for (LocalDate day = beginDate; !day.isAfter(endDate); day = day.plusDays(1)) {
		if (unjustifiedDays.contains(day)) {
		    totaldays--;
		} else {
		    for (LeaveBean leaveBean : leavesBeans) {
			if (leaveBean.getLeave().getJustificationMotive().getJustificationType()
				.equals(JustificationType.MULTIPLE_MONTH_BALANCE)
				|| leaveBean.getLeave().getJustificationMotive().getJustificationType()
					.equals(JustificationType.OCCURRENCE)) {
			    if (leaveBean.getLeave().getTotalInterval().contains(day.toDateTimeAtStartOfDay())) {
				totaldays--;
			    }
			}
		    }
		}
	    }
	}
	return totaldays;
    }

    private static StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, LeaveBean leaveBean,
	    StateWrapper state) {
	return getLeaveLine(assiduousnessClosedMonth, leaveBean, assiduousnessClosedMonth.getBeginDate(),
		assiduousnessClosedMonth.getEndDate(), null, state);
    }

    private static StringBuilder getLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, LeaveBean leaveBean,
	    LocalDate beginDate, LocalDate endDate, LocalDate paymentMonth, StateWrapper state) {
	StringBuilder line = new StringBuilder();
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay().plusDays(1));
	HashMap<LocalDate, WorkSchedule> workSchedules = leaveBean.getLeave().getAssiduousness()
		.getWorkSchedulesBetweenDates(beginDate, endDate);
	if (leaveBean.getLeave().getTotalInterval().overlaps(interval)) {
	    LocalDate start = getNextWorkingDay(workSchedules, leaveBean.getLeave(), beginDate, false);
	    LocalDate end = endDate;
	    if (leaveBean.getDate().toLocalDate().isAfter(beginDate)) {
		start = getNextWorkingDay(workSchedules, leaveBean.getLeave(), leaveBean.getDate().toLocalDate(), false);
	    }
	    if (leaveBean.getEndLocalDate() == null) {
		end = getPreviousWorkingDay(workSchedules, leaveBean.getLeave().getJustificationMotive(), leaveBean.getLeave()
			.getAssiduousness(), leaveBean.getDate().toLocalDate(), false);
	    } else if (leaveBean.getEndLocalDate().isBefore(endDate)) {
		end = getPreviousWorkingDay(workSchedules, leaveBean.getLeave().getJustificationMotive(), leaveBean.getLeave()
			.getAssiduousness(), leaveBean.getEndLocalDate(), false);
	    }
	    if (!end.isBefore(start)) {
		String code = leaveBean.getLeave().getJustificationMotive()
			.getGiafCode(assiduousnessClosedMonth.getAssiduousnessStatusHistory());

		if (!emptyCodes.contains(code)) {
		    if (leaveBean.getLeave().getJustificationMotive() == state.maternityJustificationMotive
			    && endDate.getDayOfMonth() != 30
			    && Days.daysBetween(start, end).getDays() + 1 == endDate.getDayOfMonth()) {
			if (!state.maternityJustificationList.contains(leaveBean.getLeave().getAssiduousness())
				&& !isContractedEmployee(leaveBean.getLeave().getAssiduousness(), start, end)) {
			    state.maternityJustificationList.add(leaveBean.getLeave().getAssiduousness());
			}
		    }
		    if (paymentMonth == null) {
			paymentMonth = start.plusMonths(1);
		    }
		    line.append(paymentMonth.getYear()).append(fieldSeparator);
		    line.append(monthFormat.format(paymentMonth.getMonthOfYear())).append(fieldSeparator);
		    line.append(
			    employeeNumberFormat
				    .format(leaveBean.getLeave().getAssiduousness().getEmployee().getEmployeeNumber())).append(
			    fieldSeparator);
		    line.append("F").append(fieldSeparator);
		    line.append(code).append(fieldSeparator);
		    line.append(dateFormat.print(start)).append(fieldSeparator);
		    line.append(dateFormat.print(end)).append(fieldSeparator);
		    int days = Days.daysBetween(start, end).getDays() + 1;
		    line.append(days).append("00").append(fieldSeparator);
		    interval = new Interval(start.toDateTimeAtStartOfDay().getMillis(), end.toDateTimeAtStartOfDay().getMillis());
		    line.append(getUtilDaysBetween(workSchedules, leaveBean.getLeave().getAssiduousness(), interval))
			    .append("00");
		    if (leaveBean.getLeave().getJustificationMotive().getHasReferenceDate()) {
			line.append(fieldSeparator).append(dateFormat.print(leaveBean.getDate().toLocalDate()));
		    }
		    line.append("\r\n");
		}
	    }
	}
	return line;
    }

    private static int getUtilDaysBetween(HashMap<LocalDate, WorkSchedule> workSchedules, Assiduousness assiduousness,
	    Interval interval) {
	int days = 0;
	for (LocalDate thisDay = interval.getStart().toLocalDate(); !thisDay.isAfter(interval.getEnd().toLocalDate()); thisDay = thisDay
		.plusDays(1)) {
	    if (workSchedules.get(thisDay) != null && (!assiduousness.isHoliday(thisDay))) {
		days++;
	    }
	}
	return days;
    }

    private static LocalDate getNextWorkingDay(HashMap<LocalDate, WorkSchedule> workSchedules, Leave leave, LocalDate day,
	    boolean putOnlyWorkingDays) {
	if (leave.getJustificationMotive().getDayType().equals(DayType.WORKDAY) || putOnlyWorkingDays) {
	    List<Campus> campus = leave.getAssiduousness().getCampusForInterval(day, day);
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day))) || workSchedules.get(day) == null) {
		day = day.plusDays(1);
	    }
	}
	return day;
    }

    private static LocalDate getPreviousWorkingDay(HashMap<LocalDate, WorkSchedule> workSchedules,
	    JustificationMotive justificationMotive, Assiduousness assiduousness, LocalDate day, boolean putOnlyWorkingDays) {
	if (justificationMotive.getDayType().equals(DayType.WORKDAY) || putOnlyWorkingDays) {
	    List<Campus> campus = assiduousness.getCampusForInterval(day, day);
	    while (((campus.size() != 0 && Holiday.isHoliday(day, campus.get(0))) || (campus.size() == 0 && Holiday
		    .isHoliday(day))) || workSchedules.get(day) == null) {
		day = day.minusDays(1);
	    }
	}
	return day;
    }

    private static StringBuilder getHalfLeaveLine(AssiduousnessClosedMonth assiduousnessClosedMonth, Leave leave) {
	StringBuilder line = new StringBuilder();
	Interval interval = new Interval(assiduousnessClosedMonth.getBeginDate().toDateTimeAtStartOfDay(),
		assiduousnessClosedMonth.getEndDate().toDateTimeAtStartOfDay().plusDays(1));
	if (interval.contains(leave.getDate())) {
	    String code = leave.getJustificationMotive().getGiafCode(assiduousnessClosedMonth.getAssiduousnessStatusHistory());
	    if (!emptyCodes.contains(code)) {
		LocalDate nextMontDate = assiduousnessClosedMonth.getBeginDate().plusMonths(1);
		line.append(nextMontDate.getYear()).append(fieldSeparator);
		line.append(monthFormat.format(nextMontDate.getMonthOfYear())).append(fieldSeparator);
		line.append(employeeNumberFormat.format(leave.getAssiduousness().getEmployee().getEmployeeNumber())).append(
			fieldSeparator);
		line.append("F").append(fieldSeparator);
		line.append(code).append(fieldSeparator);
		line.append(dateFormat.print(leave.getDate().toLocalDate())).append(fieldSeparator);
		line.append(dateFormat.print(leave.getDate().toLocalDate())).append(fieldSeparator);
		line.append("050").append(fieldSeparator).append("050\r\n");
	    }
	}
	return line;
    }

    private static HashMap<Assiduousness, List<LeaveBean>> getLeaves(Set<AssiduousnessRecord> assiduousnessRecords,
	    LocalDate beginDate, LocalDate endDate, Boolean processingInCurrentMonth) {
	HashMap<Assiduousness, List<Leave>> assiduousnessLeaves = new HashMap<Assiduousness, List<Leave>>();
	Interval interval = new Interval(beginDate.toDateTimeAtStartOfDay(), Assiduousness.defaultEndWorkDay.toDateTime(endDate
		.toDateMidnight()));
	for (AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
	    if (assiduousnessRecord.isLeave() && !assiduousnessRecord.isAnulated()) {
		Leave leave = ((Leave) assiduousnessRecord);
		if (processingInCurrentMonth == leave.getJustificationMotive().getProcessingInCurrentMonth()) {
		    Interval leaveInterval = new Interval(assiduousnessRecord.getDate(), ((Leave) assiduousnessRecord)
			    .getEndDate().plusSeconds(1));
		    if (leaveInterval.overlaps(interval)) {
			List<Leave> leavesList = assiduousnessLeaves.get(assiduousnessRecord.getAssiduousness());
			if (leavesList == null) {
			    leavesList = new ArrayList<Leave>();
			}
			leavesList.add(leave);
			assiduousnessLeaves.put(assiduousnessRecord.getAssiduousness(), leavesList);
		    }
		}
	    }
	}

	HashMap<Assiduousness, List<LeaveBean>> finalAssiduousnessLeaves = new HashMap<Assiduousness, List<LeaveBean>>();
	for (Assiduousness assiduousness : assiduousnessLeaves.keySet()) {
	    finalAssiduousnessLeaves.put(assiduousness, joinLeaves(assiduousnessLeaves.get(assiduousness)));

	}
	return finalAssiduousnessLeaves;
    }

    private static List<LeaveBean> joinLeaves(List<Leave> leaves) {
	if (leaves != null) {
	    List<LeaveBean> leaveBeanList = new ArrayList<LeaveBean>();
	    Map<JustificationMotive, List<Leave>> leavesByMotive = new HashMap<JustificationMotive, List<Leave>>();
	    for (Leave leave : leaves) {
		if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)
			|| leave.getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    if (leavesByMotive.get(leave.getJustificationMotive()) == null) {
			List<Leave> newList = new ArrayList<Leave>();
			newList.add(leave);
			leavesByMotive.put(leave.getJustificationMotive(), newList);
		    } else {
			leavesByMotive.get(leave.getJustificationMotive()).add(leave);
		    }
		}
	    }
	    for (JustificationMotive justificationMotive : leavesByMotive.keySet()) {
		List<Leave> leavesMotiveList = leavesByMotive.get(justificationMotive);
		Collections.sort(leavesMotiveList, AssiduousnessRecord.COMPARATOR_BY_DATE);
		Iterator<Leave> iterator = leavesMotiveList.iterator();
		Leave nextLeave = null;
		LeaveBean leaveBean = null;
		while (iterator.hasNext()) {
		    Leave leave = null;
		    if (nextLeave != null) {
			leave = nextLeave;
		    } else {
			leave = iterator.next();
		    }
		    if (iterator.hasNext()) {
			nextLeave = iterator.next();
		    }
		    if (nextLeave != null) {
			if (leave.getEndLocalDate().plusDays(1).isEqual(nextLeave.getDate().toLocalDate())) {
			    if (leaveBean == null) {
				leaveBean = new LeaveBean(leave, nextLeave);
			    } else {
				leaveBean.setEndLocalDate(nextLeave.getEndLocalDate());
			    }
			    leaves.remove(nextLeave);
			} else {
			    if (leaveBean == null) {
				leaveBeanList.add(new LeaveBean(leave));
			    } else {
				leaveBeanList.add(leaveBean);
				leaveBean = null;
			    }
			}
		    } else {
			if (leaveBean == null) {
			    leaveBeanList.add(new LeaveBean(leave));
			} else {
			    leaveBeanList.add(leaveBean);
			    leaveBean = null;
			}
		    }
		    leaves.remove(leave);
		}
		if (leaveBean != null) {
		    leaveBeanList.add(leaveBean);
		}
	    }
	    for (Leave leave : leaves) {
		leaveBeanList.add(new LeaveBean(leave));
	    }

	    return leaveBeanList;
	}
	return null;
    }

    private static Boolean isContractedEmployee(Assiduousness assiduousness, LocalDate start, LocalDate end) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousness.getStatusBetween(start, end)) {
	    if (assiduousnessStatusHistory.getAssiduousnessStatus().isContractedEmployee()) {
		return true;
	    }
	}
	return false;
    }

    public static JustificationMotive getMaternityJustificationMotive() {
	for (JustificationMotive justificationMotive : rootDomainObject.getJustificationMotives()) {
	    if (justificationMotive.getAcronym().equalsIgnoreCase("LP25%")) {
		return justificationMotive;
	    }
	}
	return null;
    }
}