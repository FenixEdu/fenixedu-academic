package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	// FIXME : David-Ricardo: Todas estas constantes sao para parametrizar
	private static final int MAX_INCREMENT_NAC = 2;
	private static final int MIN_INCREMENT_NAC = 1;
	
	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		 
		int NAC = 0;
		int maxCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMaxCoursesToEnrol().intValue();
		int maxNAC = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMaxNACToEnrol().intValue();
		int minCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentGroupInfo().getMinCoursesToEnrol().intValue();

		Iterator iterator = enrolmentContext.getActualEnrolments().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + MAX_INCREMENT_NAC;
			} else {
				if(curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) {
					NAC = NAC + (2 * MIN_INCREMENT_NAC);
				} else {
					NAC = NAC + MIN_INCREMENT_NAC;
				}
			}
		}

		
		Iterator iterator2 = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
		while (iterator2.hasNext()) {
			IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse = (IEnrolmentInOptionalCurricularCourse) iterator2.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(enrolmentInOptionalCurricularCourse.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + MAX_INCREMENT_NAC;
			} else {
				NAC = NAC + MIN_INCREMENT_NAC;
			}
		}

		if ((enrolmentContext.getActualEnrolments().size() + enrolmentContext.getOptionalCurricularCoursesEnrolments().size()) < minCourses) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.MINIMUM_CURRICULAR_COURSES_TO_ENROLL, String.valueOf(minCourses));
		}
		if ((enrolmentContext.getActualEnrolments().size() + enrolmentContext.getOptionalCurricularCoursesEnrolments().size()) > maxCourses) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.MAXIMUM_CURRICULAR_COURSES_TO_ENROLL, String.valueOf(maxCourses));
		}
		if (NAC > maxNAC) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.ACUMULATED_CURRICULAR_COURSES, String.valueOf(maxNAC));
		}
		return enrolmentContext;
	}
}