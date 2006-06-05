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
                System.out.println("almoco e' menor q o tempo dado para almoco");
                turnDayToAbsence(dailyBalance);
            } else {
                System.out.println("nwp: "
                        + (dailyBalance.getWorkedOnNormalWorkPeriod()).toPeriod().toString());
                System.out.println("saldo nwp antes: "
                        + getNormalWorkPeriod().getWorkPeriodDuration().minus(
                                (dailyBalance.getWorkedOnNormalWorkPeriod())).toPeriod().toString());
                System.out.println("almoco" + dailyBalance.getLunchBreak().toPeriod().toString());
                System.out.println("demorou mais de 15m a almocar");
                Duration mealDiscount = ((Meal) getMeal()).calculateMealDiscount(dailyBalance
                        .getLunchBreak());
                System.out.println("descontar " + mealDiscount.toPeriod().toString());
                // periodo normal menos desconto
                dailyBalance.setWorkedOnNormalWorkPeriod(dailyBalance.getWorkedOnNormalWorkPeriod()
                        .minus(mealDiscount));
                System.out.println("nwp: depois "
                        + (dailyBalance.getWorkedOnNormalWorkPeriod()).toPeriod().toString());
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
            System.out.println(dailyBalance.getFixedPeriodAbsence().toPeriod().toString());
        }
    }

    // TODO
    // Os funcionario so' podem trabalhar 5 horas seguidas
    // o tempo para alem dessas 5 horas nao e' contabilizado.
    // No caso de fazerem mais de 5 horas de manha o periodo da tarde nao deve ser contabilizado
    // Excepto Jornadas Continuas em q ha periodos de 6 horas com intervalo de 30 minutos (?)
    // public Duration checkNormalWorkPeriodAccordingToRules(Duration normalWorkPeriodWorked) {
    // System.out.println("normalworkperiod worked" +normalWorkPeriodWorked.toPeriod().toString());
    // if (normalWorkPeriodWorked.isLongerThan(DomainConstants.MAX_MORNING_WORK)) {
    // return DomainConstants.MAX_MORNING_WORK;
    // } else {
    // return normalWorkPeriodWorked;
    // }
    // }

    /*
     * ATTRIBUTE BUILDERS WITH DATA FROM THE PRESENTATION TODO check if this makes any sense in the new
     * arch
     */

    // // builds an Interval from start date and time and end date and time.
    // public static Interval createIntervalFromTimes(Integer startYear, Integer startMonth, Integer
    // startDay, Integer startHours, Integer startMinutes, Integer endHours,
    // Integer endMinutes, boolean nextDay) throws InvalidIntervalLimitsException {
    // DateTime startTime = new DateTime(startYear.intValue(), startMonth.intValue(),
    // startDay.intValue(), startHours.intValue(), startMinutes.intValue(), 0, 0);
    // DateTime endTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(),
    // endHours.intValue(), endMinutes.intValue(), 0, 0);
    // if (startTime.isBefore(endTime)) {
    // // next day
    // if (nextDay) {
    // endTime = endTime.plus(Period.days(1));
    // }
    // return new Interval(startTime, endTime);
    // } else {
    // // TODO throw exception the startTime and endTime are inconsistent...
    // throw new InvalidIntervalLimitsException("startTime and endTime are inconsistent...");
    // }
    // }
    // // builds an Interval from start date and time and end date and time.
    // public static TimeInterval createTimeIntervalFromTimes(Integer startYear, Integer startMonth,
    // Integer startDay, Integer startHours, Integer startMinutes, Integer endHours,
    // Integer endMinutes, boolean nextDay) throws InvalidIntervalLimitsException {
    //        
    // DateTime startTime = new DateTime(startYear.intValue(), startMonth.intValue(),
    // startDay.intValue(), startHours.intValue(), startMinutes.intValue(), 0, 0);
    // DateTime endTime = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(),
    // endHours.intValue(), endMinutes.intValue(), 0, 0);
    // if (startTime.isBefore(endTime)) {
    // // next day
    // if (nextDay) {
    // endTime = endTime.plus(Period.days(1));
    // }
    // return new Interval(startTime, endTime);
    // } else {
    // // TODO throw exception the startTime and endTime are inconsistent...
    // throw new InvalidIntervalLimitsException("startTime and endTime are inconsistent...");
    // }
    // }
    // // builds regular schedule/fixed period/meal break intervals from the data got from the
    // presentation
    // public static Interval createTimeIntervalPeriod(Integer startYear, Integer startMonth, Integer
    // startDay, Integer startHours, Integer startMinutes, Integer endHours,
    // Integer endMinutes, boolean nextDay, FenixDomainException specificException) throws
    // FenixDomainException {
    // Interval interval = null;
    // try {
    // interval = createIntervalFromTimes(startYear, startMonth, startDay, startHours, startMinutes,
    // endHours, endMinutes, nextDay);
    // } catch (InvalidIntervalLimitsException e) {
    // throw specificException;
    // }
    // return interval;
    // }
    // builds regular schedule/fixed period/meal break intervals from the data got from the presentation
    public static TimeInterval createTimeInterval(Integer startHours, Integer startMinutes,
            Integer endHours, Integer endMinutes, boolean nextDay, FenixDomainException specificException)
            throws FenixDomainException {
        TimeInterval timeInterval = null;
        TimeOfDay startTime = new TimeOfDay(startHours, startMinutes);
        TimeOfDay endTime = new TimeOfDay(endHours, endMinutes);
        if (startTime.isBefore(endTime)) {
            timeInterval = new TimeInterval(startTime, endTime, nextDay);
        } else {
            throw specificException;
        }
        return timeInterval;
    }

    // // builds and validates the workday
    // public static TimeInterval createWorkDay(Integer startHours, Integer startMinutes, Integer
    // endHours,
    // Integer endMinutes, boolean nextDay) throws FenixDomainException {
    // // expediente
    // TimeInterval workDay = null;
    // try {
    // workDay = createTimeInterval(startHours, startMinutes, endHours, endMinutes, nextDay, null);
    // // Se duracao do expediente for menor que a duracao do expediente minimo
    // // ou duracao do expediente for maior que a duracao do expediente maximo da' erro!
    // if (workDay.getDuration().isShorterThan(DomainConstants.DURATION_MINIMUM_WORKDAY)
    // || workDay.getDuration().isLongerThan(DomainConstants.DURATION_MAXIMUM_WORKDAY)) {
    // System.out.println("erro: workday ta fora dos limites.");
    // // TODO mudar msg
    // throw new WorkdayOutOfLegalBoundsException("workdayOutofBounds exception");
    // }
    // } catch (InvalidIntervalLimitsException e) {
    // throw new InvalidWorkdayIntervalException("Invalid WorkDay Interval Exception");
    // }
    // return workDay;
    // }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            Meal meal = getMeal();
            removeMeal();
            meal.delete();
            WorkPeriod normalWorkPeriod = getNormalWorkPeriod();
            removeNormalWorkPeriod();
            normalWorkPeriod.delete();
            WorkPeriod fixedWorkPeriod = getFixedWorkPeriod();
            removeFixedWorkPeriod();
            fixedWorkPeriod.delete();
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

    public TimeOfDay getEndClockingTime() {
        return getClockingTime().plus(getClockingTimeDuration().toPeriod());
    }

    public boolean isNextDay() {
        DateTime now = TimeOfDay.MIDNIGHT.toDateTimeToday();
        Duration maxDuration = new Duration(getWorkTime().toDateTime(now).getMillis(), now.plusDays(1)
                .getMillis());
        return (getWorkTimeDuration().compareTo(maxDuration) >= 0);
    }
}
