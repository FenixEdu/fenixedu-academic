package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

// NOTE: David-Ricardo: Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
public class EnrolmentFilterNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List possibleScopesSpan = new ArrayList();
		List possibleScopesEnroled = new ArrayList();
		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;

		//		possibleND = enrolmentContext.getCurricularCoursesScopesEnroledByStudent().size();

		// FIXME: David-Ricardo: Parametrizar possibleND, possibleNAC, quantidades (1 e 2) e year
		while ((possibleND < 7) && (possibleNAC < 10) && (year < 6)) {

			Iterator iteratorEnroled = enrolmentContext.getCurricularCoursesScopesEnroledByStudent().iterator();
			while (iteratorEnroled.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorEnroled.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopesEnroled.add(curricularCourseScope);
					possibleND = possibleND + 1;

					if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
						possibleNAC = possibleNAC + 2;
					} else {
						possibleNAC = possibleNAC + 1;
					}
				}
			}

			Iterator iteratorSpan = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
			while (iteratorSpan.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorSpan.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopesSpan.add(curricularCourseScope);
					possibleND = possibleND + 1;

					if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
						possibleNAC = possibleNAC + 2;
					} else {
						possibleNAC = possibleNAC + 1;
					}
				}
			}
			year++;
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(possibleScopesSpan);
		enrolmentContext.setCurricularCoursesScopesEnroledByStudent(possibleScopesEnroled);
		return enrolmentContext;
	}

}
