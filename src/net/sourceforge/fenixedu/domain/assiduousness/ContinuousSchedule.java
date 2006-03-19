/*
 * Created on Mar 25, 2005
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
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.FixedPeriodsExceedPlatformsDurationException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod1IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.InvalidNormalWorkPeriod2IntervalException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1EndsAfterWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriod1StartsBeforeWorkDayException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkPeriodExceedsLegalDayDurationException;
import net.sourceforge.fenixedu.domain.exceptions.assiduousness.NormalWorkSchedule1StartsAfterFixedPeriod1;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
import net.sourceforge.fenixedu.domain.assiduousness.util.Attributes;
import net.sourceforge.fenixedu.domain.assiduousness.util.DomainConstants;
import net.sourceforge.fenixedu.domain.assiduousness.util.TimeInterval;
import net.sourceforge.fenixedu.domain.assiduousness.util.WeekDays;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRegime;

/**
 * @author velouria
 *
 */
public class ContinuousSchedule extends ContinuousSchedule_Base {

	public ContinuousSchedule() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public static ContinuousSchedule makeContinuousSchedule(Employee employee,  NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriod, List<AssiduousnessRegime> regimes, 
			TimeInterval workDay, Interval validFromTo, WorkWeek workWeek, boolean exception, boolean template, String acronym) {
		ContinuousSchedule newContinuousSchedule = new ContinuousSchedule();
 //       newContinuousSchedule.setEmployee(employee);
        newContinuousSchedule.setNormalWorkPeriod(normalWorkPeriod);
        newContinuousSchedule.setFixedPeriod(fixedPeriod);
 //       newContinuousSchedule.addRegimesToWorkSchedule(regimes);
        newContinuousSchedule.setWorkDay(workDay);
        newContinuousSchedule.setValidFromTo(validFromTo);
        newContinuousSchedule.setExceptionSchedule(exception);
        newContinuousSchedule.setWorkWeek(workWeek); 
        newContinuousSchedule.setAcronym(acronym);
        newContinuousSchedule.setTemplateSchedule(template);
        return newContinuousSchedule;
    	}
    
    public static ContinuousSchedule makeContinuousScheduleTemplate(NormalWorkPeriod normalWorkPeriod, FixedPeriod fixedPeriod, List<AssiduousnessRegime> regimes, TimeInterval workDay, 
            WorkWeek workWeek, String acronym) {
        ContinuousSchedule continuousSchedule = new ContinuousSchedule();
        continuousSchedule.setNormalWorkPeriod(normalWorkPeriod);
        continuousSchedule.setFixedPeriod(fixedPeriod);
//        continuousSchedule.addRegimesToWorkSchedule(regimes);
        continuousSchedule.setWorkDay(workDay);
        continuousSchedule.setExceptionSchedule(false);
        continuousSchedule.setWorkWeek(workWeek);
        continuousSchedule.setTemplateSchedule(true);
        continuousSchedule.setAcronym(acronym);
        return continuousSchedule;
    }

    public Duration checkNormalWorkPeriodAccordingToRules(Duration normalWorkPeriodWorked) {
        return normalWorkPeriodWorked;
    }

        
//    // Returns the schedule Attributes
//    public Attributes getAttributes() {
//        EnumSet<AttributeType> attributes = EnumSet.of(AttributeType.NWP1, AttributeType.FP1);
//        return new Attributes(attributes);
//    }

    
//////////////////////
// Presentation stuff - TODO this will eventually be changed to JSF
/////////////////////
    
	// validates and builds a schedule with data got from the presentation
    public static ContinuousSchedule createContinuousSchedule(DTO presentationDTO) throws FenixDomainException {
	    
	    /** criacao dos objectos **/
        // data validade - usada como data nos TimeIntervals do horario
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
	    TimeInterval normalWorkSchedule1 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.START_REGULAR_SCHEDULE_1_MINUTES),
                (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_HOURS),
                (Integer)presentationDTO.get(PresentationConstants.END_REGULAR_SCHEDULE_1_MINUTES),
                ((Boolean)presentationDTO.get(PresentationConstants.NEXT_DAY_REGULAR_SCHEDULE_1)).booleanValue(), new InvalidNormalWorkPeriod1IntervalException());
                
        // periodo fixo 1
	    TimeInterval fixedPeriod1 = createTimeInterval((Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_HOURS),
                    (Integer)presentationDTO.get(PresentationConstants.START_FIXED_PERIOD_1_MINUTES),
                    (Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_HOURS),
                    (Integer)presentationDTO.get(PresentationConstants.END_FIXED_PERIOD_1_MINUTES), false, new InvalidNormalWorkPeriod2IntervalException());

	    // workdays
        EnumSet<WeekDays> workDays = createWorkDays(((Boolean) presentationDTO.get(PresentationConstants.WORK_EVERYDAY)).booleanValue(), 
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_MONDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_TUESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_WEDNESDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_THURSDAY)).booleanValue(),
                ((Boolean)presentationDTO.get(PresentationConstants.WORK_FRIDAY)).booleanValue());
        
        // regime
        List<AssiduousnessRegime> regimes = (List<AssiduousnessRegime>)presentationDTO.get(PresentationConstants.REGIMES);
        
        // TODO ver se isto e' preciso ou nao... penso que nao pq ja se faz a verificacao com o regular schedule...
        // Se duracao do expediente for menor que a duracao do expediente minimo
            // ou duracao do expediente for maior que a duracao do expediente maximo da' erro!
