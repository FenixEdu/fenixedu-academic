/*
 * Created on 9/Mai/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author jpvl
 */
public class RestrictionAlreadyOrActuallyEnroledInCurricularCourse extends Restriction implements IRestrictionCurricularCourseAlreadyEnrolled {
	private ICurricularCourse curricularCourseAlreadyEnroled;
	private Integer keyCurricularCourseAlreadyEnroled;
	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		Integer acumulatedEnrolments =
			enrolmentContext.getCurricularCourseAcumulatedEnrolments(
				curricularCourseAlreadyEnroled);
		List actualEnrolment = enrolmentContext.getActualEnrolment();

		return actualEnrolment.contains(curricularCourseAlreadyEnroled)
			|| acumulatedEnrolments.intValue() > 0;
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
