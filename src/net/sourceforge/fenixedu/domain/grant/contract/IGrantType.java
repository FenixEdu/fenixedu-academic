/*
 * Created on 19/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public interface IGrantType extends IDomainObject {

    public Double getIndicativeValue();

    public Integer getMaxPeriodDays();

    public Integer getMinPeriodDays();

    public String getName();

    public String getSigla();

    public String getSource();

    public Date getState();

    public void setIndicativeValue(Double indicativeValue);

    public void setMaxPeriodDays(Integer maxPeriodDays);

    public void setMinPeriodDays(Integer minPeriodDays);

    public void setName(String name);

    public void setSigla(String sigla);

    public void setSource(String source);

    public void setState(Date state);
}