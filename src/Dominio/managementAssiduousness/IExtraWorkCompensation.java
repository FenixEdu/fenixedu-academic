/*
 * Created on 13/Fev/2005
 */
package Dominio.managementAssiduousness;

import java.util.Date;

import Dominio.IDomainObject;
import Dominio.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public interface IExtraWorkCompensation extends IDomainObject {
    public Date getBeginDate();
    public void setBeginDate(Date beginDate);
    public Boolean getDayPerWeek();
    public void setDayPerWeek(Boolean dayPerWeek);
    public IEmployee getEmployee();
    public void setEmployee(IEmployee employee);
    public Integer getEmployeeKey();
    public void setEmployeeKey(Integer employeeKey);
    public Date getEndDate();
    public void setEndDate(Date endDate);
    public Boolean getHoliday();
    public void setHoliday(Boolean holiday);
    public Boolean getRemuneration();
    public void setRemuneration(Boolean remuneration);
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee);
}
