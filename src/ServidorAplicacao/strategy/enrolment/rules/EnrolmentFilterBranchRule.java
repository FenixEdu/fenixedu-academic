package ServidorAplicacao.strategy.enrolment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourseScope;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 3/Abr/2003
 */
public class EnrolmentFilterBranchRule implements IEnrolmentRule {

	public EnrolmentContext apply(EnrolmentContext enrolmentContext) {

		List curricularCoursesScopesFromBranch = new ArrayList();

		IBranch studentBranch = enrolmentContext.getStudentActiveCurricularPlan().getBranch();

		Iterator iterator = enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if ((curricularCourseScope.getBranch().equals(studentBranch)) || (curricularCourseScope.getBranch().getName().equals(""))) {
				curricularCoursesScopesFromBranch.add(curricularCourseScope);
			}
		}

		enrolmentContext.setFinalCurricularCoursesScopesSpanToBeEnrolled(curricularCoursesScopesFromBranch);
		return enrolmentContext;
	}
}