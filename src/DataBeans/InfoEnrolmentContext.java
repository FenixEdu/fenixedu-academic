package DataBeans;

import java.util.List;
import java.util.Map;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoEnrolmentContext {

	private InfoStudent infoStudent;
	private List curricularCoursesDoneByStudent;
	private Map acumulatedEnrolments;
	private Integer semester;
	private InfoStudentCurricularPlan infoStudentActiveCurricularPlan;
	private List actualEnrolment;
	private List finalCurricularCoursesScopesSpanToBeEnrolled;
	
	public InfoEnrolmentContext() {
		setInfoStudent(null);
		setCurricularCoursesDoneByStudent(null);
		setAcumulatedEnrolments(null);
		setSemester(null);
		setInfoStudentActiveCurricularPlan(null);
		setActualEnrolment(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoEnrolmentContext) {
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) obj;
			resultado = this.finalCurricularCoursesScopesSpanToBeEnrolled.equals(infoEnrolmentContext.finalCurricularCoursesScopesSpanToBeEnrolled) &&
									(this.infoStudent.equals(infoEnrolmentContext.infoStudent));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "student = " + this.infoStudent + "; ";
		result += "curricularCoursesDoneByStudent = " + this.curricularCoursesDoneByStudent + "]";
		result += "acumulatedEnrolments = " + this.acumulatedEnrolments + "]";
		result += "semester = " + this.semester + "]";
		result += "StudentActiveCurricularPlan = " + this.infoStudentActiveCurricularPlan + "]";
		result += "actualEnrolment = " + this.actualEnrolment + "]";
		result += "finalCurricularCoursesScopesSpanToBeEnrolled = " + this.finalCurricularCoursesScopesSpanToBeEnrolled + "]";
		return result;
	}

	/**
	 * @return List
	 */
	public List getActualEnrolment() {
		return actualEnrolment;
	}

	/**
	 * @return Map
	 */
	public Map getAcumulatedEnrolments() {
		return acumulatedEnrolments;
	}

	/**
	 * @return List
	 */
	public List getCurricularCoursesDoneByStudent() {
		return curricularCoursesDoneByStudent;
	}

	/**
	 * @return List
	 */
	public List getFinalCurricularCoursesScopesSpanToBeEnrolled() {
		return finalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * @return InfoStudent
	 */
	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	/**
	 * @return InfoStudentCurricularPlan
	 */
	public InfoStudentCurricularPlan getInfoStudentActiveCurricularPlan() {
		return infoStudentActiveCurricularPlan;
	}

	/**
	 * @return Integer
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Sets the actualEnrolment.
	 * @param actualEnrolment The actualEnrolment to set
	 */
	public void setActualEnrolment(List actualEnrolment) {
		this.actualEnrolment = actualEnrolment;
	}

	/**
	 * Sets the acumulatedEnrolments.
	 * @param acumulatedEnrolments The acumulatedEnrolments to set
	 */
	public void setAcumulatedEnrolments(Map acumulatedEnrolments) {
		this.acumulatedEnrolments = acumulatedEnrolments;
	}

	/**
	 * Sets the curricularCoursesDoneByStudent.
	 * @param curricularCoursesDoneByStudent The curricularCoursesDoneByStudent to set
	 */
	public void setCurricularCoursesDoneByStudent(List curricularCoursesDoneByStudent) {
		this.curricularCoursesDoneByStudent = curricularCoursesDoneByStudent;
	}

	/**
	 * Sets the finalCurricularCoursesScopesSpanToBeEnrolled.
	 * @param finalCurricularCoursesScopesSpanToBeEnrolled The finalCurricularCoursesScopesSpanToBeEnrolled to set
	 */
	public void setFinalCurricularCoursesScopesSpanToBeEnrolled(List finalCurricularCoursesScopesSpanToBeEnrolled) {
		this.finalCurricularCoursesScopesSpanToBeEnrolled = finalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * Sets the infoStudent.
	 * @param infoStudent The infoStudent to set
	 */
	public void setInfoStudent(InfoStudent infoStudent) {
		this.infoStudent = infoStudent;
	}

	/**
	 * Sets the infoStudentActiveCurricularPlan.
	 * @param infoStudentActiveCurricularPlan The infoStudentActiveCurricularPlan to set
	 */
	public void setInfoStudentActiveCurricularPlan(InfoStudentCurricularPlan infoStudentActiveCurricularPlan) {
		this.infoStudentActiveCurricularPlan = infoStudentActiveCurricularPlan;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

}