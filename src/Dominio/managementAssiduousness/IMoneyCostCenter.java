/*
 * Created on 11/Dez/2004
 */
package Dominio.managementAssiduousness;

import java.util.Date;

import Dominio.ICostCenter;
import Dominio.IDomainObject;
import Dominio.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public interface IMoneyCostCenter extends IDomainObject  {
    public ICostCenter getCostCenter();
    public void setCostCenter(ICostCenter costCenter);
    public Integer getCostCenterKey();
    public void setCostCenterKey(Integer costCenterKey);
    public Double getInitialValue();
    public void setInitialValue(Double initialValue);
    public Double getSpentValue();
    public void setSpentValue(Double spentValue);
    public Double getTotalValue();
    public void setTotalValue(Double totalValue);
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee); 
    public Integer getYear();
    public void setYear(Integer year);
}
