/*
 * Created on Jan 22, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantPaymentEntity extends InfoObject
{
    private Integer number;
    private String designation;
    private String ojbConcreteClass;
	
	public InfoGrantPaymentEntity()
	{
		super();
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
