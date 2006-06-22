package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
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

    // Returns true if the WorkSchedule
    // Return true if definedInterval contains the date and the date's day of
    // week is in WorkWeek
    public boolean isDefinedInDate(YearMonthDay date, int weekNumber, int maxWorkWeek) {
        DateTime dateAtMidnight = date.toDateTimeAtMidnight();
        if (getWorkWeek().contains(dateAtMidnight) && getPeriodicity().occur(weekNumber, maxWorkWeek)) {
            return true;
        } else {
            return false;
        }
    }

    // TODO ver se o funcionario trabalhou dentro dos periodos
    public DailyBalance calculateWorkingPeriods(YearMonthDay day, Timeline timeline,
            List<Leave> timeLeaves) {
        DailyBalance dailyBalance = new DailyBalance(day, this);
        Duration firstWorkPeriod = Duration.ZERO;
        Duration lastWorkPeriod = Duration.ZERO;
        TimeInterval mealInterval = null;
        WorkScheduleType wsType = getWorkScheduleType();
        TimeOfDay firstClockingDate = timeline.getFirstWorkTimePoint().getTime();
        TimeOfDay lastClockingDate = timeline.getLastWorkTimePoint().getTime();

        if (wsType.definedMeal()) { // o horario tem um intervalo de refeicao definido

            // calcular qd e' q funcionario foi almocar
            mealInterval = timeline.calculateMealBreakInterval(wsType.getMeal().getMealBreak());

            if (mealInterval != null) {
                // funcionario fez intervalo para almoco => ha 2 periodos de
                // trabalho
                dailyBalance.setLunchBreak(mealInterval.getDuration());
                // actualiza almoco no dailyBalance calcula primeiro periodo de trabalho do horario
                // normal: deste a entrada ate' 'a saida para o almoco

                firstWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesToTime(
                        new TimePoint(mealInterval.getStartTime(), false, null),
                        DomainConstants.WORKED_ATTRIBUTES, new TimePoint(getWorkScheduleType()
                                .getClockingTime(), AttributeType.NULL), new TimePoint(
                                getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
                                        .isClokingTimeNextDay(), AttributeType.NULL));
                // calcula segundo periodo de trabalho do horario normal: desde entrada depois do almoco
                // ate' 'a saida
                if (((WorkPeriod) wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
                    lastWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesFromTime(
                            new TimePoint(mealInterval.getEndTime(), false, null),
                            DomainConstants.WORKED_ATTRIBUTES, new TimePoint(getWorkScheduleType()
                                    .getClockingTime(), AttributeType.NULL), new TimePoint(
                                    getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
                                            .isClokingTimeNextDay(), AttributeType.NULL));
                }
                dailyBalance.setWorkedOnNormalWorkPeriod(firstWorkPeriod.plus(lastWorkPeriod));
                // Fixed Periods if defined
                wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                wsType.checkMealDurationAccordingToRules(dailyBalance);

            } else { // o funcionario nao foi almocar so fez 1 periodo de trabalho
                Duration workPeriod = timeline.calculateDurationAllIntervalsByAttributes(
                        DomainConstants.WORKED_ATTRIBUTES, new TimePoint(getWorkScheduleType()
                                .getClockingTime(), AttributeType.NULL), new TimePoint(
                                getWorkScheduleType().getClockingEndTime(), getWorkScheduleType()
                                        .isClokingTimeNextDay(), AttributeType.NULL));

                // caso dos horarios de isencao de horario
                if (wsType.getMeal().getMinimumMealBreakInterval().isEqual(Duration.ZERO)) {

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
                        if (lunchTime.contains(firstClockingDate, false)) {
                            workPeriod = workPeriod.minus(new TimeInterval(firstClockingDate, lunchEnd,
                                    false).getDurationMillis());
                        }

                    } else if (firstClockingDate != null
                            && firstClockingDate.isBefore(wsType.getMeal().getBeginMealBreak())) {
                        workPeriod = workPeriod.minus(wsType.getMeal().getMandatoryMealDiscount());
                    }
                    dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);

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
                            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                            dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);

                            // primeiro clocking e' antes do periodo de almoco;
                            // ver se o ultimo clocking e' depois do periodo de almoco
                        } else if (lastClockingDate != null
                                && (!wsType.getMeal().getMealBreak().contains(lastClockingDate, false))) {
                            if (justificationInMealBreak(timeLeaves)) {
                                workPeriod = workPeriod.minus(wsType.getMeal()
                                        .getMandatoryMealDiscount());
                                dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);
                                wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                            } else {
                                wsType.turnDayToAbsence(dailyBalance);
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
                            dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);
                            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                        }
                    }
                }
            }
        } else { // meal nao esta definida - so ha 1 periodo de trabalho
            Duration worked = timeline.calculateDurationAllIntervalsByAttributes(
                    DomainConstants.WORKED_ATTRIBUTES, new TimePoint(getWorkScheduleType()
                            .getClockingTime(), AttributeType.NULL), new TimePoint(getWorkScheduleType()
                            .getClockingEndTime(), getWorkScheduleType().isClokingTimeNextDay(),
                            AttributeType.NULL));
            dailyBalance.setWorkedOnNormalWorkPeriod(worked);
            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);

        }
        return dailyBalance;
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
