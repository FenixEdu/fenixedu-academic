package Dominio;

/**
 * @author David Santos in Jan 27, 2004
 */

public abstract class RestrictionByCurricularCourse extends Restriction implements IRestrictionByCurricularCourse
{
	protected Integer keyPrecedentCurricularCourse;
	protected ICurricularCourse precedentCurricularCourse;

	/**
	 * @return Returns the keyPrecedentCurricularCourse.
	 */
	public Integer getKeyPrecedentCurricularCourse()
	{
		return keyPrecedentCurricularCourse;
	}

	/**
	 * @param keyPrecedentCurricularCourse The keyPrecedentCurricularCourse to set.
	 */
	public void setKeyPrecedentCurricularCourse(Integer keyPrecedentCurricularCourse)
	{
		this.keyPrecedentCurricularCourse = keyPrecedentCurricularCourse;
	}

	/**
	 * @return Returns the precedentCurricularCourse.
	 */
	public ICurricularCourse getPrecedentCurricularCourse()
	{
		return precedentCurricularCourse;
	}

	/**
	 * @param precedentCurricularCourse The precedentCurricularCourse to set.
	 */
	public void setPrecedentCurricularCourse(ICurricularCourse precedentCurricularCourse)
	{
		this.precedentCurricularCourse = precedentCurricularCourse;
	}

	public boolean equals(Object obj)
	{
		boolean result = super.equals(obj);
		if ((result) && (obj instanceof IRestrictionByCurricularCourse))
		{
			IRestrictionByCurricularCourse restrictionByCurricularCourse = (IRestrictionByCurricularCourse) obj;
			result =
				this.getPrecedentCurricularCourse().equals(restrictionByCurricularCourse.getPrecedentCurricularCourse())
					&& this.getClass().getName().equals(restrictionByCurricularCourse.getClass().getName());
		} else if (result)
		{
			result = false;
		}
		return result;
	}

	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
		stringBuffer.append(this.getPrecedentCurricularCourse()).append("\n");
		return stringBuffer.toString();
	}
}