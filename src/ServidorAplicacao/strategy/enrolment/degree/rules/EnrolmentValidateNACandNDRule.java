package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		 
		int NAC = 0;
		int maxCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMaxCoursesToEnrol().intValue();
		int maxNAC = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMaxNACToEnrol().intValue();
		int minCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMinCoursesToEnrol().intValue();
		int number_of_enrolments = 0;
 		
		Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + curricularCourseScope.getCurricularCourse().getCurricularCourseEnrolmentInfo().getMaxIncrementNac().intValue();
			} else {
				NAC = NAC + curricularCourseScope.getCurricularCourse().getCurricularCourseEnrolmentInfo().getMinIncrementNac().intValue();
			}
			number_of_enrolments += curricularCourseScope.getCurricularCourse().getCurricularCourseEnrolmentInfo().getWeigth().intValue();
		}

		Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
		while (iterator2.hasNext()) {
			IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(enrolmentInOptionalCurricularCourse.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + enrolmentInOptionalCurricularCourse.getCurricularCourse().getCurricularCourseEnrolmentInfo().getMaxIncrementNac().intValue();
			} else {
				NAC = NAC + enrolmentInOptionalCurricularCourse.getCurricularCourse().getCurricularCourseEnrolmentInfo().getMinIncrementNac().intValue();
			}
			
			number_of_enrolments += enrolmentInOptionalCurricularCourse.getCurricularCourse().getCurricularCourseEnrolmentInfo().getWeigth().intValue();
		}
		
		if ((number_of_enrolments) < minCourses) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.MINIMUM_CURRICULAR_COURSES_TO_ENROLL, String.valueOf(minCourses));
		}
		if ((number_of_enrolments) > maxCourses) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.MAXIMUM_CURRICULAR_COURSES_TO_ENROLL, String.valueOf(maxCourses));
		}
		if (NAC > maxNAC) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.ACUMULATED_CURRICULAR_COURSES, String.valueOf(maxNAC));
		}
		return enrolmentContext;
	}
}