/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 */
public interface IPrecedence extends IDomainObject
{
	/**
	 * @return
	 */
	public abstract ICurricularCourse getCurricularCourse();
	/**
	 * @return
	 */
	public abstract List getRestrictions();
	/**
	 * @param course
	 */
	public abstract void setCurricularCourse(ICurricularCourse course);
	/**
	 * @param list
	 */
	public abstract void setRestrictions(List restrictionList);

	/**
	 * If restriction list as two restriction A and B: this method returns (A ^ B).
	 * @param curricularCoursesDone
	 * @return
	 */
	public abstract boolean evaluate(EnrolmentContext enrolmentContext);

	public PrecedenceScopeToApply getPrecedenceScopeToApply();
	public void setPrecedenceScopeToApply(PrecedenceScopeToApply apply);
}