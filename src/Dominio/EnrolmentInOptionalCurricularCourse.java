package Dominio;

import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentInOptionalCurricularCourse extends Enrolment implements IEnrolmentInOptionalCurricularCourse {

	private Integer curricularCourseForOptionKey;
	private ICurricularCourse curricularCourseForOption;

	public EnrolmentInOptionalCurricularCourse() {
		super();
	}

	public EnrolmentInOptionalCurricularCourse(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourse curricularCourse,
		EnrolmentState state,
		ICurricularCourse curricularCourseForOption) {
		super(studentCurricularPlan, curricularCourse, state);
		setCurricularCourseForOption(curricularCourseForOption);
	}

	public String toString() {
		String result = super.toString();
		result += "curricularCourseForOption = " + this.curricularCourseForOption + "; ";
		return result;
	}
	/**
	 * @return ICurricularCourse
	 */
	public ICurricularCourse getCurricularCourseForOption() {
		return curricularCourseForOption;
	}

	/**
	 * @return Integer
	 */
	public Integer getCurricularCourseForOptionKey() {
		return curricularCourseForOptionKey;
	}

	/**
	 * Sets the curricularCourseForOption.
	 * @param curricularCourseForOption The curricularCourseForOption to set
	 */
	public void setCurricularCourseForOption(ICurricularCourse curricularCourseForOption) {
		this.curricularCourseForOption = curricularCourseForOption;
	}

	/**
	 * Sets the curricularCourseForOptionKey.
	 * @param curricularCourseForOptionKey The curricularCourseForOptionKey to set
	 */
	public void setCurricularCourseForOptionKey(Integer curricularCourseForOptionKey) {
		this.curricularCourseForOptionKey = curricularCourseForOptionKey;
	}

	
	/* (non-Javadoc)
	 * @see Dominio.IEnrolment#getRealCurricularCourse()
	 */
	public ICurricularCourse getRealCurricularCourse() {
		return this.getCurricularCourseForOption();
	}
}