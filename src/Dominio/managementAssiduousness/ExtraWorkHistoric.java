/*
 * Created on 13/Fev/2005
 */
package Dominio.managementAssiduousness;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkHistoric extends DomainObject implements
        IExtraWorkHistoric{
    private IEmployee employee;
    
    private Integer employeeKey;
    
    private Integer year;
    
    private Double serviceDismissalPerYear;
    private Double holidaysNumberPerYear;
    private Date hoursExtraWorkPerYear;
    private Date hoursExtraWorkPerDay;
    
    private int who;
    private IEmployee whoEmployee;
    private Date when;
    
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
     * @return Returns the holidaysNumberPerYear.
     */
    public Double getHolidaysNumberPerYear() {
        return holidaysNumberPerYear;
    }
    /**
     * @param holidaysNumberPerYear The holidaysNumberPerYear to set.
     */
    public void setHolidaysNumberPerYear(Double holidaysNumberPerYear) {
        this.holidaysNumberPerYear = holidaysNumberPerYear;
    }
    /**
     * @return Returns the hoursExtraWorkPerDay.
     */
    public Date getHoursExtraWorkPerDay() {
        return hoursExtraWorkPerDay;
    }
    /**
     * @param hoursExtraWorkPerDay The hoursExtraWorkPerDay to set.
     */
    public void setHoursExtraWorkPerDay(Date hoursExtraWorkPerDay) {
        this.hoursExtraWorkPerDay = hoursExtraWorkPerDay;
    }
    /**
     * @return Returns the hoursExtraWorkPerYear.
     */
    public Date getHoursExtraWorkPerYear() {
        return hoursExtraWorkPerYear;
    }
    /**
     * @param hoursExtraWorkPerYear The hoursExtraWorkPerYear to set.
     */
    public void setHoursExtraWorkPerYear(Date hoursExtraWorkPerYear) {
        this.hoursExtraWorkPerYear = hoursExtraWorkPerYear;
    }
    /**
     * @return Returns the serviceDismissalPerYear.
     */
    public Double getServiceDismissalPerYear() {
        return serviceDismissalPerYear;
    }
    /**
     * @param serviceDismissalPerYear The serviceDismissalPerYear to set.
     */
    public void setServiceDismissalPerYear(Double serviceDismissalPerYear) {
        this.serviceDismissalPerYear = serviceDismissalPerYear;
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
     * @return Returns the year.
     */
    public Integer getYear() {
        return year;
    }
    /**
     * @param year The year to set.
     */
    public void setYear(Integer year) {
        year = year;
    }
}
