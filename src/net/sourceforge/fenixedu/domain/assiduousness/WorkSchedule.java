package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimePoint;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.DateTime;
import org.joda.time.Duration;
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
    public DailyBalance calculateWorkingPeriods(YearMonthDay day, DateTime firstClockingDate, DateTime lastClockingDate,
            Timeline timeline) {
        DailyBalance dailyBalance = new DailyBalance(day, this);
        Duration firstWorkPeriod = Duration.ZERO;
        Duration lastWorkPeriod = Duration.ZERO;
        TimeInterval mealInterval = null;
        WorkScheduleType wsType = getWorkScheduleType();

        if (wsType.definedMeal()) { // o horario tem um intervalo de refeicao definido

            // calcular qd e' q funcionario foi almocar
            mealInterval = timeline.calculateMealBreakInterval(wsType.getMeal().getMealBreak()); // calcular
            System.out.println("intervalo refeicao: " + mealInterval);

            if (mealInterval != null) { // funcionario fez intervalo para almoco => ha 2 periodos de
                // trabalho
                dailyBalance.setLunchBreak(mealInterval.getDuration());
                // actualiza almoco no dailyBalance calcula primeiro periodo de trabalho do horario
                // normal: deste a entrada ate' 'a saida para o almoco
                firstWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesToTime(
                        new TimePoint(mealInterval.getStartTime(), false, null),
                        DomainConstants.WORKED_ATTRIBUTES);
                System.out.println("primeiro periodo de trabalho:"
                        + firstWorkPeriod.toPeriod().toString());

                // calcula segundo periodo de trabalho do horario normal: desde entrada depois do almoco
                // ate' 'a saida
                if (((WorkPeriod) wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
                    System.out.println("fim da meal " + mealInterval.getEndTime().toString());
                    lastWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesFromTime(
                            new TimePoint(mealInterval.getEndTime(), false, null),
                            DomainConstants.WORKED_ATTRIBUTES);
                    System.out.println("segundo periodo de trabalho:"
                            + lastWorkPeriod.toPeriod().toString());
                }
                dailyBalance.setWorkedOnNormalWorkPeriod(firstWorkPeriod.plus(lastWorkPeriod));
                System.out.println("saldo antes de descontar a meal: "
                        + dailyBalance.getNormalWorkPeriodBalance().toString());
                // Fixed Periods if defined
                wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                wsType.checkMealDurationAccordingToRules(dailyBalance);
            
            } else { // o funcionario nao foi almocar so fez 1 periodo de trabalho
                System.out.println("funcionario nao foi almocar");
                Duration workPeriod = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
                System.out.println("nwp: " + workPeriod);
                
                // caso dos horarios de isencao de horario
                if (wsType.getMeal().getMinimumMealBreakInterval().isEqual(Duration.ZERO)) {
                    System.out.println("horario de isencao de horario");
                    
                    if (firstClockingDate != null && wsType.getMeal().getMealBreak().contains(firstClockingDate.toTimeOfDay(),
                                    false)) {
                        // funcionario entrou no intervalo de almoco
                        System.out.println("funcionario entrou no intervalo de almoco");
                        // considera-se o periodo desde o inicio do intervalo de almoco + desconto
                        // obrigatorio de almoco
                        // se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a entrada
                        // ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)

                        TimeOfDay lunchEnd = wsType.getMeal().getLunchEnd();
                        TimeInterval lunchTime = new TimeInterval(wsType.getMeal().getBeginMealBreak(),
                                lunchEnd, false);
                        if (lunchTime.contains(firstClockingDate.toTimeOfDay(), false)) {
                            workPeriod = workPeriod.minus(new TimeInterval(firstClockingDate.toTimeOfDay(),
                                    lunchEnd, false).getDurationMillis());
                        }
                    }

                } else { // caso dos horarios em q o minimo de intervalo de almoco e' tipicamente de 15minutos
                    if (firstClockingDate != null) {
                        if (wsType.getMeal().getMealBreak().contains(firstClockingDate.toTimeOfDay(), false)) {
                            // funcionario entrou no intervalo de almoco
                            System.out.println("funcionario entrou no intervalo de almoco");
                            // considera-se o periodo desde o inicio do intervalo de almoco + desconto
                            // obrigatorio de almoco
                            // se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a entrada
                            // ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)

                            TimeOfDay lunchEnd = wsType.getMeal().getLunchEnd();
                            TimeInterval lunchTime = new TimeInterval(wsType.getMeal().getBeginMealBreak(),
                                    lunchEnd, false);

                            if (lunchTime.contains(firstClockingDate.toTimeOfDay(), false)) {
                                workPeriod = workPeriod.minus(new TimeInterval(firstClockingDate.toTimeOfDay(),
                                        lunchEnd, false).getDurationMillis());
                            }
                            // calcular o periodo fixo
                            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                            dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);
                            System.out.println("nwp: "
                                  + dailyBalance.getWorkedOnNormalWorkPeriod().toPeriod().toString());

                            
                            // primeiro clocking e' antes do periodo de almoco;
                            // ver se o ultimo clocking e' depois do periodo de almoco
                        } else if (lastClockingDate != null && (wsType.getMeal().getMealBreak().contains(lastClockingDate.toTimeOfDay(), false) == false)) {
                            wsType.turnDayToAbsence(dailyBalance);
                        } else { // ultimo clocking e' dentro do periodo de almoco
                            // ver se e' dentro do final do intervalo de almoco menos o desconto obrigatorio
                            if (wsType.getMeal().getEndOfMealBreakMinusDiscountInterval().contains(lastClockingDate.toTimeOfDay(), false)) { 
                                // descontar periodo de tempo desde fim da refeicao menos periodo obrigatorio de refeicao ate' 'a ultima marcacao do funcionario
                                workPeriod = workPeriod.minus((new TimeInterval(wsType.getMeal().getEndOfMealBreakMinusMealDiscount(), lastClockingDate.toTimeOfDay(), 
                                        false)).getDurationMillis());
                            }
                            dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);
                            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);
                        }
                    }
                }
            }
        } else { // meal nao esta definida - so ha 1 periodo de trabalho
            Duration worked = timeline
                    .calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
            dailyBalance.setWorkedOnNormalWorkPeriod(worked);
            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);

        }
        System.out.println("total worked -> "
                + dailyBalance.getWorkedOnNormalWorkPeriod().toPeriod().toString());
        System.out.println("devia ter trabalhado ->"
                + ((WorkPeriod) wsType.getNormalWorkPeriod()).getWorkPeriodDuration().toPeriod()
                        .toString());
        System.out.println("saldo ->"
                + dailyBalance.getWorkedOnNormalWorkPeriod().minus(
                        wsType.getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod().toString());

        return dailyBalance;
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
