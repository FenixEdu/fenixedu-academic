package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.CurricularCourseType;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

// NOTE: David-Ricardo: Esta regra para ser geral para todos os cursos TEM que ser a ultima a ser chamada
public class EnrolmentFilterNACandNDRule implements IEnrolmentRule {

	// FIXME : David-Ricardo: Todas estas constantes sao para parametrizar
	private static final int MAXCOURSES = 7;
	private static final int MAXNAC = 10;
	private static final int YEAR = 6;
	private static final int MAX_INCREMENT_NAC = 2;
	private static final int MIN_INCREMENT_NAC = 1;
	
	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List possibleScopesSpan = new ArrayList();
		List possibleScopesEnroled = new ArrayList();
		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;

		while ((possibleND < MAXCOURSES) && (possibleNAC < MAXNAC) && (year < YEAR)) {

			Iterator iteratorEnroled = enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().iterator();
			while (iteratorEnroled.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iteratorEnroled.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopesEnroled.add(curricularCourseScope);
					possibleND = possibleND + 1;

					if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
						possibleNAC = possibleNAC + MAX_INCREMENT_NAC;
					} else {
						if(curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) {
							possibleNAC = possibleNAC + (2 * MIN_INCREMENT_NAC);
						} else {
							possibleNAC = possibleNAC + MIN_INCREMENT_NAC;
						}
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
						possibleNAC = possibleNAC + MAX_INCREMENT_NAC;
					} else {
						if(curricularCourseScope.getCurricularCourse().getType().equals(new CurricularCourseType(CurricularCourseType.TFC_COURSE))) {
							possibleNAC = possibleNAC + (2 * MIN_INCREMENT_NAC);
						} else {
							possibleNAC = possibleNAC + MIN_INCREMENT_NAC;
						}
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
