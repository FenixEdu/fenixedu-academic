package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkScheduleType extends WorkScheduleType_Base {

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

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        Attributes attributes = new Attributes(AttributeType.NORMAL_WORK_PERIOD_1);
        if ((getNormalWorkPeriod()).getSecondPeriodInterval() != null) {
            attributes.addAttribute(AttributeType.NORMAL_WORK_PERIOD_2);
        }
        if (definedFixedPeriod()) {
            attributes.addAttribute(AttributeType.FIXED_PERIOD_1);
            if ((getFixedWorkPeriod()).getSecondPeriodInterval() != null) {
                attributes.addAttribute(AttributeType.FIXED_PERIOD_2);
            }
        }
        if (((Meal) getMeal()).definedMealBreak()) {
            attributes.addAttribute(AttributeType.MEAL);
        }
        return attributes;
    }

    // Checks if the Fixed Period is defined. There are schedules that don't have a fixed period
    public boolean definedFixedPeriod() {
        return (getFixedWorkPeriod() != null);
    }

    // Checks if the Fixed Period is defined. Some schedules that don't have a meal period
    public boolean definedMeal() {
        return (getMeal() != null);
    }

    public void calculateFixedPeriodDuration(DailyBalance dailyBalance, Timeline timeline) {
        if (definedFixedPeriod()) {
            dailyBalance.setFixedPeriodAbsence(timeline
                    .calculateFixedPeriod(AttributeType.FIXED_PERIOD_1));
            if ((getFixedWorkPeriod()).getSecondPeriodInterval() != null) {
                Duration fixedPeriod2Duration = timeline
                        .calculateFixedPeriod(AttributeType.FIXED_PERIOD_2);
                dailyBalance.setFixedPeriodAbsence(dailyBalance.getFixedPeriodAbsence().plus(
                        fixedPeriod2Duration));
            }
            dailyBalance.setFixedPeriodAbsence(getFixedWorkPeriod().getWorkPeriodDuration().minus(
                    dailyBalance.getFixedPeriodAbsence()));
        }
    }

    public void checkMealDurationAccordingToRules(DailyBalance dailyBalance) {
        // According to Nota Informativa 55/03
        // if mealDuration is shorter than 15 minutes the whole day is subtracted from the normal work
        // period
        if (definedMeal()) {
            if (dailyBalance.getLunchBreak().isShorterThan(getMeal().getMinimumMealBreakInterval())) {
                // NormalWorkPeriodBalance will be - totalNormalWorkPeriodDuration
                turnDayToAbsence(dailyBalance);
            } else {
                Duration mealDiscount = ((Meal) getMeal()).calculateMealDiscount(dailyBalance
                        .getLunchBreak());
                dailyBalance.setWorkedOnNormalWorkPeriod(dailyBalance.getWorkedOnNormalWorkPeriod()
                        .minus(mealDiscount));
            }
        }
    }

    // sets the working day to an absence day
    public void turnDayToAbsence(DailyBalance dailyBalance) {
        dailyBalance.setWorkedOnNormalWorkPeriod(Duration.ZERO);
        if (definedFixedPeriod()) {
            // FixedPeriod absence will be the whole fixed period duration
            dailyBalance.setFixedPeriodAbsence(((WorkPeriod) this.getFixedWorkPeriod())
                    .getWorkPeriodDuration());
        }
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
}
