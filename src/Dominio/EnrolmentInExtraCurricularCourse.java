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
		ICurricularCourse curricularCourse,
		EnrolmentState state,
		IExecutionPeriod executionPeriod,
		EnrolmentEvaluationType enrolmentEvaluationType,
		String universityCode) {
		super(studentCurricularPlan, curricularCourse, state, executionPeriod, enrolmentEvaluationType, universityCode);
	}

	public String toString() {
		String result = super.toString();
		return result;
	}
}