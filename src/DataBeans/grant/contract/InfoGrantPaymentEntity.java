/*
 * Created on Jan 22, 2004
 */
package DataBeans.grant.contract;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantPaymentEntity extends InfoObject
{
    private Integer number;
    private String designation;
    private String ojbConcreteClass;
    private InfoTeacher infoResponsibleTeacher;
	
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

	/**
	 * @return Returns the infoResponsibleTeacher.
	 */
	public InfoTeacher getInfoResponsibleTeacher()
	{
		return infoResponsibleTeacher;
	}

	/**
	 * @param infoResponsibleTeacher The infoResponsibleTeacher to set.
	 */
	public void setInfoResponsibleTeacher(InfoTeacher infoResponsibleTeacher)
	{
		this.infoResponsibleTeacher = infoResponsibleTeacher;
	}

}
