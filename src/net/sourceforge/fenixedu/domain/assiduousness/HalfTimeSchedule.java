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
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidFixedPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidMealBreakIntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1StartsBeforeWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod2EndsAfterWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsLegalDayDurationException;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.util.WeekDay;

/**
 * @author velouria@velouria.org
 *
 */
public class HalfTimeSchedule extends HalfTimeSchedule_Base {

	public HalfTimeSchedule() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}
	
	public static HalfTimeSchedule makeHalfTimeSchedule(NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPlatforms, Meal mealPeriod, TimeInterval workDay, String acronym) {
        HalfTimeSchedule newHalfTimeSchedule = new HalfTimeSchedule();
        newHalfTimeSchedule.setNormalWorkPeriod(normalWorkPeriod);
        newHalfTimeSchedule.setFixedPeriod(fixedPlatforms);
        newHalfTimeSchedule.setMeal(mealPeriod);
        newHalfTimeSchedule.setWorkDay(workDay);
        newHalfTimeSchedule.setAcronym(acronym);
        return newHalfTimeSchedule;
    }
	
    // TODO NAo tem trabalho extraordinario
    
    // Returns the schedule Attributes
    public Attributes getAttributes() {
        Attributes attributes = new Attributes(AttributeType.NORMAL_WORK_PERIOD_1);
        if (definedFixedPeriod()) {
            attributes.addAttribute(AttributeType.FIXED_PERIOD_1);
        }
        return attributes;
    }

    
    
    public static HalfTimeSchedule createHalfTimeSchedule(DTO presentationDTO) throws FenixDomainException {
	    
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
	            
	    // horario normal 1	    
	    TimeInterval normalWorkPeriod1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS)),
                ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES)),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());
        
        // periodo fixo 1 pode nao estar definido
	    // TODO confirmar isto, porque nao esta' definido no Horario MeioTempo.
	    FixedPeriod fixedPeriods = null;
	    if ((presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS) != null) && (presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES) != null) &&
	           (presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS) != null) && (presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES) != null)) {
	        TimeInterval fixedPeriod1 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS)),
                    	((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES)),
                    	((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS)),
                    	((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES)),
                    	((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_FIXED_PERIOD_1)).booleanValue(), new InvalidFixedPeriod1IntervalException());
		    //criacao das platformas fixas
		    if (fixedPeriod1 != null) {
		        fixedPeriods = new FixedPeriod(fixedPeriod1, null);
		    }
	    }

	    // intervalo de refeicao  pode nao estar definido
	    Meal mealPeriod = null;
	    if ((presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS) != null) && (presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES) != null) &&
	            (presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS) != null) && (presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES) != null)) {
	        TimeInterval mealBreak = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)),
                    ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_MEAL_BREAK)).booleanValue(), new InvalidMealBreakIntervalException());
	        if (mealBreak != null) {
	            mealPeriod = new Meal(mealBreak);
	        }
	    }
	    
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
 

	    /** verificacoes que dependem de varios objectos simultaneamente **/
	    NormalWorkPeriod regularSchedule = null;
	    if (normalWorkPeriod1 != null) {
	        // criacao do horario normal

	        regularSchedule = new NormalWorkPeriod(normalWorkPeriod1, null);
	        // verificar se horario normal totaliza as horas que era suposto
	        // TODO verificar isto com funcionarios
	        // duracao do HN nao pode exceder 3.5 horas por dia
	        if (regularSchedule.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.HALFTIME_DAY_DURATION) != 0) {
	            System.out.println("erro: hn e' maior que trabalho flexivel permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
	        }
	        if (workDay != null) {
	            // Se inicio de expediente for depois do horario normal 1 da' erro!
	            if (workDay.getStartTime().isAfter(normalWorkPeriod1.getStartTime())) {
	                System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
	            }
	            // Se fim de expediente for antes do horario normal 1 da' erro!
	            if (workDay.getEndTime().isBefore(normalWorkPeriod1.getEndTime())) {
	                System.out.println("erro: expediente acaba antes do hn2");
                    throw new NormalWorkPeriod2EndsAfterWorkDayException();
                }
	        }
	    }
	    
        // TODO alterar isto da appresentacao
	    HalfTimeSchedule halfTimeSchedule = makeHalfTimeSchedule(regularSchedule, fixedPeriods, mealPeriod, workDay, null);
	    return halfTimeSchedule;
	}
    
    
}
