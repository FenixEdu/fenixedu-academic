/*
 * InfoShiftEnrolment.java
 *
 * Created on December 20th, 2002, 05:26
 */

package DataBeans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  tdi-dev
 */
public class InfoShiftStudentEnrolment extends InfoObject {
	
	/**
	 * List with the executionCourses in which the student is enrolled
	 */
	private List enrolledExecutionCourses;
	/**
	 * List with the student lessons for the time table display
	 */
	private List lessons;

	/**    
	 * List containing all the classes (InfoClass) in wich the student is allowed to be in.
	 */
	private List allowedClasses;
	
	
	/**    
	 * List containing all the shifts (InfoShifts) that the student is enroled in.
	 */
	private List currentEnrolment;
	
	/**    
	 * List containing all the shifts that the student is able to chose. 
	 * It is calculated in the ReadStudentShiftEnrolment service, 
	 * and it is used in the action to display data.
	 * It contains objects of the type InfoShiftWithAssociatedInfoClassesAndLessons:
	 * 	1 - InfoShift
	 * 	2 - ArrayList: classes (InfoClass) associated with the shift
	 *     3 - ArrayList: lessons (InfoLessons) associated with that shift
	 */
	private List availableShift;

	/**    
	 * List containing all the shifts (InfoShifts) that the student chose. It also contains the current enrolment. This means this list has the wanted configuration for the student.
	 */
	private List wantedShifts;

	/**    
	 * List containing all the shifts (InfoShifts) that the student chose that are currently filled, that is, they have 0 availlability
	 */
	private List filledShifts;

	/**    
	 * List containing vectors with two elements each, of the type InfoShift. The semantic is as follows:
	 *    - [null, shiftXXX]  -->  a new shift (shiftXXX) was added in the database for the student.
	 *    - [shiftXXX, null]  -->  the shift (shiftXXX) was removed from the database for the student.
	 *    - [shiftXXX, shiftYYY]  -->  the shift (shiftXXX) was removed from the database for the student and the new shift (shiftYYY) was added. They are of the same course, and of the same type.
	 */
	private List newShifts;
	
	/**
	 * List containing errors. These are objects of the type ValidateAndConfirmShiftStudentEnrolment.ShiftConflict, 
	 * and contains the Shift that have problems and a message string that are bound to the resources for an appropriate error message.  
	 */
	private List errors;

	/**  
	   * List containing a divided list with a list of lists of InfoShiftWithAssociatedInfoClassesAndInfoLessons associated 
       * with the corresponding class
	   * @see ServidorApresentacao.Action.student.ShiftStudentEnrolmentManagerDispatchAction.InfoShiftDividedList  
	   */  
	private ArrayList dividedList;

    /**
     * Element containing the information about the student that is enroling
     */
	private InfoStudent InfoStudent;

	/**
	 * @return
	 */
	public List getEnrolledExecutionCourses() {
		return enrolledExecutionCourses;
	}

	/**
	 * @param enrolledExecutionCourses
	 */
	public void setEnrolledExecutionCourses(List enrolledExecutionCourses) {
		this.enrolledExecutionCourses = enrolledExecutionCourses;
	}

	/**
	 * @return
	 */
	public List getLessons() {
		return lessons;
	}

	/**
	 * @param lessons
	 */
	public void setLessons(List lessons) {
		this.lessons = lessons;
	}

	public InfoShiftStudentEnrolment() {
	}

	public String toString() {
		String result = "[INFOSHIFTSTUDENTENROLMENT";
		result += "]";
		return result;
	}

	/**
	 * @return
	 */
	public List getAvailableShift() {
		return availableShift;
	}

	/**
	 * @return
	 */
	public List getCurrentEnrolment() {
		return currentEnrolment;
	}

	/**
	 * @return
	 */
	public InfoStudent getInfoStudent() {
		return InfoStudent;
	}

	/**
	 * @param list
	 */
	public void setAvailableShift(List list) {
		availableShift = list;
	}

	/**
	 * @param list
	 */
	public void setCurrentEnrolment(List list) {
		currentEnrolment = list;
	}

	/**
	 * @param student
	 */
	public void setInfoStudent(InfoStudent student) {
		InfoStudent = student;
	}

	/**
	 * @return
	 */
	public List getWantedShifts() {
		return wantedShifts;
	}

	/**
	 * @param list
	 */
	public void setWantedShifts(List list) {
		wantedShifts = list;
	}

	/**
	 * @return
	 */
	public List getFilledShifts() {
		return filledShifts;
	}

	/**
	 * @return
	 */
	public List getNewShifts() {
		return newShifts;
	}

	/**
	 * @param list
	 */
	public void setFilledShifts(List list) {
		filledShifts = list;
	}

	/**
	 * @param list
	 */
	public void setNewShifts(List list) {
		newShifts = list;
	}

	/**
	 * @return
	 */
	public List getErrors() {
		return errors;
	}

	/**
	 * @param list
	 */
	public void setErrors(List list) {
		errors = list;
	}

	/**
	 * @return
	 */
	public ArrayList getDividedList() {
		return dividedList;
	}

	/**
	 * @param map
	 */
	public void setDividedList(ArrayList map) {
		dividedList = map;
	}

	/**
	 * @return
	 */
	public List getAllowedClasses() {
		return allowedClasses;
	} 

	/**
	 * @param list
	 */
	public void setAllowedClasses(List list) {
		allowedClasses = list;
	}

	

}
