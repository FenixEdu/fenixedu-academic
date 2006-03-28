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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidMealBreakIntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod2IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1StartsBeforeWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod2EndsAfterWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsConsecutiveWorkPeriodException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsLegalDayDurationException;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.ScheduleType;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.util.WeekDay;
/**
 * @author velouria
 * 
 */
public class TwoHoursExemptionSchedule extends TwoHoursExemptionSchedule_Base {
    	
    public TwoHoursExemptionSchedule() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    // TODO verify what happens when we get the FixedPeriod
	public static TwoHoursExemptionSchedule makeTwoHoursExemptionSchedule(NormalWorkPeriod normalWorkPeriod, Meal mealPeriod, TimeInterval workDay, String acronym) {
        TwoHoursExemptionSchedule newTwoHoursExemptionSchedule = new TwoHoursExemptionSchedule();
        newTwoHoursExemptionSchedule.setNormalWorkPeriod(normalWorkPeriod);
        newTwoHoursExemptionSchedule.setMeal(mealPeriod);
        newTwoHoursExemptionSchedule.setWorkDay(workDay);
        newTwoHoursExemptionSchedule.setAcronym(acronym);
        return newTwoHoursExemptionSchedule;
	}
    
    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributeSet = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1, AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.MEAL);
        Attributes attributes = new Attributes(attributeSet);
        return attributes;
    }

	
	public static TwoHoursExemptionSchedule createTwoHoursExemptionSchedule(DTO presentationDTO) throws FenixDomainException {
	    
//	    /** criacao dos objectos **/
//	    Integer startYear = (Integer)presentationDTO.get(PresentationConstants.START_YEAR);
//	    Integer startMonth = (Integer)presentationDTO.get(PresentationConstants.START_MONTH);
//	    Integer startDay = (Integer)presentationDTO.get(PresentationConstants.START_DAY);
//	    
//	    Interval validFromTo = createValidFromToInterval(startYear, startMonth, startDay, (Integer)presentationDTO.get(PresentationConstants.END_YEAR), 
//	            (Integer)presentationDTO.get(PresentationConstants.END_MONTH), (Integer)presentationDTO.get(PresentationConstants.END_DAY));
	    	    
	    // expediente
	    TimeInterval workDay = createWorkDay((Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_MINUTES),
                (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_MINUTES),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_WORK_DAY)).booleanValue());
	            
//	    // consecutive work
//	    Duration consecutiveWork = new Duration(((Long)presentationDTO.get(PresentationConstants.CONSECUTIVE_WORK)).longValue());

	    // horario normal 1	    
	    TimeInterval normalWorkSchedule1 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES),
                (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());

	    // horario normal 2
	    TimeInterval regularSchedule2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_2)).booleanValue(), new InvalidNormalWorkPeriod2IntervalException());

	    // intervalo de refeicao 
	    TimeInterval mealBreak = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_MEAL_BREAK)).booleanValue(), new InvalidMealBreakIntervalException());
        
//        // workdays
//        EnumSet<WeekDay> workDays = createWorkDays(((Boolean) presentationDTO.get(PresentationConstants.WORK_EVERYDAY)).booleanValue(),
//                ((Boolean)presentationDTO.get(PresentationConstants.WORK_MONDAY)).booleanValue(),
//                ((Boolean)presentationDTO.get(PresentationConstants.WORK_TUESDAY)).booleanValue(),
//                ((Boolean)presentationDTO.get(PresentationConstants.WORK_WEDNESDAY)).booleanValue(),
//                ((Boolean)presentationDTO.get(PresentationConstants.WORK_THURSDAY)).booleanValue(),
//                ((Boolean)presentationDTO.get(PresentationConstants.WORK_FRIDAY)).booleanValue());
//
//        // regime
//        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);
// 
	    /** verificacoes que dependem de varios objectos simultaneamente **/
	    NormalWorkPeriod normalWorkPeriod = null;
	    if ((normalWorkSchedule1 != null) && (regularSchedule2 != null)) {
	        // criacao do horario normal

	        normalWorkPeriod = new NormalWorkPeriod(normalWorkSchedule1, regularSchedule2);
	        // verificar se horario normal totaliza as horas que era suposto
	        // TODO verificar isto com funcionarios
	        if (normalWorkPeriod.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.EXEMPTION_2HOURS_DAY_DURATION) != 0) {
	            System.out.println("erro: hn e' maior que trabalho flexivel permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
	        }
	        if (workDay != null) {
	            // Se inicio de expediente for depois do horario normal 1 da' erro!
	            if (workDay.getStartTime().isAfter(normalWorkSchedule1.getStartTime())) {
	                System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
	            }
	            // Se fim de expediente for antes do horario normal 2 da' erro!
	            if (workDay.getEndTime().isBefore(regularSchedule2.getEndTime())) {
	                System.out.println("erro: expediente acaba antes do hn2");
	                throw new NormalWorkPeriod2EndsAfterWorkDayException();
	            }
	        }
	    }
	    
	    Meal mealPeriod = new Meal(mealBreak);
        
	    TwoHoursExemptionSchedule twoHoursExemptionSchedule = makeTwoHoursExemptionSchedule(normalWorkPeriod, mealPeriod, workDay, null);
	    return twoHoursExemptionSchedule;
	}
	
    public ScheduleType getType() {
        return ScheduleType.TWO_HOURS_EXEMPTION;
    }

}




