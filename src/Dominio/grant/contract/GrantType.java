/*
 * Created on 19/Nov/2003
 * 
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.DomainObject;

/**
 * @author Barbosa
 * @author Pica
 * 
 */
public class GrantType extends DomainObject implements IGrantType
{
	private String name;
	private String sigla;
	private Integer minPeriodDays;
	private Integer maxPeriodDays;
	private Double indicativeValue;
	private String source;
	private Date state; 
	
    /** 
     * Constructor
     */
    public GrantType()
    {
    }
    
    /**
     * @return
     */
    public Double getIndicativeValue()
    {
        return indicativeValue;
    }

    /**
     * @param indicativeValue
     */
    public void setIndicativeValue(Double indicativeValue)
    {
        this.indicativeValue = indicativeValue;
    }

    /**
     * @return
     */
    public Integer getMaxPeriodDays()
    {
        return maxPeriodDays;
    }

    /**
     * @param maxPeriodDays
     */
    public void setMaxPeriodDays(Integer maxPeriodDays)
    {
        this.maxPeriodDays = maxPeriodDays;
    }

    /**
     * @return
     */
    public Integer getMinPeriodDays()
    {
        return minPeriodDays;
    }

    /**
     * @param minPeriodDays
     */
    public void setMinPeriodDays(Integer minPeriodDays)
    {
        this.minPeriodDays = minPeriodDays;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return
     */
    public String getSigla()
    {
        return sigla;
    }

    /**
     * @param sigla
     */
    public void setSigla(String sigla)
    {
        this.sigla = sigla;
    }

    /**
     * @return
     */
    public String getSource()
    {
        return source;
    }

    /**
     * @param source
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * @return
     */
    public Date getState()
    {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Date state)
    {
        this.state = state;
    }
}