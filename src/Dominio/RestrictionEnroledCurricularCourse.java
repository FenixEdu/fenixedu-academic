package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionEnroledCurricularCourse	extends RestrictionDoneOrAlreadyEnrolledInCurricularCourse implements IRestrictionByCurricularCourse {

	public RestrictionEnroledCurricularCourse() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		List scopes = curricularCourse.getScopes();
		for (int scopeIndex = 0; scopeIndex < scopes.size();scopeIndex++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
			if(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().contains(curricularCourseScope)) {
				return true;
			} else if(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().contains(curricularCourseScope)) {
				return true;
			}
		}
		return super.evaluate(enrolmentContext);
	}
}