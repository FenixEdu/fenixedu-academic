package Dominio.precedences;

import java.util.List;

import Dominio.DomainObject;
import Dominio.ICurricularCourse;
import Util.PrecedenceScopeToApply;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
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

	public ICurricularCourse getCurricularCourse()
	{
		return curricularCourse;
	}

	public void setCurricularCourse(ICurricularCourse curricularCourse)
	{
		this.curricularCourse = curricularCourse;
	}

	public Integer getKeyCurricularCourse()
	{
		return keyCurricularCourse;
	}

	public void setKeyCurricularCourse(Integer keyCurricularCourse)
	{
		this.keyCurricularCourse = keyCurricularCourse;
	}

	public PrecedenceScopeToApply getPrecedenceScopeToApply()
	{
		return precedenceScopeToApply;
	}

	public void setPrecedenceScopeToApply(PrecedenceScopeToApply precedenceScopeToApply)
	{
		this.precedenceScopeToApply = precedenceScopeToApply;
	}

	public List getRestrictions()
	{
		return restrictions;
	}

	public void setRestrictions(List restrictions)
	{
		this.restrictions = restrictions;
	}

//	public boolean evaluate(PrecedenceContext precedenceContext)
//	{
//		List restrictions = getRestrictions();
//		boolean evaluate = true;
//
//		for (int i = 0; i < restrictions.size() && evaluate; i++)
//		{
//			IRestriction restriction = (IRestriction) restrictions.get(i);
//			evaluate = restriction.evaluate(precedenceContext);
//		}
//		return evaluate;
//	}

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

	public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext)
	{
	    List restrictions = getRestrictions();
		
		int size = restrictions.size();
		CurricularCourseEnrollmentType evaluate = ((IRestriction) restrictions.get(0)).evaluate(precedenceContext);

		for (int i = 1; i < size; i++)
		{
			IRestriction restriction = (IRestriction) restrictions.get(i);
			evaluate = evaluate.and(restriction.evaluate(precedenceContext));
		}

		return evaluate;
	}

}