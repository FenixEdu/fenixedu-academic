/*
 * Created on 8/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IPrecedence;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @see ServidorAplicacao.strategy.enrolment.degree.rules.EnrolmentPrecedenceRule
 * @author jpvl
 */
public class EnrolmentFilterPrecedenceSpanRule extends EnrolmentPrecedenceRule implements IEnrolmentRule {

	//	NOTE DAVID-RICARDO: Esta regra para ser geral para todos os cursos TEM que ser chamada em penultimo
	protected void doApply(EnrolmentContext enrolmentContext, List precedenceList, List curricularCourseToApply) {
		List curricularCourseScopesNotToStay = new ArrayList();
		for (int i = 0; i < precedenceList.size(); i++) {
			IPrecedence precedence = (IPrecedence) precedenceList.get(i);
			if (!precedence.evaluate(enrolmentContext)) {
				ICurricularCourse curricularCourse = precedence.getCurricularCourse();
				List scopes = curricularCourse.getScopes();
				for (int scopeIndex = 0; scopeIndex < scopes.size();scopeIndex++) {
					ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
					if (!curricularCourseScopesNotToStay.contains(curricularCourseScope)) {
						curricularCourseScopesNotToStay.add(curricularCourseScope);
					}
				}
			}
		}
		enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().removeAll(curricularCourseScopesNotToStay);
		curricularCourseToApply.removeAll(curricularCourseScopesNotToStay);
	}

	protected PrecedenceScopeToApply getScopeToApply() {
		return PrecedenceScopeToApply.TO_APPLY_TO_SPAN;
	}

	/**
	 * 
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(EnrolmentContext enrolmentContext) {
		List finalCurricularCourseScopesSpan =	enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled();
		return finalCurricularCourseScopesSpan;
	}
}
