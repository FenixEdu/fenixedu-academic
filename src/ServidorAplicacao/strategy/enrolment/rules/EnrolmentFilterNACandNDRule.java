package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 * 
 * This rule should be used when the intention is to remove curricular courses
 * that cannot be enrolled due to overlaping the maximum number of acumulated enrolments
 * or/and the maximum number of curricular courses that a student can be enrolled in one semester.
 */

// NOTE DAVID-RICARDO: Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
public class EnrolmentFilterNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		int degreeDuration = enrolmentContext.getStudentActiveCurricularPlan().getDegreeCurricularPlan().getDegreeDuration().intValue();
		int maxCourses = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentKind().getMaxCoursesToEnrol().intValue();
		int maxNAC = enrolmentContext.getStudentActiveCurricularPlan().getStudent().getStudentKind().getMaxNACToEnrol().intValue();

		List possibleScopesSpan = new ArrayList();
		List possibleScopesEnroled = new ArrayList();
		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;

		while ((possibleND < maxCourses) && (possibleNAC < maxNAC) && (year <= degreeDuration)) {

			Iterator iteratorEnroled = enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().iterator();
			while (iteratorEnroled.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorEnroled.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopesEnroled.add(curricularCourseScope);

					possibleND += curricularCourseScope.getWeigth().intValue();

					if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
						possibleNAC = possibleNAC + curricularCourseScope.getMaxIncrementNac().intValue();
					} else {
						possibleNAC = possibleNAC + curricularCourseScope.getMinIncrementNac().intValue();
					}
				}
			}

			Iterator iteratorSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
			while (iteratorSpan.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorSpan.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopesSpan.add(curricularCourseScope);
					
					possibleND += curricularCourseScope.getWeigth().intValue();
					
					if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
						possibleNAC = possibleNAC + curricularCourseScope.getMaxIncrementNac().intValue();
					} else {
						possibleNAC = possibleNAC + curricularCourseScope.getMinIncrementNac().intValue();
					}
				}
			}
			year++;
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(possibleScopesSpan);
		enrolmentContext.setCurricularCoursesScopesAutomaticalyEnroled(possibleScopesEnroled);
		return enrolmentContext;
	}
}