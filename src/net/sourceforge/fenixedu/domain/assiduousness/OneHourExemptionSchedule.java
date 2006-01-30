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
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;

/**
 * @author velouria@velouria.org
 * 
 */

public class OneHourExemptionSchedule extends OneHourExemptionSchedule_Base {
    	
	public static OneHourExemptionSchedule makeOneHourExemptionSchedule(Employee employee,  NormalWorkPeriod regularSchedule, FixedPeriod fixedPlatforms, List<AssiduousnessRegime> regimes, Meal mealPeriod, Duration consecutiveWork, 
            TimeInterval workDay, Interval validFromTo, WorkWeek workWeek, boolean exception, boolean template, String acronym) {
        OneHourExemptionSchedule newOneHourExemptionSchedule = new OneHourExemptionSchedule();
        newOneHourExemptionSchedule.setEmployee(employee);
        newOneHourExemptionSchedule.setNormalWorkPeriod(regularSchedule);
        newOneHourExemptionSchedule.setFixedPeriod(fixedPlatforms);
        newOneHourExemptionSchedule.setMeal(mealPeriod);
        newOneHourExemptionSchedule.addRegimesToWorkSchedule(regimes);
        newOneHourExemptionSchedule.setWorkDay(workDay);
        newOneHourExemptionSchedule.setValidFromTo(validFromTo);
        newOneHourExemptionSchedule.setExceptionSchedule(exception);
        newOneHourExemptionSchedule.setWorkWeek(workWeek);
        newOneHourExemptionSchedule.setAcronym(acronym);
        newOneHourExemptionSchedule.setTemplateSchedule(template);
        return newOneHourExemptionSchedule;
    }
    
