/*
 * Created on 8/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IPrecedence;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @see ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentPrecedenceRule
 * @author jpvl
 */
public class EnrolmentFilterPrecedenceNotDoneRule extends EnrolmentPrecedenceRule implements IEnrolmentRule {

	protected void doApply(EnrolmentContext enrolmentContext, List precedenceList, List curricularCourseToApply) {
		List curricularCourseScopesNotToStay = new ArrayList();
		for (int i = 0; i < precedenceList.size(); i++) {
			IPrecedence precedence = (IPrecedence) precedenceList.get(i);
			if (!precedence.evaluate(enrolmentContext)) {
				ICurricularCourse curricularCourse = precedence.getCurricularCourse();
				curricularCourseScopesNotToStay.add(curricularCourse);
			}
		}
		curricularCourseToApply.removeAll(curricularCourseScopesNotToStay);
	}

	protected PrecedenceScopeToApply getScopeToApply() {
		return PrecedenceScopeToApply.TO_APLLY_TO_OPTION_LIST;
	}

	/**
	 * 
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(EnrolmentContext enrolmentContext) {
		List optionalList =	enrolmentContext.getOptionalCurricularCoursesToChooseFromDegree();
		return optionalList;
	}
}
