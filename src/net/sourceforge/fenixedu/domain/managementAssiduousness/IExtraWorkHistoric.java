/*
 * Created on 13/Fev/2005
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public interface IExtraWorkHistoric extends IDomainObject {
    public Integer getYear();
    public void setYear(Integer year);
    public IEmployee getEmployee();
    public void setEmployee(IEmployee employee);
    public Integer getEmployeeKey();
    public void setEmployeeKey(Integer employeeKey);
     public Double getHolidaysNumberPerYear();
    public void setHolidaysNumberPerYear(Double holidaysNumberPerYear);
    public Date getHoursExtraWorkPerDay();
    public void setHoursExtraWorkPerDay(Date hoursExtraWorkPerDay);
    public Date getHoursExtraWorkPerYear();
    public void setHoursExtraWorkPerYear(Date hoursExtraWorkPerYear);
    public Double getServiceDismissalPerYear();
    public void setServiceDismissalPerYear(Double serviceDismissalPerYear);
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee);
}
