/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantSubsidy extends InfoObject
{
    private Date dateBeginSubsidy;
    private Date dateEndSubsidy;
	private String valueFullName;
	private Double value;
	private Double totalCost;
	private InfoGrantContract infoGrantContract;
	
	
	public InfoGrantSubsidy()
	{
	}

	/**
	 * @return Returns the infoGrantContract.
	 */
	public InfoGrantContract getInfoGrantContract()
	{
		return infoGrantContract;
	}

	/**
	 * @param infoGrantContract The infoGrantContract to set.
	 */
	public void setInfoGrantContract(InfoGrantContract infoGrantContract)
	{
		this.infoGrantContract = infoGrantContract;
	}

	/**
	 * @return Returns the totalCost.
	 */
	public Double getTotalCost()
	{
		return totalCost;
	}

	/**
	 * @param totalCost The totalCost to set.
	 */
	public void setTotalCost(Double totalCost)
	{
		this.totalCost = totalCost;
	}

	/**
	 * @return Returns the value.
	 */
	public Double getValue()
	{
		return value;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}

	/**
	 * @return Returns the valueFullName.
	 */
	public String getValueFullName()
	{
		return valueFullName;
	}

	/**
	 * @param valueFullName The valueFullName to set.
	 */
	public void setValueFullName(String valueFullName)
	{
		this.valueFullName = valueFullName;
	}

	/**
	 * @return Returns the dateBeginSubsidy.
	 */
	public Date getDateBeginSubsidy()
	{
		return dateBeginSubsidy;
	}

	/**
	 * @param dateBeginSubsidy The dateBeginSubsidy to set.
	 */
	public void setDateBeginSubsidy(Date dateBeginSubsidy)
	{
		this.dateBeginSubsidy = dateBeginSubsidy;
	}

	/**
	 * @return Returns the dateEndSubsidy.
	 */
	public Date getDateEndSubsidy()
	{
		return dateEndSubsidy;
	}

	/**
	 * @param dateEndSubsidy The dateEndSubsidy to set.
	 */
	public void setDateEndSubsidy(Date dateEndSubsidy)
	{
		this.dateEndSubsidy = dateEndSubsidy;
	}

}
