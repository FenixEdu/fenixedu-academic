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
     public Integer getHolidaysNumberPerYear();
    public void setHolidaysNumberPerYear(Integer holidaysNumberPerYear);
    public Date getHoursExtraWorkPerDay();
    public void setHoursExtraWorkPerDay(Date hoursExtraWorkPerDay);
    public Date getHoursExtraWorkPerYear();
    public void setHoursExtraWorkPerYear(Date hoursExtraWorkPerYear);
    public Integer getServiceDismissalPerYear();
    public void setServiceDismissalPerYear(Integer serviceDismissalPerYear);
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee);
    public Date getTimeForHoliday();
    public void setTimeForHoliday(Date timeForHoliday);
    public Date getTimeForServiceDismissal() ;
    public void setTimeForServiceDismissal(Date timeForServiceDismissal);
    public void inicialize();
}
