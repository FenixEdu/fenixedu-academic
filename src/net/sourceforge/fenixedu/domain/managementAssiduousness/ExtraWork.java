/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWork extends DomainObject implements IExtraWork {
    private IEmployee employee;
    private ICostCenter costCenter;

    private Integer employeeKey;
    private Integer costCenterKey;
    
    private Date day;
    private Date beginHour;
    private Date endHour;
    private Integer mealSubsidy;
    private Date diurnalFirstHour;
    private Date diurnalAfterSecondHour;
    private Date nocturnalFirstHour;
    private Date nocturnalAfterSecondHour;
    private Date restDay;

    private Boolean mealSubsidyAuthorized;
    private Boolean diurnalFirstHourAuthorized;
    private Boolean diurnalAfterSecondHourAuthorized;
    private Boolean nocturnalFirstHourAuthorized;
    private Boolean nocturnalAfterSecondHourAuthorized;
    private Boolean restDayAuthorized;

    private int who;
    private IEmployee whoEmployee;
    private Date when;
    
    private Date totalExtraWork;
    
    /**
     * @return Returns the costCenter.
     */
    public ICostCenter getCostCenter() {
        return costCenter;
    }
    /**
     * @param costCenter The costCenter to set.
     */
    public void setCostCenter(ICostCenter costCenter) {
        this.costCenter = costCenter;
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
     * @return Returns the beginHour.
     */
    public Date getBeginHour() {
        return beginHour;
    }
    /**
     * @param beginHour The beginHour to set.
     */
    public void setBeginHour(Date beginHour) {
        this.beginHour = beginHour;
    }
    /**
     * @return Returns the costCenterKey.
     */
    public Integer getCostCenterKey() {
        return costCenterKey;
    }
    /**
     * @param costCenterKey The costCenterKey to set.
     */
    public void setCostCenterKey(Integer costCenterKey) {
        this.costCenterKey = costCenterKey;
    }
    /**
     * @return Returns the day.
     */
    public Date getDay() {
        return day;
    }
    /**
     * @param day The day to set.
     */
    public void setDay(Date day) {
        this.day = day;
    }
    /**
     * @return Returns the diurnalAfterSecondHour.
     */
    public Date getDiurnalAfterSecondHour() {
        return diurnalAfterSecondHour;
    }
    /**
     * @param diurnalAfterSecondHour The diurnalAfterSecondHour to set.
     */
    public void setDiurnalAfterSecondHour(Date diurnalAfterSecondHour) {
        this.diurnalAfterSecondHour = diurnalAfterSecondHour;
    }
    /**
     * @return Returns the diurnalAfterSecondHourAuthorized.
     */
    public Boolean getDiurnalAfterSecondHourAuthorized() {
        return diurnalAfterSecondHourAuthorized;
    }
    /**
     * @param diurnalAfterSecondHourAuthorized The diurnalAfterSecondHourAuthorized to set.
     */
    public void setDiurnalAfterSecondHourAuthorized(
            Boolean diurnalAfterSecondHourAuthorized) {
        this.diurnalAfterSecondHourAuthorized = diurnalAfterSecondHourAuthorized;
    }
    /**
     * @return Returns the diurnnalFirstHour.
     */
    public Date getDiurnalFirstHour() {
        return diurnalFirstHour;
    }
    /**
     * @param diurnnalFirstHour The diurnnalFirstHour to set.
     */
    public void setDiurnalFirstHour(Date diurnalFirstHour) {
        this.diurnalFirstHour = diurnalFirstHour;
    }
    /**
     * @return Returns the diurnnalFirstHourAuthorized.
     */
    public Boolean getDiurnalFirstHourAuthorized() {
        return diurnalFirstHourAuthorized;
    }
    /**
     * @param diurnnalFirstHourAuthorized The diurnnalFirstHourAuthorized to set.
     */
    public void setDiurnalFirstHourAuthorized(
            Boolean diurnalFirstHourAuthorized) {
        this.diurnalFirstHourAuthorized = diurnalFirstHourAuthorized;
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
     * @return Returns the endHour.
     */
    public Date getEndHour() {
        return endHour;
    }
    /**
     * @param endHour The endHour to set.
     */
    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }
    /**
     * @return Returns the mealSubsidy.
     */
    public Integer getMealSubsidy() {
        return mealSubsidy;
    }
    /**
     * @param mealSubsidy The mealSubsidy to set.
     */
    public void setMealSubsidy(Integer mealSubsidy) {
        this.mealSubsidy = mealSubsidy;
    }
    /**
     * @return Returns the mealSubsidyAuthorized.
     */
    public Boolean getMealSubsidyAuthorized() {
        return mealSubsidyAuthorized;
    }
    /**
     * @param mealSubsidyAuthorized The mealSubsidyAuthorized to set.
     */
    public void setMealSubsidyAuthorized(Boolean mealSubsidyAuthorized) {
        this.mealSubsidyAuthorized = mealSubsidyAuthorized;
    }
    /**
     * @return Returns the nocturnalAfterSecondHour.
     */
    public Date getNocturnalAfterSecondHour() {
        return nocturnalAfterSecondHour;
    }
    /**
     * @param nocturnalAfterSecondHour The nocturnalAfterSecondHour to set.
     */
    public void setNocturnalAfterSecondHour(Date nocturnalAfterSecondHour) {
        this.nocturnalAfterSecondHour = nocturnalAfterSecondHour;
    }
    /**
     * @return Returns the nocturnalAfterSecondHourAuthorized.
     */
    public Boolean getNocturnalAfterSecondHourAuthorized() {
        return nocturnalAfterSecondHourAuthorized;
    }
    /**
     * @param nocturnalAfterSecondHourAuthorized The nocturnalAfterSecondHourAuthorized to set.
     */
    public void setNocturnalAfterSecondHourAuthorized(
            Boolean nocturnalAfterSecondHourAuthorized) {
        this.nocturnalAfterSecondHourAuthorized = nocturnalAfterSecondHourAuthorized;
    }
    /**
     * @return Returns the nocturnalFirstHour.
     */
    public Date getNocturnalFirstHour() {
        return nocturnalFirstHour;
    }
    /**
     * @param nocturnalFirstHour The nocturnalFirstHour to set.
     */
    public void setNocturnalFirstHour(Date nocturnalFirstHour) {
        this.nocturnalFirstHour = nocturnalFirstHour;
    }
    /**
     * @return Returns the nocturnalFirstHourAuthorized.
     */
    public Boolean getNocturnalFirstHourAuthorized() {
        return nocturnalFirstHourAuthorized;
    }
    /**
     * @param nocturnalFirstHourAuthorized The nocturnalFirstHourAuthorized to set.
     */
    public void setNocturnalFirstHourAuthorized(
            Boolean nocturnalFirstHourAuthorized) {
        this.nocturnalFirstHourAuthorized = nocturnalFirstHourAuthorized;
    }
    /**
     * @return Returns the restDay.
     */
    public Date getRestDay() {
        return restDay;
    }
    /**
     * @param restDay The restDay to set.
     */
    public void setRestDay(Date restDay) {
        this.restDay = restDay;
    }
    /**
     * @return Returns the restDayAuthorized.
     */
    public Boolean getRestDayAuthorized() {
        return restDayAuthorized;
    }
    /**
     * @param restDayAuthorized The restDayAuthorized to set.
     */
    public void setRestDayAuthorized(Boolean restDayAuthorized) {
        this.restDayAuthorized = restDayAuthorized;
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
    /**
     * @return Returns the totalExtraWork.
     */
    public Date getTotalExtraWork() {
        return totalExtraWork;
    }
    /**
     * @param totalExtraWork The totalExtraWork to set.
     */
    public void setTotalExtraWork(Date totalExtraWork) {
        this.totalExtraWork = totalExtraWork;
    }
}
