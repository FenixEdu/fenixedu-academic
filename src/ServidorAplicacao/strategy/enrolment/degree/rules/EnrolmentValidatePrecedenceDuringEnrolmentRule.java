/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public class EnrolmentValidatePrecedenceDuringEnrolmentRule extends PrecedenceRule{

	/* (non-Javadoc)
	 * @see ServidorAplicacao.strategy.enrolment.degree.rules.PrecedenceRule#getScopeToApply()
	 */
	protected PrecedenceScopeToApply getScopeToApply() {
		return PrecedenceScopeToApply.TO_APPLY_DURING_ENROLMENT;
	}

	protected void doApply(EnrolmentContext enrolmentContext, List precedenceList, List curricularCourseToApply) {
		throw new IllegalStateException("method not implemented!");
	}

	/**
	 * 
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(EnrolmentContext enrolmentContext) {
		// FIXME : Put enrolment in optional to...
		List actualEnrolment =
			enrolmentContext.getActualEnrolment();
		return actualEnrolment;
	}

}
