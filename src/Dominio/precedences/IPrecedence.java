package Dominio.precedences;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;
import Util.PrecedenceScopeToApply;

/**
 * @author David Santos in Jun 9, 2004
 */

public interface IPrecedence extends IDomainObject
{
	public ICurricularCourse getCurricularCourse();

	public void setCurricularCourse(ICurricularCourse curricularCourse);

	public PrecedenceScopeToApply getPrecedenceScopeToApply();

	public void setPrecedenceScopeToApply(PrecedenceScopeToApply precedenceScopeToApply);

	public List getRestrictions();

	public void setRestrictions(List restrictions);

	public boolean evaluate(PrecedenceContext precedenceContext);
}