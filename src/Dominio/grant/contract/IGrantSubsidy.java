/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

import java.util.Date;

import Dominio.IDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IGrantSubsidy extends IDomainObject {

    public Integer getState();

    public Date getDateEndSubsidy();

    public Date getDateBeginSubsidy();

    public IGrantContract getGrantContract();

    public Double getTotalCost();

    public Double getValue();

    public String getValueFullName();

    public void setState(Integer state);

    public void setDateBeginSubsidy(Date dateBeginSubsidy);

    public void setDateEndSubsidy(Date dateEndSubsidy);

    public void setGrantContract(IGrantContract grantContract);

    public void setTotalCost(Double totalCost);

    public void setValue(Double value);

    public void setValueFullName(String valueFullName);
}