    public static OneHourExemptionSchedule makeOneHourExemptionScheduleTemplate(NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPlatforms, List<AssiduousnessRegime> regimes, Meal mealPeriod, 
            Duration consecutiveWork, TimeInterval workDay, WorkWeek workWeek, String acronym) {
        OneHourExemptionSchedule oneHourExemptionSchedule = new OneHourExemptionSchedule();
        oneHourExemptionSchedule.setNormalWorkPeriod(normalWorkPeriod);
        oneHourExemptionSchedule.setFixedPeriod(fixedPlatforms);
        oneHourExemptionSchedule.addRegimesToWorkSchedule(regimes);
        oneHourExemptionSchedule.setWorkDay(workDay);
        oneHourExemptionSchedule.setMeal(mealPeriod);
        oneHourExemptionSchedule.setExceptionSchedule(false);
        oneHourExemptionSchedule.setWorkWeek(workWeek);
        oneHourExemptionSchedule.setTemplateSchedule(true);
        oneHourExemptionSchedule.setAcronym(acronym);
        return oneHourExemptionSchedule;
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributeSet = EnumSet.of(AttributeType.NWP1, AttributeType.NWP2, AttributeType.FP1, AttributeType.FP2);
        Attributes attributes = new Attributes(attributeSet);
        if (((Meal)getMeal()).definedMealBreak()) {
            attributes.addAttribute(AttributeType.MEAL);
        }
        return attributes;
    }

	
	public static OneHourExemptionSchedule createOneHourExemptionSchedule(DTO presentationDTO) throws FenixDomainException {
	    
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
	    TimeInterval normalWorkSchedule1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES)),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());

	    // horario normal 2
	    TimeInterval normalWorkSchedule2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_2)).booleanValue(), new InvalidNormalWorkPeriod2IntervalException());
        
        // periodo fixo 1
	    TimeInterval fixedPeriod1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_FIXED_PERIOD_1)).booleanValue(), new InvalidFixedPeriod1IntervalException());
        
        // periodo fixo 2
	    TimeInterval fixedPeriod2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_FIXED_PERIOD_2)).booleanValue(), new InvalidFixedPeriod2IntervalException());
	    
        // intervalo de refeicao 
	    TimeInterval mealBreak = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_MEAL_BREAK)).booleanValue(), new InvalidMealBreakIntervalException());
        
        // workdays
        EnumSet<WeekDays> workDays = createWorkDays(((Boolean) presentationDTO.get(PresentationConstants.WORK_EVERYDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_MONDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_TUESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_WEDNESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_THURSDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_FRIDAY)).booleanValue());

        // regime
        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);
        
	    /** verificacoes que dependem de varios objectos simultaneamente **/
	    NormalWorkPeriod regularSchedule = null;
	    if ((normalWorkSchedule1 != null) && (normalWorkSchedule2 != null)) {
	        // criacao do horario normal

	        regularSchedule = new NormalWorkPeriod(normalWorkSchedule1, normalWorkSchedule2);
	        // verificar se horario normal totaliza as horas que era suposto
	        // TODO verificar isto com funcionarios
	        if (regularSchedule.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.EXEMPTION_1HOUR_DAY_DURATION) != 0) {
	            System.out.println("erro: hn e' maior que trabalho flexivel permitido");
	            throw new NormalWorkPeriodExceedsLegalDayDurationException();
	        }
	        // verificar se horario normal1 e 2 nao sao maiores que o trabalho consecutivo
	        // TODO verificatr isto
	        if ((normalWorkSchedule1.getDuration().isShorterThan(consecutiveWork)) || (normalWorkSchedule2.getDuration().isLongerThan(consecutiveWork))) {
	            System.out.println("erro: hn1 ou hn2 e' maior que trabalho consecutivo");
//	            System.out.println(regularSchedule1.length() + " " + consecutiveWork);
//	            System.out.println(regularSchedule2.length() + " " + consecutiveWork);
	            throw new NormalWorkPeriodExceedsConsecutiveWorkPeriodException();
	        }
	        if (workDay != null) {
	            // Se inicio de expediente for depois do horario normal 1 da' erro!
	            if (workDay.getStartTime().isAfter(normalWorkSchedule1.getStartTime())) {
	                System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
	            }
	            // Se fim de expediente for antes do horario normal 2 da' erro!
	            if (workDay.getEndTime().isBefore(normalWorkSchedule2.getEndTime())) {
	                System.out.println("erro: expediente acaba antes do hn2");
                    throw new NormalWorkPeriod2EndsAfterWorkDayException();
                }
	        }
	    }
	    
	    //criacao das platformas fixas
	    FixedPeriod fixedPlatforms = null;
	    if ((fixedPeriod1 != null) && (fixedPeriod2 != null)) {

	        fixedPlatforms = new FixedPeriod(fixedPeriod1, fixedPeriod2);
	        // verificar  se as plataformas duram 4 horas

	        if (fixedPlatforms.getTotalFixedPeriodDuration().compareTo(DomainConstants.EXEMPTION_1HOUR_PLATFORMS_DURATION) != 0) {
	            System.out.println("erro: pf duram mais de 4 horas");
                throw new FixedPeriodsExceedPlatformsDurationException();
	        }
            // verificar se o fp nao esta sobreposto ao intervalo de refeicao
            if (mealBreak != null) {
//                System.out.println(fixedPeriod1.getEnd() + " "+ mealBreak.start());
//                System.out.println(mealBreak.getEnd() + " "+ fixedPeriod2.start());

                if (fixedPeriod1.getEndTime().isAfter(mealBreak.getStartTime()) || (mealBreak.getEndTime().isAfter(fixedPeriod2.getStartTime()))) {
                    System.out.println("erro: fim pf1 e' depois da meal break");
                    throw new MealBreakOverlapsFixedPeriod1Exception();
                }
            }
        }

	    Employee employee = (Employee)presentationDTO.get(PresentationConstants.EMPLOYEE);
	    Meal mealPeriod = new Meal(mealBreak);
	    WorkWeek workWeek = new WorkWeek(workDays);
        // TODO mudar isto com dados apresentacao
	    OneHourExemptionSchedule oneHourExemptionSchedule = makeOneHourExemptionSchedule(employee,  regularSchedule, fixedPlatforms, regimes, mealPeriod, consecutiveWork, 
                workDay, validFromTo, workWeek, ((Boolean)presentationDTO.get(PresentationConstants.EXCEPTION_TIMETABLE)).booleanValue(), false, "xpto");
	    return oneHourExemptionSchedule;
	}

    public ScheduleType getType() {
        return ScheduleType.ONE_HOUR_EXEMPTION;
    }
    
}