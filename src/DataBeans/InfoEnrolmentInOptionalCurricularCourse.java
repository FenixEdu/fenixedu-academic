package DataBeans;

import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class InfoEnrolmentInOptionalCurricularCourse extends InfoEnrolment {

	private InfoCurricularCourse infoCurricularCourseForOption;

	public InfoEnrolmentInOptionalCurricularCourse() {
		super();
	}

	public InfoEnrolmentInOptionalCurricularCourse(
		InfoStudentCurricularPlan infoStudentCurricularPlan,
		InfoCurricularCourse infoCurricularCourse,
		EnrolmentState state,
		InfoExecutionPeriod infoExecutionPeriod,
		InfoCurricularCourse infoCurricularCourseForOption) {
		super(infoStudentCurricularPlan, infoCurricularCourse, state, infoExecutionPeriod);
		setInfoCurricularCourseForOption(infoCurricularCourseForOption);
	}

	public String toString() {
		String result = super.toString();
		result += "infoCurricularCourseForOption = " + this.infoCurricularCourseForOption + "; ";
		return result;
	}
	/**
	 * @return ICurricularCourse
	 */
	public InfoCurricularCourse getInfoCurricularCourseForOption() {
		return infoCurricularCourseForOption;
	}

	/**
	 * Sets the infoCurricularCourseForOption.
	 * @param infoCurricularCourseForOption The infoCurricularCourseForOption to set
	 */
	public void setInfoCurricularCourseForOption(InfoCurricularCourse infoCurricularCourseForOption) {
		this.infoCurricularCourseForOption = infoCurricularCourseForOption;
	}
}