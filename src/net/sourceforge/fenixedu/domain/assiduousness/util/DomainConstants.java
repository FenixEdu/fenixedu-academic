/*
 * Created on Apr 21, 2005
 *
 */
package net.sourceforge.fenixedu.domain.assiduousness.util;


import java.util.EnumSet;

import net.sourceforge.fenixedu.domain.assiduousness.WorkWeek;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.TimeOfDay;


/**
 * @author velouria@velouria.org
 *
 */
public class DomainConstants {
    
    public final static String ERROR_WORKDAY = "errorWorkday";
    public final static String ERROR_WORKDAY_MSG = "error.workday";
    public final static String ERROR_WORKDAY_RS1 = "errorWorkDayRS1";
    public final static String ERROR_WORKDAY_RS1_MSG = "error.workDayRS1";
    public final static String ERROR_WORKDAY_RS2 = "errorWorkDayRS2";
    public final static String ERROR_WORKDAY_RS2_MSG = "error.workDayRS2";
    
    public final static String ERROR_WORKDAY_LIMITS = "errorWorkdayLimits";
    public final static String ERROR_WORKDAY_LIMITS_MSG = "error.workdayLimits";
    
    public final static String ERROR_RS1 = "errorRS1";
    public final static String ERROR_RS1_MSG = "error.RS1";
    
    public final static String ERROR_RS2 = "errorRegularSchedule2";
    public final static String ERROR_RS2_MSG = "error.regularSchedule2";
    
    public final static String ERROR_DURATION_RS = "errorDurationRegularSchedule";
    public final static String ERROR_DURATION_RS_MSG = "error.durationRegularSchedule";
    
    public final static String ERROR_DURATION_RS_EXCEEDS_CONSECUTIVE_WORK = "errorDurationRSExceedsConsecutiveWork";
    public final static String ERROR_DURATION_RS_EXCEEDS_CONSECUTIVE_WORK_MSG = "error.durationRSExceedsConsecutiveWork";
//    public final static String ERROR_DURATION_RS2_EXCEEDS_CONSECUTIVE_WORK = "errorDurationRS1ExceedsConsecutiveWork";
//    public final static String ERROR_DURATION_RS2_EXCEEDS_CONSECUTIVE_WORK_MSG = "error.durationRS2ExceedsConsecutiveWork";
    
    public final static String ERROR_FP1 = "errorFixedPeriod1";
    public final static String ERROR_FP1_MSG = "error.fixedPeriod1";
    
    public final static String ERROR_FP2 = "errorFixedPeriod2";
    public final static String ERROR_FP2_MSG = "error.fixedPeriod2";

    public final static String ERROR_DURATION_FP = "errorDurationFixedPeriod";
    public final static String ERROR_DURATION_FP_MSG = "error.durationFixedPeriod";

    public final static String ERROR_MEAL_BREAK = "errorMealBreak";
    public final static String ERROR_MEAL_BREAK_MSG = "error.mealBreak";
    
    public final static String ERROR_MEAL_OVER_FP = "errorMealBreakOverFixedPeriod";
    public final static String ERROR_MEAL_OVER_FP_MSG = "error.mealBreakOverFixedPeriod";
    
    public final static String ERROR_WEEKLY_DURATION_CJ_LIMITS = "errorWeeklyDurationCJLimits";
    public final static String ERROR_WEEKLY_DURATION_CJ_LIMITS_MSG = "error.WeeklyDurationCJLimits";
 
    public final static String ERROR_WEEKLY_DURATION = "errorWeeklyDuration";
    public final static String ERROR_WEEKLY_DURATION_MSG = "error.WeeklyDuration";
    
    public final static String ERROR_FP1_HN1 = "errorPF1HN1";
    public final static String ERROR_FP1_HN1_MSG = "error.PF1HN1";
    
    public final static String ERROR_DATES = "errorDates";
    public final static String ERROR_DATES_MSG = "error.Dates";
    
    
    /*** Domain constants ****/
    public static final Duration DURATION_MAXIMUM_WEEK_WORK = new Duration(35 * 3600 * 1000); //35 horas em milliseccs
    
