package Dominio;

/**
 * @author jpvl
 * @author David Santos in Jan 27, 2004
 */

public abstract class Restriction extends DomainObject implements IRestriction
{
	protected String ojbConcreteClass;
	protected IPrecedence precedence;
	protected Integer keyPrecedence;

	public Restriction()
	{
		super();
		this.ojbConcreteClass = this.getClass().getName();
	}

	/**
	 * @return Returns the keyPrecedence.
	 */
	public Integer getKeyPrecedence()
	{
		return keyPrecedence;
	}

	/**
	 * @param keyPrecedence The keyPrecedence to set.
	 */
	public void setKeyPrecedence(Integer keyPrecedence)
	{
		this.keyPrecedence = keyPrecedence;
	}

	/**
	 * @return Returns the ojbConcreteClass.
	 */
	public String getOjbConcreteClass()
	{
		return ojbConcreteClass;
	}

	/**
	 * @param ojbConcreteClass The ojbConcreteClass to set.
	 */
	public void setOjbConcreteClass(String ojbConcreteClass)
	{
		this.ojbConcreteClass = ojbConcreteClass;
	}

	/**
	 * @return Returns the precedence.
	 */
	public IPrecedence getPrecedence()
	{
		return precedence;
	}

	/**
	 * @param precedence The precedence to set.
	 */
	public void setPrecedence(IPrecedence precedence)
	{
		this.precedence = precedence;
	}

	public boolean equals(Object obj)
	{
		boolean result = false;
		if (obj instanceof IRestriction)
		{
			IRestriction restriction = (IRestriction) obj;
			result = restriction.getPrecedence().getCurricularCourse().equals(this.getPrecedence().getCurricularCourse());
		}
		return result;
	}
}