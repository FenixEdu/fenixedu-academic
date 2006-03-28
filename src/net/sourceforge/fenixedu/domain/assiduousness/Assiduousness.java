package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.assiduousness.Clocking;
import net.sourceforge.fenixedu.domain.assiduousness.ClockingInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.Timeline;

import org.joda.time.Duration;
import org.joda.time.YearMonthDay;
import org.joda.time.Interval;
import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class Assiduousness extends Assiduousness_Base {
    
    public Assiduousness() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }
    
    
    // Clockings, schedule, justification
    public DailyBalance calculateDailyBalance(YearMonthDay day) {
    		Timeline timeline = new Timeline();
    		// Obter o workSchedule para este dia;
    		// TODO
    		WorkSchedule workSchedule = null;// getSchedule().workScheduleWithDate(day);
    		workSchedule.getWorkScheduleType().plotInTimeline(timeline);
    		// Obter as marcacoes para este dia
    		Iterator<AttributeType> attributesIt = DomainConstants.WORKED_ATTRIBUTES.getAttributes().iterator();
    		List<Clocking> dailyClockings = clockingsWithDate(day);
    		Clocking.plotListInTimeline(dailyClockings, attributesIt, timeline);
    		List<Leave> leaveList = leavesWithDate(day);
    		Leave.plotListInTimeline(leaveList, attributesIt, timeline);

    		timeline.print();
    		// fazer os calculos
 //   		DailyBalance db = calculateWorkingPeriods(day, workSchedule, dailyClockings, timeline);
 //   		return db;
    		return null;
    }
    
    
    // Returns a list of clockings made on date
    public List<Clocking> clockingsWithDate(YearMonthDay date){
    		List<Clocking> clockingsList = new ArrayList<Clocking>();
    		Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
		while (itAssidRecord.hasNext()) {
			AssiduousnessRecord assidRecord = itAssidRecord.next();
			if (assidRecord instanceof Clocking) {
				Clocking clocking = (Clocking)assidRecord;
				if (clocking.getDate().toYearMonthDay().equals(date)) {
					clockingsList.add(clocking);
				}
			}
		}
		return clockingsList;
    }
 
    // Returns a list of leaves that occured in date
    public List<Leave> leavesWithDate(YearMonthDay date) {
    		List<Leave> leavesList = new ArrayList<Leave>();
    		Iterator<AssiduousnessRecord> itAssidRecord = getAssiduousnessRecordsIterator();
    		while (itAssidRecord.hasNext()) {
    			AssiduousnessRecord assidRecord = itAssidRecord.next();
    			if (assidRecord instanceof Leave) {
    				Leave leave  = (Leave)assidRecord;
    				if (leave.occuredInDate(date)) {
    					leavesList.add(leave);
    				}
    			}
    		}
    		return leavesList;
    }
    

    
    // Get all clockings
    public List<Clocking> allClockings() {
		List<Clocking> clockingsList = new ArrayList<Clocking>();
		Iterator<AssiduousnessRecord> itAssidRecord = this.getAssiduousnessRecordsIterator();
		while (itAssidRecord.hasNext()) {
			AssiduousnessRecord assidRecord = itAssidRecord.next();
			if (assidRecord instanceof Clocking) {
				clockingsList.add((Clocking)assidRecord);
			}
		}
		return clockingsList;
    }
   
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
