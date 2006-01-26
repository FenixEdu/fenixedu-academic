/*
 * Created on Apr 6, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.util;

/**
 * @author velouria
 *
 */
public final class PresentationConstants {

	// Session constants
	public final static String SESSION_EMPLOYEE = "sessionEmployee";
	public final static String SESSION_PERSON = "sessionPerson";
	public final static String SESSION_PERSON_IN_SESSION = "sessionPersonInSession";
	public final static String SESSION_TIMETABLE = "sessionTimetable";
    public final static String SCHEDULE = "schedule";
	
	// Struts forward mapping constants JSPs
	public final static String FORWARD_SHOW_EMPLOYEE = "ShowEmployee";
	public final static String FORWARD_ASSOCIATE_FLEXIBLE_TIMETABLE = "AssociateFlexibleTimetable";
	public final static String FORWARD_ASSOCIATE_CONTINUOUSJ_TIMETABLE = "AssociateContinuousJourneyTimetable";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_FROM_TIMETABLE ="AssociateExemptionFromTimetable";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_1HOUR_TIMETABLE ="AssociateExemption1HourTimetable";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_2HOURS_TIMETABLE ="AssociateExemption2HoursTimetable";
	public final static String FORWARD_ASSOCIATE_HALFTIME_TIMETABLE ="AssociateHalfTimeTimetable";
	public final static String FORWARD_ASSOCIATE_ADJUSTED_FLEXIBLE_TIMETABLE = "AssociateAdjustedFlexibleTimetable";
	
	// Struts forward mapping constants Actions
	public final static String FORWARD_POPULATE_ASSOCIATE_FLEXIBLE_TIMETABLE_ACTION = "PopulateAssociateFlexibleTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_CONTINUOUSJ_TIMETABLE_ACTION = "PopulateAssociateContinuousJourneyTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_EXEMPTION_FROM_TIMETABLE_ACTION = "PopulateAssociateExemptionFromTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_EXEMPTION_1HOUR_TIMETABLE_ACTION = "PopulateAssociateExemption1HourTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_EXEMPTION_2HOURS_TIMETABLE_ACTION = "PopulateAssociateExemption2HoursTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_HALFTIME_TIMETABLE_ACTION = "PopulateAssociateHalfTimeTimetableAction";
	public final static String FORWARD_POPULATE_ASSOCIATE_ADJUSTED_FLEXIBLE_TIMETABLE_ACTION = "PopulateAssociateAdjustedFlexibleTimetableAction";

	// Struts forward mapping constans Action Confirm
	public final static String FORWARD_ASSOCIATE_FLEXIBLE_TIMETABLE_CONFIRM = "AssociateFlexibleTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_CONTINUOUSJ_TIMETABLE_CONFIRM = "AssociateContinuousJourneyTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_FROM_TIMETABLE_CONFIRM = "AssociateExemptionFromTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_1HOUR_TIMETABLE_CONFIRM = "AssociateExemption1HourTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_EXEMPTION_2HOURS_TIMETABLE_CONFIRM = "AssociateExemption2HourTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_HALFTIME_TIMETABLE_CONFIRM = "AssociateHalfTimeTimetableConfirm";
	public final static String FORWARD_ASSOCIATE_ADJUSTED_FLEXIBLE_TIMETABLE_CONFIRM = "AssociateAdjustedFlexibleTimetableConfirm";

	
	// Timetable form constants
	public final static String FLEXIBLE_TIMETABLE_TYPE = "flexible";
	public final static String CONTINUOUSJ_TIMETABLE_TYPE = "continuousJourney";
	public final static String EXEMPTION_FROM_TIMETABLE_TYPE = "exemption";
	public final static String EXEMPTION_1HOUR_TIMETABLE_TYPE = "exemption1Hour";
	public final static String EXEMPTION_2HOURS_TIMETABLE_TYPE = "exemption2Hours";
	public final static String HALFTIME_TIMETABLE_TYPE = "halfTime";
	public final static String ADJUSTED_FLEXIBLE_TIMETABLE_TYPE = "adjustedFlexible";
	
	// time stuff
	// TODO put this in preferences!
//	public final static String TIMEZONE = "GMT+1";
	public final static String TIMEZONE = "Universal";
	
	//form key constants
	public final static String RESPONSIBLE_PERSON = "responsiblePerson";
	public final static String PERSON = "person";
	public final static String EMPLOYEE = "employee";
	
	public final static String START_WORK_DAY_MINUTES = "startWorkDayMinutes"; // expediente
	public final static String START_WORK_DAY_HOURS = "startWorkDayHours";
	public final static String END_WORK_DAY_MINUTES = "endWorkDayMinutes"; // expediente
	public final static String END_WORK_DAY_HOURS = "endWorkDayHours";
	public final static String NEXT_DAY_WORK_DAY = "nextDayWorkDay";
	
