/*
 * Created on 2/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Tânia Pousão
 *  
 */
public interface IEmployeeHistoric extends IDomainObject {
    public IEmployee getResponsableEmployee();

    public ICostCenter getWorkingPlaceCostCenter();

    public ICostCenter getMailingCostCenter();

    public ICostCenter getSalaryCostCenter();

    public String getCalendar();

    public StatusAssiduidade getStatus();

    public Date getBeginDate();

    public Date getEndDate();

    public Timestamp getWhen();

    public Integer getWho();

    public IEmployee getEmployee();

    public void setResponsableEmployee(IEmployee rsponsableEmployee);

    public void setWorkingPlaceCostCenter(ICostCenter workingPlaceCostCenter);

    public void setMailingCostCenter(ICostCenter mailingCostCenter);

    public void setSalaryCostCenter(ICostCenter salaryCostCenter);

    public void setCalendar(String calendar);

    public void setStatus(StatusAssiduidade status);

    public void setBeginDate(Date beginDate);

    public void setEndDate(Date endDate);

    public void setWhen(Timestamp when);

    public void setWho(Integer who);

    public void setEmployee(IEmployee employee);
}