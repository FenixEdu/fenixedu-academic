package ServidorAplicacao.strategy.enrolment.degree;

import java.util.List;
import java.util.Map;

import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;


/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class InfoEnrolmentContext {

	private Map acumulatedEnrolments;
	private InfoStudentCurricularPlan infoStudentActiveCurricularPlan;
	private List actualEnrolment;
	private List infoFinalCurricularCoursesScopesSpanToBeEnrolled;
	private InfoStudent infoStudent;
	private Integer semester;
	private EnrolmentValidationResult enrolmentValidationResult;
	private List infoCurricularCoursesScopesEnroledByStudent;
	
	public InfoEnrolmentContext() {
		setAcumulatedEnrolments(null);
		setInfoStudentActiveCurricularPlan(null);
		setActualEnrolment(null);
		setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(null);
		setInfoStudent(null);
		setSemester(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof InfoEnrolmentContext) {
			InfoEnrolmentContext infoEnrolmentContext = (InfoEnrolmentContext) obj;
			resultado = this.infoFinalCurricularCoursesScopesSpanToBeEnrolled.equals(infoEnrolmentContext.infoFinalCurricularCoursesScopesSpanToBeEnrolled) &&
									(this.infoStudentActiveCurricularPlan.equals(infoEnrolmentContext.infoStudentActiveCurricularPlan));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "acumulatedEnrolments = " + this.acumulatedEnrolments + "\n";
		result += "StudentActiveCurricularPlan = " + this.infoStudentActiveCurricularPlan + "\n";
		result += "actualEnrolment = " + this.actualEnrolment + "\n";
		result += "infoFinalCurricularCoursesScopesSpanToBeEnrolled = " + this.infoFinalCurricularCoursesScopesSpanToBeEnrolled + "\n";
		result += "enrolmentValidationResult = " + this.enrolmentValidationResult + "\n";
		result += "student = " + this.infoStudent + "\n";
		result += "semester= " + this.semester + "]";

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
	public List getInfoFinalCurricularCoursesScopesSpanToBeEnrolled() {
		return infoFinalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * @return InfoStudentCurricularPlan
	 */
	public InfoStudentCurricularPlan getInfoStudentActiveCurricularPlan() {
		return infoStudentActiveCurricularPlan;
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
	 * Sets the infoFinalCurricularCoursesScopesSpanToBeEnrolled.
	 * @param infoFinalCurricularCoursesScopesSpanToBeEnrolled The infoFinalCurricularCoursesScopesSpanToBeEnrolled to set
	 */
	public void setInfoFinalCurricularCoursesScopesSpanToBeEnrolled(List finalCurricularCoursesScopesSpanToBeEnrolled) {
		this.infoFinalCurricularCoursesScopesSpanToBeEnrolled = finalCurricularCoursesScopesSpanToBeEnrolled;
	}

	/**
	 * Sets the infoStudentActiveCurricularPlan.
	 * @param infoStudentActiveCurricularPlan The infoStudentActiveCurricularPlan to set
	 */
	public void setInfoStudentActiveCurricularPlan(InfoStudentCurricularPlan infoStudentActiveCurricularPlan) {
		this.infoStudentActiveCurricularPlan = infoStudentActiveCurricularPlan;
	}

	/**
	 * @return InfoStudent
	 */
	public InfoStudent getInfoStudent() {
		return infoStudent;
	}

	/**
	 * Sets the infoStudent.
	 * @param infoStudent The infoStudent to set
	 */
	public void setInfoStudent(InfoStudent infoStudent) {
		this.infoStudent = infoStudent;
	}

	/**
	 * @return Integer
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * Sets the semester.
	 * @param semester The semester to set
	 */
	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	/**
	 * @return EnrolmentValidationResult
	 */
	public EnrolmentValidationResult getEnrolmentValidationResult() {
		return enrolmentValidationResult;
	}

	/**
	 * Sets the enrolmentValidationResult.
	 * @param enrolmentValidationResult The enrolmentValidationResult to set
	 */
	public void setEnrolmentValidationResult(EnrolmentValidationResult enrolmentValidationError) {
		this.enrolmentValidationResult = enrolmentValidationError;
	}

	/**
	 * @return List
	 */
	public List getInfoCurricularCoursesScopesEnroledByStudent() {
		return infoCurricularCoursesScopesEnroledByStudent;
	}

	/**
	 * Sets the infoCurricularCoursesScopesEnroledByStudent.
	 * @param infoCurricularCoursesScopesEnroledByStudent The infoCurricularCoursesScopesEnroledByStudent to set
	 */
	public void setInfoCurricularCoursesScopesEnroledByStudent(List infoCurricularCoursesScopesEnroledByStudent) {
		this.infoCurricularCoursesScopesEnroledByStudent = infoCurricularCoursesScopesEnroledByStudent;
	}

}