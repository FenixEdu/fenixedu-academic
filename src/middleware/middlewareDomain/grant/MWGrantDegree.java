/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantDegree extends MWDomainObject
{
	private Integer idInternal;
    private String categoryCode;
    private String categoryShort;
    private String categoryLong;
    
	public MWGrantDegree()
	{
		super();
	}

	/**
	 * @return Returns the categoryCode.
	 */
	public String getCategoryCode()
	{
		return categoryCode;
	}

	/**
	 * @param categoryCode The categoryCode to set.
	 */
	public void setCategoryCode(String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	/**
	 * @return Returns the categoryLong.
	 */
	public String getCategoryLong()
	{
		return categoryLong;
	}

	/**
	 * @param categoryLong The categoryLong to set.
	 */
	public void setCategoryLong(String categoryLong)
	{
		this.categoryLong = categoryLong;
	}

	/**
	 * @return Returns the categoryShort.
	 */
	public String getCategoryShort()
	{
		return categoryShort;
	}

	/**
	 * @param categoryShort The categoryShort to set.
	 */
	public void setCategoryShort(String categoryShort)
	{
		this.categoryShort = categoryShort;
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

}
