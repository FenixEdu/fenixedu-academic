package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleClockingType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class WorkScheduleType extends WorkScheduleType_Base {

    public static Duration maximumContinuousWorkPeriod = new Duration(18000000); // 5

    // hours

    public WorkScheduleType() {
	super();
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
		workedfixedPeriod = workedfixedPeriod.plus(timeline.calculateFixedPeriod(AttributeType.FIXED_PERIOD_2));
	    }
	    return getFixedWorkPeriod().getWorkPeriodDuration().minus(workedfixedPeriod);
	}
	return Duration.ZERO;
    }

    public Duration checkMealDurationAccordingToRules(TimeInterval lunchBreak, boolean justification, Timeline timeline,
	    TimePoint firstClocking, TimePoint lastWorkTimePoint) {

	Duration lunchBreakDuration = lunchBreak.getDuration();
	if (definedMeal() && !getScheduleClockingType().equals(ScheduleClockingType.OLD_RIGID_CLOCKING)) {
	    if (lunchBreak.overlap(getMeal().getMealBreak()) == null) {
		if (getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
		    lunchBreakDuration = Duration.ZERO;
		} else {
		    return null;
		}
	    }
	    Duration discount = calculateNotWorkedMealDuration(timeline, lunchBreak.getStartTime(), lunchBreak.getEndTime(),
		    firstClocking, lastWorkTimePoint);
	    if (discount.isLongerThan(getMeal().getMandatoryMealDiscount())
		    || discount.isEqual(getMeal().getMandatoryMealDiscount())) {
		return Duration.ZERO;
	    }
	    // /////////remove in 2007
	    // if
	    // (!getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO))
	    // {
	    Interval overlaps = getMeal().getMealBreak().overlap(lunchBreak);
	    Duration overlapsDuration = Duration.ZERO;
	    if (overlaps == null) {
		if (!getMeal().getMealBreak().abuts(lunchBreak)) {
		    return Duration.ZERO;
		}
	    } else {
		overlapsDuration = overlaps.toDuration();
	    }
	    Duration mealDiscount = getMeal().calculateMealDiscount(overlapsDuration);
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
	    // }
	    // /////////////////////////
	    // Duration mealDiscount =
	    // getMeal().calculateMealDiscount(lunchBreakDuration);
	    // if (discount.isLongerThan(mealDiscount)) {
	    // return Duration.ZERO;
	    // }
	    // Duration finalDiscount = mealDiscount.minus(discount);
	    // if (!justification
	    // && getMeal().getMandatoryMealDiscount() != Duration.ZERO
	    // &&
	    // lunchBreakDuration.isShorterThan(getMeal().
	    // getMinimumMealBreakInterval())
	    // &&
	    // getMeal().getMandatoryMealDiscount().minus(finalDiscount).
	    // isShorterThan(
	    // getMeal().getMinimumMealBreakInterval())) {
	    // return null;
	    // }
	    // return finalDiscount;
	}

	return Duration.ZERO;
    }

    private Duration calculateNotWorkedMealDuration(Timeline timeline, LocalTime beginLunch, LocalTime endLunch,
	    TimePoint firstClocking, TimePoint lastWorkTimePoint) {
	LocalDate today = new LocalDate();
	Duration totalDuration = Duration.ZERO;
	Interval scheduleMealBreak = getMeal().getMealBreak().toInterval(today.toDateTimeAtStartOfDay());
	TimePoint previousVerifiedTimePoint = null;
	for (TimePoint timePoint : timeline.getTimePoints()) {
	    DateTime timePointDateTime = timePoint.getDateTime(today);
	    boolean isClosingAndNotOpeningWorkedPeriod = timeline.isClosingAndNotOpeningWorkedPeriod(timePoint);
	    if (scheduleMealBreak.contains(timePointDateTime)
		    && !timePoint.getTime().isEqual(beginLunch)
		    && (!timePoint.getTime().isEqual(endLunch) || timePoint.isAtSameTime(lastWorkTimePoint))
		    && (timeline.isOpeningAndNotClosingWorkedPeriod(timePoint) || isClosingAndNotOpeningWorkedPeriod || timePoint
			    .isAtSameTime(firstClocking))) {
		if (timePoint.isAtSameTime(firstClocking)) {
		    totalDuration = totalDuration.plus(new Duration(scheduleMealBreak.getStart(), timePointDateTime));
		} else {
		    if (isClosingAndNotOpeningWorkedPeriod) {
			TimePoint nextWorkedTimePoint = timeline.getNextWorkedPoint(timePoint);
			DateTime endDateTime = scheduleMealBreak.getEnd();
			if (nextWorkedTimePoint != null
				&& nextWorkedTimePoint.getDateTime(endDateTime.toLocalDate()).isBefore(endDateTime)) {
			    endDateTime = nextWorkedTimePoint.getDateTime(endDateTime.toLocalDate());
			}
			totalDuration = totalDuration.plus(new Duration(timePointDateTime, endDateTime));
			previousVerifiedTimePoint = timePoint;
		    } else { // isOpeningAndNotClosingWorkedPeriod
			TimePoint previousWorkedTimePoint = timeline.getPreviousWorkedPoint(timePoint);
			if (previousVerifiedTimePoint == null || !previousWorkedTimePoint.isAtSameTime(previousVerifiedTimePoint)) {
			    DateTime beginDateTime = scheduleMealBreak.getStart();
			    if (previousWorkedTimePoint != null
				    && previousWorkedTimePoint.getDateTime(beginDateTime.toLocalDate()).isAfter(beginDateTime)) {
				beginDateTime = previousWorkedTimePoint.getDateTime(beginDateTime.toLocalDate());
			    }
			    totalDuration = totalDuration.plus(new Duration(beginDateTime, timePointDateTime));

			    if (timePoint.isAtSameTime(lastWorkTimePoint)) {
				totalDuration = totalDuration.plus(new Duration(timePointDateTime, scheduleMealBreak.getEnd()));
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

    public LocalTime getWorkEndTime() {
	return getWorkTime().plus(getWorkTimeDuration().toPeriod());
    }

    public LocalTime getClockingEndTime() {
	return getClockingTime().plus(getClockingTimeDuration().toPeriod());
    }

    public boolean isWorkTimeNextDay() {
	DateTime now = LocalTime.MIDNIGHT.toDateTimeToday();
	Duration maxDuration = new Duration(getWorkTime().toDateTime(now).getMillis(), now.plusDays(1).getMillis());
	return (getWorkTimeDuration().compareTo(maxDuration) >= 0);
    }

    public boolean isClokingTimeNextDay() {
	DateTime now = LocalTime.MIDNIGHT.toDateTimeToday();
	Duration maxDuration = new Duration(getClockingTime().toDateTime(now).getMillis(), now.plusDays(1).getMillis());
	return (getClockingTimeDuration().compareTo(maxDuration) >= 0);
    }

    private static final LocalTime beginDefaultTimeOfDay = new LocalTime(3, 0, 0, 0);
    private static final LocalTime endDefaultTimeOfDay = new LocalTime(6, 0, 0, 0);
    public static Interval getDefaultWorkTime(LocalDate localDate) {
	return new Interval(localDate.toDateTime(beginDefaultTimeOfDay), localDate.toDateTime(endDefaultTimeOfDay).plusDays(1));
    }

    public boolean isValidWorkScheduleType() {
	if (getEndValidDate() == null) {
	    return !new LocalDate().isBefore(getBeginValidDate());
	}
	return new Interval(getBeginValidDate().toDateMidnight(), getEndValidDate().toDateMidnight()).contains(new LocalDate()
		.toDateMidnight());
    }

    public Duration getMaximumContinuousWorkPeriod() {
	// if (getMeal() == null ||
	// getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
	// return null;
	// }
	// return maximumContinuousWorkPeriod;
	return null;
    }

    public boolean equals(LocalDate beginValid, LocalDate endValid, LocalTime workTime, Duration workTimeDuration,
	    LocalTime clockingTime, Duration clockingTimeDuration, ScheduleClockingType scheduleClockingType,
	    LocalTime firstNormalPeriod, Duration firstNormalPeriodDuration, LocalTime secondNormalPeriod,
	    Duration secondNormalPeriodDuration, LocalTime firstFixedPeriod, Duration firstFixedPeriodDuration,
	    LocalTime secondFixedPeriod, Duration secondFixedPeriodDuration, LocalTime beginMeal, LocalTime endMeal,
	    Duration minium, Duration maxium) {

	if (getBeginValidDate().equals(beginValid)
		&& ((getEndValidDate() == null && endValid == null) || (getEndValidDate() != null && endValid != null && getEndValidDate()
			.equals(endValid)))
		&& equivalent(workTime, workTimeDuration, clockingTime, clockingTimeDuration, scheduleClockingType,
			firstNormalPeriod, firstNormalPeriodDuration, secondNormalPeriod, secondNormalPeriodDuration,
			firstFixedPeriod, firstFixedPeriodDuration, secondFixedPeriod, secondFixedPeriodDuration, beginMeal,
			endMeal, minium, maxium)) {
	    return true;
	}
	return false;
    }

    public boolean equivalent(LocalTime workTime, Duration workTimeDuration, LocalTime clockingTime,
	    Duration clockingTimeDuration, ScheduleClockingType scheduleClockingType, LocalTime firstNormalPeriod,
	    Duration firstNormalPeriodDuration, LocalTime secondNormalPeriod, Duration secondNormalPeriodDuration,
	    LocalTime firstFixedPeriod, Duration firstFixedPeriodDuration, LocalTime secondFixedPeriod,
	    Duration secondFixedPeriodDuration, LocalTime beginMeal, LocalTime endMeal, Duration minimum, Duration maximum) {
	if ((getWorkTime().equals(workTime) && getWorkTimeDuration().equals(workTimeDuration)
		&& getClockingTime().equals(clockingTime) && getClockingTimeDuration().equals(clockingTimeDuration))
		&& (getScheduleClockingType().equals(scheduleClockingType))
		&& ((!hasNormalWorkPeriod() && firstNormalPeriod == null) || (hasNormalWorkPeriod() && getNormalWorkPeriod()
			.equivalent(firstNormalPeriod, firstNormalPeriodDuration, secondNormalPeriod, secondNormalPeriodDuration)))
		&& ((!hasFixedWorkPeriod() && firstFixedPeriod == null) || (hasFixedWorkPeriod() && getFixedWorkPeriod()
			.equivalent(firstFixedPeriod, firstFixedPeriodDuration, secondFixedPeriod, secondFixedPeriodDuration)))
		&& ((!hasMeal() && beginMeal == null) || (hasMeal() && getMeal().equivalent(beginMeal, endMeal, minimum, maximum)))) {
	    return true;
	}
	return false;
    }

    public boolean equivalent(WorkScheduleType workScheduleType) {
	LocalTime firstFixedPeriod = null, secondFixedPeriod = null, beginMeal = null, endMeal = null;
	Duration firstFixedPeriodDuration = null, secondFixedPeriodDuration = null, minium = null, maxium = null;

	if (workScheduleType.hasFixedWorkPeriod()) {
	    firstFixedPeriod = workScheduleType.getFixedWorkPeriod().getFirstPeriod();
	    firstFixedPeriodDuration = workScheduleType.getFixedWorkPeriod().getFirstPeriodDuration();
	    if (workScheduleType.getFixedWorkPeriod().isSecondWorkPeriodDefined()) {
		secondFixedPeriod = workScheduleType.getFixedWorkPeriod().getSecondPeriod();
		secondFixedPeriodDuration = workScheduleType.getFixedWorkPeriod().getSecondPeriodDuration();
	    }
	}
	if (workScheduleType.hasMeal()) {
	    beginMeal = workScheduleType.getMeal().getBeginMealBreak();
	    endMeal = workScheduleType.getMeal().getEndMealBreak();
	    minium = workScheduleType.getMeal().getMinimumMealBreakInterval();
	    maxium = workScheduleType.getMeal().getMandatoryMealDiscount();
	}

	return equivalent(workScheduleType.getWorkTime(), workScheduleType.getWorkTimeDuration(), workScheduleType
		.getClockingTime(), workScheduleType.getClockingTimeDuration(), workScheduleType.getScheduleClockingType(),
		workScheduleType.getNormalWorkPeriod().getFirstPeriod(), workScheduleType.getNormalWorkPeriod()
			.getFirstPeriodDuration(), workScheduleType.getNormalWorkPeriod().getSecondPeriod(), workScheduleType
			.getNormalWorkPeriod().getSecondPeriodDuration(), firstFixedPeriod, firstFixedPeriodDuration,
		secondFixedPeriod, secondFixedPeriodDuration, beginMeal, endMeal, minium, maxium);
    }

    public boolean getIsEditable() {
	LocalDate now = new LocalDate();
	if ((getBeginValidDate() != null && getBeginValidDate().isBefore(now))
		&& (getEndValidDate() != null && getEndValidDate().isBefore(now))) {
	    return false;
	}
	return true;
    }

    public boolean isNocturnal() {
	LocalTime endTime = getNormalWorkPeriod().getEndSecondPeriod() != null ? getNormalWorkPeriod().getEndSecondPeriod()
		: getNormalWorkPeriod().getEndFirstPeriod();
	return endTime.isAfter(Assiduousness.defaultStartNightWorkDay) || endTime.isBefore(Assiduousness.defaultEndNightWorkDay);
    }

    public boolean canDoExtraWorkInWeekDays() {
	return true;
    }
}
