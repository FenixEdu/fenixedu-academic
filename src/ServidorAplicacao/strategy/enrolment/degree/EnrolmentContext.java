/*
 * Created on 3/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;

/**
 * @author jpvl
 */
public final class EnrolmentContext {
	private IStudent student;
	private List curricularCoursesDoneByStudent;
	private Map acumulatedEnrolments;
	private Integer semester;
	private List validateMessage;

	private IStudentCurricularPlan studentActiveCurricularPlan;

	private List actualEnrolment;
	//	private List curricularCoursesFromStudentCurricularPlan;

	/**
	 * defines the list that student can be enrolled
	 */
	private List finalCurricularCoursesScopesSpanToBeEnrolled;

	/**
	 *  
	 */
	protected EnrolmentContext() {
		super();
		actualEnrolment = new ArrayList();
	}

	/**
	 * @return
	 */
	public List getCurricularCoursesDoneByStudent() {
		return curricularCoursesDoneByStudent;
	}

	/**
	 * @return
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * @return
	 */
	public IStudent getStudent() {
		return student;
	}

	/**
	 * @param list
	 */
	public void setCurricularCoursesDoneByStudent(List list) {
		curricularCoursesDoneByStudent = list;
	}

	/**
	 * @param integer
	 */
	public void setSemester(Integer integer) {
		semester = integer;
	}

	/**
	 * @param student
	 */
	public void setStudent(IStudent student) {
		this.student = student;
	}

	/**
	 * @return
	 */
	public List getFinalCurricularCoursesScopesSpanToBeEnrolled() {
		return finalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * @param list
	 */
	public void setFinalCurricularCoursesScopesSpanToBeEnrolled(List list) {
		finalCurricularCoursesScopesSpanToBeEnrolled = list;
	}

	/**
	 * @return
	 */
	public Map getAcumulatedEnrolments() {
		return acumulatedEnrolments;
	}

	/**
	 * @param map
	 */
	public void setAcumulatedEnrolments(Map map) {
		acumulatedEnrolments = map;
	}

	/**
	 * @return
	 */
	public List getActualEnrolment() {
		return actualEnrolment;
	}

	/**
	 * @return
	 */
	public IStudentCurricularPlan getStudentActiveCurricularPlan() {
		return studentActiveCurricularPlan;
	}

	/**
	 * @param list
	 */
	public void setActualEnrolment(List list) {
		actualEnrolment = list;
	}

	/**
	 * @param plan
	 */
	public void setStudentActiveCurricularPlan(IStudentCurricularPlan plan) {
		studentActiveCurricularPlan = plan;
	}

	//	/**
	//	 * @return List
	//	 */
	//	public List getCurricularCoursesFromStudentCurricularPlan() {
	//		return curricularCoursesFromStudentCurricularPlan;
	//	}
	//
	//	/**
	//	 * Sets the curricularCoursesFromStudentCurricularPlan.
	//	 * @param curricularCoursesFromStudentCurricularPlan The curricularCoursesFromStudentCurricularPlan to set
	//	 */
	//	public void setCurricularCoursesFromStudentCurricularPlan(List curricularCoursesFromStudentCurricularPlan) {
	//		this.curricularCoursesFromStudentCurricularPlan = curricularCoursesFromStudentCurricularPlan;
	//	}
	//
	/**
	 * @return List
	 */
	public List getValidateMessage() {
		return validateMessage;
	}

	/**
	 * Sets the validateMessage.
	 * @param validateMessage The validateMessage to set
	 */
	public void setValidateMessage(List validateMessage) {
		this.validateMessage = validateMessage;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "acumulatedEnrolments = " + this.acumulatedEnrolments + "]";
		result += "\n StudentActiveCurricularPlan = " + this.studentActiveCurricularPlan + "]";
		result += "\n actualEnrolment = " + this.actualEnrolment + "]";
		result += "\n finalCurricularCoursesScopesSpanToBeEnrolled = " + this.finalCurricularCoursesScopesSpanToBeEnrolled + "]";
		result += "\n validade message = " + this.validateMessage + "]";
		result += "\n student = " + this.student + "]";
		return result;
	}	
}
