/*
 * Created on 29/Jan/2005
 */
package Dominio.managementAssiduousness;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ICostCenter;
import Dominio.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkRequests extends DomainObject implements
        IExtraWorkRequests {
    private IEmployee employee;
    private ICostCenter costCenter;
    private ICostCenter costCenterExtraWork;
    private ICostCenter costCenterMoney;

    private Integer employeeKey;
    private Integer costCenterExtraWorkKey;
    private Integer costCenterMoneyKey;
    
    private Date beginDate;
    private Date endDate;
    
    private Boolean option1 = new Boolean(false);
    private Boolean option2 = new Boolean(false);
    private Boolean option3 = new Boolean(false);
    private Boolean option4 = new Boolean(false);
    private Boolean option5 = new Boolean(false);
    private Boolean option6 = new Boolean(false);
    private Boolean option7 = new Boolean(false);
    private Boolean option8 = new Boolean(false);
    private Boolean option9 = new Boolean(false);
    private Boolean option10 = new Boolean(false);
    private Boolean option11 = new Boolean(false);
    private Boolean option12 = new Boolean(false);
   
    private int who;
    private IEmployee whoEmployee;
    private Date when;
    
    
    
    /**
     * @return Returns the begindate.
     */
    public Date getBeginDate() {
        return beginDate;
    }
    /**
     * @param begindate The begindate to set.
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    /**
     * @return Returns the costCenter.
     */
    public ICostCenter getCostCenterExtraWork() {
        return costCenterExtraWork;
    }
    /**
     * @param costCenter The costCenter to set.
     */
    public void setCostCenterExtraWork(ICostCenter costCenter) {
        this.costCenterExtraWork = costCenter;
    }
    /**
     * @return Returns the costCenterKey.
     */
    public Integer getCostCenterExtraWorkKey() {
        return costCenterExtraWorkKey;
    }
    /**
     * @param costCenterKey The costCenterKey to set.
     */
    public void setCostCenterExtraWorkKey(Integer costCenterKey) {
        this.costCenterExtraWorkKey = costCenterKey;
    }
    /**
     * @return Returns the costCenterMoney.
     */
    public ICostCenter getCostCenterMoney() {
        return costCenterMoney;
    }
    /**
     * @param costCenterMoney The costCenterMoney to set.
     */
    public void setCostCenterMoney(ICostCenter costCenterMoney) {
        this.costCenterMoney = costCenterMoney;
    }
    /**
     * @return Returns the costCenterMoneyKey.
     */
    public Integer getCostCenterMoneyKey() {
        return costCenterMoneyKey;
    }
    /**
     * @param costCenterMoneyKey The costCenterMoneyKey to set.
     */
    public void setCostCenterMoneyKey(Integer costCenterMoneyKey) {
        this.costCenterMoneyKey = costCenterMoneyKey;
    }
    /**
     * @return Returns the employee.
     */
    public IEmployee getEmployee() {
        return employee;
    }
    /**
     * @param employee The employee to set.
     */
    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }
    /**
     * @return Returns the employeeKey.
     */
    public Integer getEmployeeKey() {
        return employeeKey;
    }
    /**
     * @param employeeKey The employeeKey to set.
     */
    public void setEmployeeKey(Integer employeeKey) {
        this.employeeKey = employeeKey;
    }
    /**
     * @return Returns the endDate.
     */
    public Date getEndDate() {
        return endDate;
    }
    /**
     * @param endDate The endDate to set.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    /**
     * @return Returns the option1.
     */
    public Boolean getOption1() {
        return option1;
    }
    /**
     * @param option1 The option1 to set.
     */
    public void setOption1(Boolean option1) {
        this.option1 = option1;
    }
    /**
     * @return Returns the option10.
     */
    public Boolean getOption10() {
        return option10;
    }
    /**
     * @param option10 The option10 to set.
     */
    public void setOption10(Boolean option10) {
        this.option10 = option10;
    }
    /**
     * @return Returns the option11.
     */
    public Boolean getOption11() {
        return option11;
    }
    /**
     * @param option11 The option11 to set.
     */
    public void setOption11(Boolean option11) {
        this.option11 = option11;
    }
    /**
     * @return Returns the option12.
     */
    public Boolean getOption12() {
        return option12;
    }
    /**
     * @param option12 The option12 to set.
     */
    public void setOption12(Boolean option12) {
        this.option12 = option12;
    }
    /**
     * @return Returns the option2.
     */
    public Boolean getOption2() {
        return option2;
    }
    /**
     * @param option2 The option2 to set.
     */
    public void setOption2(Boolean option2) {
        this.option2 = option2;
    }
    /**
     * @return Returns the option3.
     */
    public Boolean getOption3() {
        return option3;
    }
    /**
     * @param option3 The option3 to set.
     */
    public void setOption3(Boolean option3) {
        this.option3 = option3;
    }
    /**
     * @return Returns the option4.
     */
    public Boolean getOption4() {
        return option4;
    }
    /**
     * @param option4 The option4 to set.
     */
    public void setOption4(Boolean option4) {
        this.option4 = option4;
    }
    /**
     * @return Returns the option5.
     */
    public Boolean getOption5() {
        return option5;
    }
    /**
     * @param option5 The option5 to set.
     */
    public void setOption5(Boolean option5) {
        this.option5 = option5;
    }
    /**
     * @return Returns the option6.
     */
    public Boolean getOption6() {
        return option6;
    }
    /**
     * @param option6 The option6 to set.
     */
    public void setOption6(Boolean option6) {
        this.option6 = option6;
    }
    /**
     * @return Returns the option7.
     */
    public Boolean getOption7() {
        return option7;
    }
    /**
     * @param option7 The option7 to set.
     */
    public void setOption7(Boolean option7) {
        this.option7 = option7;
    }
    /**
     * @return Returns the option8.
     */
    public Boolean getOption8() {
        return option8;
    }
    /**
     * @param option8 The option8 to set.
     */
    public void setOption8(Boolean option8) {
        this.option8 = option8;
    }
    /**
     * @return Returns the option9.
     */
    public Boolean getOption9() {
        return option9;
    }
    /**
     * @param option9 The option9 to set.
     */
    public void setOption9(Boolean option9) {
        this.option9 = option9;
    }
    /**
     * @return Returns the when.
     */
    public Date getWhen() {
        return when;
    }
    /**
     * @param when The when to set.
     */
    public void setWhen(Date when) {
        this.when = when;
    }
    /**
     * @return Returns the who.
     */
    public int getWho() {
        return who;
    }
    /**
     * @param who The who to set.
     */
    public void setWho(int who) {
        this.who = who;
    }
    /**
     * @return Returns the whoEmployee.
     */
    public IEmployee getWhoEmployee() {
        return whoEmployee;
    }
    /**
     * @param whoEmployee The whoEmployee to set.
     */
    public void setWhoEmployee(IEmployee whoEmployee) {
        this.whoEmployee = whoEmployee;
    }
    
