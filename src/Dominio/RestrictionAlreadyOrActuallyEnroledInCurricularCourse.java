package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */

public class RestrictionAlreadyOrActuallyEnroledInCurricularCourse extends RestrictionDoneOrAlreadyEnrolledInCurricularCourse implements IRestrictionByCurricularCourse {

	public RestrictionAlreadyOrActuallyEnroledInCurricularCourse() {
		super();
	}

	public boolean evaluate(EnrolmentContext enrolmentContext) {

		ICurricularCourse curricularCourse = super.getPrecedentCurricularCourse();

		List scopes = curricularCourse.getScopes();
		for (int scopeIndex = 0; scopeIndex < scopes.size();scopeIndex++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
			if(enrolmentContext.getActualEnrolments().contains(curricularCourseScope)) {
				return true;
			}
		}

		return super.evaluate(enrolmentContext);
	}
}