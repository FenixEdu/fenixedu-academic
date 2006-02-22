/*
 * Created on Mar 24, 2005
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.EnumSet;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.Interval;

import net.sourceforge.fenixedu.presentationTier.util.DTO;
import net.sourceforge.fenixedu.presentationTier.util.PresentationConstants;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod2IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1StartsBeforeWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod2EndsAfterWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsLegalDayDurationException;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;

/**
 * @author velouria
 *
 */
public class ScheduleExemption extends ScheduleExemption_Base {
    
	public static ScheduleExemption fillScheduleExemption(Employee employee,  NormalWorkPeriod normalWorkPeriod, List<AssiduousnessRegime> regimes, Meal mealPeriod, TimeInterval workDay, Interval validFromTo, 
            WorkWeek workWeek, boolean exception) {
        ScheduleExemption newScheduleExemption = new ScheduleExemption();
//        newScheduleExemption.setEmployee(employee);
        newScheduleExemption.setNormalWorkPeriod(normalWorkPeriod);
        newScheduleExemption.setMeal(mealPeriod);
//        newScheduleExemption.addRegimesToWorkSchedule(regimes);
        newScheduleExemption.setWorkDay(workDay);
        newScheduleExemption.setValidFromTo(validFromTo);
        newScheduleExemption.setExceptionSchedule(exception);
        newScheduleExemption.setWorkWeek(workWeek);
        return newScheduleExemption;
	}

    // Exemption Schedule is now allowed to have overtime
    public Duration countOvertimeWorkDone(Clocking clockingIn, Clocking clockingOut) {
        return Duration.ZERO;
    }
    
    
    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributes = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1, AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.MEAL);
        return new Attributes(attributes);
    }

    
    
	public static ScheduleExemption createScheduleExemption(DTO presentationDTO) throws FenixDomainException {
	    
	    /** criacao dos objectos **/
	    Integer startYear = (Integer)presentationDTO.get(PresentationConstants.START_YEAR);
	    Integer startMonth = (Integer)presentationDTO.get(PresentationConstants.START_MONTH);
	    Integer startDay = (Integer)presentationDTO.get(PresentationConstants.START_DAY);
	    
	    Interval validFromTo = createValidFromToInterval(startYear, startMonth, startDay, (Integer)presentationDTO.get(PresentationConstants.END_YEAR), 
	            (Integer)presentationDTO.get(PresentationConstants.END_MONTH), (Integer)presentationDTO.get(PresentationConstants.END_DAY));
	    	    
	    // expediente
	    TimeInterval workDay = createWorkDay((Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_MINUTES),
                (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_MINUTES),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_WORK_DAY)).booleanValue());
	            
	    	    
	    // horario normal 1
	    TimeInterval normalWorkPeriod1 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS),
	            (Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES),
	            (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS),
	            (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES),
	            ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());

	    // horario normal 2
	    TimeInterval normalWorkPeriod2 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_HOURS),
	            (Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_MINUTES),
	            (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_HOURS),
	            (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_MINUTES),
	            ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_2)).booleanValue(), new InvalidNormalWorkPeriod2IntervalException());
        
        // intervalo de refeicao 
	    TimeInterval mealBreak = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_MEAL_BREAK)).booleanValue(), new InvalidNormalWorkPeriod2IntervalException());
        
        // workdays
        EnumSet<WeekDays> workDays = createWorkDays(true, true, true, true, true, true);
        
        // regime
        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);
        
	    /** verificacoes que dependem de varios objectos simultaneamente **/
	    NormalWorkPeriod regularSchedule = null;
	    if ((normalWorkPeriod1 != null) && (normalWorkPeriod2 != null)) {
	        // criacao do horario normal

	        regularSchedule = new NormalWorkPeriod(normalWorkPeriod1, normalWorkPeriod2);
	        // verificar se horario normal totaliza as horas que era suposto
	        // TODO verificar isto com funcionarios
	        if (regularSchedule.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.EXEMPTION_DAY_DURATION) != 0) {
	            System.out.println("erro: hn e' maior que trabalho flexivel permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
	        }
	        if (workDay != null) {
	            // Se inicio de expediente for depois do horario normal 1 da' erro!
	            if (workDay.getStartTime().isAfter(normalWorkPeriod1.getStartTime())) {
	                System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
	            }
	            // Se fim de expediente for antes do horario normal 2 da' erro!
	            if (workDay.getEndTime().isBefore(normalWorkPeriod2.getEndTime())) {
                    System.out.println("erro: expediente acaba antes do hn2");
                    throw new NormalWorkPeriod2EndsAfterWorkDayException();
	            }
	        }
	    }
	    
	    Employee employee = (Employee)presentationDTO.get(PresentationConstants.EMPLOYEE);
	    Meal mealPeriod = new Meal(mealBreak);
	    WorkWeek workWeek = new WorkWeek(workDays);
	    ScheduleExemption scheduleExemption = fillScheduleExemption(employee, regularSchedule, regimes, mealPeriod, workDay, validFromTo, workWeek,
                    ((Boolean)presentationDTO.get(PresentationConstants.EXCEPTION_TIMETABLE)).booleanValue());
	    return scheduleExemption;
	}

    
    public ScheduleType getType() {
        return ScheduleType.EXEMPTION;
    }    
}
