package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkScheduleType extends WorkScheduleType_Base {

    public static Duration maximumContinuousWorkPeriod = new Duration(18000000); // 5

    // hours

    public WorkScheduleType() {
	super();
    }

    public void update(String className, ScheduleClockingType scheduleClockingType,
	    YearMonthDay beginValidDate, YearMonthDay endValidDate, String acronym, TimeOfDay dayTime,
	    Duration dayTimeDuration, TimeOfDay clockingTime, Duration clockingTimeDuration,
	    WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod, Meal meal,
	    DateTime lastModifiedDate, Employee modifiedBy) {
	setOjbConcreteClass(className);
	setAcronym(acronym);
	setScheduleClockingType(scheduleClockingType);
	setWorkTime(dayTime);
	setWorkTimeDuration(dayTimeDuration);
	setClockingTime(clockingTime);
	setClockingTimeDuration(clockingTimeDuration);
	setNormalWorkPeriod(normalWorkPeriod);
	setFixedWorkPeriod(fixedWorkPeriod);
	setMeal(meal);
	setBeginValidDate(beginValidDate);
	setEndValidDate(endValidDate);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
    }

    public boolean definedFixedPeriod() {
	return (getFixedWorkPeriod() != null);
    }

    public boolean definedMeal() {
	return (getMeal() != null);
    }

    public Duration calculateFixedPeriodDuration(Timeline timeline) {
	if (definedFixedPeriod()) {
	    Duration workedfixedPeriod = timeline.calculateFixedPeriod(AttributeType.FIXED_PERIOD_1);
	    if ((getFixedWorkPeriod()).getSecondPeriodInterval() != null) {
		workedfixedPeriod = workedfixedPeriod.plus(timeline
			.calculateFixedPeriod(AttributeType.FIXED_PERIOD_2));
	    }
	    return getFixedWorkPeriod().getWorkPeriodDuration().minus(workedfixedPeriod);
	}
	return Duration.ZERO;
    }

    public Duration checkMealDurationAccordingToRules(TimeInterval lunchBreak, boolean justification,
	    Timeline timeline, TimePoint firstClocking, TimePoint lastWorkTimePoint) {

	Duration lunchBreakDuration = lunchBreak.getDuration();
	if (definedMeal() && !getScheduleClockingType().equals(ScheduleClockingType.OLD_RIGID_CLOCKING)) {
	    if (lunchBreak.overlap(getMeal().getMealBreak()) == null) {
		if (getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
		    lunchBreakDuration = Duration.ZERO;
		} else {
		    return null;
		}
	    }
	    Duration discount = calculateNotWorkedMealDuration(timeline, lunchBreak.getStartTime(),
		    lunchBreak.getEndTime(), firstClocking, lastWorkTimePoint);
	    if (discount.isLongerThan(getMeal().getMandatoryMealDiscount())
		    || discount.isEqual(getMeal().getMandatoryMealDiscount())) {
		return Duration.ZERO;
	    }
	    // /////////remove in 2007
	    if (!getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
		if (getMeal().getMealBreak().overlap(lunchBreak) == null) {
		    return Duration.ZERO;
		}
		Duration mealDiscount = getMeal().calculateMealDiscount(
			getMeal().getMealBreak().overlap(lunchBreak).toDuration());
		if (discount.isLongerThan(mealDiscount)) {
		    return Duration.ZERO;
		}
		Duration finalDiscount = mealDiscount.minus(discount);
		if (!justification
			&& getMeal().getMandatoryMealDiscount() != Duration.ZERO
			&& lunchBreakDuration.isShorterThan(getMeal().getMinimumMealBreakInterval())
			&& getMeal().getMandatoryMealDiscount().minus(finalDiscount).isShorterThan(
				getMeal().getMinimumMealBreakInterval())) {
		    return null;
		}
		return finalDiscount;
	    }
	    // /////////////////////////
	    Duration mealDiscount = getMeal().calculateMealDiscount(lunchBreakDuration);
	    if (discount.isLongerThan(mealDiscount)) {
		return Duration.ZERO;
	    }
	    Duration finalDiscount = mealDiscount.minus(discount);
	    if (!justification
		    && getMeal().getMandatoryMealDiscount() != Duration.ZERO
		    && lunchBreakDuration.isShorterThan(getMeal().getMinimumMealBreakInterval())
		    && getMeal().getMandatoryMealDiscount().minus(finalDiscount).isShorterThan(
			    getMeal().getMinimumMealBreakInterval())) {
		return null;
	    }
	    return finalDiscount;
	}

	return Duration.ZERO;
    }

    private Duration calculateNotWorkedMealDuration(Timeline timeline, TimeOfDay beginLunch,
	    TimeOfDay endLunch, TimePoint firstClocking, TimePoint lastWorkTimePoint) {
	Duration totalDuration = Duration.ZERO;
	Interval scheduleMealBreak = getMeal().getMealBreak().toInterval(
		new YearMonthDay().toDateTimeAtMidnight());
	TimePoint previousVerifiedTimePoint = null;
	for (TimePoint timePoint : timeline.getTimePoints()) {
	    DateTime timePointDateTime = timePoint.getDateTime(new YearMonthDay());
	    boolean isClosingAndNotOpeningWorkedPeriod = timeline
		    .isClosingAndNotOpeningWorkedPeriod(timePoint);
	    if (scheduleMealBreak.contains(timePointDateTime)
		    && !timePoint.getTime().isEqual(beginLunch)
		    && (!timePoint.getTime().isEqual(endLunch) || timePoint
			    .isAtSameTime(lastWorkTimePoint))
		    && (timeline.isOpeningAndNotClosingWorkedPeriod(timePoint)
			    || isClosingAndNotOpeningWorkedPeriod || timePoint
			    .isAtSameTime(firstClocking))) {
		if (timePoint.isAtSameTime(firstClocking)) {
		    totalDuration = totalDuration.plus(new Duration(scheduleMealBreak.getStart(),
			    timePointDateTime));
		} else {
		    if (isClosingAndNotOpeningWorkedPeriod) {
			TimePoint nextWorkedTimePoint = timeline.getNextWorkedPoint(timePoint);
			DateTime endDateTime = scheduleMealBreak.getEnd();
			if (nextWorkedTimePoint != null
				&& nextWorkedTimePoint.getDateTime(endDateTime.toYearMonthDay())
					.isBefore(endDateTime)) {
			    endDateTime = nextWorkedTimePoint.getDateTime(endDateTime.toYearMonthDay());
			}
			totalDuration = totalDuration.plus(new Duration(timePointDateTime, endDateTime));
			previousVerifiedTimePoint = timePoint;
		    } else { // isOpeningAndNotClosingWorkedPeriod
			TimePoint previousWorkedTimePoint = timeline.getPreviousWorkedPoint(timePoint);
			if (previousVerifiedTimePoint == null
				|| !previousWorkedTimePoint.isAtSameTime(previousVerifiedTimePoint)) {
			    DateTime beginDateTime = scheduleMealBreak.getStart();
			    if (previousWorkedTimePoint != null
				    && previousWorkedTimePoint.getDateTime(
					    beginDateTime.toYearMonthDay()).isAfter(beginDateTime)) {
				beginDateTime = previousWorkedTimePoint.getDateTime(beginDateTime
					.toYearMonthDay());
			    }
			    totalDuration = totalDuration.plus(new Duration(beginDateTime,
				    timePointDateTime));

			    if (timePoint.isAtSameTime(lastWorkTimePoint)) {
				totalDuration = totalDuration.plus(new Duration(timePointDateTime,
					scheduleMealBreak.getEnd()));
			    }
			}
		    }
		}
	    }
	}
	return totalDuration;
    }

    public void delete() {
	if (canBeDeleted()) {
	    removeRootDomainObject();
	    Meal meal = getMeal();
	    if (meal != null) {
		removeMeal();
		meal.delete();
	    }
	    WorkPeriod normalWorkPeriod = getNormalWorkPeriod();
	    removeNormalWorkPeriod();
	    normalWorkPeriod.delete();
	    WorkPeriod fixedWorkPeriod = getFixedWorkPeriod();
	    if (fixedWorkPeriod != null) {
		removeFixedWorkPeriod();
		fixedWorkPeriod.delete();
	    }
	    removeModifiedBy();
	    deleteDomainObject();
	}
    }

    public boolean canBeDeleted() {
	return !hasAnyWorkSchedules();
    }

    public TimeOfDay getWorkEndTime() {
	return getWorkTime().plus(getWorkTimeDuration().toPeriod());
    }

    public TimeOfDay getClockingEndTime() {
	return getClockingTime().plus(getClockingTimeDuration().toPeriod());
    }

    public boolean isWorkTimeNextDay() {
	DateTime now = TimeOfDay.MIDNIGHT.toDateTimeToday();
	Duration maxDuration = new Duration(getWorkTime().toDateTime(now).getMillis(), now.plusDays(1)
		.getMillis());
	return (getWorkTimeDuration().compareTo(maxDuration) >= 0);
    }

    public boolean isClokingTimeNextDay() {
	DateTime now = TimeOfDay.MIDNIGHT.toDateTimeToday();
	Duration maxDuration = new Duration(getClockingTime().toDateTime(now).getMillis(), now.plusDays(
		1).getMillis());
	return (getClockingTimeDuration().compareTo(maxDuration) >= 0);
    }

    public static Interval getDefaultWorkTime(YearMonthDay workDay) {
	return new Interval(workDay.toDateTime(new TimeOfDay(3, 0, 0, 0)), workDay.toDateTime(
		new TimeOfDay(6, 0, 0, 0)).plusDays(1));
    }

    public boolean isValidWorkScheduleType() {
	if (getEndValidDate() == null) {
	    return !new YearMonthDay().isBefore(getBeginValidDate());
	}
	return new Interval(getBeginValidDate().toDateMidnight(), getEndValidDate().toDateMidnight())
		.contains(new YearMonthDay().toDateMidnight());
    }

    public Duration getMaximumContinuousWorkPeriod() {
	// if (getMeal() == null ||
	// getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
	// return null;
	// }
	// return maximumContinuousWorkPeriod;
	return null;
    }

    public boolean equals(YearMonthDay beginValid, YearMonthDay endValid, TimeOfDay workTime,
	    Duration workTimeDuration, TimeOfDay clockingTime, Duration clockingTimeDuration,
	    ScheduleClockingType scheduleClockingType, TimeOfDay firstNormalPeriod,
	    Duration firstNormalPeriodDuration, TimeOfDay secondNormalPeriod,
	    Duration secondNormalPeriodDuration, TimeOfDay firstFixedPeriod,
	    Duration firstFixedPeriodDuration, TimeOfDay secondFixedPeriod,
	    Duration secondFixedPeriodDuration, TimeOfDay beginMeal, TimeOfDay endMeal, Duration minium,
	    Duration maxium) {

	if (getBeginValidDate().equals(beginValid)
		&& ((getEndValidDate() == null && endValid == null) || (getEndValidDate() != null
			&& endValid != null && getEndValidDate().equals(endValid)))
		&& equivalent(workTime, workTimeDuration, clockingTime, clockingTimeDuration,
			scheduleClockingType, firstNormalPeriod, firstNormalPeriodDuration,
			secondNormalPeriod, secondNormalPeriodDuration, firstFixedPeriod,
			firstFixedPeriodDuration, secondFixedPeriod, secondFixedPeriodDuration,
			beginMeal, endMeal, minium, maxium)) {
	    return true;
	}
	return false;
    }

    public boolean equivalent(TimeOfDay workTime, Duration workTimeDuration, TimeOfDay clockingTime,
	    Duration clockingTimeDuration, ScheduleClockingType scheduleClockingType,
	    TimeOfDay firstNormalPeriod, Duration firstNormalPeriodDuration,
	    TimeOfDay secondNormalPeriod, Duration secondNormalPeriodDuration,
	    TimeOfDay firstFixedPeriod, Duration firstFixedPeriodDuration, TimeOfDay secondFixedPeriod,
	    Duration secondFixedPeriodDuration, TimeOfDay beginMeal, TimeOfDay endMeal,
	    Duration minimum, Duration maximum) {
	if ((getWorkTime().equals(workTime) && getWorkTimeDuration().equals(workTimeDuration)
		&& getClockingTime().equals(clockingTime) && getClockingTimeDuration().equals(
		clockingTimeDuration))
		&& (getScheduleClockingType().equals(scheduleClockingType))
		&& ((!hasNormalWorkPeriod() && firstNormalPeriod == null) || (hasNormalWorkPeriod() && getNormalWorkPeriod()
			.equivalent(firstNormalPeriod, firstNormalPeriodDuration, secondNormalPeriod,
				secondNormalPeriodDuration)))
		&& ((!hasFixedWorkPeriod() && firstFixedPeriod == null) || (hasFixedWorkPeriod() && getFixedWorkPeriod()
			.equivalent(firstFixedPeriod, firstFixedPeriodDuration, secondFixedPeriod,
				secondFixedPeriodDuration)))
		&& ((!hasMeal() && beginMeal == null) || (hasMeal() && getMeal().equivalent(beginMeal,
			endMeal, minimum, maximum)))) {
	    return true;
	}
	return false;
    }

    public boolean equivalent(WorkScheduleType workScheduleType) {
	TimeOfDay firstFixedPeriod = null, secondFixedPeriod = null, beginMeal = null, endMeal = null;
	Duration firstFixedPeriodDuration = null, secondFixedPeriodDuration = null, minium = null, maxium = null;

	if (workScheduleType.hasFixedWorkPeriod()) {
	    firstFixedPeriod = workScheduleType.getFixedWorkPeriod().getFirstPeriod();
	    firstFixedPeriodDuration = workScheduleType.getFixedWorkPeriod().getFirstPeriodDuration();
	    if (workScheduleType.getFixedWorkPeriod().isSecondWorkPeriodDefined()) {
		secondFixedPeriod = workScheduleType.getFixedWorkPeriod().getSecondPeriod();
		secondFixedPeriodDuration = workScheduleType.getFixedWorkPeriod()
			.getSecondPeriodDuration();
	    }
	}
	if (workScheduleType.hasMeal()) {
	    beginMeal = workScheduleType.getMeal().getBeginMealBreak();
	    endMeal = workScheduleType.getMeal().getEndMealBreak();
	    minium = workScheduleType.getMeal().getMinimumMealBreakInterval();
	    maxium = workScheduleType.getMeal().getMandatoryMealDiscount();
	}

	return equivalent(workScheduleType.getWorkTime(), workScheduleType.getWorkTimeDuration(),
		workScheduleType.getClockingTime(), workScheduleType.getClockingTimeDuration(),
		workScheduleType.getScheduleClockingType(), workScheduleType.getNormalWorkPeriod()
			.getFirstPeriod(), workScheduleType.getNormalWorkPeriod()
			.getFirstPeriodDuration(), workScheduleType.getNormalWorkPeriod()
			.getSecondPeriod(), workScheduleType.getNormalWorkPeriod()
			.getSecondPeriodDuration(), firstFixedPeriod, firstFixedPeriodDuration,
		secondFixedPeriod, secondFixedPeriodDuration, beginMeal, endMeal, minium, maxium);
    }

    public boolean getIsEditable() {
	YearMonthDay now = new YearMonthDay();
	if ((getBeginValidDate() != null && getBeginValidDate().isBefore(now))
		&& (getEndValidDate() != null && getEndValidDate().isBefore(now))) {
	    return false;
	}
	return true;
    }
}
