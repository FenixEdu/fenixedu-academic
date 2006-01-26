/*
 * Created on Mar 6, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.util;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @author velouria@velouria.org
 * 
 */

public class UtilForm {

	/** 
     * Verifies if the form field is not null and is filled with something
     **/
	public static boolean validateFormField(String field) {
		return ((field != null) && (field.length() > 0));
    }

    // Field must be and integer.
	public static boolean validateInteger(String field) {
	    try {
	        new Integer(field).intValue();
	        return true;
	    } catch (java.lang.NumberFormatException e) {
	        return false;
	    }
	}

    // Validates an integer form field and adds the error message to ActionErrors var.
    public static boolean validateIntegerFormField(String field, ActionErrors errors, String errorItem, String errorMsg) {
        if (validateInteger(field)) {
            return true;
        } else {
            errors.add(errorItem, new ActionError(errorMsg));
            return false;
        }
    }
    
    // Validates a timetable with start and end hours and start and end minutes and adds the error message to ActionErrors var.
    public static boolean validateTimetableFormField(String startHours, String startMinutes, String endHours, String endMinutes, 
            ActionErrors errors, String errorItem, String errorMsg) {
        if (validateHours(startHours) && validateMinutes(startMinutes) && validateHours(endHours) && validateMinutes(endMinutes)) {
            return true;
        	} else {
        	    errors.add(errorItem, new ActionError(errorMsg));
        	    return false;
        	}        
    }
    
    // Validates a duration (hours and minutes) and adds the error message to ActionErrors var.
    public static boolean validateDurationFormField(String hours, String minutes, ActionErrors errors, String errorToken, String errorMsg) {
        if (validateHours(hours) && validateMinutes(minutes)) {
            return true;
        } else {
            errors.add(errorToken, new ActionError(errorMsg));
            return false;
        }
    }
    
    // Validates a date dd/mm/yy and adds the error message to ActionErrors var.
    public static boolean validateDateFormField(String day, String month, String year, ActionErrors errors, String errorToken, 
            String errorMsg) {
        if (validateDay(day) && validateMonth(month) && validateInteger(year)) {
            return true;
        } else {
            errors.add(errorToken, new ActionError(errorMsg));
            return false;
        }	
    	}
    
    
    
    // Validates a Date with start day/start month/start year and end day/end month/end year and adds the error message to ActionErrors var.
    public static boolean validateDateIntervalFormField(String startDay, String startMonth, String startYear, String endDay, String endMonth, 
            String endYear, ActionErrors errors, String errorToken, String errorMsg) {
        if (validateDay(startDay) && validateMonth(startMonth) && validateInteger(startYear) && validateDay(endDay) && 
                validateMonth(endMonth) && validateInteger(endYear)) {
            return true;
        } else {
            errors.add(errorToken, new ActionError(errorMsg));
            return false;
        }	
    	}
    
    // Hours must be an integer between 0 and 23.
    public static boolean validateHours(String hoursField) {
    		try {
    			int hours = new Integer(hoursField).intValue();
    			if ((hours >= 0) && (hours <= 23)) {
    				return true;
    			} else {
    				return false;
    			}
    		} catch (java.lang.NumberFormatException e) {
    			return false;
    		}
    	}
        
    // Minutes must be an integer between 0 and 59.
    public static boolean validateMinutes(String minutesField) {
		try {
			int minutes = new Integer(minutesField).intValue();
			if ((minutes >= 0) && (minutes <= 59)) {
				return true;
			} else {
				return false;
			}
		} catch (java.lang.NumberFormatException e) {
			return false;
		}
	}
    
    // Day must be an integer between 1 and 31.
    public static boolean validateDay(String dayField) {
    		try {
			int day = new Integer(dayField).intValue();
			if ((day >= 1) && (day <= 31)) {
				return true;
			} else {
				return false;
			}
		} catch (java.lang.NumberFormatException e) {
			return false;
		}   	
    }
    
    //  Month must be an integer between 1 and 12.
    public static boolean validateMonth(String monthField) {
    	try {
    		int month = new Integer(monthField).intValue();
			if ((month >= 1) && (month <= 12)) {
				return true;
			} else {
				return false;
			}
		} catch (java.lang.NumberFormatException e) {
			return false;
		}   	
    }
    
    // Add a 0 to the day and month when < 10.
    public static String addZeroToDate(int number) {
        String stringNumber = String.valueOf(number);
        if (number < 10) {
            return "0" + stringNumber;
        } else {
            return stringNumber;
        }
    }
    
   
	private static long convertHoursToMillisecs(int hours) {
	    return (hours * 3600 * 1000);
	}
	
	private static long convertMinutesToMillisecs(int minutes) {
	    return (minutes * 60 * 1000);
	}

//    // Validate Specific items
//    public static void validateRegularSchedule(String startRSHours, String startRSMinutes, String endRSHours, String endRSMinutes, String errorMsg, ArrayList errorList) {
//        if (UtilForm.validateFormField(startRSHours) && UtilForm.validateFormField(startRSMinutes) && UtilForm.validateFormField(endRSHours) && 
//                UtilForm.validateFormField(endRSMinutes)) {
//            UtilForm.validateTimetableFormField(startRSHours, startRSMinutes, endRSHours, endRSMinutes, errors, "horarioNormal2", "error.horarioNormal2.invalid");
//	} else {
//	    errors.add("horarioNormal2", new ActionError("error.horarioNormal2.mandatory"));
//	}
//
    
    
    
}
