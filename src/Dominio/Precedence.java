package Dominio;

import java.util.List;

import ServidorAplicacao.strategy.enrolment.context.StudentEnrolmentContext;
import Util.PrecedenceScopeToApply;

/**
 * 
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public class Precedence extends DomainObject implements IPrecedence
{
	private Integer keyCurricularCourse;
	private ICurricularCourse curricularCourse;
	private List restrictions;
	private PrecedenceScopeToApply precedenceScopeToApply;

	public Precedence()
	{
		super();
	}

	/**
	 * @return Returns the curricularCourse.
	 */
	public ICurricularCourse getCurricularCourse()
	{
		return curricularCourse;
	}

	/**
	 * @param curricularCourse The curricularCourse to set.
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse)
	{
		this.curricularCourse = curricularCourse;
	}

	/**
	 * @return Returns the keyCurricularCourse.
	 */
	public Integer getKeyCurricularCourse()
	{
		return keyCurricularCourse;
	}

	/**
	 * @param keyCurricularCourse The keyCurricularCourse to set.
	 */
	public void setKeyCurricularCourse(Integer keyCurricularCourse)
	{
		this.keyCurricularCourse = keyCurricularCourse;
	}

	/**
	 * @return Returns the precedenceScopeToApply.
	 */
	public PrecedenceScopeToApply getPrecedenceScopeToApply()
	{
		return precedenceScopeToApply;
	}

	/**
	 * @param precedenceScopeToApply The precedenceScopeToApply to set.
	 */
	public void setPrecedenceScopeToApply(PrecedenceScopeToApply precedenceScopeToApply)
	{
		this.precedenceScopeToApply = precedenceScopeToApply;
	}

	/**
	 * @return Returns the restrictions.
	 */
	public List getRestrictions()
	{
		return restrictions;
	}

	/**
	 * @param restrictions The restrictions to set.
	 */
	public void setRestrictions(List restrictions)
	{
		this.restrictions = restrictions;
	}

	/**
	 * @param studentEnrolmentContext
	 * @return true/false
	 */
	public boolean evaluate(StudentEnrolmentContext studentEnrolmentContext)
	{
		List restrictions = getRestrictions();
		boolean evaluate = true;

		for (int i = 0; i < restrictions.size() && evaluate; i++)
		{
			IRestriction restriction = (IRestriction) restrictions.get(i);
			evaluate = restriction.evaluate(studentEnrolmentContext);
		}
		return evaluate;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if ((obj != null) && (this.getClass().equals(obj.getClass())))
		{
			IPrecedence precedence = (IPrecedence) obj;
			result = this.getCurricularCourse().equals(precedence.getCurricularCourse());
			if (result)
			{
				List precedenceRestrictions = precedence.getRestrictions();
				if (precedenceRestrictions != null)
				{
					for (int i = 0; i < precedenceRestrictions.size() && result; i++)
					{
						IRestriction restriction = (IRestriction) precedenceRestrictions.get(i);
						result = this.getRestrictions().contains(restriction);
					}
				} else
				{
					result = this.getRestrictions() == null;
				}

			}
		}
		return result;
	}

	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Precedence:\n");
		stringBuffer.append(this.getCurricularCourse()).append("\n");
		List restrictions = this.getRestrictions();
		for (int i = 0; i < restrictions.size(); i++)
		{
			IRestriction restriction = (IRestriction) restrictions.get(i);
			stringBuffer.append(restriction).append("\n");
		}
		stringBuffer.append("---------\n");
		return stringBuffer.toString();
	}
}