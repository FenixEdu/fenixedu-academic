/*
 * Created on 2/Abr/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public interface ICurricularCourseDoneRestriction extends IRestriction{
	/**
	 * @return
	 */
	public abstract CurricularCourse getPrecedentCurricularCourse();
	/**
	 * @param course
	 */
	public abstract void setPrecedentCurricularCourse(CurricularCourse course);
}