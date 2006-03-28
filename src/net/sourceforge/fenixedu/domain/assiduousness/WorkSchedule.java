package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.Duration;

/**
 *
 * @author velouria@velouria.org
 * 
 */
public class WorkSchedule extends WorkSchedule_Base {
    
    public WorkSchedule() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    
    // returns true if the actual date is before the schedule's end date
    public boolean isCurrent() {
    		return false;
    	//    return getValidInterval().contains(new DateTime());
    }

    // Returns the duration of normal period times the number of days per week the Employee has that schedule
	public Duration calculateWeekDuration() {
		return Duration.ZERO;
		//	    return new Duration(getWorkScheduleType().getNormalWorkPeriod().getTotalNormalWorkPeriodDuration().getMillis() * getWorkWeek().workDaysPerWeek());
	}
    
    // Returns true if the schedule is defined in the week day weekDay
    public boolean isDefinedInWeekDay(WeekDay weekDay) {
    		return false;
    	// return getWorkWeek().worksAt(weekDay);
    }

//	// validates and creates the Valid From To Interval
//    // TODO find a decent name for this... god it sucks!
//    public static Interval createValidFromToInterval(Integer startYear, Integer startMonth, Integer startDay, Integer endYear, Integer endMonth, Integer endDay) {
//        // 	data validade - usada como data nos TimeIntervals do horario
//        DateTime startScheduleDate = new DateTime(startYear.intValue(), startMonth.intValue(), startDay.intValue(), 0, 0, 0, 0);
//        // end day might not be defined
//        DateTime endScheduleDate = null;
//        if ((endYear != null) && (endMonth != null) && (endDay != null)) {
//            System.out.println("dafa final def");
//            endScheduleDate = new DateTime(endYear.intValue(), endMonth.intValue(), endDay.intValue(), 0, 0, 0, 0);
//        } else {
//            System.out.println("dafa final nao def");
//            endScheduleDate = DomainConstants.FAR_FUTURE;
//        }
//        return new Interval(startScheduleDate, endScheduleDate);
//    }

    
//    // creates a Set with the days the employee works from the stuff got from the presentation
//    public static EnumSet<WeekDay> createWorkDays(boolean everyDay, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri) {
//        if (everyDay) {
//            return EnumSet.allOf(WeekDay.class);
//        } else {
//            EnumSet<WeekDay> days = EnumSet.noneOf(WeekDay.class);
//            if (mon) {
//               days.add(WeekDay.MONDAY);
//            }
//            if (tue) {
//                days.add(WeekDay.TUESDAY);
//            }
//            if (wed) {
//                days.add(WeekDay.WEDNESDAY);
//            }
//            if (thu) {
//                days.add(WeekDay.THURSDAY);
//            }
//            if (fri) {
//                days.add(WeekDay.FRIDAY);
//            }
//            return days;
//        }
//    }    
    
//    // TODO ver se o funcionario trabalhou dentro dos periodos
//    public DailyBalance calculateWorkingPeriods(YearMonthDay day, WorkSchedule workSchedule, List<Clocking> clockingList, Timeline timeline) {
//    		DailyBalance dailyBalance = new DailyBalance(day);
//    		Duration firstWorkPeriod = Duration.ZERO;
//    		Duration lastWorkPeriod = Duration.ZERO;
//    		TimeInterval mealInterval = null;
//    		
//    		if (workSchedule.definedMeal()) { // o horario tem um intervalo de refeicao definido
//    			mealInterval = timeline.calculateMealBreakInterval(workSchedule.getMeal().getMealBreak()); // calcular o intervalo de refeicao dentro do periodo de refeicao definido
//    			System.out.println("intervalo refeicao: " + mealInterval);
//
//    			if (mealInterval != null) { // funcionario fez intervalo para almoco => ha 2 periodos de trabalho
//    				dailyBalance.setLunchBreak(mealInterval.getDuration()); // actualiza almoco no dailyBalance
//    				// calcula primeiro periodo de trabalho do horario normal: deste a entrada ate' 'a saida para o almoco
//    				firstWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesToTime(mealInterval.getStartTime(), DomainConstants.WORKED_ATTRIBUTES);
//    				System.out.println("primeiro periodo de trabalho:" + firstWorkPeriod.toPeriod().toString());
//    				dailyBalance.setNormalWorkPeriod1Balance(workSchedule.checkNormalWorkPeriodAccordingToRules(firstWorkPeriod).
//    						minus(((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).getNormalWorkPeriod1Duration()));
//                  // calcula segundo periodo de trabalho do horario normal: desde entrada depois do almoco ate' 'a saida
//    				if (((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).definedNormalWorkPeriod2()) {
//    					lastWorkPeriod = timeline.calculateDurationAllIntervalsByAttributesFromTime(mealInterval.getEndTime(), DomainConstants.WORKED_ATTRIBUTES);
//    					System.out.println("segundo periodo de trabalho: " +lastWorkPeriod.toDuration().toPeriod().toString());
//    					dailyBalance.setNormalWorkPeriod2Balance(lastWorkPeriod.minus(((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).getNormalWorkPeriod2Duration()));
//    				}
//    			} else { // o funcionario nao foi almocar
//    				System.out.println("o funcionario nao foi almocar");
//    				Duration workPeriod = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
////    				dailyBalance.setNormalWorkPeriod1Balance(worked.minus)
//    				Clocking firstClocking = clockingList.get(0);
//    				
//    				if (workSchedule.getMeal().getMealBreak().contains(firstClocking.getDate().toTimeOfDay(), false)) { // funcionario entrou no intervalo de almoco
//    					// considera-se o periodo desde o  inicio do intervalo de almoco + desconto obrigatorio de almoco
//    					// se funcionario entrar nesse periodo de tempo e'-lhe descontado desde a entrada ate' (inicio do intervalo de almoco + desconto obrigatorio de almoco)
//    					DateTime endLunchTime = workSchedule.getMeal().getLunchEnd().toDateTime(firstClocking.getDate());
//    					if (firstClocking.getDate().isBefore(endLunchTime)) {
//    						Duration enteredDuringLunchDiscount = (new Interval(firstClocking.getDate(), endLunchTime)).toDuration();
//    						workPeriod = workPeriod.minus(enteredDuringLunchDiscount);
//    					}
//    				}// else { //entrou antes ou depois do intervalo de almoco
//    				
//    					// situacao em q o funcionario entrou antes da hora de almoco
//    					// situacao em que o funcionario entrou depois da hora de almoco
//    					
//    			//	}
//    				dailyBalance.setNormalWorkPeriod1Balance(workPeriod.minus(workSchedule.getNormalWorkPeriod().getTotalNormalWorkPeriodDuration()));
//    			}
//    			
//    		} else { // meal nao esta definida
//    			Duration worked = timeline.calculateDurationAllIntervalsByAttributes(DomainConstants.WORKED_ATTRIBUTES);
//    			dailyBalance.setNormalWorkPeriod1Balance(worked.minus(((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).getNormalWorkPeriod1Duration()));
//    		}
//    		
//    		System.out.println("total worked1 ->" +dailyBalance.getNormalWorkPeriod1Balance().toPeriod().toString());
//    		System.out.println("total worked2 ->" +dailyBalance.getNormalWorkPeriod2Balance().toPeriod().toString());
//    		System.out.println(dailyBalance.getNormalWorkPeriod1Balance().plus(dailyBalance.getNormalWorkPeriod2Balance()).
//    				minus(((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).getTotalNormalWorkPeriodDuration()).toPeriod().toString());
//    		System.out.println("devia ter trabalhado ->" + ((NormalWorkPeriod)workSchedule.getNormalWorkPeriod()).getTotalNormalWorkPeriodDuration().toPeriod().toString());
//
//    		// Fixed Periods if defined
//    		workSchedule.calculateFixedPeriodDuration(dailyBalance, timeline);
//
//    		if (workSchedule.definedMeal()) {
//    			System.out.println("actualizar o meal");
//    			workSchedule.checkMealDurationAccordingToRules(dailyBalance);
//    		}
////          System.out.println("total worked ->" + dailyBalance.getNormalWorkPeriod1Balance().plus(dailyBalance.getNormalWorkPeriod2Balance()).minus(this.getNormalWorkPeriod().
////                  getTotalNormalWorkPeriodDuration()).toPeriod().toString());
//          return dailyBalance;
//    }
    
    
}
