/*
 * Created on Mar 9, 2005
 *
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
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.FixedPeriodsExceedPlatformsDurationException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidFixedPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidFixedPeriod2IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidMealBreakIntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod2IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.MealBreakOverlapsFixedPeriod1Exception;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1StartsBeforeWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod2EndsAfterWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsConsecutiveWorkPeriodException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsLegalDayDurationException;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;
/**
 * @author velouria
 * 
 */
public class CustomFlexibleSchedule extends CustomFlexibleSchedule_Base {
	
	public static CustomFlexibleSchedule makeCustomFlexibleSchedule(Employee employee,  NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<AssiduousnessRegime> regimes, Meal mealPeriod, Duration consecutiveWork, 
            TimeInterval workDay, Interval validFromTo, WorkWeek workWeek, boolean exception, boolean template, String acronym) {
        CustomFlexibleSchedule newCustomFlexibleSchedule = new CustomFlexibleSchedule();
//        newCustomFlexibleSchedule.setEmployee(employee);
        newCustomFlexibleSchedule.setNormalWorkPeriod(normalWorkPeriod);
        newCustomFlexibleSchedule.setFixedPeriod(fixedPeriods);
        newCustomFlexibleSchedule.setMeal(mealPeriod);
//        newCustomFlexibleSchedule.addRegimesToWorkSchedule(regimes);
        newCustomFlexibleSchedule.setWorkDay(workDay);
        newCustomFlexibleSchedule.setValidFromTo(validFromTo);
        newCustomFlexibleSchedule.setExceptionSchedule(exception);
        newCustomFlexibleSchedule.setWorkWeek(workWeek);
        newCustomFlexibleSchedule.setAcronym(acronym);
        newCustomFlexibleSchedule.setTemplateSchedule(template);
        return newCustomFlexibleSchedule;
    }
    
