/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import Dominio.DomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class GrantSubsidy extends DomainObject implements IGrantSubsidy
{
	private String valueFullName;
	private Double value;
	private Double totalCost;
	private IGrantContract grantContract;
	private Integer keyGrantContract;
	/**
	 *  
	 */
	public GrantSubsidy()
	{
		super();
	}

	/**
	 * @param idInternal
	 */
	public GrantSubsidy(Integer idInternal)
	{
		super(idInternal);
	}

	/**
	 * @return Returns the grantContract.
	 */
	public IGrantContract getGrantContract()
	{
		return grantContract;
	}

	/**
	 * @param grantContract The grantContract to set.
	 */
	public void setGrantContract(IGrantContract grantContract)
	{
		this.grantContract = grantContract;
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
	 * @return Returns the keyGrantContract.
	 */
	public Integer getKeyGrantContract()
	{
		return keyGrantContract;
	}

	/**
	 * @param keyGrantContract The keyGrantContract to set.
	 */
	public void setKeyGrantContract(Integer keyGrantContract)
	{
		this.keyGrantContract = keyGrantContract;
	}

}
