/*
 * Created on 29/Jan/2005
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public interface IExtraWorkRequests  extends IDomainObject{
    public Date getBeginDate();
    public void setBeginDate(Date begindate);
    public ICostCenter getCostCenterExtraWork();
    public void setCostCenterExtraWork(ICostCenter costCenter);
    public Integer getCostCenterExtraWorkKey();
    public void setCostCenterExtraWorkKey(Integer costCenterKey);
    public ICostCenter getCostCenterMoney();
    public void setCostCenterMoney(ICostCenter costCenterMoney);
    public Integer getCostCenterMoneyKey();
    public void setCostCenterMoneyKey(Integer costCenterMoneyKey);
    public IEmployee getEmployee();
    public void setEmployee(IEmployee employee);
    public Integer getEmployeeKey();
    public void setEmployeeKey(Integer employeeKey);
    public Date getEndDate() ;
    public void setEndDate(Date endDate);
    public Boolean getOption1();
    public void setOption1(Boolean option1);
    public Boolean getOption10();
    public void setOption10(Boolean option10);
    public Boolean getOption11();
    public void setOption11(Boolean option11);
    public Boolean getOption12() ;
    public void setOption12(Boolean option12);
    public Boolean getOption2();
    public void setOption2(Boolean option2);
    public Boolean getOption3();
    public void setOption3(Boolean option3);
    public Boolean getOption4();
    public void setOption4(Boolean option4);
    public Boolean getOption5() ;
    public void setOption5(Boolean option5);
    public Boolean getOption6();
    public void setOption6(Boolean option6);
    public Boolean getOption7();
    public void setOption7(Boolean option7);
    public Boolean getOption8();
    public void setOption8(Boolean option8);
    public Boolean getOption9();
    public void setOption9(Boolean option9);
    public Date getWhen();
    public void setWhen(Date when);
    public int getWho();
    public void setWho(int who);
    public IEmployee getWhoEmployee();
    public void setWhoEmployee(IEmployee whoEmployee);
}
