package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.HashMap;
import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		
		HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();
		int NAC = 0;

		Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (acumulatedEnrolments.containsKey(curricularCourseScope.getCurricularCourse().getCode() + curricularCourseScope.getCurricularCourse().getName())) {
				if (((Integer) acumulatedEnrolments.get(curricularCourseScope.getCurricularCourse().getCode() + curricularCourseScope.getCurricularCourse().getName())).intValue() > 0) {
					NAC = NAC + 2;
				}
			} else {
				NAC = NAC + 1;
			}
		}

		// FIXME: David-Ricardo: Parametrizar possibleND, possibleNAC e year
		// FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.

		//		FIXME: David-Ricardo: A regra dos 3 nao se aplica aos trabalhadores estudantes
		if (enrolmentContext.getActualEnrolment().size() < 3) {
			enrolmentContext.getEnrolmentValidationResult().setMessage("Deve inscrever-se a pelo menos 3 disciplinas");
		}
		if (enrolmentContext.getActualEnrolment().size() > 7) {
			enrolmentContext.getEnrolmentValidationResult().setMessage("Não se pode inscrever a mais de 7 disciplinas");
		}
		if (NAC > 10) {
			enrolmentContext.getEnrolmentValidationResult().setMessage("Não se pode inscrever a mais de 10 disciplinas acumuladas");
		}
		return enrolmentContext;
	}
}