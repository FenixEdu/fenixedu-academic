package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.Iterator;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateNACandNDRule implements IEnrolmentRule {

	// FIXME : David-Ricardo: Todas estas constantes sao para parametrizar
	private static final int MINCOURSES = 3;
	private static final int MAXCOURSES = 7;
	private static final int MAXNAC = 10;
	private static final int MAX_INCREMENT_NAC = 2;
	private static final int MIN_INCREMENT_NAC = 1;
	
	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		 
		int NAC = 0;

		Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseScope.getCurricularCourse()).intValue() > 0) {
				NAC = NAC + MAX_INCREMENT_NAC;
			} else {
				NAC = NAC + MIN_INCREMENT_NAC;
			}
		}

		// FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
		// FIXME: David-Ricardo: A regra dos MINCOURSES nao se aplica aos trabalhadores estudantes
		if (enrolmentContext.getActualEnrolment().size() < MINCOURSES) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage("Deve inscrever-se a pelo menos 3 disciplinas");
		}
		if (enrolmentContext.getActualEnrolment().size() > MAXCOURSES) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage("Não se pode inscrever a mais de 7 disciplinas");
		}
		if (NAC > MAXNAC) {
			enrolmentContext.getEnrolmentValidationResult().setErrorMessage("Não se pode inscrever a mais de 10 disciplinas acumuladas");
		}
		return enrolmentContext;
	}
}