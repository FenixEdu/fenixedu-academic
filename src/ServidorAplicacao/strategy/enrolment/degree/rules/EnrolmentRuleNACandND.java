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

// NOTE: David-Ricardo: Esta regra para ser geral para todos os cursos TEM que ser chamada DEPOIS da regra dos BRANCH
public class EnrolmentRuleNACandND implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia {

		List possibleScopes = new ArrayList();
		int possibleNAC = 0;
		int possibleND = 0;
		int year = 1;

		HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();

		// TODO: David-Ricardo: Parametrizar possibleND, possibleNAC e year
		while ( (possibleND < 7) && (possibleNAC < 10) && (year < 6)) {
			
			Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
			while (iterator.hasNext()) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
				if (curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().equals(new Integer(year))) {
					possibleScopes.add(curricularCourseScope);
					possibleND = possibleND + 1;
										
					if (acumulatedEnrolments.containsKey(curricularCourseScope.getCurricularCourse())) {
						if (((Integer) acumulatedEnrolments.get(curricularCourseScope.getCurricularCourse())).intValue() > 0){
							possibleNAC = possibleNAC + 2;
						}
					}
					else{
						possibleNAC = possibleNAC + 1;
					}
				}
			}
			year++;
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(possibleScopes);
		return enrolmentContext;
	}
}