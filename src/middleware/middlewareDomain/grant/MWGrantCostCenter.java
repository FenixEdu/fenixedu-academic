/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantCostCenter extends MWDomainObject
{
	private Integer idInternal;
    private Integer number;
    private String designation;
    
	public MWGrantCostCenter()
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

}
