/**
 * Created on 9/Mai/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author jpvl
 */
public class RestrictionAlreadyOrActuallyEnroledInCurricularCourse extends Restriction implements IRestrictionCurricularCourseAlreadyEnrolled {
	private ICurricularCourse curricularCourseAlreadyEnroled;
	private Integer keyCurricularCourseAlreadyEnroled;

	/**
	 *
	 */

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {

		Integer acumulatedEnrolments = enrolmentContext.getCurricularCourseAcumulatedEnrolments(curricularCourseAlreadyEnroled);

		ICurricularCourse curricularCourse = this.getCurricularCourseAlreadyEnroled();

		System.out.println("CurricularCourseAlreadyEnroled: " + curricularCourse.getName());

		List scopes = curricularCourse.getScopes();
		for (int scopeIndex = 0; scopeIndex < scopes.size();scopeIndex++) {
			ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) scopes.get(scopeIndex);
			if(enrolmentContext.getActualEnrolments().contains(curricularCourseScope)) {
				return true;
			}
		}

		return enrolmentContext.isCurricularCourseDone(this.getCurricularCourseAlreadyEnroled()) || acumulatedEnrolments.intValue() > 0;
	}

	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourseAlreadyEnroled() {
		return curricularCourseAlreadyEnroled;
	}

	/**
	 * @param course
	 */
	public void setCurricularCourseAlreadyEnroled(ICurricularCourse course) {
		curricularCourseAlreadyEnroled = course;
	}

	/**
	 * @return
	 */
	public Integer getKeyCurricularCourseAlreadyEnroled() {
		return keyCurricularCourseAlreadyEnroled;
	}

	/**
	 * @param integer
	 */
	public void setKeyCurricularCourseAlreadyEnroled(Integer integer) {
		keyCurricularCourseAlreadyEnroled = integer;
	}

}