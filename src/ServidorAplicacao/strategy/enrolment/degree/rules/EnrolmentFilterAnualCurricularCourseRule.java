package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.CurricularCourseExecutionScope;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */

public class EnrolmentFilterAnualCurricularCourseRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List finalListToRemove = new ArrayList();

		List anualCurricularCourseScopes = (List) CollectionUtils.select(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled(), new Predicate() {
			public boolean evaluate(Object obj) {
				ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
				return curricularCourseScope.getCurricularCourse().getCurricularCourseExecutionScope().equals(new CurricularCourseExecutionScope(CurricularCourseExecutionScope.ANUAL));
			}
		});
		
		if(enrolmentContext.getSemester().intValue() == 2) {
			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(anualCurricularCourseScopes);
		} else {
			while(!anualCurricularCourseScopes.isEmpty()) {
				final ICurricularCourseScope curricularCourseScope1 = (ICurricularCourseScope) anualCurricularCourseScopes.get(0);
				List aux = (List) CollectionUtils.select(anualCurricularCourseScopes, new Predicate() {
					public boolean evaluate(Object obj) {
						ICurricularCourseScope curricularCourseScope2 = (ICurricularCourseScope) obj;
						return curricularCourseScope2.getCurricularCourse().equals(curricularCourseScope1.getCurricularCourse());
					}
				});

				anualCurricularCourseScopes.removeAll(aux);

				int year = 10; // este valor importa que seja maior que o numero maximo de anos curriculares dos cursos.
				int index = 0;
				Iterator iterator = aux.iterator();
				while (iterator.hasNext()) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
					if (	(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue() < year) &&
							(curricularCourseScope.getCurricularSemester().getSemester().intValue() == 1) ) {
						year = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();
						index = aux.indexOf(curricularCourseScope);
					}
				}
				aux.remove(index);
				finalListToRemove.addAll(aux);
				aux.clear();
			}
			enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().removeAll(finalListToRemove);
		}

		return enrolmentContext;
	}
}