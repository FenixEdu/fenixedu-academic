package Dominio;

/**
 * @author dcs-rjao
 *
 */
public interface IPossibleCurricularCourseForOptionalCurricularCourse {
	/**
	 * @return
	 */
	public abstract ICurricularCourse getPossibleCurricularCourse();
	/**
	 * @return
	 */
	public abstract ICurricularCourse getOptionalCurricularCourse();
	/**
	 * @param course
	 */
	public abstract void setPossibleCurricularCourse(ICurricularCourse course);
	/**
	 * @param course
	 */
	public abstract void setOptionalCurricularCourse(ICurricularCourse course);
}