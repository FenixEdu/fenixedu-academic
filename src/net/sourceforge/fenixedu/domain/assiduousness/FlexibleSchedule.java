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
import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
/**
 * @author velouria
 * 
 */
public class FlexibleSchedule extends FlexibleSchedule_Base {
	
    private FlexibleSchedule() {
    }

//    // change to static
//	public FlexibleSchedule(Employee employee,  NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<Regime> regimes, Meal mealPeriod, 
//	        TimeInterval workDay, Interval validFromTo, WorkWeek workWeek, boolean exception, boolean template, String acronym) {
//	    this.setEmployee(employee);
//        this.setNormalWorkPeriod(normalWorkPeriod);
//        this.setFixedPeriod(fixedPeriods);
//        this.setMeal(mealPeriod);
//        this.addRegimesToWorkSchedule(regimes);
//        this.setWorkDay(workDay);
//        this.setValidFromTo(validFromTo);
//        this.setExceptionSchedule(exception);
//        this.setWorkWeek(workWeek);
//        this.setTemplateSchedule(template);
//        this.setAcronym(acronym);
//	}

    public static FlexibleSchedule makeFlexibleScheduleTemplate(NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<AssiduousnessRegime> regimes, Meal mealPeriod, 
            TimeInterval workDay, WorkWeek workWeek, String acronym) {
        FlexibleSchedule flexibleSchedule = new FlexibleSchedule();
        flexibleSchedule.setNormalWorkPeriod(normalWorkPeriod);
        flexibleSchedule.setFixedPeriod(fixedPeriods);
        flexibleSchedule.setMeal(mealPeriod);
//        flexibleSchedule.addRegimesToWorkSchedule(regimes);
        flexibleSchedule.setWorkDay(workDay);
        flexibleSchedule.setWorkWeek(workWeek);
        flexibleSchedule.setAcronym(acronym);
        flexibleSchedule.turnTemplateSchedule();
        flexibleSchedule.setExceptionSchedule(false);
        return flexibleSchedule;
    }
    
    public static FlexibleSchedule makeFlexibleSchedule(Employee employee, NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<AssiduousnessRegime> regimes, Meal mealPeriod, 
            TimeInterval workDay,
            Interval validFromTo, WorkWeek workWeek, boolean exception, String acronym) {
        FlexibleSchedule flexibleSchedule = new FlexibleSchedule();
//        flexibleSchedule.setEmployee(employee);
        flexibleSchedule.setNormalWorkPeriod(normalWorkPeriod);
        flexibleSchedule.setFixedPeriod(fixedPeriods);
        flexibleSchedule.setMeal(mealPeriod);
//        flexibleSchedule.addRegimesToWorkSchedule(regimes);
        flexibleSchedule.setWorkDay(workDay);
        flexibleSchedule.setExceptionSchedule(exception);
        flexibleSchedule.setValidFromTo(validFromTo);
        flexibleSchedule.setWorkWeek(workWeek);
        flexibleSchedule.setAcronym(acronym);
        flexibleSchedule.turnWorkSchedule();
        return flexibleSchedule;
    }
    
    public static FlexibleSchedule makeFlexibleSchedule(NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriods, List<AssiduousnessRegime> regimes, Meal mealPeriod, TimeInterval workDay,
            Interval validFromTo, WorkWeek workWeek, boolean exception, String acronym) {
        FlexibleSchedule flexibleSchedule = new FlexibleSchedule();
        flexibleSchedule.setNormalWorkPeriod(normalWorkPeriod);
        flexibleSchedule.setFixedPeriod(fixedPeriods);
        flexibleSchedule.setMeal(mealPeriod);
//        flexibleSchedule.addRegimesToWorkSchedule(regimes);
        flexibleSchedule.setWorkDay(workDay);
        flexibleSchedule.setExceptionSchedule(exception);
        flexibleSchedule.setValidFromTo(validFromTo);
        flexibleSchedule.setWorkWeek(workWeek);
        flexibleSchedule.setAcronym(acronym);
        flexibleSchedule.turnWorkSchedule();
        return flexibleSchedule;
    }
    
    
    // TODO verify this...
    // method 
    public static FlexibleSchedule createFlexibleSchedule(DTO presentationDTO) throws FenixDomainException {
        FlexibleSchedule flexibleSchedule = FlexibleSchedule.initFlexibleSchedule(presentationDTO);
        if (flexibleSchedule.validateFlexibleSchedule()) {
            System.out.println("LOG: flexible schedule created.");
            return flexibleSchedule;
        } else {
            System.out.println("LOG: flexible schedule null.");
            return null;
        }
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributes = EnumSet.of(AttributeType.NWP1, AttributeType.NWP2, AttributeType.FP1, AttributeType.FP2, AttributeType.MEAL);
        return new Attributes(attributes);
    }
    
