package DataBeans;

import Dominio.IEnrollment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
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
	
	public void copyFromDomain(IEnrollment enrolment) {
		super.copyFromDomain(enrolment);		
	}
	
	public static InfoEnrolmentInOptionalCurricularCourse newInfoFromDomain(IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse) {
		InfoEnrolmentInOptionalCurricularCourse infoEnrolmentInOptionalCurricularCourse = null;
		if(enrolmentInOptionalCurricularCourse != null) {
			infoEnrolmentInOptionalCurricularCourse = new InfoEnrolmentInOptionalCurricularCourse();
			infoEnrolmentInOptionalCurricularCourse .copyFromDomain(enrolmentInOptionalCurricularCourse);
		}
		return infoEnrolmentInOptionalCurricularCourse;
	} 
}