package Dominio.precedences;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IPrecedence extends IDomainObject
{
	public ICurricularCourse getCurricularCourse();

	public void setCurricularCourse(ICurricularCourse curricularCourse);

	public List getRestrictions();

	public void setRestrictions(List restrictions);

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext);
}