    /* Expediente */
	public static final int MINIMUM_WORKDAY_MILLISECS = 7 * 3600 * 1000; // 7 hours per day in millisecs
	public static final long MAXIMUM_WORKDAY_MILLISECS = (30 * 3600 + 30 * 60) * 1000; // 30 hours and 30 minutes in millisecs
	
	// Expediente maximo e minimo
    public final static Duration DURATION_MINIMUM_WORKDAY = new Duration(MINIMUM_WORKDAY_MILLISECS);
    public final static Duration DURATION_MAXIMUM_WORKDAY = new Duration(MAXIMUM_WORKDAY_MILLISECS);

    // Meal
    public final static int MANDATORY_MEAL_DISCOUNT_MILLISECS = 1 * 3600 * 1000; // 1 hour
    public final static Duration MANDATORY_MEAL_DISCOUNT = new Duration(MANDATORY_MEAL_DISCOUNT_MILLISECS);
    public final static int MINIMUM_BREAK_INTERVAL_MILLISECS = 15 * 60 * 1000; // 15 minutes
    public final static Duration MINIMUM_BREAK_INTERVAL = new Duration(MINIMUM_BREAK_INTERVAL_MILLISECS);
        
	/* Regimes de Horários */
	public static final String REGIME_NORMAL = new String("normal");
	public static final String REGIME_TE = new String("trabalhadorEstudante");
	public static final String REGIME_IPF = new String("isencaoPeriodoFixo");
	public static final String REGIME_APOIOFAMILIA = new String("apoioFamilia");
	public static final String REGIME_AMAMENTACAO = new String("amamentacao");
	public static final String REGIME_ALEITACAO = new String("aleitacao");
	public static final String REGIME_MENORES12 = new String("assistenciaMenores12");
	public static final String REGIME_FILHOSDEFICIENTES = new String("filhosDeficientes");
	public static final String REGIME_FILHOSDEFICIENTES_MENORES1 = new String("filhosDeficientesMenores1");
	public static final String REGIME_MOTIVOSSAUDE = new String("motivosSaude");
	public static final String REGIME_SERVICO = new String("convenienciaServico");
	public static final String REGIME_OUTRAS_CIRCUNSTANCIAS = new String("outrasCircunstancias");
    public static final String REGIME_DIRIGENTES= "dirigentesChefes";
    
	/* Flexible Timetable */
//    public static final int FLEXIBLE_WEEK_DAYS = 5; // 5 working days per week TODO verify if this is necessary
    public static final long FLEXIBLE_HOURS_PER_DAY = 7 * 3600 * 1000; //  7 hours per day
    

    public static final Duration FLEXIBLE_WEEK_HOURS_DURATION = new Duration(35 * 3600 * 1000); // 35 hours per week in millisecs
//    public static final Duration FLEXIBLE_WEEK_HOURS_DURATION = Duration.hours(35); // 35 hours per week
	public static final Duration FLEXIBLE_DAY_DURATION = new Duration(FLEXIBLE_HOURS_PER_DAY); // (35 hours)/(5 days) => 7 hours per day 
	public static final Duration FLEXIBLE_PLATFORMS_DURATION = new Duration(4 * 3600 * 1000); //4 hours per day

    
	/* Continuous Journey Timetable */
	public static final Duration MIN_DURATION_CJ_WEEK_HOURS = new Duration(30 * 3600 * 1000); // 30 hours per week
	public static final Duration MAX_DURATION_CJ_WEEK_HOURS = new Duration(35 * 3600 * 1000); // 35 hours per week
	public static final Duration MIN_DURATION_CJ_DAY = new Duration(6 * 3600 * 1000); // (30 hours)/(5 days) => 6 hours per day
	public static final Duration MAX_DURATION_CJ_DAY = new Duration(7 * 3600 * 1000); // (35 hours)/(5 days) =>7 hours per day
	public static final Duration CJ_PLATFORMS_DURATION = new Duration(4 * 3600 * 1000); // 4 hours per day

