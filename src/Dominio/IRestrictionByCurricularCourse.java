package Dominio;

/**
 * @author David Santos
 */

public interface IRestrictionByCurricularCourse extends IRestriction {

	public abstract ICurricularCourse getPrecedentCurricularCourse();
	public abstract void setPrecedentCurricularCourse(ICurricularCourse curricularCourse);

}