//            if ((weeklyDuration.compareTo(DomainConstants.MIN_DURATION_CJ_WEEK_HOURS) < 0 ) || (weeklyDuration.compareTo(DomainConstants.MAX_DURATION_CJ_WEEK_HOURS) > 0)) {
//                System.out.println("erro: weeklyDuration ta fora dos limites.");
//                errors.put(DomainConstants.ERROR_WEEKLY_DURATION_CJ_LIMITS, DomainConstants.ERROR_WEEKLY_DURATION_CJ_LIMITS_MSG);
//            }
//        } catch (Exception e) {
//            System.out.println("erro: criacao do intervalo weekly duration.");
//            errors.put(DomainConstants.ERROR_WEEKLY_DURATION, DomainConstants.ERROR_WEEKLY_DURATION_MSG);
//        }

        /** verificacoes que dependem de varios objectos simultaneamente **/
        NormalWorkPeriod normalWorkSchedule = null;
        if (normalWorkSchedule1 != null) {
            // criacao do horario normal
            
            normalWorkSchedule = new NormalWorkPeriod(normalWorkSchedule1, null);
            // verificar se horario normal totaliza as horas que era suposto (o RS2 e' null!)
            // TODO verificar isto com funcionarios
            if (normalWorkSchedule.getTotalNormalWorkPeriodDuration().isShorterThan(DomainConstants.MIN_DURATION_CJ_DAY) || 
                    normalWorkSchedule.getTotalNormalWorkPeriodDuration().isLongerThan(DomainConstants.MAX_DURATION_CJ_DAY)) {
                System.out.println("erro: hn e' maior que trabalho JC permitido");
                throw new NormalWorkPeriodExceedsLegalDayDurationException();
            }
            if (workDay != null) {
                // Se inicio de expediente for depois do horario normal 1 da' erro!
                if (workDay.getStartTime().isAfter(normalWorkSchedule1.getStartTime())) {
                    System.out.println("erro: expediente comeca depois do hn1");
                    throw new NormalWorkPeriod1StartsBeforeWorkDayException();
                }
                // Se fim de expediente for antes do horario normal 1 da' erro!
                if (workDay.getEndTime().isBefore(normalWorkSchedule1.getEndTime())) {
                    System.out.println("erro: expediente acaba antes do hn1");
                    throw new NormalWorkPeriod1EndsAfterWorkDayException();
                }
            }
        }
	    
	    //criacao das platformas fixas
	    FixedPeriod fixedPeriods = null;
	    if (fixedPeriod1 != null) {
	        fixedPeriods = new FixedPeriod(fixedPeriod1, null);
	        // verificar  se as plataformas duram 4 horas
	        if (fixedPeriods.getTotalFixedPeriodDuration().isLongerThan(DomainConstants.CJ_PLATFORMS_DURATION)) {
                System.out.println("erro: pf duram mais de 4 horas");
                throw new FixedPeriodsExceedPlatformsDurationException();
	        }
        }
	    // verificar se o pf1 e' antes do hc.
	    if ((normalWorkSchedule1.getStartTime().isAfter(fixedPeriod1.getStartTime())) || (normalWorkSchedule1.getEndTime().isBefore(fixedPeriod1.getEndTime()))) {
            throw new NormalWorkSchedule1StartsAfterFixedPeriod1();
	    }
	    
	    Employee employee = (Employee)presentationDTO.get(PresentationConstants.EMPLOYEE);
	    WorkWeek workWeek = new WorkWeek(workDays);
        // TODO ir buscar isto a appresentacao
	    ContinuousSchedule continuousSchedule = makeContinuousSchedule(employee, normalWorkSchedule, fixedPeriods, regimes, workDay, validFromTo, workWeek,
	                ((Boolean)presentationDTO.get(PresentationConstants.EXCEPTION_TIMETABLE)).booleanValue(), false, "xpto");
	    return continuousSchedule;
	}

//    public String getName() {
//        return "Jornada";
//    }
}
