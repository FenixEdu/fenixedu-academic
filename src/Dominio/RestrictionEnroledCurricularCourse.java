/*
 * Created on 12/Mai/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author jpvl
 */
public class RestrictionEnroledCurricularCourse	extends RestrictionCurricularCourseDone {

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
		List scopes = curricularCourse.getScopes();
		for (int scopeIndex = 0; scopeIndex < scopes.size();scopeIndex++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
			if(enrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().contains(curricularCourseScope)){
				return true;
			}else if(enrolmentContext.getCurricularCoursesScopesAutomaticalyEnroled().contains(curricularCourseScope)){
				return true;
			}
		}
		return 	super.evaluate(enrolmentContext) || 
				enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 0;
	}
}
