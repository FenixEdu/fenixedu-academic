/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree.rules;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IPrecedence;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.degree.EnrolmentValidationResult;
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
		EnrolmentValidationResult enrolmentValidationResult = enrolmentContext.getEnrolmentValidationResult();
		for (int precedenceIndex = 0; precedenceIndex < precedenceList.size(); precedenceIndex ++){
			IPrecedence precedence = (IPrecedence) precedenceList.get(precedenceIndex);
			ICurricularCourse precedenceCurricularCourse =precedence.getCurricularCourse();
			if (curricularCourseToApply.contains(precedenceCurricularCourse)){
				if (!precedence.evaluate(enrolmentContext)){
					enrolmentValidationResult.setErrorMessage(EnrolmentValidationResult.PRECEDENCE_DURING_ENROLMENT, precedenceCurricularCourse.getName());
				}
			}
		}
	}

	/**
	 * 
	 * @param enrolmentContext
	 * @return List to apply this rule
	 */
	protected List getListOfCurricularCoursesToApply(EnrolmentContext enrolmentContext) {
		// FIXME : Put enrolment in optional to...
		List actualEnrolment =
			enrolmentContext.getActualEnrolments();
		return actualEnrolment;
	}

}
