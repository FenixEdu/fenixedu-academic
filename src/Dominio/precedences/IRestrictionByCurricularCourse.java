package Dominio.precedences;

import Dominio.ICurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IRestrictionByCurricularCourse extends IRestriction
{
	public ICurricularCourse getPrecedentCurricularCourse();
	public void setPrecedentCurricularCourse(ICurricularCourse curricularCourse);
}