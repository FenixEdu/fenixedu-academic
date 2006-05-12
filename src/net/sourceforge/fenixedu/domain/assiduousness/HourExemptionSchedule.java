/*
 * Created on Mar 9, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author velouria@velouria.org
 * 
 */

public class HourExemptionSchedule extends HourExemptionSchedule_Base {

    public HourExemptionSchedule(String acronym, YearMonthDay beginValidDate, YearMonthDay endValidDate,
            TimeOfDay dayTime, Duration dayTimeDuration, TimeOfDay clockingTime,
            Duration clockingTimeDuration, WorkPeriod normalWorkPeriod, WorkPeriod fixedWorkPeriod,
            Meal meal, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
        setAcronym(acronym);
        setWorkTime(dayTime);
        setWorkTimeDuration(dayTimeDuration);
        setClockingTime(clockingTime);
        setClockingTimeDuration(clockingTimeDuration);
        setNormalWorkPeriod(normalWorkPeriod);
        setFixedWorkPeriod(fixedWorkPeriod);
        setMeal(meal);
        setBeginValidDate(beginValidDate);
        setEndValidDate(endValidDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
    }

    // Returns the schedule Attributes
    public Attributes getAttributes() {
        EnumSet<AttributeType> attributeSet = EnumSet.of(AttributeType.NORMAL_WORK_PERIOD_1,
                AttributeType.NORMAL_WORK_PERIOD_2, AttributeType.FIXED_PERIOD_1,
                AttributeType.FIXED_PERIOD_2);
        Attributes attributes = new Attributes(attributeSet);
        // if (((Meal)getMeal()).definedMealBreak()) {
        // attributes.addAttribute(AttributeType.MEAL);
        // }
        return attributes;
    }

    // public static OneHourExemptionSchedule createOneHourExemptionSchedule(DTO presentationDTO) throws
    // FenixDomainException {
    // //
    // // /** criacao dos objectos **/
    // // Integer startYear = (Integer)presentationDTO.get(PresentationConstants.START_YEAR);
    // // Integer startMonth = (Integer)presentationDTO.get(PresentationConstants.START_MONTH);
    // // Integer startDay = (Integer)presentationDTO.get(PresentationConstants.START_DAY);
    // //
    // // Interval validFromTo = createValidFromToInterval(startYear, startMonth, startDay,
    // (Integer)presentationDTO.get(PresentationConstants.END_YEAR),
    // // (Integer)presentationDTO.get(PresentationConstants.END_MONTH),
    // (Integer)presentationDTO.get(PresentationConstants.END_DAY));
    //	    	    
    // // expediente
    // TimeInterval workDay =
    // createWorkDay((Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_HOURS),
    // (Integer)presentationDTO.get(PresentationConstants.START_WORK_DAY_MINUTES),
    // (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_HOURS),
    // (Integer)presentationDTO.get(PresentationConstants.END_WORK_DAY_MINUTES),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_WORK_DAY)).booleanValue());
    //	            
    // // horario normal 1
    // TimeInterval normalWorkSchedule1 =
    // createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES)),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(),
    // new InvalidNormalWorkPeriod1IntervalException());
    //
    // // horario normal 2
    // TimeInterval normalWorkSchedule2 =
    // createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_2_MINUTES)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_2_MINUTES)),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_2)).booleanValue(),
    // new InvalidNormalWorkPeriod2IntervalException());
    //        
    // // periodo fixo 1
    // TimeInterval fixedPeriod1 =
    // createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES)),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_FIXED_PERIOD_1)).booleanValue(), new
    // InvalidFixedPeriod1IntervalException());
    //        
    // // periodo fixo 2
    // TimeInterval fixedPeriod2 =
    // createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_2_MINUTES)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_2_MINUTES)),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_FIXED_PERIOD_2)).booleanValue(), new
    // InvalidFixedPeriod2IntervalException());
    //	    
    // // intervalo de refeicao
    // TimeInterval mealBreak =
    // createTimeInterval(((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.START_MEAL_BREAK_MINUTES)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_HOURS)),
    // ((Integer)presentationDTO.get(PresentationConstants.END_MEAL_BREAK_MINUTES)),
    // ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_MEAL_BREAK)).booleanValue(), new
    // InvalidMealBreakIntervalException());
    //        
    // // // workdays
    // // EnumSet<WeekDay> workDays = createWorkDays(((Boolean)
    // presentationDTO.get(PresentationConstants.WORK_EVERYDAY)).booleanValue(),
    // // ((Boolean)presentationDTO.get(PresentationConstants.WORK_MONDAY)).booleanValue(),
    // // ((Boolean)presentationDTO.get(PresentationConstants.WORK_TUESDAY)).booleanValue(),
    // // ((Boolean)presentationDTO.get(PresentationConstants.WORK_WEDNESDAY)).booleanValue(),
    // // ((Boolean)presentationDTO.get(PresentationConstants.WORK_THURSDAY)).booleanValue(),
    // // ((Boolean)presentationDTO.get(PresentationConstants.WORK_FRIDAY)).booleanValue());
    // //
    // // // regime
    // // List<AssiduousnessRegime> regimes =
    // (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);
    //        
    // /** verificacoes que dependem de varios objectos simultaneamente **/
    // NormalWorkPeriod regularSchedule = null;
    // if ((normalWorkSchedule1 != null) && (normalWorkSchedule2 != null)) {
    // // criacao do horario normal
    //
    // regularSchedule = new NormalWorkPeriod(normalWorkSchedule1, normalWorkSchedule2);
    // // verificar se horario normal totaliza as horas que era suposto
    // // TODO verificar isto com funcionarios
    // if
    // (regularSchedule.getTotalNormalWorkPeriodDuration().compareTo(DomainConstants.EXEMPTION_1HOUR_DAY_DURATION)
    // != 0) {
    // System.out.println("erro: hn e' maior que trabalho flexivel permitido");
    // throw new NormalWorkPeriodExceedsLegalDayDurationException();
    // }
    // if (workDay != null) {
    // // Se inicio de expediente for depois do horario normal 1 da' erro!
    // if (workDay.getStartTime().isAfter(normalWorkSchedule1.getStartTime())) {
    // System.out.println("erro: expediente comeca depois do hn1");
    // throw new NormalWorkPeriod1StartsBeforeWorkDayException();
    // }
    // // Se fim de expediente for antes do horario normal 2 da' erro!
    // if (workDay.getEndTime().isBefore(normalWorkSchedule2.getEndTime())) {
    // System.out.println("erro: expediente acaba antes do hn2");
    // throw new NormalWorkPeriod2EndsAfterWorkDayException();
    // }
    // }
    // }
    //	    
    // //criacao das platformas fixas
    // FixedPeriod fixedPlatforms = null;
    // if ((fixedPeriod1 != null) && (fixedPeriod2 != null)) {
    //
    // fixedPlatforms = new FixedPeriod(fixedPeriod1, fixedPeriod2);
    // // verificar se as plataformas duram 4 horas
    //
    // if
    // (fixedPlatforms.getTotalFixedPeriodDuration().compareTo(DomainConstants.EXEMPTION_1HOUR_PLATFORMS_DURATION)
    // != 0) {
    // System.out.println("erro: pf duram mais de 4 horas");
    // throw new FixedPeriodsExceedPlatformsDurationException();
    // }
    // // verificar se o fp nao esta sobreposto ao intervalo de refeicao
    // if (mealBreak != null) {
    // // System.out.println(fixedPeriod1.getEnd() + " "+ mealBreak.start());
    // // System.out.println(mealBreak.getEnd() + " "+ fixedPeriod2.start());
    //
    // if (fixedPeriod1.getEndTime().isAfter(mealBreak.getStartTime()) ||
    // (mealBreak.getEndTime().isAfter(fixedPeriod2.getStartTime()))) {
    // System.out.println("erro: fim pf1 e' depois da meal break");
    // throw new MealBreakOverlapsFixedPeriod1Exception();
    // }
    // }
    // }
    //
    // Meal mealPeriod = new Meal(mealBreak);
    // // TODO mudar isto com dados apresentacao
    // OneHourExemptionSchedule oneHourExemptionSchedule = makeOneHourExemptionSchedule(regularSchedule,
    // fixedPlatforms, mealPeriod,
    // workDay, null);
    // return oneHourExemptionSchedule;
    // }

}