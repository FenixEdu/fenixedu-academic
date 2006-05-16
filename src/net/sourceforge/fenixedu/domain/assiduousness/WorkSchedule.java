package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;
import org.joda.time.DateTime;
import org.joda.time.Interval;

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
    public boolean isDefinedInDate(YearMonthDay date, int weekNumber) {
        System.out.println("date: " + date + "  week number:" + weekNumber);
        DateTime dateAtMidnight = date.toDateTimeAtMidnight();
        if (getWorkWeek().contains(dateAtMidnight) && getPeriodicity().occur(weekNumber)) {
            return true;
        } else {
            return false;
        }
    }

    // TODO ver se o funcionario trabalhou dentro dos periodos
    public DailyBalance calculateWorkingPeriods(YearMonthDay day, List<Clocking> clockingList, Timeline timeline) {
            DailyBalance dailyBalance = new DailyBalance(day, this);
            Duration firstWorkPeriod = Duration.ZERO;
            Duration lastWorkPeriod = Duration.ZERO;
            TimeInterval mealInterval = null;
            WorkScheduleType wsType = getWorkScheduleType();
            
            if (wsType.definedMeal()) { // o horario tem um intervalo de refeicao definido
                mealInterval = timeline.calculateMealBreakInterval(wsType.getMeal().getMealBreak()); // calcular o intervalo de refeicao dentro do periodo de refeicao definido
                System.out.println("intervalo refeicao: " + mealInterval);

                if (mealInterval != null) { // funcionario fez intervalo para almoco => ha 2 periodos de trabalho
                    dailyBalance.setLunchBreak(mealInterval.getDuration()); // actualiza almoco no dailyBalance
                    // calcula primeiro periodo de trabalho do horario normal: deste a entrada ate' 'a saida para o almoco
                    firstWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesToTime(mealInterval.getStartTime(), DomainConstants.WORKED_ATTRIBUTES);
                    System.out.println("primeiro periodo de trabalho:" + firstWorkPeriod.toPeriod().toString());
                    
                    // calcula segundo periodo de trabalho do horario normal: desde entrada depois do almoco ate' 'a saida
                    if (((WorkPeriod)wsType.getNormalWorkPeriod()).isSecondWorkPeriodDefined()) {
                        lastWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesFromTime(mealInterval.getEndTime(), DomainConstants.WORKED_ATTRIBUTES);
//                      System.out.println("segundo periodo de trabalho: " +lastWorkPeriod.toDuration().toPeriod().toString());
//                      dailyBalance.setNormalWorkPeriod2Balance(lastWorkPeriod.minus(((NormalWorkPeriod)wsType.getNormalWorkPeriod()).getNormalWorkPeriod2Duration()));
//                      System.out.println("balance 2o periodo: " + dailyBalance.getNormalWorkPeriod2Balance().toPeriod().toString());
                        System.out.println("segundo periodo de trabalho:" + lastWorkPeriod.toPeriod().toString());
                    }
//                  dailyBalance.setWorkedOnNormalWorkPeriod(wsType.checkNormalWorkPeriodAccordingToRules(firstWorkPeriod).plus(lastWorkPeriod));
                    dailyBalance.setWorkedOnNormalWorkPeriod(firstWorkPeriod.plus(lastWorkPeriod));
                } else { // o funcionario nao foi almocar so ha 1 periodo de trabalho
                    System.out.println("funcionario nao foi almocar");
                    Duration workPeriod = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
//                  dailyBalance.setNormalWorkPeriod1Balance(worked.minus)
                    Clocking firstClocking = clockingList.get(0);
                    
                    if (wsType.getMeal().getMealBreak().contains(firstClocking.getDate().toTimeOfDay(), false)) { // funcionario entrou no intervalo de almoco
                        System.out.println("funcionario entrou no intervalo de almoco");
                        // considera-se o periodo desde o  inicio do intervalo de almoco + desconto obrigatorio de almoco
                        // se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a entrada ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)
                        DateTime endLunchTime = wsType.getMeal().getLunchEnd().toDateTime(firstClocking.getDate());
                        if (firstClocking.getDate().isBefore(endLunchTime)) {
                            Duration enteredDuringLunchDiscount = (new Interval(firstClocking.getDate(), endLunchTime)).toDuration();
                            workPeriod = workPeriod.minus(enteredDuringLunchDiscount);
                        }
                    }
                    dailyBalance.setWorkedOnNormalWorkPeriod(workPeriod);
                    System.out.println("nwp: " + dailyBalance.getWorkedOnNormalWorkPeriod().toPeriod().toString());
                }
            } else { // meal nao esta definida - so ha 1 periodo de trabalho
                Duration worked = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
             dailyBalance.setWorkedOnNormalWorkPeriod(worked);
            }
            System.out.println("total worked -> " +dailyBalance.getWorkedOnNormalWorkPeriod().toPeriod().toString());
            System.out.println("devia ter trabalhado ->" + ((WorkPeriod)wsType.getNormalWorkPeriod()).getWorkPeriodDuration().toPeriod().toString());
            System.out.println("saldo ->" + dailyBalance.getWorkedOnNormalWorkPeriod().minus(wsType.getNormalWorkPeriod().getWorkPeriodDuration()).toPeriod().toString());

            // Fixed Periods if defined
            wsType.calculateFixedPeriodDuration(dailyBalance, timeline);

            if (wsType.definedMeal()) {
                System.out.println("saldo antes de descontar a meal: " + dailyBalance.getNormalWorkPeriodBalance().toPeriod().toString());
                wsType.checkMealDurationAccordingToRules(dailyBalance);
            }
            return dailyBalance;
    }


    // // Returns the duration of normal period times the number of days per week the Employee has that
    // schedule
    // public Duration calculateWeekDuration() {
    // return Duration.ZERO;
    // // return new
    // Duration(getWorkScheduleType().getNormalWorkPeriod().getTotalNormalWorkPeriodDuration().getMillis()
    // * getWorkWeek().workDaysPerWeek());
    // }

    // // Returns true if the schedule is defined in the week day weekDay
    // public boolean isDefinedInWeekDay(WeekDay weekDay) {
    // return false;
    // // return getWorkWeek().worksAt(weekDay);
    // }

    // // validates and creates the Valid From To Interval
    // // TODO find a decent name for this... god it sucks!
    // public static Interval createValidFromToInterval(Integer startYear, Integer startMonth, Integer
    // startDay, Integer endYear, Integer endMonth, Integer endDay) {
    // // data validade - usada como data nos TimeIntervals do horario
    // DateTime startScheduleDate = new DateTime(startYear.intValue(), startMonth.intValue(),
    // startDay.intValue(), 0, 0, 0, 0);
    // // end day might not be defined
    // DateTime endScheduleDate = null;
    // if ((endYear != null) && (endMonth != null) && (endDay != null)) {
    // System.out.println("dafa final def");
    // endScheduleDate = new DateTime(endYear.intValue(), endMonth.intValue(), endDay.intValue(), 0, 0,
    // 0, 0);
    // } else {
    // System.out.println("dafa final nao def");
    // endScheduleDate = DomainConstants.FAR_FUTURE;
    // }
    // return new Interval(startScheduleDate, endScheduleDate);
    // }

    
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
