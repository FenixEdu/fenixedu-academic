package Dominio.precedences;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionByNumberOfCurricularCourses extends IRestriction
{
	public abstract Integer getNumberOfCurricularCourses();
	public abstract void setNumberOfCurricularCourses(Integer numberOfCurricularCourses);
}