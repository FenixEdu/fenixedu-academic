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
public interface IExtraWork extends IDomainObject {
    public Date getBeginHour();
    public void setBeginHour(Date beginHour);
    public ICostCenter getCostCenter();
    public void setCostCenter(ICostCenter costCenter);
    public Integer getCostCenterKey();
    public void setCostCenterKey(Integer costCenterKey);
    public Date getDay();
    public void setDay(Date day);
    public Date getDiurnalAfterSecondHour();
    public void setDiurnalAfterSecondHour(Date diurnalAfterSecondHour);
    public Boolean getDiurnalAfterSecondHourAuthorized();
    public void setDiurnalAfterSecondHourAuthorized(
            Boolean diurnalAfterSecondHourAuthorized);
    public Date getDiurnalFirstHour();
    public void setDiurnalFirstHour(Date diurnnalFirstHour);
    public Boolean getDiurnalFirstHourAuthorized();
    public void setDiurnalFirstHourAuthorized(
            Boolean diurnnalFirstHourAuthorized);
    public IEmployee getEmployee();
    public void setEmployee(IEmployee employee);
    public Integer getEmployeeKey();
    public void setEmployeeKey(Integer employeeKey);
    public Date getEndHour();
    public void setEndHour(Date endHour);
    public Integer getMealSubsidy();
    public void setMealSubsidy(Integer mealSubsidy);
    public Boolean getMealSubsidyAuthorized();
    public void setMealSubsidyAuthorized(Boolean mealSubsidyAuthorized);
    public Date getNocturnalAfterSecondHour();
    public void setNocturnalAfterSecondHour(Date nocturnalAfterSecondHour);
    public Boolean getNocturnalAfterSecondHourAuthorized();
    public void setNocturnalAfterSecondHourAuthorized(
            Boolean nocturnalAfterSecondHourAuthorized);
    public Date getNocturnalFirstHour();
    public void setNocturnalFirstHour(Date nocturnalFirstHour);
    public Boolean getNocturnalFirstHourAuthorized();
    public void setNocturnalFirstHourAuthorized(
            Boolean nocturnalFirstHourAuthorized);
    public Date getRestDay();
    public void setRestDay(Date restDay);
    public Boolean getRestDayAuthorized();
    public void setRestDayAuthorized(Boolean restDayAuthorized);    
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee); 
    public Date getTotalExtraWork();
    public void setTotalExtraWork(Date totalExtraWork);
}