	public final static String START_MEAL_BREAK_MINUTES = "startMealBreakMinutes"; // periodo de refeicao
	public final static String START_MEAL_BREAK_HOURS = "startMealBreakHours";
	public final static String END_MEAL_BREAK_MINUTES = "endMealBreakMinutes";
	public final static String END_MEAL_BREAK_HOURS = "endMealBreakHours";
	public final static String NEXT_DAY_MEAL_BREAK = "nextDayMealBreak";
	
	public final static String MIN_MEAL_BREAK_INTERVAL_MINUTES = "minMealBreakIntervalMinutes";
	public final static String MIN_MEAL_BREAK_INTERVAL_HOURS = "minMealBreakIntervalHours";

	public final static String MANDATORY_MEAL_DISCOUNT_MINUTES = "mandatoryMealDiscountMinutes";
	public final static String MANDATORY_MEAL_DISCOUNT_HOURS = "mandatoryMealDiscountHours";
	
	public final static String START_REGULAR_SCHEDULE_1_MINUTES = "startRegularSchedule1Minutes";
	public final static String START_REGULAR_SCHEDULE_1_HOURS = "startRegularSchedule1Hours";
	public final static String END_REGULAR_SCHEDULE_1_MINUTES = "endRegularSchedule1Minutes";
	public final static String END_REGULAR_SCHEDULE_1_HOURS = "endRegularSchedule1Hours";
	public final static String NEXT_DAY_REGULAR_SCHEDULE_1 = "nextDayRegularSchedule1";

	public final static String START_REGULAR_SCHEDULE_2_MINUTES = "startRegularSchedule2Minutes";
	public final static String START_REGULAR_SCHEDULE_2_HOURS = "startRegularSchedule2Hours";
	public final static String END_REGULAR_SCHEDULE_2_MINUTES = "endRegularSchedule2Minutes";
	public final static String END_REGULAR_SCHEDULE_2_HOURS = "endRegularSchedule2Hours";
	public final static String NEXT_DAY_REGULAR_SCHEDULE_2 = "nextDayRegularSchedule2";
	
	public final static String START_FIXED_PERIOD_1_MINUTES ="startFixedPeriod1Minutes";
	public final static String START_FIXED_PERIOD_1_HOURS ="startFixedPeriod1Hours";
	public final static String END_FIXED_PERIOD_1_MINUTES ="endFixedPeriod1Minutes";
	public final static String END_FIXED_PERIOD_1_HOURS ="endFixedPeriod1Hours";
	public final static String NEXT_DAY_FIXED_PERIOD_1 = "nextDayFixedPeriod1";

	public final static String START_FIXED_PERIOD_2_MINUTES ="startFixedPeriod2Minutes";
	public final static String START_FIXED_PERIOD_2_HOURS ="startFixedPeriod2Hours";
	public final static String END_FIXED_PERIOD_2_MINUTES ="endFixedPeriod2Minutes";
	public final static String END_FIXED_PERIOD_2_HOURS ="endFixedPeriod2Hours";
	public final static String NEXT_DAY_FIXED_PERIOD_2 = "nextDayFixedPeriod2";
	
	public final static String START_DAY = "startDay";
	public final static String START_MONTH = "startMonth";
	public final static String START_YEAR = "startYear";
	public final static String END_DAY = "endDay";
	public final static String END_MONTH = "endMonth";
	public final static String END_YEAR = "endYear";
	
	public final static String CONSECUTIVE_WORK = "consecutiveWork";
	public final static String CONSECUTIVE_WORK_MINUTES = "consecutiveWorkMinutes";
	public final static String CONSECUTIVE_WORK_HOURS = "consecutiveWorkHours";

	public final static String REGIMES = "regimes"; 
	
	public final static String EXCEPTION_TIMETABLE = "exceptionTimetable"; // TODO ask jey

    public final static String WORK_WEEK = "workWeek";
    
	public final static String WORK_MONDAY = "workMonday";
	public final static String WORK_TUESDAY = "workTuesday";
	public final static String WORK_WEDNESDAY = "workWednesday";
	public final static String WORK_THURSDAY = "workThursday";
	public final static String WORK_FRIDAY = "workFriday";
	public final static String WORK_EVERYDAY = "workEveryday";
	
	public final static String WEEKLY_DURATION_HOURS = "weeklyDurationHours";
	public final static String WEEKLY_DURATION_MINUTES = "weeklyDurationMinutes";
	
    public final static String SCHEDULE_TYPE = "scheduleType";
}
