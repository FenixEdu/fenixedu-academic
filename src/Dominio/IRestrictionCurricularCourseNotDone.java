/*
 * Created on 19/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;

/**
 * @author dcs-rjao
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IRestrictionCurricularCourseNotDone {
	/**
	 * @return
	 */
	public abstract ICurricularCourse getPrecedentCurricularCourse();
	/**
	 * @param course
	 */
	public abstract void setPrecedentCurricularCourse(ICurricularCourse course);
	/* (non-Javadoc)
	 * @see Dominio.IRestriction#evaluate(ServidorAplicacao.strategy.enrolment.EnrolmentContext)
	 */
	public abstract boolean evaluate(EnrolmentContext enrolmentContext);
}