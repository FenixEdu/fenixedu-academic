package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

// NOTE: David-Ricardo: Esta regra para ser geral para todos os cursos TEM que ser chamada DEPOIS das regras do BRANCH e do SEMESTER
public class EnrolmentFilterNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		List possibleScopes = new ArrayList();
		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;

		HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();

		// FIXME: David-Ricardo: Parametrizar possibleND, possibleNAC e year
		while ((possibleND < 7) && (possibleNAC < 10) && (year < 6)) {

//			if(!checkOutPastEnrolments(year,enrolmentContext)){
//				possibleScopes = new ArrayList();
//				enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(possibleScopes);
//				return enrolmentContext; 
//			}
			
			Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopes.add(curricularCourseScope);
					possibleND = possibleND + 1;

					if (acumulatedEnrolments.containsKey(curricularCourseScope.getCurricularCourse())) {
						if (((Integer) acumulatedEnrolments.get(curricularCourseScope.getCurricularCourse())).intValue() > 0) {
							possibleNAC = possibleNAC + 2;
						}
					} else {
						possibleNAC = possibleNAC + 1;
					}
				}
			}
			year++;
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(possibleScopes);
		return enrolmentContext;
	}

//	public boolean checkOutPastEnrolments(int year, EnrolmentContext enrolmentContext) {
//
//		List curricularCourseScopes = new ArrayList();
//
//		if (year == 1) {
//			return true;
//		} else {
//
//			HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();
//			
//			Iterator iterator = enrolmentContext.getCurricularCoursesFromStudentCurricularPlan().iterator();
//			while (iterator.hasNext()) {
//				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
//				if
//				(
//					(!curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) &&
//					(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() == (year - 1)) &&
//					(
//						(curricularCourseScope.getBranch().equals(enrolmentContext.getStudentActiveCurricularPlan().getBranch())) ||
//						(curricularCourseScope.getBranch().getName().equals(""))
//					) &&
//					(
//						(!acumulatedEnrolments.containsKey(curricularCourseScope.getCurricularCourse())) &&
//						(!enrolmentContext.getCurricularCoursesDoneByStudent().contains(curricularCourseScope.getCurricularCourse()))
//					)
//				) {
//					return false;
//				}
//			}
//
//			return true;
//		}
//	}
}