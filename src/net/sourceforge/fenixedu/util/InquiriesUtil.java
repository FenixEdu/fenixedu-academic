/*
 * Created on Nov 30, 2004
 * 
 */
package net.sourceforge.fenixedu.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InquiriesUtil extends FenixUtil {
    
	public static final String STUDENT_ATTENDS= "studentAttends";
	public static final String EVALUATED_STUDENT_ATTENDS = "evaluatedStudentAttends";
	public static final String ATTENDING_COURSE_TEACHERS = "attendingCourseTeachers";
	public static final String ATTENDING_EXECUTION_COURSE = "attendingExecutionCourse";
	public static final String STUDENT_ATTENDS_ID = "studentAttendsId";
	public static final String ATTENDING_COURSE_TEACHER_ID = "attendingCourseTeacherId";
	public static final String CLASS_TYPE = "classType";
	public static final String ATTENDING_COURSE_ROOMS = "attendingCourseRooms";
	public static final String ATTENDING_COURSE_ROOM_ID = "attendingCourseRoomId";
	public static final String ATTENDING_COURSE_SCHOOL_CLASSES = "attendingCourseSchoolClasses";
	
	public static final String CURRENT_EXECUTION_PERIOD = "currentExecutionPeriod";
	public static final String STUDENT_EXECUTION_DEGREE = "studentExecutionDegree";
	public static final String ATTENDING_COURSE_EXECUTION_DEGREES = "attendingCourseExecutionDegrees";
	public static final String ATTENDING_COURSE_EXECUTION_DEGREE = "attendingCourseExecutionDegree";
	
	public static final String INFO_STUDENT = "infoStudent";
	
	public static final String SESSION_INQUIRY = "sessionInquiry";
	public static final String ATTENDING_COURSE_TEACHER = "attendingCourseTeacher";
	public static final String SELECTED_ATTENDING_COURSE_TEACHERS = "selectedAttendingCourseTeachers";
	public static final String CURRENT_ATTENDING_COURSE_TEACHER = "currentAttendingCourseTeacher";
	public static final String CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION = "currentAttendingCourseTeacherFormPosition";
	
	public static final String COURSE_FORM_ERROR = "courseFormError";
	public static final String STUDENT_FORM_ERROR = "studentFormError";
	public static final String CURRENT_ATTENDING_COURSE_TEACHER_FORM_ERROR = "currentAttendingCourseTeacherFormError";
	
	public static final String COMPLETE_ATTENDING_COURSE_TEACHER_ID = "completeAttendingCourseTeacherId";
	public static final String COMPLETE_ATTENDING_COURSE_NON_AFFILIATED_TEACHER_ID = "completeAttendingCourseNonAffiliatedTeacherId";

	public static final String CURRENT_ATTENDING_COURSE_ROOM = "currentAttendingCourseRoom";
	public static final String COMPLETE_ATTENDING_COURSE_ROOM_ID = "completeAttendingCourseRoomId";
	public static final String SELECTED_ATTENDING_COURSE_ROOMS = "selectedAttendingCourseRooms";
	public static final String CURRENT_ATTENDING_COURSE_ROOM_FORM_ERROR = "currentAttendingCourseRoomFormError";
	
	public static final String ANCHOR = "anchor";
	public static final String STUDENT_FORM_ANCHOR = "studentForm";
	public static final String ATTENDING_COURSE_FORM_ANCHOR = "attendingCourseForm";
	public static final String ATTENDING_COURSE_TEACHER_FORM_ANCHOR = "attendingCourseTeacherForm";
	public static final String CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR = "currentAttendingCourseTeacherForm";
	public static final String ATTENDING_COURSE_ROOM_FORM_ANCHOR = "attendingCourseRoomForm";
	public static final String CURRENT_ATTENDING_COURSE_ROOM_FORM_ANCHOR = "currentAttendingCourseRoomForm";
	public static final String COMPLETE_ATTENDING_COURSE_ROOM_FORM_ANCHOR = "completeAttendingCourseRoomForm";
	public static final String SUBMIT_FORM_ANCHOR = "submitFormAnchor";
	
	public static final String INQUIRY = "inquiry";
	public static final String INFO_ATTENDING_INQUIRIES_COURSE = "infoAttendingInquiriesCourse";
	public static final String NO_ATTENDING_COURSE_TEACHER_FORM_ERROR = "noAttendingCourseTeacherForm";
	

	public static String formatAnswer(Double answer) {
        String result = "-";
        double ans = answer.doubleValue();
        
        if((answer != null) && (ans > 0)) {
            result = Math.floor(ans) == ans ?
                    String.valueOf(answer.intValue()) : String.valueOf(ans);
        }
        return result;
    }
    
    public static String formatAnswer(Integer answer) {
        String result = "-";
        int ans = answer.intValue();
        
        if((answer != null) && (ans > 0))
            result = "" + ans;
        
        return result;
    }

    
    public static String formatAnswer(String str) {
        return ((str == null || str.length() == 0) ? "-" : str);
    }
    
    public static String formatAnswer(Double answer, Double repQuota, Double minRepQuota) {
        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return formatAnswer(answer);

        }
        return "-";
    }
    
    public static String formatAnswer(String answer, Double repQuota, Double minRepQuota) {
        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return formatAnswer(answer);

        }
        return "-";
    }
    
    
    public static String getTdClass(Double val, final String[] classes, final String defaultClass, final double[] values) {
        String classVal = defaultClass;

        if(val == null)
            return classVal;
        
        if(classes.length == (values.length-1)) {
            double v = val.doubleValue();
            for(int i = 0; i < classes.length; i++) {
                if((v >= values[i]) && (v < values[i+1])) {
                    classVal = classes[i];
                    break;
                }
            }
            
        }
        return classVal;
    }
    
    public static String getTdClass(String val, final String[] classes, final String defaultClass, final String[] values) {
        String classVal = defaultClass;

        if(val == null)
            return classVal;
        
        if(classes.length == values.length) {
            for(int i = 0; i < classes.length; i++) {
                if(val.equalsIgnoreCase(values[i])) {
                    classVal = classes[i];
                    break;
                }
            }
            
        }
        return classVal;
        
    }
	
	public static boolean isValidAnswer(Number answer) {
		return ((answer != null) && (answer.doubleValue() > 0) && (answer.doubleValue() <= 5));
	}
    
    public static String getTdClass(Double val, final String[] classes, final String defaultClass,
            						final double[] values, Double repQuota, Double minRepQuota) {

        if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
            return getTdClass(val, classes, defaultClass, values);

        }
        return defaultClass;
    }

    public static String getTdClass(String val, final String[] classes, final String defaultClass,
            						final String[] values, Double repQuota, Double minRepQuota) {

		if(repQuota.doubleValue() > minRepQuota.doubleValue()) {
		    return getTdClass(val, classes, defaultClass, values);
		
		}
        return defaultClass;
	}
	

	public static Object getFromRequest(String name,
			HttpServletRequest request) {
		Object parameter = null;

		parameter = request.getParameter(name);
		if (parameter == null) {
			parameter = request.getAttribute(name);
		}

		return parameter;
	}
	
	/**
	 * @return Returns the list containing with only the first occurrence of the elementes based on the equals method
	 *             the result list is ordered by the idInternal
	 */
	public static void removeDuplicates(List beanList) {
		if(beanList.isEmpty())
			return;
		
		Collections.sort(beanList, new BeanComparator ("idInternal"));
		Iterator iter = beanList.iterator();
		InfoObject prev = (InfoObject) iter.next();
		while(iter.hasNext()) {
			InfoObject curr = (InfoObject) iter.next();
			if(curr.equals(prev))
				iter.remove();
			prev = curr;
		}
	}


}
