package Dominio;

/**
 * @author dcs-rjao
 *
 */
public interface IDegreeEnrolmentInfo {
	/**
	 * @return
	 */
	public abstract IDegreeCurricularPlan getDegreeCurricularPlan();
	/**
	 * @return
	 */
	public abstract Integer getDegreeDuration();
	/**
	 * @return
	 */
	public abstract Integer getMinimalYearForOptionalCourses();
	/**
	 * @param plan
	 */
	public abstract void setDegreeCurricularPlan(IDegreeCurricularPlan plan);
	/**
	 * @param integer
	 */
	public abstract void setDegreeDuration(Integer integer);
	/**
	 * @param integer
	 */
	public abstract void setMinimalYearForOptionalCourses(Integer integer);
}