package ServidorAplicacao.strategy.enrolment.rules;

import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to validate if the curricular courses
 * the student has chosen to be enrolled in, overlap the maximum number of acumulated enrolments
 * or/and the maximum number of curricular courses that a student can be enrolled in one semester
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		 
		int NAC = 0;
		int maxCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentKind().getMaxCoursesToEnrol().intValue();
		int maxNAC = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentKind().getMaxNACToEnrol().intValue();
		int minCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentKind().getMinCoursesToEnrol().intValue();
		int number_of_enrolments = 0;
 		
		Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + curricularCourseScope.getMaxIncrementNac().intValue();
			} else {
				NAC = NAC + curricularCourseScope.getMinIncrementNac().intValue();
			}
			number_of_enrolments += curricularCourseScope.getWeigth().intValue();
		}

		Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
		while (iterator2.hasNext()) {
			IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(enrolmentInOptionalCurricularCourse.getCurricularCourseScope().getCurricularCourse()).intValue() > 0) {
				NAC = NAC + enrolmentInOptionalCurricularCourse.getCurricularCourseScope().getMaxIncrementNac().intValue();		
			} else {
				NAC = NAC + enrolmentInOptionalCurricularCourse.getCurricularCourseScope().getMinIncrementNac().intValue();
			}
			
			number_of_enrolments += enrolmentInOptionalCurricularCourse.getCurricularCourseScope().getWeigth().intValue();
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