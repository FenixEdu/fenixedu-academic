package Dominio;

/**
 * @author David Santos in Jan 27, 2004
 */

public interface IRestrictionByCurricularCourse extends IRestriction
{
	public abstract ICurricularCourse getPrecedentCurricularCourse();
	public abstract void setPrecedentCurricularCourse(ICurricularCourse curricularCourse);
}