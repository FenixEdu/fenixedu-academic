package Dominio;

import java.util.List;



import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */
public interface IPrecedence extends IDomainObject
{
	/**
	 * @return Returns the curricularCourse.
	 */
	public ICurricularCourse getCurricularCourse();

	/**
	 * @param curricularCourse The curricularCourse to set.
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse);

	/**
	 * @return Returns the keyCurricularCourse.
	 */
	public Integer getKeyCurricularCourse();

	/**
	 * @param keyCurricularCourse The keyCurricularCourse to set.
	 */
	
	public void setKeyCurricularCourse(Integer keyCurricularCourse);

	/**
	 * @return Returns the precedenceScopeToApply.
	 */
	public PrecedenceScopeToApply getPrecedenceScopeToApply();

	/**
	 * @param precedenceScopeToApply The precedenceScopeToApply to set.
	 */
	public void setPrecedenceScopeToApply(PrecedenceScopeToApply precedenceScopeToApply);

	/**
	 * @return Returns the restrictions.
	 */
	public List getRestrictions();

	/**
	 * @param restrictions The restrictions to set.
	 */
	public void setRestrictions(List restrictions);

	/**
	 * @param studentEnrolmentContext
	 * @return true/false
	 */
	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext);
}