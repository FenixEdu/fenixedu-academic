package Dominio;

/**
 * @author David Santos
 */
public interface IRestrictionByNumberOfCurricularCourses extends IRestriction {

	public abstract Integer getNumberOfCurricularCourses();
	public abstract void setNumberOfCurricularCourses(Integer numberOfCurricularCourses);

}