/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.DomainObject;

/**
 * @author pica
 * @author barbosa
 */
public abstract class GrantPaymentEntity extends DomainObject implements IGrantPaymentEntity
{
    protected Integer number;
    protected String designation;
    protected String ojbConcreteClass;
    
    /**
     * Constructor
     */
    public GrantPaymentEntity()
    {
        super();
        this.ojbConcreteClass = this.getClass().getName();
    }
    
	/**
	 * @return Returns the designation.
	 */
	public String getDesignation()
	{
		return designation;
	}

	/**
	 * @param designation The designation to set.
	 */
	public void setDesignation(String designation)
	{
		this.designation = designation;
	}

	/**
	 * @return Returns the number.
	 */
	public Integer getNumber()
	{
		return number;
	}

	/**
	 * @param number The number to set.
	 */
	public void setNumber(Integer number)
	{
		this.number = number;
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
	
}
