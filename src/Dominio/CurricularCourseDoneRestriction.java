/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext;

/**
 * @author jpvl
 */
public class CurricularCourseDoneRestriction extends Restriction implements ICurricularCourseDoneRestriction {
	private Integer keyPrecedentCurricularCourse;
	private CurricularCourse precedentCurricularCourse;
	/**
	 * 
	 */
	public CurricularCourseDoneRestriction() {
		super();
	}

	/**
	 * @return
	 */
	public CurricularCourse getPrecedentCurricularCourse() {
		return precedentCurricularCourse;
	}

	/**
	 * @param course
	 */
	public void setPrecedentCurricularCourse(CurricularCourse course) {
		precedentCurricularCourse = course;
	}

	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.degree.EnrolmentContext)
	 */
	public boolean evaluate(EnrolmentContext enrolmentContext) {
		return enrolmentContext.getCurricularCoursesDoneByStudent().contains(this.getPrecedentCurricularCourse());
	}

}
