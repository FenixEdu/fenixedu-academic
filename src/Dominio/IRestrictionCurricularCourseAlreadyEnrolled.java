/*
 * Created on 11/Mai/2003 by jpvl
 *
 */
package Dominio;

/**
 * @author jpvl
 */
public interface IRestrictionCurricularCourseAlreadyEnrolled {
	/**
	 * @return
	 */
	public abstract ICurricularCourse getCurricularCourseAlreadyEnroled();
	/**
	 * @param course
	 */
	public abstract void setCurricularCourseAlreadyEnroled(ICurricularCourse course);
}