    public static CustomFlexibleSchedule makeCustomFlexibleScheduleTemplate (NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<AssiduousnessRegime> regimes, Meal mealPeriod, 
            TimeInterval workDay, WorkWeek workWeek, String acronym) {
        CustomFlexibleSchedule customFlexibleSchedule = new CustomFlexibleSchedule();
        customFlexibleSchedule.setNormalWorkPeriod(normalWorkPeriod);
        customFlexibleSchedule.setFixedPeriod(fixedPeriods);
        customFlexibleSchedule.setMeal(mealPeriod);
//        customFlexibleSchedule.addRegimesToWorkSchedule(regimes);
        customFlexibleSchedule.setWorkDay(workDay);
        customFlexibleSchedule.setExceptionSchedule(false);
        customFlexibleSchedule.setWorkWeek(workWeek);
        customFlexibleSchedule.setTemplateSchedule(true);
        customFlexibleSchedule.setAcronym(acronym);
        return customFlexibleSchedule;
    }
    
    
    // Returns the schedule Attributes
    public final Attributes getAttributes() {
        EnumSet<AttributeType> attributeSet = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1, AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.MEAL);
        Attributes attributes = new Attributes(attributeSet);
        if (definedFixedPeriod()) {
            attributes.addAttribute(AttributeType.FIXED_PERIOD_1);
            if (((FixedPeriod)getFixedPeriod()).definedFixedPeriod2()) {
                attributes.addAttribute(AttributeType.FIXED_PERIOD_2);
            }
        }
        return attributes;
    }


    
	public static CustomFlexibleSchedule createCustomFlexibleSchedule(DTO presentationDTO) throws FenixDomainException {
	    
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
	            
	    // consecutive work
	    Duration consecutiveWork = new Duration(((Long)presentationDTO.get(PresentationConstants.CONSECUTIVE_WORK)).longValue());
        
	    // horario normal 1	    
	    TimeInterval normalWorkPeriod1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES)),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());

	    // horario normal 2
	    TimeInterval normalWorkPeriod2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_2)).booleanValue(), new InvalidNormalWorkPeriod2IntervalException());
        
	    FixedPeriod fixedPlatforms = null;
        // periodo fixo 1
	    if ((presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS) != null) && (presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES) != null) &&
	            (presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS) != null) && (presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES) != null)) {
	        TimeInterval fixedPeriod1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS)),
	                ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES)),
	                ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS)),
	                ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES)), false, new InvalidFixedPeriod1IntervalException());
	        
	        	// periodo fixo 2
	        TimeInterval fixedPeriod2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_MINUTES)), false, new InvalidFixedPeriod2IntervalException());
	        fixedPlatforms = new FixedPeriod(fixedPeriod1, fixedPeriod2);
	    }
        
        // intervalo de refeicao 
	    TimeInterval mealBreak = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)), false, new InvalidMealBreakIntervalException());
        
        // workdays
        EnumSet<WeekDays> workDays = createWorkDays(((Boolean) presentationDTO.get(PresentationConstants.WORK_EVERYDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_MONDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_TUESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_WEDNESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_THURSDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_FRIDAY)).booleanValue());
        
        // regime
        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>) presentationDTO.get(PresentationConstants.REGIMES);
	    
	    /** verificacoes que dependem de varios objectos simultaneamente **/
	    NormalWorkPeriod regularSchedule = null;
	    if ((normalWorkPeriod1 != null) && (normalWorkPeriod2 != null)) {
	        // criacao do horario normal

	        regularSchedule = new NormalWorkPeriod(normalWorkPeriod1, normalWorkPeriod2);
	        // verificar se horario normal totaliza as horas que era suposto
	        // TODO verificar isto com funcionarios
	        if (regularSchedule.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.FLEXIBLE_DAY_DURATION) != 0) {
	            System.out.println("erro: hn e' maior que trabalho flexivel permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
	        }
	        // verificar se horario normal1 e 2 nao sao maiores que o trabalho consecutivo
	        // TODO verificatr isto
	        if ((normalWorkPeriod1.getDuration().isShorterThan(consecutiveWork)) || (normalWorkPeriod2.getDuration().isLongerThan(consecutiveWork))) {
	            System.out.println("erro: hn1 ou hn2 e' maior que trabalho consecutivo");
//	            System.out.println(regularSchedule1.length() + " " + consecutiveWork);
//	            System.out.println(regularSchedule2.length() + " " + consecutiveWork);
                throw new NormalWorkPeriodExceedsConsecutiveWorkPeriodException();
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
	    
	    //criacao das platformas fixas
	    if (fixedPlatforms != null) {

	        // verificar  se as plataformas duram 4 horas
	        if (fixedPlatforms.getTotalFixedPeriodDuration().compareTo(DomainConstants.FLEXIBLE_PLATFORMS_DURATION) != 0) {
	            System.out.println("erro: pf duram mais de 4 horas");
                throw new FixedPeriodsExceedPlatformsDurationException();
	        }
            // verificar se o fp nao esta sobreposto ao intervalo de refeicao
            if (mealBreak != null) {
                if (fixedPlatforms.getFixedPeriod1().getEndTime().isAfter(mealBreak.getStartTime()) 
                        || (mealBreak.getEndTime().isAfter(fixedPlatforms.getFixedPeriod2().getStartTime()))) {
                    System.out.println("erro: fim pf1 e' depois da meal break");
                    throw new MealBreakOverlapsFixedPeriod1Exception();
                }
            }
        }

	    Employee employee = (Employee)presentationDTO.get(PresentationConstants.EMPLOYEE);
	    Meal mealPeriod = new Meal(mealBreak);
	    WorkWeek workWeek = new WorkWeek(workDays);
        // TODO ir buscar estes valores 'a form!!!
	    CustomFlexibleSchedule customFlexibleSchedule = makeCustomFlexibleSchedule(employee,  regularSchedule, fixedPlatforms, regimes, mealPeriod, consecutiveWork, workDay, 
                validFromTo, workWeek, ((Boolean)presentationDTO.get(PresentationConstants.EXCEPTION_TIMETABLE)).booleanValue(), false, "xpto");
        return customFlexibleSchedule;
	}
	
//    public String getName() {
//        return "FlexivelAjustado";
//    }

}




