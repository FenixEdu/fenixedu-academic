/*
 * Created on 13/Fev/2005
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkHistoric extends DomainObject implements
        IExtraWorkHistoric{
    private IEmployee employee;
    
    private Integer employeeKey;
    
    private Integer year;
    
    private Integer serviceDismissalPerYear;
    private Integer holidaysNumberPerYear;

    private Date timeForServiceDismissal;
    private Date timeForHoliday;
    
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
    public Integer getHolidaysNumberPerYear() {
        return holidaysNumberPerYear;
    }
    /**
     * @param holidaysNumberPerYear The holidaysNumberPerYear to set.
     */
    public void setHolidaysNumberPerYear(Integer holidaysNumberPerYear) {
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
    public Integer getServiceDismissalPerYear() {
        return serviceDismissalPerYear;
    }
    /**
     * @param serviceDismissalPerYear The serviceDismissalPerYear to set.
     */
    public void setServiceDismissalPerYear(Integer serviceDismissalPerYear) {
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
        this.year = year;
    }
    
    /**
     * @return Returns the timeForHoliday.
     */
    public Date getTimeForHoliday() {
        return timeForHoliday;
    }
    /**
     * @param timeForHoliday The timeForHoliday to set.
     */
    public void setTimeForHoliday(Date timeForHoliday) {
        this.timeForHoliday = timeForHoliday;
    }
    /**
     * @return Returns the timeForServiceDismissal.
     */
    public Date getTimeForServiceDismissal() {
        return timeForServiceDismissal;
    }
    /**
     * @param timeForServiceDismissal The timeForServiceDismissal to set.
     */
    public void setTimeForServiceDismissal(Date timeForServiceDismissal) {
        this.timeForServiceDismissal = timeForServiceDismissal;
    }
    
    public void inicialize() {
        if(getHolidaysNumberPerYear() == null) {
            setHolidaysNumberPerYear(new Integer(0));
        }
        
        if(getServiceDismissalPerYear() == null) {
            setServiceDismissalPerYear(new Integer(0));
        }
        
        Calendar calendar= Calendar.getInstance();
        calendar.clear();
        
        if(getHoursExtraWorkPerYear() == null) {
            setHoursExtraWorkPerYear(calendar.getTime());
        }
        if(getHoursExtraWorkPerDay() == null) {
        	setHoursExtraWorkPerDay(calendar.getTime());
        }
        
        if(getTimeForHoliday() == null) {
            setTimeForHoliday(calendar.getTime());
        }
        if(getTimeForServiceDismissal() == null) {
            setTimeForServiceDismissal(calendar.getTime());
        }
    }
}