//    /**
//     * @return Returns the holidaysNumber.
//     */
//    public Integer getHolidaysNumber() {
//        return holidaysNumber;
//    }
//    /**
//     * @param holidaysNumber The holidaysNumber to set.
//     */
//    public void setHolidaysNumber(Integer holidaysNumber) {
//        this.holidaysNumber = holidaysNumber;
//    }
//    /**
//     * @return Returns the hoursExtraWorkPerYear.
//     */
//    public Date getHoursExtraWorkPerYear() {
//        return hoursExtraWorkPerYear;
//    }    
//    /**
//     * @param hoursExtraWorkPerYear The hoursExtraWorkPerYear to set.
//     */
//    public void setHoursExtraWorkPerYear(Date hoursExtraWorkPerYear) {
//        this.hoursExtraWorkPerYear = hoursExtraWorkPerYear;
//    }   
    public String toString() {
        String string = new String();
        string = string.concat("[");
        string = string.concat(getEmployee().getEmployeeNumber().toString());
        string = string.concat(", ");
        string = string.concat(getBeginDate().toString());
        string = string.concat(", ");
        string = string.concat(getEndDate().toString());
        string = string.concat("]");        
        
        return string;
    }
    public ICostCenter getCostCenter()
    {
        return costCenter;
    }
    public void setCostCenter(ICostCenter costCenter)
    {
        this.costCenter = costCenter;
    }
}
