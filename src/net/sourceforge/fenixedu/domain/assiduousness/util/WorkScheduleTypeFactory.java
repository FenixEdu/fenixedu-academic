package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ContinuousSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.FlexibleSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.HalfTimeSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.HourExemptionSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.Meal;
import net.sourceforge.fenixedu.domain.assiduousness.Periodicity;
import net.sourceforge.fenixedu.domain.assiduousness.Schedule;
import net.sourceforge.fenixedu.domain.assiduousness.ScheduleExemption;
import net.sourceforge.fenixedu.domain.assiduousness.WorkPeriod;
import net.sourceforge.fenixedu.domain.assiduousness.WorkSchedule;
import net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

import org.apache.struts.action.ActionMessage;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.Partial;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class WorkScheduleTypeFactory implements Serializable, FactoryExecutor {

    private Integer oldIdInternal;

    private LocalDate beginValidDate;

    private LocalDate endValidDate;

    private String acronym;

    private LocalTime beginDayTime;

    private LocalTime endDayTime;

    private Boolean endDayNextDay;

    private LocalTime beginClockingTime;

    private LocalTime endClockingTime;

    private Boolean endClockingNextDay;

    private LocalTime beginNormalWorkFirstPeriod;

    private LocalTime endNormalWorkFirstPeriod;

    private Boolean endNormalWorkFirstPeriodNextDay;

    private LocalTime beginNormalWorkSecondPeriod;

    private LocalTime endNormalWorkSecondPeriod;

    private Boolean endNormalWorkSecondPeriodNextDay;

    private LocalTime beginFixedWorkFirstPeriod;

    private LocalTime endFixedWorkFirstPeriod;

    private Boolean endFixedWorkFirstPeriodNextDay;

    private LocalTime beginFixedWorkSecondPeriod;

    private LocalTime endFixedWorkSecondPeriod;

    private Boolean endFixedWorkSecondPeriodNextDay;

    private LocalTime mealBeginTime;

    private LocalTime mealEndTime;

    private LocalTime mandatoryMealDiscount;

    private LocalTime minimumMealBreakInterval;

    private ScheduleClockingType scheduleClockingType;

    private Employee modifiedBy;

    public WorkScheduleTypeFactory() {
	setBeginValidDate(new LocalDate(1970, 01, 02));
	setEndValidDate(new LocalDate(2036, 01, 01));
	setBeginDayTime(new LocalTime(03, 00, 00));
	setEndDayTime(LocalTime.MIDNIGHT);
	setEndDayNextDay(true);
	setBeginClockingTime(new LocalTime(07, 30, 00));
	setEndClockingTime(new LocalTime(23, 59, 00));
    }

    public Integer getOldIdInternal() {
	return oldIdInternal;
    }

    public void setOldIdInternal(Integer oldIdInternal) {
	this.oldIdInternal = oldIdInternal;
    }

    public ScheduleClockingType getScheduleClockingType() {
	return scheduleClockingType;
    }

    public void setScheduleClockingType(ScheduleClockingType scheduleClockingType) {
	this.scheduleClockingType = scheduleClockingType;
    }

    public String getAcronym() {
	return acronym;
    }

    public void setAcronym(String acronym) {
	this.acronym = acronym;
    }

    public LocalTime getBeginClockingTime() {
	return beginClockingTime;
    }

    public void setBeginClockingTime(LocalTime beginClockingTime) {
	this.beginClockingTime = beginClockingTime;
    }

    public LocalTime getBeginDayTime() {
	return beginDayTime;
    }

    public void setBeginDayTime(LocalTime beginDayTime) {
	this.beginDayTime = beginDayTime;
    }

    public LocalTime getBeginFixedWorkFirstPeriod() {
	return beginFixedWorkFirstPeriod;
    }

    public void setBeginFixedWorkFirstPeriod(LocalTime beginFixedWorkFirstPeriod) {
	this.beginFixedWorkFirstPeriod = beginFixedWorkFirstPeriod;
    }

    public LocalTime getBeginFixedWorkSecondPeriod() {
	return beginFixedWorkSecondPeriod;
    }

    public void setBeginFixedWorkSecondPeriod(LocalTime beginFixedWorkSecondPeriod) {
	this.beginFixedWorkSecondPeriod = beginFixedWorkSecondPeriod;
    }

    public LocalTime getBeginNormalWorkFirstPeriod() {
	return beginNormalWorkFirstPeriod;
    }

    public void setBeginNormalWorkFirstPeriod(LocalTime beginNormalWorkFirstPeriod) {
	this.beginNormalWorkFirstPeriod = beginNormalWorkFirstPeriod;
    }

    public LocalTime getBeginNormalWorkSecondPeriod() {
	return beginNormalWorkSecondPeriod;
    }

    public void setBeginNormalWorkSecondPeriod(LocalTime beginNormalWorkSecondPeriod) {
	this.beginNormalWorkSecondPeriod = beginNormalWorkSecondPeriod;
    }

    public LocalDate getBeginValidDate() {
	if (beginValidDate == null) {
	    beginValidDate = new LocalDate(1970, 01, 02);
	}
	return beginValidDate;
    }

    public void setBeginValidDate(LocalDate beginValidDate) {
	this.beginValidDate = beginValidDate;
    }

    public Boolean getEndClockingNextDay() {
	return endClockingNextDay;
    }

    public void setEndClockingNextDay(Boolean endClockingNextDay) {
	this.endClockingNextDay = endClockingNextDay;
    }

    public LocalTime getEndClockingTime() {
	return endClockingTime;
    }

    public void setEndClockingTime(LocalTime endClockingTime) {
	this.endClockingTime = endClockingTime;
    }

    public Boolean getEndDayNextDay() {
	return endDayNextDay;
    }

    public void setEndDayNextDay(Boolean endDayNextDay) {
	this.endDayNextDay = endDayNextDay;
    }

    public LocalTime getEndDayTime() {
	return endDayTime;
    }

    public void setEndDayTime(LocalTime endDayTime) {
	this.endDayTime = endDayTime;
    }

    public LocalTime getEndFixedWorkFirstPeriod() {
	return endFixedWorkFirstPeriod;
    }

    public void setEndFixedWorkFirstPeriod(LocalTime endFixedWorkFirstPeriod) {
	this.endFixedWorkFirstPeriod = endFixedWorkFirstPeriod;
    }

    public Boolean getEndFixedWorkFirstPeriodNextDay() {
	return endFixedWorkFirstPeriodNextDay;
    }

    public void setEndFixedWorkFirstPeriodNextDay(Boolean endFixedWorkFirstPeriodNextDay) {
	this.endFixedWorkFirstPeriodNextDay = endFixedWorkFirstPeriodNextDay;
    }

    public LocalTime getEndFixedWorkSecondPeriod() {
	return endFixedWorkSecondPeriod;
    }

    public void setEndFixedWorkSecondPeriod(LocalTime endFixedWorkSecondPeriod) {
	this.endFixedWorkSecondPeriod = endFixedWorkSecondPeriod;
    }

    public Boolean getEndFixedWorkSecondPeriodNextDay() {
	return endFixedWorkSecondPeriodNextDay;
    }

    public void setEndFixedWorkSecondPeriodNextDay(Boolean endFixedWorkSecondPeriodNextDay) {
	this.endFixedWorkSecondPeriodNextDay = endFixedWorkSecondPeriodNextDay;
    }

    public LocalTime getEndNormalWorkFirstPeriod() {
	return endNormalWorkFirstPeriod;
    }

    public void setEndNormalWorkFirstPeriod(LocalTime endNormalWorkFirstPeriod) {
	this.endNormalWorkFirstPeriod = endNormalWorkFirstPeriod;
    }

    public Boolean getEndNormalWorkFirstPeriodNextDay() {
	return endNormalWorkFirstPeriodNextDay;
    }

    public void setEndNormalWorkFirstPeriodNextDay(Boolean endNormalWorkFirstPeriodNextDay) {
	this.endNormalWorkFirstPeriodNextDay = endNormalWorkFirstPeriodNextDay;
    }

    public LocalTime getEndNormalWorkSecondPeriod() {
	return endNormalWorkSecondPeriod;
    }

    public void setEndNormalWorkSecondPeriod(LocalTime endNormalWorkSecondPeriod) {
	this.endNormalWorkSecondPeriod = endNormalWorkSecondPeriod;
    }

    public Boolean getEndNormalWorkSecondPeriodNextDay() {
	return endNormalWorkSecondPeriodNextDay;
    }

    public void setEndNormalWorkSecondPeriodNextDay(Boolean endNormalWorkSecondPeriodNextDay) {
	this.endNormalWorkSecondPeriodNextDay = endNormalWorkSecondPeriodNextDay;
    }

    public LocalDate getEndValidDate() {
	return endValidDate;
    }

    public void setEndValidDate(LocalDate endValidDate) {
	this.endValidDate = endValidDate;
    }

    public LocalTime getMandatoryMealDiscount() {
	return mandatoryMealDiscount;
    }

    public void setMandatoryMealDiscount(LocalTime mandatoryMealDiscount) {
	this.mandatoryMealDiscount = mandatoryMealDiscount;
    }

    public LocalTime getMealBeginTime() {
	return mealBeginTime;
    }

    public void setMealBeginTime(LocalTime mealBeginTime) {
	this.mealBeginTime = mealBeginTime;
    }

    public LocalTime getMealEndTime() {
	return mealEndTime;
    }

    public void setMealEndTime(LocalTime mealEndTime) {
	this.mealEndTime = mealEndTime;
    }

    public LocalTime getMinimumMealBreakInterval() {
	return minimumMealBreakInterval;
    }

    public void setMinimumMealBreakInterval(LocalTime minimumMealBreakInterval) {
	this.minimumMealBreakInterval = minimumMealBreakInterval;
    }

    public Employee getModifiedBy() {
	return modifiedBy;
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = modifiedBy;
	}
    }

    protected Duration getDuration(LocalTime begin, LocalTime end, boolean nextDay) {
	if (begin == null || end == null) {
	    return null;
	}
	LocalDate now = new LocalDate();
	DateTime endDateTime = now.toDateTime(end);
	if (nextDay) {
	    endDateTime = endDateTime.plusDays(1);
	}
	return new Duration(now.toDateTime(begin), endDateTime);
    }

    protected Duration getDuration(LocalTime time) {
	if (time == null) {
	    return null;
	}
	return new Duration(new LocalDate().toDateTimeAtStartOfDay().getMillis(), time.toDateTimeToday().getMillis());
    }

    protected WorkScheduleType getOrCreateWorkScheduleType() {
	WorkScheduleType workScheduleType = null;
	Chronology chronology = GregorianChronology.getInstanceUTC();
	DateTime lastModifiedDate = new DateTime(chronology);
	WorkPeriod normalWorkPeriod = getOrCreateWorkPeriod(getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
		getEndNormalWorkFirstPeriodNextDay(), getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
		getEndNormalWorkSecondPeriodNextDay());

	WorkPeriod fixedWorkPeriod = getOrCreateWorkPeriod(getBeginFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriod(),
		getEndFixedWorkFirstPeriodNextDay(), getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
		getEndFixedWorkSecondPeriodNextDay());
	Meal meal = getOrCreateMeal(getMealBeginTime(), getMealEndTime(), getMinimumMealBreakInterval(),
		getMandatoryMealDiscount());

	Duration dayTimeDuration = getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay());
	Duration clockingTimeDurtion = getDuration(getBeginClockingTime(), getEndClockingTime(), getEndClockingNextDay());
	if (normalWorkPeriod == null) {
	    workScheduleType = new ScheduleExemption(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
		    getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
		    normalWorkPeriod, meal, lastModifiedDate, getModifiedBy());
	} else if (normalWorkPeriod.getWorkPeriodDuration().compareTo(HalfTimeSchedule.normalHalfTimeWorkDayDuration) <= 0) {
	    workScheduleType = new HalfTimeSchedule(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
		    getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
		    normalWorkPeriod, fixedWorkPeriod, lastModifiedDate, getModifiedBy());
	} else if (normalWorkPeriod.getWorkPeriodDuration().compareTo(Assiduousness.normalWorkDayDuration) < 0
		&& (normalWorkPeriod.getSecondPeriod() != null && !normalWorkPeriod.getEndFirstPeriod().equals(
			normalWorkPeriod.getSecondPeriod()))) {
	    workScheduleType = new HourExemptionSchedule(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
		    getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
		    normalWorkPeriod, fixedWorkPeriod, meal, lastModifiedDate, getModifiedBy());
	} else {
	    if (normalWorkPeriod.getSecondPeriodInterval() != null) {
		if (normalWorkPeriod.getEndFirstPeriod().equals(normalWorkPeriod.getSecondPeriod())) {
		    if (normalWorkPeriod.getWorkPeriodDuration()
			    .isShorterThan(ContinuousSchedule.normalContinuousWorkDayDuration)) {
			return null;
		    }
		    workScheduleType = new ContinuousSchedule(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
			    getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
			    normalWorkPeriod, fixedWorkPeriod, lastModifiedDate, getModifiedBy());
		} else {
		    if (fixedWorkPeriod != null) {
			workScheduleType = new FlexibleSchedule(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
				getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(),
				clockingTimeDurtion, normalWorkPeriod, fixedWorkPeriod, meal, lastModifiedDate, getModifiedBy());
		    } else {
			workScheduleType = new ScheduleExemption(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
				getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(),
				clockingTimeDurtion, normalWorkPeriod, meal, lastModifiedDate, getModifiedBy());
		    }
		}
	    } else {
		if (normalWorkPeriod.getWorkPeriodDuration().isShorterThan(ContinuousSchedule.normalContinuousWorkDayDuration)) {
		    return null;
		}
		workScheduleType = new ContinuousSchedule(getAcronym(), getScheduleClockingType(), getBeginValidDate(),
			getEndValidDate(), getBeginDayTime(), dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
			normalWorkPeriod, fixedWorkPeriod, lastModifiedDate, getModifiedBy());
	    }
	}
	return workScheduleType;
    }

    protected Meal getOrCreateMeal(LocalTime beginTime, LocalTime endTime, LocalTime minimumInterval, LocalTime mandatoryDiscount) {
	if (beginTime == null || endTime == null) {
	    return null;
	}
	Duration minimumMealBreakInterval = getDuration(minimumInterval);
	Duration mandatoryMealDiscount = getDuration(mandatoryDiscount);
	return getOrCreateMeal(getMealBeginTime(), getMealEndTime(), minimumMealBreakInterval, mandatoryMealDiscount);
    }

    protected Meal getOrCreateMeal(LocalTime beginTime, LocalTime endTime, Duration minimumMealBreakInterval,
	    Duration mandatoryMealDiscount) {
	if (beginTime == null || endTime == null) {
	    return null;
	}
	Meal meal = getEquivalentMeal(beginTime, endTime, minimumMealBreakInterval, mandatoryMealDiscount);
	return meal != null ? meal : new Meal(getMealBeginTime(), getMealEndTime(), mandatoryMealDiscount,
		minimumMealBreakInterval);
    }

    protected Meal getEquivalentMeal(LocalTime beginTime, LocalTime endTime, Duration minimumMealBreakInterval,
	    Duration mandatoryMealDiscount) {
	for (Meal meal : RootDomainObject.getInstance().getMeals()) {
	    if (meal.getBeginMealBreak().equals(beginTime) && meal.getEndMealBreak().equals(endTime)) {
		if ((meal.getMandatoryMealDiscount() == null && mandatoryMealDiscount == null)
			|| (meal.getMandatoryMealDiscount() != null && mandatoryMealDiscount != null && meal
				.getMandatoryMealDiscount().equals(mandatoryMealDiscount))) {
		    if ((meal.getMinimumMealBreakInterval() == null && minimumMealBreakInterval == null)
			    || (meal.getMinimumMealBreakInterval() != null && minimumMealBreakInterval != null && meal
				    .getMinimumMealBreakInterval().equals(minimumMealBreakInterval))) {
			return meal;
		    }
		}
	    }
	}
	return null;
    }

    protected WorkPeriod getOrCreateWorkPeriod(LocalTime beginWorkFirstPeriod, LocalTime endWorkFirstPeriod,
	    Boolean endWorkFirstPeriodNextDay, LocalTime beginWorkSecondPeriod, LocalTime endWorkSecondPeriod,
	    Boolean endWorkSecondPeriodNextDay) {
	if (beginWorkFirstPeriod == null || endWorkFirstPeriod == null) {
	    return null;
	}
	Duration firstPeriodDuration = getDuration(beginWorkFirstPeriod, endWorkFirstPeriod, endWorkFirstPeriodNextDay);
	Duration secondPeriodDuration = getDuration(beginWorkSecondPeriod, endWorkSecondPeriod, endWorkSecondPeriodNextDay);

	return getOrCreateWorkPeriod(beginWorkFirstPeriod, firstPeriodDuration, beginWorkSecondPeriod, secondPeriodDuration);
    }

    protected WorkPeriod getOrCreateWorkPeriod(LocalTime firstPeriod, Duration firstPeriodDuration, LocalTime secondPeriod,
	    Duration secondPeriodDuration) {
	if (firstPeriod == null || firstPeriodDuration == null) {
	    return null;
	}
	WorkPeriod workPeriod = getEquivalentWorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod, secondPeriodDuration);
	return workPeriod != null ? workPeriod : new WorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod,
		secondPeriodDuration);
    }

    protected WorkPeriod getEquivalentWorkPeriod(LocalTime firstPeriod, Duration firstPeriodDuration, LocalTime secondPeriod,
	    Duration secondPeriodDuration) {
	for (WorkPeriod workPeriod : RootDomainObject.getInstance().getWorkPeriods()) {
	    if (workPeriod.getFirstPeriod().equals(firstPeriod)
		    && workPeriod.getFirstPeriodDuration().equals(firstPeriodDuration)) {
		if (workPeriod.getSecondPeriod() != null && secondPeriod != null
			&& workPeriod.getSecondPeriod().equals(secondPeriod)
			&& workPeriod.getSecondPeriodDuration().equals(secondPeriodDuration)) {
		    return workPeriod;
		} else if (workPeriod.getSecondPeriod() == null && secondPeriod == null) {
		    return workPeriod;
		}
	    }
	}
	return null;
    }

    protected WorkScheduleType getEquivalentWorkScheduleType(Integer workScheduleId) {
	for (WorkScheduleType workScheduleType : RootDomainObject.getInstance().getWorkScheduleTypes()) {
	    if ((workScheduleId == null || !workScheduleType.getIdInternal().equals(workScheduleId))
		    && workScheduleType.isValidWorkScheduleType()
		    && workScheduleType
			    .equivalent(getBeginDayTime(), getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay()),
				    getBeginClockingTime(), getDuration(getBeginClockingTime(), getEndClockingTime(),
					    getEndClockingNextDay()), getScheduleClockingType(), getBeginNormalWorkFirstPeriod(),
				    getDuration(getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
					    getEndNormalWorkFirstPeriodNextDay()), getBeginNormalWorkSecondPeriod(), getDuration(
					    getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
					    getEndNormalWorkSecondPeriodNextDay()), getBeginFixedWorkFirstPeriod(), getDuration(
					    getBeginFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriod(),
					    getEndFixedWorkFirstPeriodNextDay()), getBeginFixedWorkSecondPeriod(), getDuration(
					    getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
					    getEndFixedWorkSecondPeriodNextDay()), getMealBeginTime(), getMealEndTime(),
				    getDuration(getMinimumMealBreakInterval()), getDuration(getMandatoryMealDiscount()))) {
		return workScheduleType;
	    }
	}
	return null;
    }

    public static class WorkScheduleTypeFactoryCreator extends WorkScheduleTypeFactory {

	public Object execute() {
	    if (getScheduleClockingType() == null) {
		return new ActionMessage("error.emptyScheduleClockingType");
	    }
	    WorkScheduleType equivalentWorkScheduleType = getEquivalentWorkScheduleType(null);
	    if (equivalentWorkScheduleType == null) {
		if (getOrCreateWorkScheduleType() == null) {
		    Chronology chronology = GregorianChronology.getInstanceUTC();
		    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
		    LocalTime time = new LocalTime(ContinuousSchedule.normalContinuousWorkDayDuration.getMillis(), chronology);
		    return new ActionMessage("error.invalidContinuousDuration", fmt.print(time));
		}
		return null;
	    }
	    return new ActionMessage("error.existingEquivalentSchedule", equivalentWorkScheduleType.getAcronym());
	}

    }

    public static class WorkScheduleTypeFactoryEditor extends WorkScheduleTypeFactory {
	private WorkScheduleType workScheduleType;

	public WorkScheduleType getWorkScheduleType() {
	    return workScheduleType;
	}

	public void setWorkScheduleType(WorkScheduleType workScheduleType) {
	    if (workScheduleType != null) {
		this.workScheduleType = workScheduleType;
		setOldIdInternal(getWorkScheduleType().getIdInternal());
		setScheduleClockingType(workScheduleType.getScheduleClockingType());
		setBeginValidDate(getWorkScheduleType().getBeginValidDate());
		setEndValidDate(getWorkScheduleType().getEndValidDate());
		setAcronym(getWorkScheduleType().getAcronym());
		setBeginDayTime(getWorkScheduleType().getWorkTime());
		setEndDayTime(getWorkScheduleType().getWorkEndTime());
		setEndDayNextDay(getWorkScheduleType().isWorkTimeNextDay());
		setBeginClockingTime(getWorkScheduleType().getClockingTime());
		setEndClockingTime(getWorkScheduleType().getClockingEndTime());
		setEndClockingNextDay(getWorkScheduleType().isClokingTimeNextDay());
		setBeginNormalWorkFirstPeriod(getWorkScheduleType().getNormalWorkPeriod().getFirstPeriod());
		setEndNormalWorkFirstPeriod(getWorkScheduleType().getNormalWorkPeriod().getEndFirstPeriod());
		setEndNormalWorkFirstPeriodNextDay(getWorkScheduleType().getNormalWorkPeriod().isFirstPeriodNextDay());
		if (getWorkScheduleType().getNormalWorkPeriod().isSecondWorkPeriodDefined()) {
		    setBeginNormalWorkSecondPeriod(getWorkScheduleType().getNormalWorkPeriod().getSecondPeriod());
		    setEndNormalWorkSecondPeriod(getWorkScheduleType().getNormalWorkPeriod().getEndSecondPeriod());
		    setEndNormalWorkSecondPeriodNextDay(getWorkScheduleType().getNormalWorkPeriod().isSecondPeriodNextDay());
		}
		if (getWorkScheduleType().hasFixedWorkPeriod()) {
		    setBeginFixedWorkFirstPeriod(getWorkScheduleType().getFixedWorkPeriod().getFirstPeriod());
		    setEndFixedWorkFirstPeriod(getWorkScheduleType().getFixedWorkPeriod().getEndFirstPeriod());
		    setEndFixedWorkFirstPeriodNextDay(getWorkScheduleType().getFixedWorkPeriod().isFirstPeriodNextDay());
		    if (getWorkScheduleType().getFixedWorkPeriod().isSecondWorkPeriodDefined()) {
			setBeginFixedWorkSecondPeriod(getWorkScheduleType().getFixedWorkPeriod().getSecondPeriod());
			setEndFixedWorkSecondPeriod(getWorkScheduleType().getFixedWorkPeriod().getEndSecondPeriod());
			setEndFixedWorkSecondPeriodNextDay(getWorkScheduleType().getFixedWorkPeriod().isSecondPeriodNextDay());
		    }
		}
		if (getWorkScheduleType().hasMeal()) {
		    setMealBeginTime(getWorkScheduleType().getMeal().getBeginMealBreak());
		    setMealEndTime(getWorkScheduleType().getMeal().getEndMealBreak());
		    setMandatoryMealDiscount(new LocalTime(
			    getWorkScheduleType().getMeal().getMandatoryMealDiscount().getMillis(), GregorianChronology
				    .getInstanceUTC()));
		    setMinimumMealBreakInterval(new LocalTime(getWorkScheduleType().getMeal().getMinimumMealBreakInterval()
			    .getMillis(), GregorianChronology.getInstanceUTC()));
		}

	    }
	}

	public WorkScheduleTypeFactoryEditor(WorkScheduleType workScheduleType) {
	    setWorkScheduleType(workScheduleType);
	}

	public Object execute() {
	    DateTime now = new DateTime();
	    LocalDate firstDay = getNotClosedMonthFirstDay();
	    boolean changeDatesOrAcronym = false;
	    if (!getWorkScheduleType().getBeginValidDate().equals(getBeginValidDate())) {
		if (!getWorkScheduleType().getWorkSchedules().isEmpty() && isWorkScheduleTypeBeenUsedBeforeBeginDate()) {
		    return new ActionMessage("error.scheduleBeingUsedBeforeBeginDate");
		}
		changeDatesOrAcronym = true;
	    }
	    if (!areEndDatesEqual(getWorkScheduleType().getEndValidDate(), getEndValidDate())) {
		if (isWorkScheduleTypeBeenUsedBeforeEndDate()
			&& (getEndValidDate() != null && getEndValidDate().isBefore(firstDay))
			&& !getWorkScheduleType().getWorkSchedules().isEmpty()) {
		    return new ActionMessage("error.scheduleBeingUsedBeforeEndDate");
		}
		changeDatesOrAcronym = true;
	    }
	    if (!getAcronym().equalsIgnoreCase(getWorkScheduleType().getAcronym())) {
		if (alreadyExistsWorkScheduleTypeAcronymExceptThis(getAcronym())) {
		    return new ActionMessage("error.acronymAlreadyExists");
		}
		changeDatesOrAcronym = true;
	    }

	    if (getScheduleClockingType() == null) {
		return new ActionMessage("error.emptyScheduleClockingType");
	    }

	    if (!getWorkScheduleType()
		    .equivalent(
			    getBeginDayTime(),
			    getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay()),
			    getBeginClockingTime(),
			    getDuration(getBeginClockingTime(), getEndClockingTime(), getEndClockingNextDay()),
			    getScheduleClockingType(),
			    getBeginNormalWorkFirstPeriod(),
			    getDuration(getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
				    getEndNormalWorkFirstPeriodNextDay()),
			    getBeginNormalWorkSecondPeriod(),
			    getDuration(getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
				    getEndNormalWorkSecondPeriodNextDay()),
			    getBeginFixedWorkFirstPeriod(),
			    getDuration(getBeginFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriod(),
				    getEndFixedWorkFirstPeriodNextDay()),
			    getBeginFixedWorkSecondPeriod(),
			    getDuration(getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
				    getEndFixedWorkSecondPeriodNextDay()), getMealBeginTime(), getMealEndTime(),
			    getDuration(getMinimumMealBreakInterval()), getDuration(getMandatoryMealDiscount()))) {
		WorkScheduleType equivalentWorkScheduleType = getEquivalentWorkScheduleType(getWorkScheduleType().getIdInternal());
		if (equivalentWorkScheduleType == null) {
		    if (!getWorkScheduleType().getWorkSchedules().isEmpty()
			    || getWorkScheduleType().getBeginValidDate().isBefore(firstDay)) {
			Set<Schedule> scheduleToChangeDate = new HashSet<Schedule>();
			List<WorkSchedule> workScheduleToChangeWorkScheduleType = new ArrayList<WorkSchedule>();

			for (WorkSchedule workSchedule : getWorkScheduleType().getWorkSchedules()) {
			    for (Schedule schedule : workSchedule.getSchedules()) {
				if (!schedule.getBeginDate().isBefore(firstDay)) {
				    if (!workScheduleToChangeWorkScheduleType.contains(workSchedule)
					    && !scheduleToChangeDate.contains(workSchedule)) {
					workScheduleToChangeWorkScheduleType.add(workSchedule);
				    }
				} else if ((!scheduleToChangeDate.contains(workSchedule))
					&& (schedule.getEndDate() == null || (!schedule.getEndDate().isBefore(firstDay)))) {
				    scheduleToChangeDate.add(schedule);
				    if (workScheduleToChangeWorkScheduleType.contains(workSchedule)) {
					workScheduleToChangeWorkScheduleType.remove(workSchedule);
				    }
				}
			    }
			}
			if (scheduleToChangeDate.isEmpty()) {
			    // ninguém usa antes deste do firstday... basta
			    // alterar
			    editWorkScheduleType();
			} else {
			    getWorkScheduleType().setAcronym(getNewAcronym(getWorkScheduleType().getAcronym()));
			    getWorkScheduleType().setEndValidDate(firstDay.minusDays(1));
			    getWorkScheduleType().setModifiedBy(getModifiedBy());
			    getWorkScheduleType().setLastModifiedDate(now);

			    WorkScheduleType newWorkScheluleType = getOrCreateWorkScheduleType();
			    if (newWorkScheluleType == null) {
				Chronology chronology = GregorianChronology.getInstanceUTC();
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				LocalTime time = new LocalTime(ContinuousSchedule.normalContinuousWorkDayDuration.getMillis(),
					chronology);
				return new ActionMessage("error.invalidContinuousDuration", fmt.print(time));
			    }
			    for (WorkSchedule workSchedule : workScheduleToChangeWorkScheduleType) {
				workSchedule.setWorkScheduleType(newWorkScheluleType);
			    }

			    for (Schedule schedule : scheduleToChangeDate) {
				Schedule newSchedule = new Schedule(schedule.getAssiduousness(), firstDay, schedule.getEndDate(),
					schedule.getException(), now, getModifiedBy());
				List<WorkSchedule> newWorkSchedules = new ArrayList<WorkSchedule>();
				for (WorkSchedule workSchedule : schedule.getWorkSchedules()) {
				    WorkSchedule newWorkSchedule = getWorkSchedule(newWorkScheluleType.getIdInternal(),
					    workSchedule.getWorkWeek(), workSchedule.getPeriodicity());
				    if (newWorkSchedule == null) {
					newWorkSchedule = new WorkSchedule(newWorkScheluleType, workSchedule.getWorkWeek(),
						workSchedule.getPeriodicity());
				    }
				    newWorkSchedules.add(newWorkSchedule);
				}
				newSchedule.getWorkSchedules().addAll(newWorkSchedules);
				schedule.setEndDate(firstDay.minusDays(1));
			    }
			}
		    } else {
			// ninguém usa alterar horário ou foi criado depois do
			// firstday(basta alterar
			// este)
			// mas deviamos fazer validações, que não estamos a
			// fazer pq.... (nº de horas dos horários continuos,
			// etc...)
			editWorkScheduleType();
		    }

		    return null;
		} else {
		    return new ActionMessage("error.existingEquivalentSchedule", equivalentWorkScheduleType.getAcronym());
		}
	    } else if (changeDatesOrAcronym) {
		// só alterou as datas de validade e/ou acrónimo e já vi em cima
		// que pode mudar
		editWorkScheduleType();
	    }
	    return null;
	}

	private boolean isWorkScheduleTypeBeenUsedBeforeBeginDate() {
	    for (WorkSchedule workSchedule : getWorkScheduleType().getWorkSchedules()) {
		for (Schedule schedule : workSchedule.getSchedules()) {
		    if (schedule.getBeginDate().isBefore(getBeginValidDate())) {
			return true;
		    }
		}
	    }
	    return false;
	}

	private boolean isWorkScheduleTypeBeenUsedBeforeEndDate() {
	    for (WorkSchedule workSchedule : getWorkScheduleType().getWorkSchedules()) {
		for (Schedule schedule : workSchedule.getSchedules()) {
		    if ((schedule.getEndDate() == null && getEndValidDate() != null)
			    || (getEndValidDate() != null && !schedule.getEndDate().isBefore(getEndValidDate()))) {
			return true;
		    }
		}
	    }
	    return false;
	}

	private static WorkSchedule getWorkSchedule(Integer workScheduleTypeId, WorkWeek workWeek, Periodicity periodicity) {
	    for (WorkSchedule workSchedule : RootDomainObject.getInstance().getWorkSchedules()) {
		if (workSchedule.getWorkScheduleType().getIdInternal().equals(workScheduleTypeId)
			&& workSchedule.getWorkWeek().equals(workWeek) && workSchedule.getPeriodicity() == periodicity) {
		    return workSchedule;
		}
	    }
	    return null;
	}

	private String getNewAcronym(String oldAcronym) {
	    for (int i = 1;; i++) {
		String newAcronym = oldAcronym + "(" + i + ")";
		if (!alreadyExistsWorkScheduleTypeAcronym(newAcronym)) {
		    return newAcronym;
		}
	    }
	}

	private boolean alreadyExistsWorkScheduleTypeAcronym(String acronym) {
	    for (WorkScheduleType workScheduleType : RootDomainObject.getInstance().getWorkScheduleTypes()) {
		if (workScheduleType.getAcronym().equalsIgnoreCase(acronym)) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean alreadyExistsWorkScheduleTypeAcronymExceptThis(String acronym) {
	    for (WorkScheduleType workScheduleType : RootDomainObject.getInstance().getWorkScheduleTypes()) {
		if (workScheduleType.getAcronym().equalsIgnoreCase(acronym)
			&& !getWorkScheduleType().getIdInternal().equals(workScheduleType.getIdInternal())) {
		    return true;
		}
	    }
	    return false;
	}

	private void editWorkScheduleType() {
	    getWorkScheduleType().setAcronym(getAcronym());
	    getWorkScheduleType().setScheduleClockingType(getScheduleClockingType());
	    getWorkScheduleType().setBeginValidDate(getBeginValidDate());
	    getWorkScheduleType().setEndValidDate(getEndValidDate());
	    getWorkScheduleType().setWorkTime(getBeginDayTime());
	    getWorkScheduleType().setWorkTimeDuration(getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay()));
	    getWorkScheduleType().setClockingTime(getBeginClockingTime());
	    getWorkScheduleType().setClockingTimeDuration(
		    getDuration(getBeginClockingTime(), getEndClockingTime(), getEndClockingNextDay()));

	    getWorkScheduleType().setNormalWorkPeriod(
		    editWorkPeriod(getWorkScheduleType().getNormalWorkPeriod(), getBeginNormalWorkFirstPeriod(),
			    getDuration(getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
				    getEndNormalWorkFirstPeriodNextDay()), getBeginNormalWorkSecondPeriod(), getDuration(
				    getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
				    getEndNormalWorkSecondPeriodNextDay())));

	    getWorkScheduleType().setFixedWorkPeriod(
		    editWorkPeriod(getWorkScheduleType().getFixedWorkPeriod(), getBeginFixedWorkFirstPeriod(), getDuration(
			    getBeginFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriodNextDay()),
			    getBeginFixedWorkSecondPeriod(), getDuration(getBeginFixedWorkSecondPeriod(),
				    getEndFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriodNextDay())));

	    getWorkScheduleType().setMeal(
		    editMeal(getWorkScheduleType().getMeal(), getMealBeginTime(), getMealEndTime(),
			    getDuration(getMinimumMealBreakInterval()), getDuration(getMandatoryMealDiscount())));
	}

	private WorkPeriod editWorkPeriod(WorkPeriod workPeriod, LocalTime firstPeriod, Duration firstPeriodDuration,
		LocalTime secondPeriod, Duration secondPeriodDuration) {
	    if (workPeriod != null && !isWorkPeriodUsed(workPeriod)) {
		WorkPeriod equivalentWorkPeriod = getEquivalentWorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod,
			secondPeriodDuration);
		if (equivalentWorkPeriod == null) {
		    workPeriod.setFirstPeriod(firstPeriod);
		    workPeriod.setFirstPeriodDuration(firstPeriodDuration);
		    workPeriod.setSecondPeriod(secondPeriod);
		    workPeriod.setSecondPeriodDuration(secondPeriodDuration);
		} else {
		    workPeriod.delete();
		    return equivalentWorkPeriod;
		}
		return workPeriod;
	    }
	    return getOrCreateWorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod, secondPeriodDuration);
	}

	private boolean isWorkPeriodUsed(WorkPeriod workPeriod) {
	    if ((workPeriod.getNormalWorkScheduleTypesCount() == 0 || (workPeriod.getNormalWorkScheduleTypesCount() == 1 && workPeriod
		    .getNormalWorkScheduleTypes().iterator().next().getIdInternal().equals(getWorkScheduleType().getIdInternal())))
		    && (workPeriod.getFixedWorkScheduleTypesCount() == 0 || (workPeriod.getFixedWorkScheduleTypesCount() == 1 && workPeriod
			    .getFixedWorkScheduleTypes().iterator().next().getIdInternal().equals(
				    getWorkScheduleType().getIdInternal())))) {
		return false;
	    }
	    return true;
	}

	private Meal editMeal(Meal meal, LocalTime beginMeal, LocalTime endMeal, Duration minimumMealBreakInterval,
		Duration mandatoryMealDiscount) {
	    if (beginMeal != null) {
		if (minimumMealBreakInterval == null) {
		    minimumMealBreakInterval = Duration.ZERO;
		}
		if (mandatoryMealDiscount == null) {
		    mandatoryMealDiscount = Duration.ZERO;
		}
	    }
	    if (meal != null && meal.getWorkScheduleTypesCount() == 1) {
		Meal equivalentMeal = getEquivalentMeal(beginMeal, endMeal, minimumMealBreakInterval, mandatoryMealDiscount);
		if (equivalentMeal == null) {
		    meal.setBeginMealBreak(beginMeal);
		    meal.setEndMealBreak(endMeal);
		    meal.setMinimumMealBreakInterval(minimumMealBreakInterval);
		    meal.setMandatoryMealDiscount(mandatoryMealDiscount);
		    return meal;
		} else {
		    meal.delete();
		    return equivalentMeal;
		}
	    }
	    return getOrCreateMeal(beginMeal, endMeal, minimumMealBreakInterval, mandatoryMealDiscount);
	}
    }

    public LocalDate getNotClosedMonthFirstDay() {
	Partial yearMonth = null;
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (yearMonth == null || closedMonth.getClosedYearMonth().isAfter(yearMonth)) {
		yearMonth = closedMonth.getClosedYearMonth();
	    }
	}
	return new LocalDate(yearMonth.get(DateTimeFieldType.year()), yearMonth.get(DateTimeFieldType.monthOfYear()), 1)
		.plusMonths(1);
    }

    public boolean areEndDatesEqual(LocalDate endDate1, LocalDate endDate2) {
	return (endDate1 == null && endDate2 == null) || (endDate1 != null && endDate2 != null && endDate1.equals(endDate2));
    }
}