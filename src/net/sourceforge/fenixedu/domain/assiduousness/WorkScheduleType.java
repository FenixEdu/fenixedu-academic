package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkScheduleType extends WorkScheduleType_Base {

    public static Duration maximumContinuousWorkPeriod = new Duration(18000000); // 5 hours

    public void update(String className, YearMonthDay beginValidDate, YearMonthDay endValidDate,
            String acronym, TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
            Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod,
            Meal meal, DateTime lastModifiedDate, Employee modifiedBy) {
        setOjbConcreteClass(className);
        setAcronym(acronym);
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

    public Duration checkMealDurationAccordingToRules(Duration lunchBreak) {
        if (definedMeal()) {
            if (lunchBreak.isShorterThan(getMeal().getMinimumMealBreakInterval())) {
                return null;
            } else {
                return ((Meal) getMeal()).calculateMealDiscount(lunchBreak);
            }
        }
        return Duration.ZERO;
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

    public Duration getMaximumContinuousWorkPeriod() {
//        if (getMeal() == null || getMeal().getMinimumMealBreakInterval().equals(Duration.ZERO)) {
//            return null;
//        }
//        return maximumContinuousWorkPeriod;
        return null;
    }
}