	/* Exemption Timetable*/
	public static final int EXEMPTION_WEEK_DAYS = 5; // 5 days per week TODO verify if this is needed
	
	public static final Duration EXEMPTION_DAY_DURATION = new Duration(7* 3600 * 1000); // (35 hours)/(5 days) => 7 hours per day 
	public static final Duration EXEMPTION_WEEK_HOURS_DURATION = new Duration(35 * 3600 * 1000); // 35 hours per week

	/* Isencao 1 hora Timetable*/
	public static final int EXEMPTION_1HOUR_WEEK_DAYS = 5; // 5 days per week TODO verify if this is needed
	public static final Duration EXEMPTION_1HOUR_WEEK_HOURS_DURATION = new Duration(30* 3600 * 1000); // 30 hours per week
	public static final Duration EXEMPTION_1HOUR_DAY_DURATION = new Duration(6* 3600 * 1000); // (30 hours)/(5 days) => 6 hours per day
	public static final Duration EXEMPTION_1HOUR_PLATFORMS_DURATION = new Duration(4* 3600 * 1000);
	
	/* Isencao 2 horas Timetable*/
	public static final int EXEMPTION_2HOURS_WEEK_DAYS = 5; // 5 days per week TODO is needed?
	public static final Duration EXEMPTION_2HOURS_WEEK_HOURS_DURATION = new Duration(25* 3600 * 1000); // 25 hours per week
	public static final Duration EXEMPTION_2HOURS_DAY_DURATION = new Duration(5* 3600 * 1000);
	
	/* Horario Meio Tempo */
//	public static final int DAYS_HALFTIME_WEEK = 5; // 5 days per week TODO needed?!
	
    // TODO verificar isto!
	public static final Duration HALFTIME_WEEK_HOURS_DURATION = new Duration(17* 3600 * 1000 + 30 * 3600 * 1000); // 17.5 hours per week
	public static final Duration MIN_VALIDITY_DURATION = new Duration(31* 3600 * 1000); // 31 days
	public static final Duration MAX_VALIDITY_DURATION =  new Duration(2* 3600 * 1000); // 2 years
	public static final Duration HALFTIME_DAY_DURATION = new Duration(3* 3600 * 1000 + 30* 3600 * 1000); // (17.5 hours)/(5 days) => 3.5 hours per day

    // TODO check this out
    public static final TimeOfDay MORNING_PERIOD_END = new TimeOfDay(13,0,0); // The morning period ends at 13:00
    public static final Duration MAX_MORNING_WORK = new Duration(5 * 3600 * 1000); // 5 hours in milliseconds
    
	/* Trabalho Nocturno */
//	public final static TimeOfDay NIGHT_SHIFT_START = new TimeOfDay(20,0); // starts at 8 pm
//	public final static TimeOfDay NIGHT_SHIFT_END = new TimeOfDay(7,0); // ends at 7 pm next day
//	public final static TimeInterval NIGHT_SHIFT_PERIOD = new TimeInterval(NIGHT_SHIFT_START, NIGHT_SHIFT_END, true);

    
    
    
    public final static DateTime FAR_FUTURE = new DateTime(9999, 12, 31, 0, 0, 0, 0);
    
    // Timeline Constants
    public final static TimeOfDay TIMELINE_START = new TimeOfDay(0, 0, 0);
    public final static TimeOfDay TIMELINE_END = new TimeOfDay(23, 59, 59);
    
    public final static WorkWeek WORKDAYS = new WorkWeek(EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY));

    // Marcacoes
    // TODO um bocado foleiro...
    public final static EnumSet<AttributeType> WORKED_ATTRIBUTES_SET = EnumSet.range(AttributeType.WORKED1, AttributeType.WORKED5);
    public final static Attributes WORKED_ATTRIBUTES = new Attributes(WORKED_ATTRIBUTES_SET);
    
}
