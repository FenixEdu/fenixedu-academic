package ServidorAplicacao.strategy.enrolment.rules;

import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IPrecedence;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentValidationResult;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos
 */

public class EnrolmentValidatePrecedenceDuringEnrolmentRule extends EnrolmentPrecedenceRule{

	protected PrecedenceScopeToApply getScopeToApply() {
		return PrecedenceScopeToApply.TO_APPLY_DURING_ENROLMENT;
	}

	protected void doApply(EnrolmentContext enrolmentContext, List precedenceList, List curricularCourseToApply) {
		EnrolmentValidationResult enrolmentValidationResult = enrolmentContext.getEnrolmentValidationResult();
		
		for (int precedenceIndex = 0; precedenceIndex < precedenceList.size(); precedenceIndex ++){

			IPrecedence precedence = (IPrecedence) precedenceList.get(precedenceIndex);
			ICurricularCourse precedenceCurricularCourse = precedence.getCurricularCourse();
			
			if (this.contains(curricularCourseToApply, precedenceCurricularCourse)){
				
				if (!precedence.evaluate(enrolmentContext)){
					// TODO [DAVID]: A mensagem tem de ser mais clara incluindo os nomes das disciplinas precedentes.
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

	private boolean contains(List curricularCourseScopesList, ICurricularCourse curricularCourse) {
		Iterator iterator = curricularCourseScopesList.iterator();
		while (iterator.hasNext()) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
			if(curricularCourseScope.getCurricularCourse().equals(curricularCourse)) {
				return true;
			}
		}
		return false;
	}

}