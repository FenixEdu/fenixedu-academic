/*
 * Created on 11/Dez/2004
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
    private Date diurnalAfterSecondtHour;
    private Date nocturnalFirstHour;
    private Date nocturnalAfterSecondtHour;
    private Date restDay;

    private Boolean mealSubsidyAuthorized;
    private Boolean diurnalFirstHourAuthorized;
    private Boolean diurnalAfterSecondtHourAuthorized;
    private Boolean nocturnalFirstHourAuthorized;
    private Boolean nocturnalAfterSecondtHourAuthorized;
    private Boolean restDayAuthorized;

    private Boolean dayPerWeek;
    private Boolean holiday;
    private Boolean remuneration;
    
    private int who;
    private IEmployee whoEmployee;
    private Date when;
    
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
     * @return Returns the dayPerWeek.
     */
    public Boolean getDayPerWeek() {
        return dayPerWeek;
    }
    /**
     * @param dayPerWeek The dayPerWeek to set.
     */
    public void setDayPerWeek(Boolean dayPerWeek) {
        this.dayPerWeek = dayPerWeek;
    }
    /**
     * @return Returns the diurnalAfterSecondtHour.
     */
    public Date getDiurnalAfterSecondtHour() {
        return diurnalAfterSecondtHour;
    }
    /**
     * @param diurnalAfterSecondtHour The diurnalAfterSecondtHour to set.
     */
    public void setDiurnalAfterSecondtHour(Date diurnalAfterSecondtHour) {
        this.diurnalAfterSecondtHour = diurnalAfterSecondtHour;
    }
    /**
     * @return Returns the diurnalAfterSecondtHourAuthorized.
     */
    public Boolean getDiurnalAfterSecondtHourAuthorized() {
        return diurnalAfterSecondtHourAuthorized;
    }
    /**
     * @param diurnalAfterSecondtHourAuthorized The diurnalAfterSecondtHourAuthorized to set.
     */
    public void setDiurnalAfterSecondtHourAuthorized(
            Boolean diurnalAfterSecondtHourAuthorized) {
        this.diurnalAfterSecondtHourAuthorized = diurnalAfterSecondtHourAuthorized;
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
     * @return Returns the holiday.
     */
    public Boolean getHoliday() {
        return holiday;
    }
    /**
     * @param holiday The holiday to set.
     */
    public void setHoliday(Boolean holiday) {
        this.holiday = holiday;
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
     * @return Returns the nocturnalAfterSecondtHour.
     */
    public Date getNocturnalAfterSecondtHour() {
        return nocturnalAfterSecondtHour;
    }
    /**
     * @param nocturnalAfterSecondtHour The nocturnalAfterSecondtHour to set.
     */
    public void setNocturnalAfterSecondtHour(Date nocturnalAfterSecondtHour) {
        this.nocturnalAfterSecondtHour = nocturnalAfterSecondtHour;
    }
    /**
     * @return Returns the nocturnalAfterSecondtHourAuthorized.
     */
    public Boolean getNocturnalAfterSecondtHourAuthorized() {
        return nocturnalAfterSecondtHourAuthorized;
    }
    /**
     * @param nocturnalAfterSecondtHourAuthorized The nocturnalAfterSecondtHourAuthorized to set.
     */
    public void setNocturnalAfterSecondtHourAuthorized(
            Boolean nocturnalAfterSecondtHourAuthorized) {
        this.nocturnalAfterSecondtHourAuthorized = nocturnalAfterSecondtHourAuthorized;
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
     * @return Returns the remuneration.
     */
    public Boolean getRemuneration() {
        return remuneration;
    }
    /**
     * @param remuneration The remuneration to set.
     */
    public void setRemuneration(Boolean remuneration) {
        this.remuneration = remuneration;
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
}
