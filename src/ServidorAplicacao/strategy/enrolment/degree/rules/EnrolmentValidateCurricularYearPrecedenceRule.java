package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentValidateCurricularYearPrecedenceRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {
		
		HashMap acumulatedEnrolments = (HashMap) enrolmentContext.getAcumulatedEnrolments();
		List actualEnrolments = enrolmentContext.getActualEnrolment();
		int year = 0;
		int year2 = 0;

		Iterator iterator = enrolmentContext.getActualEnrolment().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			year2 = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();
			if (year2 > year) {
				year = year2;
			}
		}

		if(year == 1) {
			return enrolmentContext;
		}

		for(int i = (year - 1); i > 0; i--) {
			final int j = i;
			List precedentCurricularCourses = (List) CollectionUtils.select(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled(), new Predicate() {

				public boolean evaluate(Object obj) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
					return curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() == j;
				}

			});

			if(!enrolmentContext.getActualEnrolment().containsAll(precedentCurricularCourses)) {
				// FIXME: David-Ricardo: Aqui as strings devem ser keys do aplication resource.
				enrolmentContext.getEnrolmentValidationResult().setErrorMessage("Para se inscrever as disciplinas de um dado ano, deve inscrever-se obrigatoriamente a todas as disciplinas dos anos anteriores");
				return enrolmentContext;
			}
		}
		return enrolmentContext;
	}
}