    /*
     * Creates a FlexibleSchedule from the presentation data
     * TODO find a better name
     */
    public static FlexibleSchedule initFlexibleSchedule(DTO presentationDTO) throws FenixDomainException {
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
        
        // periodo fixo 1
        TimeInterval fixedPeriod1 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS),
                    (Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES),
                    (Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS),
                    (Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES), false, new InvalidFixedPeriod1IntervalException());
        
        // periodo fixo 2
        TimeInterval fixedPeriod2 = createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_MINUTES)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_HOURS)),
                    ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_MINUTES)), false, new InvalidFixedPeriod2IntervalException());
        
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
        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);

        NormalWorkPeriod normalWorkPeriod = new NormalWorkPeriod(normalWorkPeriod1, normalWorkPeriod2);
        FixedPeriod fixedPeriods = new FixedPeriod(fixedPeriod1, fixedPeriod2);
        Employee employee = (Employee)presentationDTO.get(PresentationConstants.EMPLOYEE);
        Meal mealPeriod = new Meal(mealBreak);
        WorkWeek workWeek = new WorkWeek(workDays);

        FlexibleSchedule flexibleSchedule = makeFlexibleSchedule(employee, normalWorkPeriod, fixedPeriods, regimes, mealPeriod, workDay, validFromTo, 
                workWeek, ((Boolean)presentationDTO.get(PresentationConstants.EXCEPTION_TIMETABLE)).booleanValue(), "xpto");
        return flexibleSchedule;
    }

    // TODO throws exceptions should be boolean?
    public boolean validateFlexibleSchedule() throws FenixDomainException {
        if (((NormalWorkPeriod)getNormalWorkPeriod()).definedNormalWorkPeriod1() && ((NormalWorkPeriod)getNormalWorkPeriod()).definedNormalWorkPeriod2()) {
            // TODO change this. total regular schedule musn't be longer than flex day duration
            if (((NormalWorkPeriod)getNormalWorkPeriod()).getTotalNormalWorkPeriodDuration().isLongerThan(DomainConstants.FLEXIBLE_DAY_DURATION)) {
                System.out.println("erro: hn e' maior que trabalho flexivel permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
            }
            if (getWorkDay() != null) {
                // if work day start is after hn1 error
                if (getWorkDay().getStartTime().isAfter(getNormalWorkPeriod().getNormalWorkPeriod1().getStartTime())) {
                    System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
                }
                // if work day end is before hn2 error!
                if (getWorkDay().getEndTime().isBefore(getNormalWorkPeriod().getNormalWorkPeriod2().getEndTime())) {
                    throw new NormalWorkPeriod2EndsAfterWorkDayException();
                }
            }
        }
        if (((FixedPeriod)getFixedPeriod()).definedFixedPeriod1() && ((FixedPeriod)getFixedPeriod()).definedFixedPeriod2()) {
            // verify if fplats are 4 hours
            if (((FixedPeriod)getFixedPeriod()).getTotalFixedPeriodDuration().isLongerThan(DomainConstants.FLEXIBLE_PLATFORMS_DURATION)) {
                throw new FixedPeriodsExceedPlatformsDurationException();
            }
            // verify if fp dont overlap meal
            // verificar se o fp nao esta sobreposto ao intervalo de refeicao
            if (((Meal)getMeal()).definedMealBreak()) {
                if (getFixedPeriod().getFixedPeriod1().getEndTime().isAfter(getMeal().getMealBreak().getStartTime()) || 
                        (getMeal().getMealBreak().getEndTime().isAfter(getFixedPeriod().getFixedPeriod2().getStartTime()))) {
                    System.out.println("erro: fim pf1 e' depois da meal break");
                    throw new MealBreakOverlapsFixedPeriod1Exception();
                }
            }
        }
        return true;
    }
    
    
    public ScheduleType getType() {
        return ScheduleType.FLEXIBLE;
    }
    
//    public String getName() {
//        return "Flexivel";
//    }
    
}




