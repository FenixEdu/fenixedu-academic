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
public class EnrolmentFilterSemesterRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List curricularCoursesFromActualExecutionPeriod = new ArrayList();

		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if (curricularCourseScope.getCurricularSemester().getSemester().equals(enrolmentContext.getSemester())) {
				curricularCoursesFromActualExecutionPeriod.add(curricularCourseScope);
			}
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCoursesFromActualExecutionPeriod);
		return enrolmentContext;
	}
}
