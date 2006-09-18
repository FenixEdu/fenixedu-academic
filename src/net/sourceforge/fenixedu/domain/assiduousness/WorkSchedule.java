package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateMidnight;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author velouria@velouria.org
 * 
 */
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

    public WorkDaySheet calculateWorkingPeriods(WorkDaySheet workDaySheet, YearMonthDay day,
            Timeline timeline, List<Leave> timeLeaves) {
        Duration firstWorkPeriod = Duration.ZERO;
        Duration lastWorkPeriod = Duration.ZERO;
        TimeInterval mealInterval = null;
        WorkScheduleType wsType = getWorkScheduleType();
        TimePoint firstWorkTimePoint = timeline.getFirstWorkTimePoint();
        TimePoint lastWorkTimePoint = timeline.getLastWorkTimePoint();
        TimeOfDay firstClockingDate = firstWorkTimePoint.getTime();
        TimeOfDay lastClockingDate = lastWorkTimePoint.getTime();
        if (wsType.definedMeal()) {
            mealInterval = timeline.calculateMealBreakInterval(wsType.getMeal().getMealBreak());
            if (mealInterval != null) {
                Duration lunchDiscount = wsType.checkMealDurationAccordingToRules(mealInterval,
                        justificationInMealBreak(timeLeaves), timeline, firstWorkTimePoint);
                if (lunchDiscount != null) {
                    firstWorkPeriod = timeline.calculateWorkPeriodDuration(new TimePoint(mealInterval
                            .getStartTime(), false, null), null, new TimePoint(getWorkScheduleType()
                            .getClockingTime(), AttributeType.NULL), new TimePoint(getWorkScheduleType()
                            .getClockingEndTime(), getWorkScheduleType().isClokingTimeNextDay(),
                            AttributeType.NULL), wsType.getMaximumContinuousWorkPeriod());

                    if (((WorkPeriod) wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
                        lastWorkPeriod = timeline
                                .calculateWorkPeriodDuration(null, new TimePoint(mealInterval
                                        .getEndTime(), false, null), new TimePoint(getWorkScheduleType()
                                        .getClockingTime(), AttributeType.NULL),
                                        new TimePoint(getWorkScheduleType().getClockingEndTime(),
                                                getWorkScheduleType().isClokingTimeNextDay(),
                                                AttributeType.NULL), wsType
                                                .getMaximumContinuousWorkPeriod());
                    }
                    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(timeline));
                    workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(
                            firstWorkPeriod.plus(lastWorkPeriod).minus(lunchDiscount),
                            getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration())
                            .toPeriod());
                } else {
                    workDaySheet.addNote("IRREG");
                    workDaySheet.setBalanceTime(Duration.ZERO.minus(
                            wsType.getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod());
                    if (wsType.getFixedWorkPeriod() != null) {
                        workDaySheet.setUnjustifiedTime(wsType.getFixedWorkPeriod()
                                .getWorkPeriodDuration());
                    }
                }
            } else { // o funcionario nao foi almocar so fez 1 periodo de trabalho
                Duration workPeriod = timeline.calculateWorkPeriodDuration(null, timeline
                        .getTimePoints().iterator().next(), new TimePoint(getWorkScheduleType()
                        .getClockingTime(), AttributeType.NULL), new TimePoint(getWorkScheduleType()
                        .getClockingEndTime(), getWorkScheduleType().isClokingTimeNextDay(),
                        AttributeType.NULL), wsType.getMaximumContinuousWorkPeriod());

                if (workPeriod.equals(Duration.ZERO)) {
                    workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(timeline));
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
                        if (lunchTime.contains(firstClockingDate, false)
                                && !workPeriod.isEqual(Duration.ZERO)) {
                            workPeriod = workPeriod.minus(new TimeInterval(firstClockingDate, lunchEnd,
                                    false).getDurationMillis());
                        }

                    } else if (firstClockingDate != null
                            && firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())) {
                        if (lastClockingDate != null
                                && (timeline.getLastWorkTimePoint().isNextDay()
                                        || lastClockingDate
                                                .isAfter(wsType.getMeal().getBeginMealBreak()) || lastClockingDate
                                        .isEqual(wsType.getMeal().getBeginMealBreak()))) {
                            if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(
                                    lastClockingDate, timeline.getLastWorkTimePoint().isNextDay())) {
                                // descontar periodo de tempo desde fim da refeicao menos periodo
                                // obrigatorio de refeicao ate' 'a ultima marcacao do funcionario
                                workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal()
                                        .getEndOfMealBreakMinusMealDiscount(), lastClockingDate, false))
                                        .getDurationMillis());
                            } else if (timeline.getLastWorkTimePoint().isNextDay()
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
                            // considera-se o periodo desde o inicio do intervalo de almoco + desconto
                            // obrigatorio de almoco
                            // se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a
                            // entrada
                            // ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)

                            TimeOfDay lunchEnd = wsType.getMeal().getLunchEnd();
                            TimeInterval lunchTime = new TimeInterval(wsType.getMeal()
                                    .getBeginMealBreak(), lunchEnd, false);

                            if (lunchTime.contains(firstClockingDate, false)) {
                                workPeriod = workPeriod.minus(new TimeInterval(firstClockingDate,
                                        lunchEnd, false).getDurationMillis());
                            }
                            // calcular o periodo fixo
                            workDaySheet.setUnjustifiedTime(wsType
                                    .calculateFixedPeriodDuration(timeline));
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
                                        .calculateFixedPeriodDuration(timeline));
                            } else {
                                if (firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())
                                        && new TimePoint(wsType.getMeal().getEndMealBreak(),
                                                (Attributes) null).isBefore(lastWorkTimePoint)) {
                                    workDaySheet.addNote("IRREG");
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
                                        workDaySheet.setUnjustifiedTime(wsType
                                                .calculateFixedPeriodDuration(timeline));
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
                                    .calculateFixedPeriodDuration(timeline));
                        }
                    }
                }
            }
        } else { // meal nao esta definida - so ha 1 periodo de trabalho
            Duration worked = timeline.calculateWorkPeriodDuration(null, timeline.getTimePoints()
                    .iterator().next(), new TimePoint(getWorkScheduleType().getClockingTime(),
                    AttributeType.NULL), new TimePoint(getWorkScheduleType().getClockingEndTime(),
                    getWorkScheduleType().isClokingTimeNextDay(), AttributeType.NULL), wsType
                    .getMaximumContinuousWorkPeriod());
            workDaySheet.setBalanceTime(subtractDurationsWithoutSeconds(worked,
                    getWorkScheduleType().getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod());
            workDaySheet.setUnjustifiedTime(wsType.calculateFixedPeriodDuration(timeline));

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
            if (leaveInterval.overlaps(mealInterval)) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        if (canBeDeleted()) {
            removeRootDomainObject();
            Periodicity periodicity = getPeriodicity();
            removePeriodicity();
            periodicity.delete();
            WorkScheduleType workScheduleType = getWorkScheduleType();
            removeWorkScheduleType();
            workScheduleType.delete();
            deleteDomainObject();
        }
    }

    public boolean canBeDeleted() {
        return !hasAnySchedules();
    }

}
