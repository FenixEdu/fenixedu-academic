/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public interface IRestrictionCurricularCourseDone extends IRestriction{
	/**
	 * @return
	 */
	public abstract ICurricularCourse getPrecedentCurricularCourse();
	/**
	 * @param course
	 */
	public abstract void setPrecedentCurricularCourse(ICurricularCourse course);
}