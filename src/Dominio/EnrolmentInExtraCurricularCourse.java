package Dominio;

import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentInExtraCurricularCourse extends Enrolment implements IEnrolmentInExtraCurricularCourse {

	public EnrolmentInExtraCurricularCourse() {
		super();
	}

	public EnrolmentInExtraCurricularCourse(
		IStudentCurricularPlan studentCurricularPlan,
		ICurricularCourseScope curricularCourseScope,
		EnrolmentState state,
		IExecutionPeriod executionPeriod,
		EnrolmentEvaluationType enrolmentEvaluationType) {
		super(studentCurricularPlan, curricularCourseScope, state, executionPeriod, enrolmentEvaluationType);
	}

	public String toString() {
		String result = super.toString();
		return result;
	}
}