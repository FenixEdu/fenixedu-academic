package Dominio;


/**
 * @author dcs-rjao
 *
 */
public interface IDegreeCurricularPlanEnrolmentInfo {

	public abstract IDegreeCurricularPlan getDegreeCurricularPlan();
	public abstract Integer getDegreeDuration();
	public abstract Integer getMinimalYearForOptionalCourses();

	public abstract void setDegreeCurricularPlan(IDegreeCurricularPlan plan);
	public abstract void setDegreeDuration(Integer integer);
	public abstract void setMinimalYearForOptionalCourses(Integer integer);
}