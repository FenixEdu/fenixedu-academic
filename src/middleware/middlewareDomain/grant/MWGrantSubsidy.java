/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import java.util.Date;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantSubsidy extends MWDomainObject
{
    private Integer idInternal;
    private String valueFullName;
    private Double value;
    private Double totalCost;
    private Date beginDate;
    
    private MWGrantContract grantContract;
    private Integer keyGrantContract;
    
	public MWGrantSubsidy()
	{
		super();
	}

	/**
	 * @return Returns the beginDate.
	 */
	public Date getBeginDate()
	{
		return beginDate;
	}

	/**
	 * @param beginDate The beginDate to set.
	 */
	public void setBeginDate(Date beginDate)
	{
		this.beginDate = beginDate;
	}

	/**
	 * @return Returns the grantContract.
	 */
	public MWGrantContract getGrantContract()
	{
		return grantContract;
	}

	/**
	 * @param grantContract The grantContract to set.
	 */
	public void setGrantContract(MWGrantContract grantContract)
	{
		this.grantContract = grantContract;
	}

	/**
	 * @return Returns the idInternal.
	 */
	public Integer getIdInternal()
	{
		return idInternal;
	}

	/**
	 * @param idInternal The idInternal to set.
	 */
	public void setIdInternal(Integer idInternal)
	{
		this.idInternal = idInternal;
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

}
