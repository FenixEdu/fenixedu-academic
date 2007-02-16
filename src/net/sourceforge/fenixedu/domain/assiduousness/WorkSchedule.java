package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkScheduleDaySheet;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class WorkSchedule extends WorkSchedule_Base {

    public WorkSchedule(WorkScheduleType workScheduleType, WorkWeek workWeek, Periodicity periodicity) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWorkWeek(workWeek);
        setWorkScheduleType(workScheduleType);
        setPeriodicity(periodicity);
    }

    public boolean isDefinedInDate(YearMonthDay date, int weekNumber, int maxWorkWeek) {
        return (getWorkWeek().contains(date.toDateTimeAtMidnight()) && getPeriodicity().occur(
                weekNumber, maxWorkWeek));
    }

    public WorkDaySheet calculateWorkingPeriods(WorkDaySheet workDaySheet, List<Leave> timeLeaves) {
        Duration firstWorkPeriod = Duration.ZERO;
        Duration lastWorkPeriod = Duration.ZERO;
        TimeInterval mealInterval = null;
        WorkScheduleType wsType = getWorkScheduleType();
        TimePoint firstWorkTimePoint = workDaySheet.getTimeline().getFirstWorkTimePoint();
        TimePoint lastWorkTimePoint = workDaySheet.getTimeline().getLastWorkTimePoint();
        TimeOfDay firstClockingDate = firstWorkTimePoint.getTime();
        TimeOfDay lastClockingDate = lastWorkTimePoint.getTime();
        if (wsType.definedMeal()) {
            mealInterval = workDaySheet.getTimeline().calculateMealBreakInterval(
                    wsType.getMeal().getMealBreak());
            if (mealInterval != null) {
                Duration lunchDiscount = wsType.checkMealDurationAccordingToRules(mealInterval,
                        justificationInMealBreak(timeLeaves), workDaySheet.getTimeline(),
                        firstWorkTimePoint, lastWorkTimePoint);
                if (lunchDiscount != null) {
                    firstWorkPeriod = workDaySheet.getTimeline().calculateWorkPeriodDuration(
                            new TimePoint(mealInterval.getStartTime(), false, null),
                            null,
                            new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
                            new TimePoint(getWorkScheduleType().getClockingEndTime(),
                                    getWorkScheduleType().isClokingTimeNextDay(), AttributeType.NULL),
                            wsType.getMaximumContinuousWorkPeriod(),wsType);

                    if (((WorkPeriod) wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
                        lastWorkPeriod = workDaySheet.getTimeline()
                                .calculateWorkPeriodDuration(
                                        null,
                                        new TimePoint(mealInterval.getEndTime(), false, null),
                                        new TimePoint(getWorkScheduleType().getClockingTime(),
                                                AttributeType.NULL),
                                        new TimePoint(getWorkScheduleType().getClockingEndTime(),
                                                getWorkScheduleType().isClokingTimeNextDay(),
                                                AttributeType.NULL),
                                        wsType.getMaximumContinuousWorkPeriod(),wsType);
                    }
                    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet
                            .getTimeline()));
                    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(
                            firstWorkPeriod.plus(lastWorkPeriod).minus(lunchDiscount),
                            getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                            .toPeriod());
                } else {
                    workDaySheet.setIrregularDay(true);
                    workDaySheet.setBalanceTime(Duration.ZERO.minus(
                            wsType.getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod());
                    if (wsType.getFixedWorkPeriod() != null) {
                        workDaySheet.setUnjustifiedTime(wsType.getFixedWorkPeriod()
                                .getWorkPeriodDuration());
                    }
                }
            } else { // o funcionario nao foi almocar so fez 1 periodo de trabalho
                Duration workPeriod = workDaySheet.getTimeline().calculateWorkPeriodDuration(
                        null,
                        workDaySheet.getTimeline().getTimePoints().iterator().next(),
                        new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
                        new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
                                .isClokingTimeNextDay(), AttributeType.NULL),
                        wsType.getMaximumContinuousWorkPeriod(),wsType);

                if (workPeriod.equals(Duration.ZERO)) {
                    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet
                            .getTimeline()));
                    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod,
                            getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                            .toPeriod());
                } else if (wsType.getMeal().getMinimumMealBreakInterval().isEqual(Duration.ZERO)) {
                    // caso dos horarios de isencao de horario
                    if (firstClockingDate != null
                            && wsType.getMeal().getMealBreak().contains(firstClockingDate, false)) {
                        // funcionario entrou no intervalo de almoco
                        // considera-se o periodo desde o inicio do intervalo de almoco + desconto
                        // obrigatorio de almoco
                        // se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a entrada
                        // ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)
                        TimeOfDay lunchEnd = wsType.getMeal().getLunchEnd();
                        TimeInterval lunchTime = new TimeInterval(wsType.getMeal().getBeginMealBreak(),
                                lunchEnd, false);

                        Duration lunch = Duration.ZERO;
                        if (lunchTime.contains(firstClockingDate, false)
                                && !workPeriod.isEqual(Duration.ZERO)) {

                            lunch = new TimeInterval(firstClockingDate, lunchEnd, false).getDuration();
                        }

                        // pode ter de descontar tb no final
                        if (lastClockingDate != null
                                && wsType.getMeal().getMealBreak().contains(lastClockingDate, false)) {
                            lunchTime = wsType.getMeal().getEndOfMealBreakMinusDiscountInterval();
                            if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(
                                    lastClockingDate,
                                    workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay())) {
                                lunch = lunch.plus(new TimeInterval(wsType.getMeal()
                                        .getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false)
                                        .getDuration());
                            } else {
                                // já fez mais de 1 hora
                                lunch = Duration.ZERO;
                            }

                        }
                        if (!wsType.getMeal().getMandatoryMealDiscount().isShorterThan(lunch)) {
                            workPeriod = workPeriod.minus(lunch);
                        }

                    } else if (firstClockingDate != null
                            && firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())) {
                        if (lastClockingDate != null
                                && (workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay()
                                        || lastClockingDate
                                                .isAfter(wsType.getMeal().getBeginMealBreak()) || lastClockingDate
                                        .isEqual(wsType.getMeal().getBeginMealBreak()))) {
                            if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(
                                    lastClockingDate,
                                    workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay())) {
                                // descontar periodo de tempo desde fim da refeicao menos periodo
                                // obrigatorio de refeicao ate' 'a ultima marcacao do funcionario
                                workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal()
                                        .getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false))
                                        .getDurationMillis());
                            } else if (workDaySheet.getTimeline().getLastWorkTimePoint().isNextDay()
                                    || lastClockingDate.isAfter(wsType.getMeal().getEndMealBreak())) {
                                workPeriod = workPeriod.minus(wsType.getMeal()
                                        .getMandatoryMealDiscount());
                            }
                        }
                    }
                    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod,
                            getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                            .toPeriod());
                } else { // caso dos horarios em q o minimo de intervalo de almoco e' tipicamente de
                    // 15minutos
                    if (firstClockingDate != null) {
                        if (wsType.getMeal().getMealBreak().contains(firstClockingDate, false)) {
                            // funcionario entrou no intervalo de almoco
                            Duration lunchDuration = new Duration(wsType.getMeal().getBeginMealBreak()
                                    .toDateTimeToday(), firstClockingDate.toDateTimeToday());
                            if (lastClockingDate != null
                                    && wsType.getMeal().getMealBreak().contains(lastClockingDate, false)) {
                                lunchDuration = lunchDuration.plus(new Duration(lastClockingDate
                                        .toDateTimeToday(), wsType.getMeal().getEndMealBreak()
                                        .toDateTimeToday()));
                            }

                            if (lunchDuration.isShorterThan(wsType.getMeal().getMandatoryMealDiscount())) {
                                workPeriod = workPeriod.minus(wsType.getMeal()
                                        .getMandatoryMealDiscount().minus(lunchDuration));
                            }
                            workDaySheet.setUnjustifiedTime(wsType
                                    .calculateFixedPeriodDuration(workDaySheet.getTimeline()));
                            workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod,
                                    getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                                    .toPeriod());

                            // primeiro clocking e' antes do periodo de almoco;
                            // ver se o ultimo clocking e' depois do periodo de almoco
                        } else if (lastClockingDate != null
                                && (!wsType.getMeal().getMealBreak().contains(lastClockingDate, false))) {
                            if (justificationInMealBreak(timeLeaves)) {
                                workPeriod = workPeriod.minus(wsType.getMeal()
                                        .getMandatoryMealDiscount());
                                workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(
                                        workPeriod,
                                        getWorkScheduleType().getNormalWorkPeriod()
                                                .getWorkPeriodDuration()).toPeriod());
                                workDaySheet.setUnjustifiedTime(wsType
                                        .calculateFixedPeriodDuration(workDaySheet.getTimeline()));
                            } else {
                                if (firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())
                                        && new TimePoint(wsType.getMeal().getEndMealBreak(),
                                                (Attributes) null).isBefore(lastWorkTimePoint)) {
                                    workDaySheet.setIrregularDay(true);
                                    workDaySheet.setBalanceTime(Duration.ZERO.minus(
                                            wsType.getNormalWorkPeriod().getWorkPeriodDuration())
                                            .toPeriod());
                                    if (wsType.getFixedWorkPeriod() != null) {
                                        workDaySheet.setUnjustifiedTime(wsType.getFixedWorkPeriod()
                                                .getWorkPeriodDuration());
                                    }
                                } else {
                                    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(
                                            workPeriod,
                                            getWorkScheduleType().getNormalWorkPeriod()
                                                    .getWorkPeriodDuration()).toPeriod());
                                    if (wsType.getFixedWorkPeriod() != null) {
                                        workDaySheet
                                                .setUnjustifiedTime(wsType
                                                        .calculateFixedPeriodDuration(workDaySheet
                                                                .getTimeline()));
                                    }
                                }
                            }
                        } else {
                            // ver se e' dentro do final do intervalo de almoco menos o desconto
                            // obrigatorio
                            if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(
                                    lastClockingDate, false)) {
                                // descontar periodo de tempo desde fim da refeicao menos periodo
                                // obrigatorio de refeicao ate' 'a ultima marcacao do funcionario
                                workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal()
                                        .getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false))
                                        .getDurationMillis());
                            }
                            workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(workPeriod,
                                    getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                                    .toPeriod());
                            workDaySheet.setUnjustifiedTime(wsType
                                    .calculateFixedPeriodDuration(workDaySheet.getTimeline()));
                        }
                    }
                }
            }
        } else { // meal nao esta definida - so ha 1 periodo de trabalho
            Duration worked = workDaySheet.getTimeline().calculateWorkPeriodDuration(
                    null,
                    workDaySheet.getTimeline().getTimePoints().iterator().next(),
                    new TimePoint(getWorkScheduleType().getClockingTime(), AttributeType.NULL),
                    new TimePoint(getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
                            .isClokingTimeNextDay(), AttributeType.NULL),
                    wsType.getMaximumContinuousWorkPeriod(),wsType);
            workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(worked,
                    getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod());
            workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(workDaySheet
                    .getTimeline()));

        }
        return workDaySheet;
    }

    public Duration subtractDurationsWithoutSeconds(Duration firstDuration, Duration secondDuration) {
        Period normalWorkedPeriod = firstDuration.toPeriod();
        normalWorkedPeriod = normalWorkedPeriod.minusSeconds(normalWorkedPeriod.getSeconds());
        return normalWorkedPeriod.toDurationFrom(new DateMidnight()).minus(secondDuration);
    }

    private boolean justificationInMealBreak(List<Leave> timeLeaves) {
        for (Leave leave : timeLeaves) {
            Interval leaveInterval = new Interval(leave.getDate().toTimeOfDay().toDateTimeToday(), leave
                    .getEndDate().toTimeOfDay().toDateTimeToday());
            Interval mealInterval = new Interval(getWorkScheduleType().getMeal().getBeginMealBreak()
                    .toDateTimeToday(), getWorkScheduleType().getMeal().getEndMealBreak()
                    .toDateTimeToday());
            if (leaveInterval.overlaps(mealInterval) || leaveInterval.abuts(mealInterval)) {
                return true;
            }
        }
        return false;
    }

    public void setWorkScheduleDays(HashMap<String, WorkScheduleDaySheet> workScheduleDays,
            ResourceBundle bundle) {
        for (WeekDay weekDay : getWorkWeek().getDays()) {
            WorkScheduleDaySheet workScheduleDaySheet = new WorkScheduleDaySheet();
            workScheduleDaySheet.setSchedule(getWorkScheduleType().getAcronym());
            workScheduleDaySheet.setWeekDay(bundle.getString(weekDay.toString() + "_ACRONYM"));
            workScheduleDaySheet.setWorkSchedule(this);
            workScheduleDays.put(weekDay.toString(), workScheduleDaySheet);
        }
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            Periodicity periodicity = getPeriodicity();
            periodicity.delete();
            removePeriodicity();
            WorkScheduleType workScheduleType = getWorkScheduleType();
            workScheduleType.delete();
            removeWorkScheduleType();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnySchedules();
    }

    // if returns false the clocking belongs to the clocking date
    // if returns true it may belong to the clocking date or the day before
    public static boolean overlapsSchedule(DateTime clocking,
            HashMap<YearMonthDay, WorkSchedule> workScheduleMap) {
        WorkSchedule thisDaySchedule = workScheduleMap.get(clocking.toYearMonthDay());
        WorkSchedule dayBeforeSchedule = workScheduleMap.get(clocking.toYearMonthDay().minusDays(1));

        Interval thisDayWorkTimeInterval = WorkScheduleType
                .getDefaultWorkTime(clocking.toYearMonthDay());
        if (thisDaySchedule != null) {
            DateTime beginThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkTime());
            DateTime endThisDayWorkTime = clocking.toYearMonthDay().toDateTime(
                    thisDaySchedule.getWorkScheduleType().getWorkEndTime());
            if (thisDaySchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endThisDayWorkTime = endThisDayWorkTime.plusDays(1);
            }
            thisDayWorkTimeInterval = new Interval(beginThisDayWorkTime, endThisDayWorkTime);
        }
        Interval dayBeforeWorkTimeInterval = WorkScheduleType.getDefaultWorkTime(clocking
                .toYearMonthDay().minusDays(1));
        if (dayBeforeSchedule != null) {
            DateTime beginDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkTime()).minusDays(1);
            DateTime endDayBeforeWorkTime = clocking.toYearMonthDay().toDateTime(
                    dayBeforeSchedule.getWorkScheduleType().getWorkEndTime()).minusDays(1);
            if (dayBeforeSchedule.getWorkScheduleType().isWorkTimeNextDay()) {
                endDayBeforeWorkTime = endDayBeforeWorkTime.plusDays(1);
            }

            dayBeforeWorkTimeInterval = new Interval(beginDayBeforeWorkTime, endDayBeforeWorkTime);
        }
        Interval overlapResult = thisDayWorkTimeInterval.overlap(dayBeforeWorkTimeInterval);
        if (overlapResult == null) {
            Interval gapResult = dayBeforeWorkTimeInterval.gap(thisDayWorkTimeInterval);
            if (gapResult != null) {
                if (!gapResult.contains(clocking)) {
                    return dayBeforeWorkTimeInterval.contains(clocking);
                }
            } else {
                return dayBeforeWorkTimeInterval.contains(clocking);
            }
        } else if (!overlapResult.contains(clocking) && clocking.isAfter(overlapResult.getStart())) {
            return false;
        }
        return true;
    }

}
