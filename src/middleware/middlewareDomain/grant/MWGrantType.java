/*
 * Created on Feb 13, 2004
 */
package middleware.middlewareDomain.grant;

import middleware.middlewareDomain.MWDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class MWGrantType extends MWDomainObject
{
	private Integer idInternal;
    private String name;
    private String sigla;
    private Integer minimalPeriod;
    private Integer maximumPeriod;
    private Double indicativeValue;
    private String origem;
    
	public MWGrantType()
	{
		super();
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
	 * @return Returns the indicativeValue.
	 */
	public Double getIndicativeValue()
	{
		return indicativeValue;
	}

	/**
	 * @param indicativeValue The indicativeValue to set.
	 */
	public void setIndicativeValue(Double indicativeValue)
	{
		this.indicativeValue = indicativeValue;
	}

	/**
	 * @return Returns the maximumPeriod.
	 */
	public Integer getMaximumPeriod()
	{
		return maximumPeriod;
	}

	/**
	 * @param maximumPeriod The maximumPeriod to set.
	 */
	public void setMaximumPeriod(Integer maximumPeriod)
	{
		this.maximumPeriod = maximumPeriod;
	}

	/**
	 * @return Returns the minimalPeriod.
	 */
	public Integer getMinimalPeriod()
	{
		return minimalPeriod;
	}

	/**
	 * @param minimalPeriod The minimalPeriod to set.
	 */
	public void setMinimalPeriod(Integer minimalPeriod)
	{
		this.minimalPeriod = minimalPeriod;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return Returns the origem.
	 */
	public String getOrigem()
	{
		return origem;
	}

	/**
	 * @param origem The origem to set.
	 */
	public void setOrigem(String origem)
	{
		this.origem = origem;
	}

	/**
	 * @return Returns the sigla.
	 */
	public String getSigla()
	{
		return sigla;
	}

	/**
	 * @param sigla The sigla to set.
	 */
	public void setSigla(String sigla)
	{
		this.sigla = sigla;
	}

}
