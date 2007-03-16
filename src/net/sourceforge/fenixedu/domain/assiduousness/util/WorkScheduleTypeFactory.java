package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
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
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class WorkScheduleTypeFactory implements Serializable, FactoryExecutor {

    private Integer oldIdInternal;

    private YearMonthDay beginValidDate;

    private YearMonthDay endValidDate;

    private String acronym;

    private TimeOfDay beginDayTime;

    private TimeOfDay endDayTime;

    private Boolean endDayNextDay;

    private TimeOfDay beginClockingTime;

    private TimeOfDay endClockingTime;

    private Boolean endClockingNextDay;

    private TimeOfDay beginNormalWorkFirstPeriod;

    private TimeOfDay endNormalWorkFirstPeriod;

    private Boolean endNormalWorkFirstPeriodNextDay;

    private TimeOfDay beginNormalWorkSecondPeriod;

    private TimeOfDay endNormalWorkSecondPeriod;

    private Boolean endNormalWorkSecondPeriodNextDay;

    private TimeOfDay beginFixedWorkFirstPeriod;

    private TimeOfDay endFixedWorkFirstPeriod;

    private Boolean endFixedWorkFirstPeriodNextDay;

    private TimeOfDay beginFixedWorkSecondPeriod;

    private TimeOfDay endFixedWorkSecondPeriod;

    private Boolean endFixedWorkSecondPeriodNextDay;

    private TimeOfDay mealBeginTime;

    private TimeOfDay mealEndTime;

    private TimeOfDay mandatoryMealDiscount;

    private TimeOfDay minimumMealBreakInterval;

    private ScheduleClockingType scheduleClockingType;

    private DomainReference<Employee> modifiedBy;

    public WorkScheduleTypeFactory() {
	setBeginValidDate(new YearMonthDay(1970, 01, 02));
	setEndValidDate(new YearMonthDay(2036, 01, 01));
	setBeginDayTime(new TimeOfDay(03, 00, 00));
	setEndDayTime(TimeOfDay.MIDNIGHT);
	setEndDayNextDay(true);
	setBeginClockingTime(new TimeOfDay(07, 30, 00));
	setEndClockingTime(new TimeOfDay(23, 59, 00));
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

    public TimeOfDay getBeginClockingTime() {
	return beginClockingTime;
    }

    public void setBeginClockingTime(TimeOfDay beginClockingTime) {
	this.beginClockingTime = beginClockingTime;
    }

    public TimeOfDay getBeginDayTime() {
	return beginDayTime;
    }

    public void setBeginDayTime(TimeOfDay beginDayTime) {
	this.beginDayTime = beginDayTime;
    }

    public TimeOfDay getBeginFixedWorkFirstPeriod() {
	return beginFixedWorkFirstPeriod;
    }

    public void setBeginFixedWorkFirstPeriod(TimeOfDay beginFixedWorkFirstPeriod) {
	this.beginFixedWorkFirstPeriod = beginFixedWorkFirstPeriod;
    }

    public TimeOfDay getBeginFixedWorkSecondPeriod() {
	return beginFixedWorkSecondPeriod;
    }

    public void setBeginFixedWorkSecondPeriod(TimeOfDay beginFixedWorkSecondPeriod) {
	this.beginFixedWorkSecondPeriod = beginFixedWorkSecondPeriod;
    }

    public TimeOfDay getBeginNormalWorkFirstPeriod() {
	return beginNormalWorkFirstPeriod;
    }

    public void setBeginNormalWorkFirstPeriod(TimeOfDay beginNormalWorkFirstPeriod) {
	this.beginNormalWorkFirstPeriod = beginNormalWorkFirstPeriod;
    }

    public TimeOfDay getBeginNormalWorkSecondPeriod() {
	return beginNormalWorkSecondPeriod;
    }

    public void setBeginNormalWorkSecondPeriod(TimeOfDay beginNormalWorkSecondPeriod) {
	this.beginNormalWorkSecondPeriod = beginNormalWorkSecondPeriod;
    }

    public YearMonthDay getBeginValidDate() {
	if (beginValidDate == null) {
	    beginValidDate = new YearMonthDay(1970, 01, 02);
	}
	return beginValidDate;
    }

    public void setBeginValidDate(YearMonthDay beginValidDate) {
	this.beginValidDate = beginValidDate;
    }

    public Boolean getEndClockingNextDay() {
	return endClockingNextDay;
    }

    public void setEndClockingNextDay(Boolean endClockingNextDay) {
	this.endClockingNextDay = endClockingNextDay;
    }

    public TimeOfDay getEndClockingTime() {
	return endClockingTime;
    }

    public void setEndClockingTime(TimeOfDay endClockingTime) {
	this.endClockingTime = endClockingTime;
    }

    public Boolean getEndDayNextDay() {
	return endDayNextDay;
    }

    public void setEndDayNextDay(Boolean endDayNextDay) {
	this.endDayNextDay = endDayNextDay;
    }

    public TimeOfDay getEndDayTime() {
	return endDayTime;
    }

    public void setEndDayTime(TimeOfDay endDayTime) {
	this.endDayTime = endDayTime;
    }

    public TimeOfDay getEndFixedWorkFirstPeriod() {
	return endFixedWorkFirstPeriod;
    }

    public void setEndFixedWorkFirstPeriod(TimeOfDay endFixedWorkFirstPeriod) {
	this.endFixedWorkFirstPeriod = endFixedWorkFirstPeriod;
    }

    public Boolean getEndFixedWorkFirstPeriodNextDay() {
	return endFixedWorkFirstPeriodNextDay;
    }

    public void setEndFixedWorkFirstPeriodNextDay(Boolean endFixedWorkFirstPeriodNextDay) {
	this.endFixedWorkFirstPeriodNextDay = endFixedWorkFirstPeriodNextDay;
    }

    public TimeOfDay getEndFixedWorkSecondPeriod() {
	return endFixedWorkSecondPeriod;
    }

    public void setEndFixedWorkSecondPeriod(TimeOfDay endFixedWorkSecondPeriod) {
	this.endFixedWorkSecondPeriod = endFixedWorkSecondPeriod;
    }

    public Boolean getEndFixedWorkSecondPeriodNextDay() {
	return endFixedWorkSecondPeriodNextDay;
    }

    public void setEndFixedWorkSecondPeriodNextDay(Boolean endFixedWorkSecondPeriodNextDay) {
	this.endFixedWorkSecondPeriodNextDay = endFixedWorkSecondPeriodNextDay;
    }

    public TimeOfDay getEndNormalWorkFirstPeriod() {
	return endNormalWorkFirstPeriod;
    }

    public void setEndNormalWorkFirstPeriod(TimeOfDay endNormalWorkFirstPeriod) {
	this.endNormalWorkFirstPeriod = endNormalWorkFirstPeriod;
    }

    public Boolean getEndNormalWorkFirstPeriodNextDay() {
	return endNormalWorkFirstPeriodNextDay;
    }

    public void setEndNormalWorkFirstPeriodNextDay(Boolean endNormalWorkFirstPeriodNextDay) {
	this.endNormalWorkFirstPeriodNextDay = endNormalWorkFirstPeriodNextDay;
    }

    public TimeOfDay getEndNormalWorkSecondPeriod() {
	return endNormalWorkSecondPeriod;
    }

    public void setEndNormalWorkSecondPeriod(TimeOfDay endNormalWorkSecondPeriod) {
	this.endNormalWorkSecondPeriod = endNormalWorkSecondPeriod;
    }

    public Boolean getEndNormalWorkSecondPeriodNextDay() {
	return endNormalWorkSecondPeriodNextDay;
    }

    public void setEndNormalWorkSecondPeriodNextDay(Boolean endNormalWorkSecondPeriodNextDay) {
	this.endNormalWorkSecondPeriodNextDay = endNormalWorkSecondPeriodNextDay;
    }

    public YearMonthDay getEndValidDate() {
	return endValidDate;
    }

    public void setEndValidDate(YearMonthDay endValidDate) {
	this.endValidDate = endValidDate;
    }

    public TimeOfDay getMandatoryMealDiscount() {
	return mandatoryMealDiscount;
    }

    public void setMandatoryMealDiscount(TimeOfDay mandatoryMealDiscount) {
	this.mandatoryMealDiscount = mandatoryMealDiscount;
    }

    public TimeOfDay getMealBeginTime() {
	return mealBeginTime;
    }

    public void setMealBeginTime(TimeOfDay mealBeginTime) {
	this.mealBeginTime = mealBeginTime;
    }

    public TimeOfDay getMealEndTime() {
	return mealEndTime;
    }

    public void setMealEndTime(TimeOfDay mealEndTime) {
	this.mealEndTime = mealEndTime;
    }

    public TimeOfDay getMinimumMealBreakInterval() {
	return minimumMealBreakInterval;
    }

    public void setMinimumMealBreakInterval(TimeOfDay minimumMealBreakInterval) {
	this.minimumMealBreakInterval = minimumMealBreakInterval;
    }

    public Employee getModifiedBy() {
	return modifiedBy == null ? null : modifiedBy.getObject();
    }

    public void setModifiedBy(Employee modifiedBy) {
	if (modifiedBy != null) {
	    this.modifiedBy = new DomainReference<Employee>(modifiedBy);
	}
    }

    protected Duration getDuration(TimeOfDay begin, TimeOfDay end, boolean nextDay) {
	if (begin == null || end == null) {
	    return null;
	}
	YearMonthDay now = new YearMonthDay();
	DateTime endDateTime = now.toDateTime(end);
	if (nextDay) {
	    endDateTime = endDateTime.plusDays(1);
	}
	return new Duration(now.toDateTime(begin), endDateTime);
    }

    protected Duration getDuration(TimeOfDay time) {
	if (time == null) {
	    return null;
	}
	return new Duration(new YearMonthDay().toDateTimeAtMidnight().getMillis(), time
		.toDateTimeToday().getMillis());
    }

    protected WorkScheduleType getOrCreateWorkScheduleType() {
	WorkScheduleType workScheduleType = null;
	Chronology chronology = GregorianChronology.getInstanceUTC();
	DateTime lastModifiedDate = new DateTime(chronology);
	WorkPeriod normalWorkPeriod = getOrCreateWorkPeriod(getBeginNormalWorkFirstPeriod(),
		getEndNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriodNextDay(),
		getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
		getEndNormalWorkSecondPeriodNextDay());

	WorkPeriod fixedWorkPeriod = getOrCreateWorkPeriod(getBeginFixedWorkFirstPeriod(),
		getEndFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriodNextDay(),
		getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
		getEndFixedWorkSecondPeriodNextDay());
	Meal meal = getOrCreateMeal(getMealBeginTime(), getMealEndTime(), getMinimumMealBreakInterval(),
		getMandatoryMealDiscount());

	Duration dayTimeDuration = getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay());
	Duration clockingTimeDurtion = getDuration(getBeginClockingTime(), getEndClockingTime(),
		getEndClockingNextDay());
	if (normalWorkPeriod == null) {
	    workScheduleType = new ScheduleExemption(getAcronym(), getScheduleClockingType(),
		    getBeginValidDate(), getEndValidDate(), getBeginDayTime(), dayTimeDuration,
		    getBeginClockingTime(), clockingTimeDurtion, normalWorkPeriod, meal,
		    lastModifiedDate, getModifiedBy());
	} else if (normalWorkPeriod.getWorkPeriodDuration().compareTo(
		HalfTimeSchedule.normalHalfTimeWorkDayDuration) <= 0) {
	    workScheduleType = new HalfTimeSchedule(getAcronym(), getScheduleClockingType(),
		    getBeginValidDate(), getEndValidDate(), getBeginDayTime(), dayTimeDuration,
		    getBeginClockingTime(), clockingTimeDurtion, normalWorkPeriod, fixedWorkPeriod,
		    lastModifiedDate, getModifiedBy());
	} else if (normalWorkPeriod.getWorkPeriodDuration().compareTo(
		Assiduousness.normalWorkDayDuration) < 0
		&& (normalWorkPeriod.getSecondPeriod() != null && !normalWorkPeriod.getEndFirstPeriod()
			.equals(normalWorkPeriod.getSecondPeriod()))) {
	    workScheduleType = new HourExemptionSchedule(getAcronym(), getScheduleClockingType(),
		    getBeginValidDate(), getEndValidDate(), getBeginDayTime(), dayTimeDuration,
		    getBeginClockingTime(), clockingTimeDurtion, normalWorkPeriod, fixedWorkPeriod,
		    meal, lastModifiedDate, getModifiedBy());
	} else {
	    if (normalWorkPeriod.getSecondPeriodInterval() != null) {
		if (normalWorkPeriod.getEndFirstPeriod().equals(normalWorkPeriod.getSecondPeriod())) {
		    if (normalWorkPeriod.getWorkPeriodDuration().isShorterThan(
			    ContinuousSchedule.normalContinuousWorkDayDuration)) {
			return null;
		    }
		    workScheduleType = new ContinuousSchedule(getAcronym(), getScheduleClockingType(),
			    getBeginValidDate(), getEndValidDate(), getBeginDayTime(), dayTimeDuration,
			    getBeginClockingTime(), clockingTimeDurtion, normalWorkPeriod,
			    fixedWorkPeriod, lastModifiedDate, getModifiedBy());
		} else {
		    if (fixedWorkPeriod != null) {
			workScheduleType = new FlexibleSchedule(getAcronym(), getScheduleClockingType(),
				getBeginValidDate(), getEndValidDate(), getBeginDayTime(),
				dayTimeDuration, getBeginClockingTime(), clockingTimeDurtion,
				normalWorkPeriod, fixedWorkPeriod, meal, lastModifiedDate,
				getModifiedBy());
		    } else {
			workScheduleType = new ScheduleExemption(getAcronym(),
				getScheduleClockingType(), getBeginValidDate(), getEndValidDate(),
				getBeginDayTime(), dayTimeDuration, getBeginClockingTime(),
				clockingTimeDurtion, normalWorkPeriod, meal, lastModifiedDate,
				getModifiedBy());
		    }
		}
	    } else {
		if (normalWorkPeriod.getWorkPeriodDuration().isShorterThan(
			ContinuousSchedule.normalContinuousWorkDayDuration)) {
		    return null;
		}
		workScheduleType = new ContinuousSchedule(getAcronym(), getScheduleClockingType(),
			getBeginValidDate(), getEndValidDate(), getBeginDayTime(), dayTimeDuration,
			getBeginClockingTime(), clockingTimeDurtion, normalWorkPeriod, fixedWorkPeriod,
			lastModifiedDate, getModifiedBy());
	    }
	}
	return workScheduleType;
    }

    protected Meal getOrCreateMeal(TimeOfDay beginTime, TimeOfDay endTime, TimeOfDay minimumInterval,
	    TimeOfDay mandatoryDiscount) {
	if (beginTime == null || endTime == null) {
	    return null;
	}
	Duration minimumMealBreakInterval = getDuration(minimumInterval);
	Duration mandatoryMealDiscount = getDuration(mandatoryDiscount);
	return getOrCreateMeal(getMealBeginTime(), getMealEndTime(), minimumMealBreakInterval,
		mandatoryMealDiscount);
    }

    protected Meal getOrCreateMeal(TimeOfDay beginTime, TimeOfDay endTime,
	    Duration minimumMealBreakInterval, Duration mandatoryMealDiscount) {
	if (beginTime == null || endTime == null) {
	    return null;
	}
	Meal meal = getEquivalentMeal(beginTime, endTime, minimumMealBreakInterval,
		mandatoryMealDiscount);
	return meal != null ? meal : new Meal(getMealBeginTime(), getMealEndTime(),
		mandatoryMealDiscount, minimumMealBreakInterval);
    }

    protected Meal getEquivalentMeal(TimeOfDay beginTime, TimeOfDay endTime,
	    Duration minimumMealBreakInterval, Duration mandatoryMealDiscount) {
	for (Meal meal : RootDomainObject.getInstance().getMeals()) {
	    if (meal.getBeginMealBreak().equals(beginTime) && meal.getEndMealBreak().equals(endTime)) {
		if ((meal.getMandatoryMealDiscount() == null && mandatoryMealDiscount == null)
			|| (meal.getMandatoryMealDiscount() != null && mandatoryMealDiscount != null && meal
				.getMandatoryMealDiscount().equals(mandatoryMealDiscount))) {
		    if ((meal.getMinimumMealBreakInterval() == null && minimumMealBreakInterval == null)
			    || (meal.getMinimumMealBreakInterval() != null
				    && minimumMealBreakInterval != null && meal
				    .getMinimumMealBreakInterval().equals(minimumMealBreakInterval))) {
			return meal;
		    }
		}
	    }
	}
	return null;
    }

    protected WorkPeriod getOrCreateWorkPeriod(TimeOfDay beginWorkFirstPeriod,
	    TimeOfDay endWorkFirstPeriod, Boolean endWorkFirstPeriodNextDay,
	    TimeOfDay beginWorkSecondPeriod, TimeOfDay endWorkSecondPeriod,
	    Boolean endWorkSecondPeriodNextDay) {
	if (beginWorkFirstPeriod == null || endWorkFirstPeriod == null) {
	    return null;
	}
	Duration firstPeriodDuration = getDuration(beginWorkFirstPeriod, endWorkFirstPeriod,
		endWorkFirstPeriodNextDay);
	Duration secondPeriodDuration = getDuration(beginWorkSecondPeriod, endWorkSecondPeriod,
		endWorkSecondPeriodNextDay);

	return getOrCreateWorkPeriod(beginWorkFirstPeriod, firstPeriodDuration, beginWorkSecondPeriod,
		secondPeriodDuration);
    }

    protected WorkPeriod getOrCreateWorkPeriod(TimeOfDay firstPeriod, Duration firstPeriodDuration,
	    TimeOfDay secondPeriod, Duration secondPeriodDuration) {
	if (firstPeriod == null || firstPeriodDuration == null) {
	    return null;
	}
	WorkPeriod workPeriod = getEquivalentWorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod,
		secondPeriodDuration);
	return workPeriod != null ? workPeriod : new WorkPeriod(firstPeriod, firstPeriodDuration,
		secondPeriod, secondPeriodDuration);
    }

    protected WorkPeriod getEquivalentWorkPeriod(TimeOfDay firstPeriod, Duration firstPeriodDuration,
	    TimeOfDay secondPeriod, Duration secondPeriodDuration) {
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
		    && workScheduleType.equivalent(getBeginDayTime(), getDuration(getBeginDayTime(),
			    getEndDayTime(), getEndDayNextDay()), getBeginClockingTime(), getDuration(
			    getBeginClockingTime(), getEndClockingTime(), getEndClockingNextDay()),
			    getScheduleClockingType(), getBeginNormalWorkFirstPeriod(), getDuration(
				    getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
				    getEndNormalWorkFirstPeriodNextDay()),
			    getBeginNormalWorkSecondPeriod(), getDuration(
				    getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
				    getEndNormalWorkSecondPeriodNextDay()),
			    getBeginFixedWorkFirstPeriod(), getDuration(getBeginFixedWorkFirstPeriod(),
				    getEndFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriodNextDay()),
			    getBeginFixedWorkSecondPeriod(), getDuration(
				    getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
				    getEndFixedWorkSecondPeriodNextDay()), getMealBeginTime(),
			    getMealEndTime(), getDuration(getMinimumMealBreakInterval()),
			    getDuration(getMandatoryMealDiscount()))) {
		return workScheduleType;
	    }
	}
	return null;
    }

    public static class WorkScheduleTypeFactoryCreator extends WorkScheduleTypeFactory {

	public Object execute() {
	    WorkScheduleType equivalentWorkScheduleType = getEquivalentWorkScheduleType(null);
	    if (equivalentWorkScheduleType == null) {
		if (getOrCreateWorkScheduleType() == null) {
		    Chronology chronology = GregorianChronology.getInstanceUTC();
		    DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
		    TimeOfDay time = new TimeOfDay(ContinuousSchedule.normalContinuousWorkDayDuration
			    .getMillis(), chronology);
		    return new ActionMessage("error.invalidContinuousDuration", fmt.print(time));
		}
		return null;
	    }
	    return new ActionMessage("error.existingEquivalentSchedule", equivalentWorkScheduleType
		    .getAcronym());
	}

    }

    public static class WorkScheduleTypeFactoryEditor extends WorkScheduleTypeFactory {
	private DomainReference<WorkScheduleType> workScheduleType;

	public WorkScheduleType getWorkScheduleType() {
	    return workScheduleType == null ? null : workScheduleType.getObject();
	}

	public void setWorkScheduleType(WorkScheduleType workScheduleType) {
	    if (workScheduleType != null) {
		this.workScheduleType = new DomainReference<WorkScheduleType>(workScheduleType);
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
		setBeginNormalWorkFirstPeriod(getWorkScheduleType().getNormalWorkPeriod()
			.getFirstPeriod());
		setEndNormalWorkFirstPeriod(getWorkScheduleType().getNormalWorkPeriod()
			.getEndFirstPeriod());
		setEndNormalWorkFirstPeriodNextDay(getWorkScheduleType().getNormalWorkPeriod()
			.isFirstPeriodNextDay());
		if (getWorkScheduleType().getNormalWorkPeriod().isSecondWorkPeriodDefined()) {
		    setBeginNormalWorkSecondPeriod(getWorkScheduleType().getNormalWorkPeriod()
			    .getSecondPeriod());
		    setEndNormalWorkSecondPeriod(getWorkScheduleType().getNormalWorkPeriod()
			    .getEndSecondPeriod());
		    setEndNormalWorkSecondPeriodNextDay(getWorkScheduleType().getNormalWorkPeriod()
			    .isSecondPeriodNextDay());
		}
		if (getWorkScheduleType().hasFixedWorkPeriod()) {
		    setBeginFixedWorkFirstPeriod(getWorkScheduleType().getFixedWorkPeriod()
			    .getFirstPeriod());
		    setEndFixedWorkFirstPeriod(getWorkScheduleType().getFixedWorkPeriod()
			    .getEndFirstPeriod());
		    setEndFixedWorkFirstPeriodNextDay(getWorkScheduleType().getFixedWorkPeriod()
			    .isFirstPeriodNextDay());
		    if (getWorkScheduleType().getFixedWorkPeriod().isSecondWorkPeriodDefined()) {
			setBeginFixedWorkSecondPeriod(getWorkScheduleType().getFixedWorkPeriod()
				.getSecondPeriod());
			setEndFixedWorkSecondPeriod(getWorkScheduleType().getFixedWorkPeriod()
				.getEndSecondPeriod());
			setEndFixedWorkSecondPeriodNextDay(getWorkScheduleType().getFixedWorkPeriod()
				.isSecondPeriodNextDay());
		    }
		}
		if (getWorkScheduleType().hasMeal()) {
		    setMealBeginTime(getWorkScheduleType().getMeal().getBeginMealBreak());
		    setMealEndTime(getWorkScheduleType().getMeal().getEndMealBreak());
		    setMandatoryMealDiscount(new TimeOfDay(getWorkScheduleType().getMeal()
			    .getMandatoryMealDiscount().getMillis(), GregorianChronology
			    .getInstanceUTC()));
		    setMinimumMealBreakInterval(new TimeOfDay(getWorkScheduleType().getMeal()
			    .getMinimumMealBreakInterval().getMillis(), GregorianChronology
			    .getInstanceUTC()));
		}

	    }
	}

	public WorkScheduleTypeFactoryEditor(WorkScheduleType workScheduleType) {
	    setWorkScheduleType(workScheduleType);
	}

	public Object execute() {
	    DateTime now = new DateTime();
	    YearMonthDay firstDay = getNotClosedMonthFirstDay();
	    boolean changeDatesOrAcronym = false;
	    if (!getWorkScheduleType().getBeginValidDate().equals(getBeginValidDate())) {
		if (!getWorkScheduleType().getWorkSchedules().isEmpty()
			&& isWorkScheduleTypeBeenUsedBeforeBeginDate()) {
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

	    if (!getWorkScheduleType().equivalent(
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
		WorkScheduleType equivalentWorkScheduleType = null;// getEquivalentWorkScheduleType(getWorkScheduleType().getIdInternal());
		if (equivalentWorkScheduleType == null) {
		    if (!getWorkScheduleType().getWorkSchedules().isEmpty()
			    || getWorkScheduleType().getBeginValidDate().isBefore(firstDay)) {
			List<Schedule> scheduleToChangeDate = new ArrayList<Schedule>();
			List<WorkSchedule> workScheduleToChangeWorkScheduleType = new ArrayList<WorkSchedule>();

			for (WorkSchedule workSchedule : getWorkScheduleType().getWorkSchedules()) {
			    for (Schedule schedule : workSchedule.getSchedules()) {
				if (!schedule.getBeginDate().isBefore(firstDay)) {
				    if (!workScheduleToChangeWorkScheduleType.contains(workSchedule)) {
					workScheduleToChangeWorkScheduleType.add(workSchedule);
				    }
				} else if (schedule.getBeginDate().isBefore(firstDay)) {
				    if (!scheduleToChangeDate.contains(workSchedule)) {
					scheduleToChangeDate.add(schedule);
				    }
				}
			    }
			}
			if (scheduleToChangeDate.isEmpty()) {
			    // ninguém usa antes deste do firstday... basta
			    // alterar
			    editWorkScheduleType();
			} else {
			    getWorkScheduleType().setAcronym(
				    getNewAcronym(getWorkScheduleType().getAcronym()));
			    getWorkScheduleType().setEndValidDate(firstDay.minusDays(1));
			    getWorkScheduleType().setModifiedBy(getModifiedBy());
			    getWorkScheduleType().setLastModifiedDate(now);

			    WorkScheduleType newWorkScheluleType = getOrCreateWorkScheduleType();
			    if (newWorkScheluleType == null) {
				Chronology chronology = GregorianChronology.getInstanceUTC();
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
				TimeOfDay time = new TimeOfDay(
					ContinuousSchedule.normalContinuousWorkDayDuration.getMillis(),
					chronology);
				return new ActionMessage("error.invalidContinuousDuration", fmt
					.print(time));
			    }
			    for (WorkSchedule workSchedule : workScheduleToChangeWorkScheduleType) {
				workSchedule.setWorkScheduleType(newWorkScheluleType);
			    }

			    for (Schedule schedule : scheduleToChangeDate) {
				Schedule newSchedule = new Schedule(schedule.getAssiduousness(),
					schedule.getBeginDate(), schedule.getEndDate(), schedule
						.getException(), now, getModifiedBy());
				List<WorkSchedule> newWorkSchedules = new ArrayList<WorkSchedule>();
				for (WorkSchedule workSchedule : schedule.getWorkSchedules()) {
				    WorkSchedule newWorkSchedule = getWorkSchedule(newWorkScheluleType
					    .getIdInternal(), workSchedule.getWorkWeek(), workSchedule
					    .getPeriodicity());
				    if (newWorkSchedule == null) {
					newWorkSchedule = new WorkSchedule(newWorkScheluleType,
						workSchedule.getWorkWeek(), workSchedule
							.getPeriodicity());
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
			editWorkScheduleType();
		    }

		    return null;
		} else {
		    return new ActionMessage("error.existingEquivalentSchedule",
			    equivalentWorkScheduleType.getAcronym());
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
			    || (getEndValidDate() != null && !schedule.getEndDate().isBefore(
				    getEndValidDate()))) {
			return true;
		    }
		}
	    }
	    return false;
	}

	private static WorkSchedule getWorkSchedule(Integer workScheduleTypeId, WorkWeek workWeek,
		Periodicity periodicity) {
	    for (WorkSchedule workSchedule : RootDomainObject.getInstance().getWorkSchedules()) {
		if (workSchedule.getWorkScheduleType().getIdInternal().equals(workScheduleTypeId)
			&& workSchedule.getWorkWeek().equals(workWeek)
			&& workSchedule.getPeriodicity() == periodicity) {
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
	    for (WorkScheduleType workScheduleType : RootDomainObject.getInstance()
		    .getWorkScheduleTypes()) {
		if (workScheduleType.getAcronym().equalsIgnoreCase(acronym)) {
		    return true;
		}
	    }
	    return false;
	}

	private boolean alreadyExistsWorkScheduleTypeAcronymExceptThis(String acronym) {
	    for (WorkScheduleType workScheduleType : RootDomainObject.getInstance()
		    .getWorkScheduleTypes()) {
		if (workScheduleType.getAcronym().equalsIgnoreCase(acronym)
			&& !getWorkScheduleType().getIdInternal().equals(
				workScheduleType.getIdInternal())) {
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
	    getWorkScheduleType().setWorkTimeDuration(
		    getDuration(getBeginDayTime(), getEndDayTime(), getEndDayNextDay()));
	    getWorkScheduleType().setClockingTime(getBeginClockingTime());
	    getWorkScheduleType().setClockingTimeDuration(
		    getDuration(getBeginClockingTime(), getEndClockingTime(), getEndClockingNextDay()));

	    getWorkScheduleType().setNormalWorkPeriod(
		    editWorkPeriod(getWorkScheduleType().getNormalWorkPeriod(),
			    getBeginNormalWorkFirstPeriod(), getDuration(
				    getBeginNormalWorkFirstPeriod(), getEndNormalWorkFirstPeriod(),
				    getEndNormalWorkFirstPeriodNextDay()),
			    getBeginNormalWorkSecondPeriod(), getDuration(
				    getBeginNormalWorkSecondPeriod(), getEndNormalWorkSecondPeriod(),
				    getEndNormalWorkSecondPeriodNextDay())));

	    getWorkScheduleType().setFixedWorkPeriod(
		    editWorkPeriod(getWorkScheduleType().getFixedWorkPeriod(),
			    getBeginFixedWorkFirstPeriod(), getDuration(getBeginFixedWorkFirstPeriod(),
				    getEndFixedWorkFirstPeriod(), getEndFixedWorkFirstPeriodNextDay()),
			    getBeginFixedWorkSecondPeriod(), getDuration(
				    getBeginFixedWorkSecondPeriod(), getEndFixedWorkSecondPeriod(),
				    getEndFixedWorkSecondPeriodNextDay())));

	    getWorkScheduleType().setMeal(
		    editMeal(getWorkScheduleType().getMeal(), getMealBeginTime(), getMealEndTime(),
			    getDuration(getMinimumMealBreakInterval()),
			    getDuration(getMandatoryMealDiscount())));
	}

	private WorkPeriod editWorkPeriod(WorkPeriod workPeriod, TimeOfDay firstPeriod,
		Duration firstPeriodDuration, TimeOfDay secondPeriod, Duration secondPeriodDuration) {
	    if (workPeriod != null && !isWorkPeriodUsed(workPeriod)) {
		WorkPeriod equivalentWorkPeriod = getEquivalentWorkPeriod(firstPeriod,
			firstPeriodDuration, secondPeriod, secondPeriodDuration);
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
	    return getOrCreateWorkPeriod(firstPeriod, firstPeriodDuration, secondPeriod,
		    secondPeriodDuration);
	}

	private boolean isWorkPeriodUsed(WorkPeriod workPeriod) {
	    if ((workPeriod.getNormalWorkScheduleTypesCount() == 0 || (workPeriod
		    .getNormalWorkScheduleTypesCount() == 1 && workPeriod.getNormalWorkScheduleTypes()
		    .iterator().next().getIdInternal().equals(getWorkScheduleType().getIdInternal())))
		    && (workPeriod.getFixedWorkScheduleTypesCount() == 0 || (workPeriod
			    .getFixedWorkScheduleTypesCount() == 1 && workPeriod
			    .getFixedWorkScheduleTypes().iterator().next().getIdInternal().equals(
				    getWorkScheduleType().getIdInternal())))) {
		return false;
	    }
	    return true;
	}

	private Meal editMeal(Meal meal, TimeOfDay beginMeal, TimeOfDay endMeal,
		Duration minimumMealBreakInterval, Duration mandatoryMealDiscount) {
	    if (beginMeal != null) {
		if (minimumMealBreakInterval == null) {
		    minimumMealBreakInterval = Duration.ZERO;
		}
		if (mandatoryMealDiscount == null) {
		    mandatoryMealDiscount = Duration.ZERO;
		}
	    }
	    if (meal != null && meal.getWorkScheduleTypesCount() == 1) {
		Meal equivalentMeal = getEquivalentMeal(beginMeal, endMeal, minimumMealBreakInterval,
			mandatoryMealDiscount);
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

    public YearMonthDay getNotClosedMonthFirstDay() {
	Partial yearMonth = null;
	for (ClosedMonth closedMonth : RootDomainObject.getInstance().getClosedMonths()) {
	    if (yearMonth == null || closedMonth.getClosedYearMonth().isAfter(yearMonth)) {
		yearMonth = closedMonth.getClosedYearMonth();
	    }
	}
	return new YearMonthDay(yearMonth.get(DateTimeFieldType.year()), yearMonth.get(DateTimeFieldType
		.monthOfYear()), 1);
    }

    public boolean areEndDatesEqual(YearMonthDay endDate1, YearMonthDay endDate2) {
	return (endDate1 == null && endDate2 == null)
		|| (endDate1 != null && endDate2 != null && endDate1.equals(endDate2));
    }
}