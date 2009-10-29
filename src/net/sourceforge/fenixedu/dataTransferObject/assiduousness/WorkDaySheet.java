package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.CalculateDailyWorkSheetStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.assiduousness.strategys.ICalculateDailyWorkSheetStrategy;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedDay;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.domain.assiduousness.Justification;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.util.DayType;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.util.WeekDay;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class WorkDaySheet implements Serializable {

    public final static Duration DAY_HOUR_LIMIT = Hours.TWO.toStandardDuration();
    public final static Duration hourDuration = Hours.ONE.toStandardDuration();

    private static final String CLOSE_SPAN = "</span>";
    private static final BigDecimal COMPLETE_DAY = BigDecimal.ONE;
    private static final BigDecimal HALF_DAY = new BigDecimal(0.5);
    private static final String NOTE_SEPARATOR = " / ";
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
    private static final String SEPARATOR = ", ";
    private static final String MISSING_CLOCKING_CORRECTION_STYLE = "<span class='assiduousnessCorrections missingClockings'>";
    private static final String MISSING_CLOCKING_STYLE = "<span class='missingClockings'>";
    private static final String CORRECTION_COLOR = "<span class='assiduousnessCorrections'>";

    private Assiduousness assiduousness;

    private LocalDate date;

    private WorkSchedule workSchedule;

    private String workScheduleAcronym;

    private Duration balanceTime;

    private Duration balanceToCompensate;

    private Duration multipleMonthBalance;

    private Duration unjustifiedTime;

    private Duration unjustifiedTimeWithoutBalanceDiscount;

    private Duration complementaryWeeklyRest;

    private Duration holidayRest;

    private Duration weeklyRest;

    private String notes;

    private List<AssiduousnessRecord> assiduousnessRecords;

    private String clockings;

    private List<Leave> leaves;

    private Timeline timeline;

    private Boolean irregular;

    private Boolean unjustifiedDay;

    private Boolean unjustifiedDayLeave;

    private BigDecimal vacation;

    private BigDecimal tolerance;

    private BigDecimal article17;

    private BigDecimal article66;

    private Duration workedTime;

    private boolean workingDay;

    private boolean discountA17;

    private boolean discountBonus;

    private HashMap<JustificationMotive, Duration> justificationsDuration;

    // Night Work
    private Duration nightWorkBalance;

    private boolean nightWork;

    // Overtime
    private Duration overtimeFirstLevel;

    private Duration overtimeSecondLevel;

    private Duration overtimeSecondLevelWithLimit;

    private Duration nightlyOvertimeFirstLevel;

    private Duration nightlyOvertimeSecondLevel;

    private Duration nightlyOvertimeSecondLevelWithLimit;

    private void initValues() {
	setBalanceTime(Duration.ZERO);
	setBalanceToCompensate(Duration.ZERO);
	setUnjustifiedTime(Duration.ZERO);
	setUnjustifiedTimeWithoutBalanceDiscount(Duration.ZERO);
	setWorkedTime(Duration.ZERO);
	setComplementaryWeeklyRest(Duration.ZERO);
	setHolidayRest(Duration.ZERO);
	setWeeklyRest(Duration.ZERO);
	setIrregular(false);
	setUnjustifiedDay(false);
	setUnjustifiedDayLeave(false);
	setVacation(BigDecimal.ZERO);
	setTolerance(BigDecimal.ZERO);
	setArticle17(BigDecimal.ZERO);
	setArticle66(BigDecimal.ZERO);
	setWorkingDay(false);
	setDiscountBonus(false);
	setDiscountA17(false);
	setNightWorkBalance(Duration.ZERO);
	setOvertimeFirstLevel(Duration.ZERO);
	setOvertimeSecondLevel(Duration.ZERO);
	setOvertimeSecondLevelWithLimit(Duration.ZERO);
	setNightlyOvertimeFirstLevel(Duration.ZERO);
	setNightlyOvertimeSecondLevel(Duration.ZERO);
	setNightlyOvertimeSecondLevelWithLimit(Duration.ZERO);
	setNightWork(false);
	justificationsDuration = new HashMap<JustificationMotive, Duration>();
	setMultipleMonthBalance(Duration.ZERO);

    }

    public WorkDaySheet(Assiduousness assiduousness, LocalDate day, WorkSchedule workSchedule,
	    List<AssiduousnessRecord> clockings, List<Leave> leaves) {
	initValues();
	setAssiduousness(assiduousness);
	setDate(day);
	setWorkSchedule(workSchedule);
	setLeaves(leaves);
	setAssiduousnessRecords(clockings);
    }

    public WorkDaySheet(AssiduousnessClosedDay assiduousnessClosedDay) {
	initValues();
	setAssiduousness(assiduousnessClosedDay.getAssiduousnessClosedMonth().getAssiduousnessStatusHistory().getAssiduousness());
	setDate(assiduousnessClosedDay.getDay());
	setWorkScheduleAcronym(assiduousnessClosedDay.getSchedule());
	setBalanceTime(assiduousnessClosedDay.getBalance());
	setUnjustifiedTime(assiduousnessClosedDay.getUnjustified());
	setNotes(assiduousnessClosedDay.getNotes());
	clockings = assiduousnessClosedDay.getClockings();
	setOvertimeFirstLevel(assiduousnessClosedDay.getExtraWorkFirstLevel());
	setOvertimeSecondLevel(assiduousnessClosedDay.getExtraWorkSecondLevel());
	setOvertimeSecondLevelWithLimit(assiduousnessClosedDay.getExtraWorkSecondLevelWithLimit());
	setNightlyOvertimeFirstLevel(assiduousnessClosedDay.getNightlyExtraWorkFirstLevel());
	setNightlyOvertimeSecondLevel(assiduousnessClosedDay.getNightlyExtraWorkSecondLevel());
	setNightlyOvertimeSecondLevelWithLimit(assiduousnessClosedDay.getNightlyExtraWorkSecondLevelWithLimit());
	setNightWorkBalance(assiduousnessClosedDay.getNightlyWorkBalance());
	setComplementaryWeeklyRest(assiduousnessClosedDay.getSaturdayBalance());
	setHolidayRest(assiduousnessClosedDay.getHolidayBalance());
	setWeeklyRest(assiduousnessClosedDay.getSundayBalance());
    }

    public void calculateWorkDaySheet(boolean closeMonth) {
	final boolean isDayHoliday = getAssiduousness().isHoliday(getDate());
	LocalDate today = new LocalDate();
	if (getDate().isBefore(today)) {
	    ICalculateDailyWorkSheetStrategy calculateDailyWorkSheetStrategy = CalculateDailyWorkSheetStrategyFactory
		    .getInstance().getCalculateDailyWorkSheetStrategy(getDate());
	    calculateDailyWorkSheetStrategy.calculateDailyBalance(getAssiduousness(), this, isDayHoliday, closeMonth);
	}
	Duration thisDayWorkedTime = Duration.ZERO;
	Duration timeLeaveToDiscount = Duration.ZERO;

	if (workSchedule != null && !isDayHoliday) {
	    setWorkingDay(true);
	    for (Leave leave : getLeaves()) {
		addNote(leave);
		if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)) {
		    Duration justificationDuration = justificationsDuration.get(leave.getJustificationMotive());
		    if (justificationDuration == null) {
			justificationDuration = Duration.ZERO;
		    }
		    justificationDuration = justificationDuration.plus(getLeaveDuration(workSchedule, leave));
		    justificationsDuration.put(leave.getJustificationMotive(), justificationDuration);
		} else if ((leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE) || leave
			.getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE))
			&& leave.getJustificationMotive().getActualWorkTime()) {
		    Interval nightInterval = new Interval(getDate().toDateTime(Assiduousness.defaultStartNightWorkDay), getDate()
			    .toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1));
		    setNightWorkBalance(workSchedule.getWorkScheduleType().getNormalWorkPeriod().getNormalNigthWorkPeriod(
			    nightInterval));
		}
		if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FINJUST")) {
		    setUnjustifiedDayLeave(true);
		}

		if (leave.getJustificationMotive().getJustificationGroup() != null
			&& leave.getJustificationMotive().getJustificationGroup().isVacation()) {
		    setVacation(vacation.add(getDayValue(leave)));
		} else if (leave.getJustificationMotive().getJustificationGroup() != null
			&& leave.getJustificationMotive().getJustificationGroup().equals(JustificationGroup.TOLERANCES)) {
		    setTolerance(tolerance.add(getDayValue(leave)));
		} else if (leave.getJustificationMotive().getAcronym().equals("A17")) {
		    setArticle17(article17.add(getDayValue(leave)));
		} else if (leave.getJustificationMotive().getAcronym().equals("A66")) {
		    setArticle66(article66.add(getDayValue(leave)));
		} else if (leave.getJustificationMotive().getAcronym().equals("1/2A66")) {
		    setArticle66(article66.add(getDayValue(leave)));
		}

		if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.MULTIPLE_MONTH_BALANCE)) {
		    setMultipleMonthBalance(getMultipleMonthBalance().plus(
			    getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()));
		} else if (leave.getJustificationMotive().getJustificationType().equals(
			JustificationType.HALF_MULTIPLE_MONTH_BALANCE)) {
		    setMultipleMonthBalance(getMultipleMonthBalance()
			    .plus(
				    getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()
					    .getMillis() / 2));
		}

		if (!leave.getJustificationMotive().getDiscountBonus()
			|| !leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
		    setDiscountBonus(true);

		}
		if (!leave.getJustificationMotive().getDiscountA17Vacations()
			|| !leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
		    setDiscountA17(true);
		}
		if (!leave.getJustificationMotive().getDiscountA17Vacations()) {
		    if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.OCCURRENCE)) {
			thisDayWorkedTime = getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration();
		    }
		} else {
		    if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)
			    && leave.getJustificationMotive().getJustificationType() != null) {
			timeLeaveToDiscount = timeLeaveToDiscount.plus(leave.getDuration());
		    }
		}
	    }
	    Duration thisDayBalance = getBalanceTime();

	    if (workSchedule.getWorkScheduleType().getScheduleClockingType().equals(ScheduleClockingType.NOT_MANDATORY_CLOCKING)) {
		thisDayWorkedTime = getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration();
		if (getLeaves().isEmpty()) {
		    setDiscountBonus(true);
		    setDiscountA17(true);
		}
	    } else if (!getAssiduousnessRecords().isEmpty()) {
		thisDayWorkedTime = getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration().plus(
			thisDayBalance);
		if (getLeaves().isEmpty()) {
		    setDiscountBonus(true);
		    setDiscountA17(true);
		}
	    }

	    setExtraWorkValues(thisDayBalance);
	    setNightExtraWorkValues(thisDayBalance);
	    if (!timeLeaveToDiscount.equals(Duration.ZERO)) {
		thisDayWorkedTime = thisDayWorkedTime.minus(timeLeaveToDiscount);
	    }
	    setWorkedTime(getWorkedTime().plus(thisDayWorkedTime));
	} else {
	    if (isDayHoliday) {
		ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
		setWorkScheduleAcronym(bundle.getString("label.holiday"));
	    }
	    for (final Leave leave : getLeaves()) {
		if (leave.getJustificationMotive().getDayType() != DayType.WORKDAY
			&& (leave.getJustificationMotive().getJustificationGroup() == null || !leave.getJustificationMotive()
				.getJustificationGroup().isVacation())) {
		    addNote(leave);
		}
	    }
	}
    }

    public Duration getLeaveDuration(final WorkSchedule workSchedule, final Leave leave) {
	Duration leaveDuration = Duration.ZERO;
	if (!getIrregular()) {
	    Duration overlapsDuration = Duration.ZERO;

	    Interval interval = workSchedule.getWorkScheduleType().getNormalWorkPeriod().getNotWorkingPeriod(getDate());
	    if (interval != null
		    && (getTimeline() == null || ((!interval.contains(leave.getDate()) || getTimeline()
			    .hasWorkingPointBeforeLeave(leave)) && (!interval.contains(leave.getEndDate()) || getTimeline()
			    .hasWorkingPointAfterLeave(leave)))

		    )) {
		Interval overlaps = interval.overlap(leave.getTotalInterval());
		if (overlaps != null) {
		    overlapsDuration = overlaps.toDuration();
		}
	    }

	    if ((leave.getDuration().minus(overlapsDuration)).isLongerThan(workSchedule.getWorkScheduleType()
		    .getNormalWorkPeriod().getWorkPeriodDuration())) {
		leaveDuration = leaveDuration.plus(workSchedule.getWorkScheduleType().getNormalWorkPeriod()
			.getWorkPeriodDuration());
	    } else {
		leaveDuration = leaveDuration.plus(leave.getDuration().minus(overlapsDuration));
	    }
	}
	return leaveDuration;
    }

    public boolean hasLeaveType(JustificationType justificationType) {
	for (Leave leave : getLeaves()) {
	    if (leave.getJustificationMotive().getJustificationType().equals(justificationType)) {
		return true;
	    }
	}
	return false;
    }

    public void discountHalfOccurrence() {
	if (workSchedule.getWorkScheduleType().hasFixedWorkPeriod()) {
	    Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(
		    workSchedule.getWorkScheduleType().getFixedWorkPeriod().getHalfWorkPeriodDuration());
	    if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
		setUnjustifiedTime(Duration.ZERO);
	    } else {
		setUnjustifiedTime(newFixedPeriodAbsence);
	    }
	}
	setBalanceTime(getBalanceTime()
		.plus(workSchedule.getWorkScheduleType().getNormalWorkPeriod().getHalfWorkPeriodDuration()));
    }

    private void setExtraWorkValues(Duration thisDayBalance) {
	if (getWorkSchedule().getWorkScheduleType().canDoExtraWorkInWeekDays()) {
	    Duration thisDayUnjustified = getUnjustifiedTime();
	    if (thisDayUnjustified == null) {
		thisDayUnjustified = Duration.ZERO;
	    }
	    Duration thisDayExtraWork = new Duration(thisDayBalance.minus(thisDayUnjustified));
	    if (!thisDayExtraWork.equals(Duration.ZERO)) {
		if (thisDayExtraWork.isLongerThan(hourDuration)) {
		    setOvertimeSecondLevel(thisDayExtraWork.minus(hourDuration));
		    setOvertimeSecondLevelWithLimit(new Duration(Math.min(thisDayExtraWork.minus(hourDuration).getMillis(),
			    DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
		    setOvertimeFirstLevel(hourDuration);
		} else if (thisDayExtraWork.isLongerThan(Duration.ZERO)) {
		    setOvertimeFirstLevel(thisDayExtraWork);
		}
	    }
	}
    }

    private void setNightExtraWorkValues(Duration thisDayBalance) {
	if (getTimeline() != null) {
	    if (!getIrregular()) {
		Duration extraWorkDuration = new Duration(getTimeline().calculateWorkPeriodDurationBetweenDates(
			getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
			getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1)));
		if (extraWorkDuration.isLongerThan(Duration.ZERO)) {
		    setNightWorkBalance(extraWorkDuration);
		}
	    }
	    if (getWorkSchedule().getWorkScheduleType().canDoExtraWorkInWeekDays()) {
		Duration nightExtraWorkDuration = new Duration(getTimeline().calculateWorkPeriodDurationBetweenDates(
			getDate().toDateTime(Assiduousness.defaultStartNightWorkDay),
			getDate().toDateTime(Assiduousness.defaultEndNightWorkDay).plusDays(1)));

		Duration thisDayUnjustified = getUnjustifiedTime();
		if (thisDayUnjustified == null) {
		    thisDayUnjustified = Duration.ZERO;
		}
		Duration thisDayExtraWork = new Duration(thisDayBalance.minus(thisDayUnjustified));
		if ((!thisDayExtraWork.equals(Duration.ZERO)) && (!nightExtraWorkDuration.equals(Duration.ZERO))) {
		    if (nightExtraWorkDuration.isShorterThan(thisDayExtraWork)) {
			if (nightExtraWorkDuration.isLongerThan(hourDuration)) {
			    setNightlyOvertimeFirstLevel(hourDuration);
			    setNightlyOvertimeSecondLevel(nightExtraWorkDuration.minus(hourDuration));
			    setNightlyOvertimeSecondLevelWithLimit(new Duration(Math.min(nightExtraWorkDuration.minus(
				    hourDuration).getMillis(), DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
			} else {
			    setNightlyOvertimeFirstLevel(nightExtraWorkDuration);
			}
		    } else {
			if (thisDayExtraWork.isLongerThan(hourDuration)) {
			    setNightlyOvertimeFirstLevel(hourDuration);
			    setNightlyOvertimeSecondLevel(thisDayExtraWork.minus(hourDuration));
			    setNightlyOvertimeSecondLevelWithLimit(new Duration(Math.min(thisDayExtraWork.minus(hourDuration)
				    .getMillis(), DAY_HOUR_LIMIT.minus(hourDuration).getMillis())));
			} else {
			    setNightlyOvertimeFirstLevel(thisDayExtraWork);
			}
		    }
		    setNightWork(true);
		}
	    }
	}
    }

    public Duration getOvertimeFirstLevel() {
	return overtimeFirstLevel;
    }

    public void setOvertimeFirstLevel(Duration overtimeFirstLevel) {
	this.overtimeFirstLevel = overtimeFirstLevel;
    }

    public Duration getOvertimeSecondLevel() {
	return overtimeSecondLevel;
    }

    public void setOvertimeSecondLevel(Duration overtimeSecondLevel) {
	this.overtimeSecondLevel = overtimeSecondLevel;
    }

    public Duration getOvertimeSecondLevelWithLimit() {
	return overtimeSecondLevelWithLimit;
    }

    public void setOvertimeSecondLevelWithLimit(Duration overtimeSecondLevelWithLimit) {
	this.overtimeSecondLevelWithLimit = overtimeSecondLevelWithLimit;
    }

    public Duration getNightWorkBalance() {
	return nightWorkBalance;
    }

    public void setNightWorkBalance(Duration nightWorkBalance) {
	this.nightWorkBalance = nightWorkBalance;
    }

    public Duration getNightlyOvertimeFirstLevel() {
	return nightlyOvertimeFirstLevel;
    }

    public void setNightlyOvertimeFirstLevel(Duration nightlyOvertimeFirstLevel) {
	this.nightlyOvertimeFirstLevel = nightlyOvertimeFirstLevel;
    }

    public Duration getNightlyOvertimeSecondLevel() {
	return nightlyOvertimeSecondLevel;
    }

    public void setNightlyOvertimeSecondLevel(Duration nightlyOvertimeSecondLevel) {
	this.nightlyOvertimeSecondLevel = nightlyOvertimeSecondLevel;
    }

    public Duration getNightlyOvertimeSecondLevelWithLimit() {
	return nightlyOvertimeSecondLevelWithLimit;
    }

    public void setNightlyOvertimeSecondLevelWithLimit(Duration nightlyOvertimeSecondLevelWithLimit) {
	this.nightlyOvertimeSecondLevelWithLimit = nightlyOvertimeSecondLevelWithLimit;
    }

    public Timeline getTimeline() {
	return timeline;
    }

    public void setTimeline(Timeline timeline) {
	this.timeline = timeline;
    }

    public Duration getBalanceTime() {
	return balanceTime;
    }

    public void setBalanceTime(Duration balanceTime) {
	this.balanceTime = balanceTime;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public String getNotes() {
	return notes == null ? "" : notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public void addNote(Leave leave) {
	if (leave.getIsCorrection(getDate())) {
	    addNote(CORRECTION_COLOR + leave.getJustificationMotive().getAcronym() + CLOSE_SPAN);
	} else {
	    addNote(leave.getJustificationMotive().getAcronym());
	}
    }

    public void addNote(String note) {
	List<String> notesList = new ArrayList<String>();
	if (!StringUtils.isEmpty(notes)) {
	    notesList.add(notes);
	}
	notesList.add(note);
	this.notes = StringUtils.join(notesList, NOTE_SEPARATOR);
    }

    public Duration getUnjustifiedTime() {
	return unjustifiedTime;
    }

    public void setUnjustifiedTime(Duration unjustifiedTime) {
	this.unjustifiedTime = unjustifiedTime;
    }

    public String getWorkScheduleAcronym() {
	return workScheduleAcronym;
    }

    public void setWorkScheduleAcronym(String workScheduleAcronym) {
	this.workScheduleAcronym = workScheduleAcronym;
    }

    public String getDateFormatted() {
	if (getDate() == null) {
	    return "";
	}
	return getDate().toString("dd/MM/yyyy");
    }

    // used on workDaySheet report
    public String getBalanceTimeFormatted() {
	return getDurationString(getBalanceTime());
    }

    public String getUnjustifiedTimeFormatted() {
	return getDurationString(getUnjustifiedTime());
    }

    public String getDurationString(Duration duration) {
	if (duration == null) {
	    return "";
	}
	PeriodFormatter fmt = new PeriodFormatterBuilder().printZeroAlways().appendHours().appendSeparator(":")
		.minimumPrintedDigits(2).appendMinutes().toFormatter();
	MutablePeriod balance = new MutablePeriod(duration.getMillis(), PeriodType.time());
	if (duration.toPeriod().getMinutes() < 0) {
	    balance.setMinutes(-duration.toPeriod().getMinutes());
	    if (duration.toPeriod().getHours() == 0) {
		fmt = new PeriodFormatterBuilder().printZeroAlways().appendLiteral("-").appendHours().appendSeparator(":")
			.minimumPrintedDigits(2).appendMinutes().toFormatter();
	    }
	}
	return fmt.print(balance);
    }

    //

    public String getWeekDay() {
	if (getDate() == null) {
	    return "";
	}
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtStartOfDay()).toString() + "_ACRONYM");
    }

    public void setAssiduousnessRecords(List<AssiduousnessRecord> clockingsList) {
	if (clockingsList == null) {
	    clockingsList = new ArrayList<AssiduousnessRecord>();
	}
	Collections.sort(clockingsList, AssiduousnessRecord.COMPARATOR_BY_DATE);
	this.assiduousnessRecords = clockingsList;
	List<String> clockings = new ArrayList<String>();
	if (assiduousnessRecords != null) {
	    for (final AssiduousnessRecord assiduousnessRecord : assiduousnessRecords) {
		final LocalTime timeOfDay = assiduousnessRecord.getDate().toLocalTime();
		final StringBuilder resultToManagement = new StringBuilder();
		if (assiduousnessRecord.isMissingClocking()) {
		    resultToManagement.append(getOpenSpan((Justification) assiduousnessRecord));
		    resultToManagement.append(fmt.print(timeOfDay));
		    resultToManagement.append(CLOSE_SPAN);
		} else {
		    resultToManagement.append(fmt.print(timeOfDay));
		}
		clockings.add(resultToManagement.toString());
	    }
	}
	setClockings(StringUtils.join(clockings, SEPARATOR));
    }

    private Object getOpenSpan(Justification assiduousnessRecord) {
	if (assiduousnessRecord.getIsCorrection(getDate())) {
	    return MISSING_CLOCKING_CORRECTION_STYLE;
	}
	return MISSING_CLOCKING_STYLE;
    }

    public List<AssiduousnessRecord> getAssiduousnessRecords() {
	return assiduousnessRecords;
    }

    public List<Leave> getLeaves() {
	if (leaves == null) {
	    setLeaves(new ArrayList<Leave>());
	}
	return leaves;
    }

    public void setLeaves(List<Leave> leavesList) {
	if (leavesList == null) {
	    leavesList = new ArrayList<Leave>();
	}
	Collections.sort(leavesList, Leave.COMPARATORY_BY_DATE);
	this.leaves = leavesList;
    }

    private BigDecimal getDayValue(Leave leave) {
	if (leave.getJustificationMotive().getJustificationType().equals(JustificationType.TIME)
		|| leave.getJustificationMotive().getJustificationType().equals(JustificationType.HALF_OCCURRENCE)) {
	    return HALF_DAY;
	}
	return COMPLETE_DAY;
    }

    public void addLeaves(List<Leave> list) {
	getLeaves().addAll(list);
    }

    public WorkSchedule getWorkSchedule() {
	return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
	this.workSchedule = workSchedule;
	if (workSchedule != null) {
	    setWorkScheduleAcronym(workSchedule.getWorkScheduleType().getAcronym());
	}
    }

    public Duration getComplementaryWeeklyRest() {
	return complementaryWeeklyRest;
    }

    public void setComplementaryWeeklyRest(Duration complementaryWeeklyRest) {
	this.complementaryWeeklyRest = complementaryWeeklyRest;
    }

    public Duration getWeeklyRest() {
	return weeklyRest;
    }

    public void setWeeklyRest(Duration weeklyRest) {
	this.weeklyRest = weeklyRest;
    }

    public Duration getHolidayRest() {
	return holidayRest;
    }

    public void setHolidayRest(Duration holidayRest) {
	this.holidayRest = holidayRest;
    }

    public Boolean getIrregular() {
	return irregular;
    }

    public void setIrregular(Boolean irregular) {
	this.irregular = irregular;
    }

    public void setIrregularDay(Boolean irregular) {
	this.irregular = irregular;
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	addNote(bundle.getString("label.irregular"));
    }

    public void setUnjustifiedDay() {
	this.unjustifiedDay = true;
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	addNote(bundle.getString("label.unjustifiedDay"));
    }

    public Boolean getUnjustifiedDay() {
	return unjustifiedDay;
    }

    public void setUnjustifiedDay(Boolean unjustifiedDay) {
	this.unjustifiedDay = unjustifiedDay;
    }

    public BigDecimal getVacation() {
	return vacation;
    }

    public void setVacation(BigDecimal vacation) {
	this.vacation = vacation;
    }

    public BigDecimal getTolerance() {
	return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
	this.tolerance = tolerance;
    }

    public BigDecimal getArticle17() {
	return article17;
    }

    public void setArticle17(BigDecimal article17) {
	this.article17 = article17;
    }

    public BigDecimal getArticle66() {
	return article66;
    }

    public void setArticle66(BigDecimal article66) {
	this.article66 = article66;
    }

    public Duration getWorkedTime() {
	return workedTime;
    }

    public void setWorkedTime(Duration workedTime) {
	this.workedTime = workedTime;
    }

    public Assiduousness getAssiduousness() {
	return assiduousness;
    }

    public void setAssiduousness(Assiduousness assiduousness) {
	this.assiduousness = assiduousness;
    }

    public boolean isWorkingDay() {
	return workingDay;
    }

    public void setWorkingDay(boolean workingDay) {
	this.workingDay = workingDay;
    }

    public boolean hasNightWork() {
	return nightWork;
    }

    public void setNightWork(boolean nightWork) {
	this.nightWork = nightWork;
    }

    public boolean isDiscountA17() {
	return discountA17;
    }

    public void setDiscountA17(boolean discountA17) {
	this.discountA17 = discountA17;
    }

    public boolean isDiscountBonus() {
	return discountBonus;
    }

    public void setDiscountBonus(boolean discountBonus) {
	this.discountBonus = discountBonus;
    }

    public HashMap<JustificationMotive, Duration> getJustificationsDuration() {
	return justificationsDuration;
    }

    public void setJustificationsDuration(HashMap<JustificationMotive, Duration> justificationsDuration) {
	this.justificationsDuration = justificationsDuration;
    }

    public Duration getMultipleMonthBalance() {
	return multipleMonthBalance;
    }

    public void setMultipleMonthBalance(Duration multipleMonthBalance) {
	this.multipleMonthBalance = multipleMonthBalance;
    }

    public Duration getUnjustifiedTimeWithoutBalanceDiscount() {
	return unjustifiedTimeWithoutBalanceDiscount;
    }

    public void setUnjustifiedTimeWithoutBalanceDiscount(Duration unjustifiedTimeWithoutBalanceDiscount) {
	this.unjustifiedTimeWithoutBalanceDiscount = unjustifiedTimeWithoutBalanceDiscount;
    }

    public Duration getBalanceToCompensate() {
	return balanceToCompensate;
    }

    public void setBalanceToCompensate(Duration balanceToCompensate) {
	this.balanceToCompensate = balanceToCompensate;
    }

    public void discountBalanceLeaveInFixedPeriod(List<Leave> balanceLeaveList) {
	setUnjustifiedTimeWithoutBalanceDiscount(getUnjustifiedTime());
	Duration balance = Duration.ZERO;
	for (Leave balanceLeave : balanceLeaveList) {
	    balance = balance.plus(balanceLeave.getDuration());
	    if (balanceLeave.getJustificationMotive().getJustificationType() == JustificationType.HALF_MULTIPLE_MONTH_BALANCE) {
		setBalanceToCompensate(getBalanceToCompensate().plus(balanceLeave.getDuration()));
	    }
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
    }

    public void discountBalanceOcurrenceLeaveInFixedPeriod(List<Leave> balanceOcurrenceLeaveList) {
	Duration balance = Duration.ZERO;
	if (!balanceOcurrenceLeaveList.isEmpty()) {
	    balance = balance.plus(getWorkSchedule().getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration());
	    setBalanceToCompensate(getBalanceToCompensate().plus(balance));
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
    }

    public void discountBalance(List<Leave> halfOccurrenceLeaves) {
	Duration balance = Duration.ZERO;
	for (Leave halfOccurrenceLeave : halfOccurrenceLeaves) {
	    balance = balance.plus(halfOccurrenceLeave.getDuration());
	}
	Duration newFixedPeriodAbsence = getUnjustifiedTime().minus(balance);
	if (newFixedPeriodAbsence.isShorterThan(Duration.ZERO)) {
	    setUnjustifiedTime(Duration.ZERO);
	} else {
	    setUnjustifiedTime(newFixedPeriodAbsence);
	}
	setBalanceTime(getBalanceTime().plus(balance));
    }

    public Boolean getUnjustifiedDayLeave() {
	return unjustifiedDayLeave;
    }

    public void setUnjustifiedDayLeave(Boolean unjustifiedDayLeave) {
	this.unjustifiedDayLeave = unjustifiedDayLeave;
    }

    public String getClockings() {
	return clockings;
    }

    public void setClockings(String clockings) {
	this.clockings = clockings;
    }

    public String getNotesWithoutStyle() {
	String result = getNotes().replaceAll("<span.*?>", "<font color='#825101'>");
	return result.replaceAll("</span>", "</font>");

    }

    public String getClockingsWithoutStyle() {
	String result = new String(" ");
	result = result.concat(getClockings().replaceAll("<span class='missingClockings.*?>", "<font color='#226B17'>"));
	result = result.replaceAll("<span.*?>", "<font color='#825101'>");
	return result.replaceAll("</span>", "</font>");
    }

}
