/*
 * Created on 13/Fev/2005
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkHistoric;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkHistoric extends InfoObject {
    private InfoEmployee infoEmployee;
    
    private Integer year;
        
    private Integer serviceDismissalPerYear;
    private Integer holidaysNumberPerYear;

    private Date timeForServiceDismissal;
    private Date timeForHoliday;
    
    private Date hoursExtraWorkPerYear;
    private Date hoursExtraWorkPerDay;
    
    private InfoEmployee infoWhoEmployee;
    private Date when;
    
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
     * @return Returns the infoEmployee.
     */
    public InfoEmployee getInfoEmployee() {
        return infoEmployee;
    }
    /**
     * @param infoEmployee The infoEmployee to set.
     */
    public void setInfoEmployee(InfoEmployee infoEmployee) {
        this.infoEmployee = infoEmployee;
    }
    /**
     * @return Returns the infoWhoEmployee.
     */
    public InfoEmployee getInfoWhoEmployee() {
        return infoWhoEmployee;
    }
    /**
     * @param infoWhoEmployee The infoWhoEmployee to set.
     */
    public void setInfoWhoEmployee(InfoEmployee infoWhoEmployee) {
        this.infoWhoEmployee = infoWhoEmployee;
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
    
    public void copyFromDomain(IExtraWorkHistoric extraWorkHistoric) {
        super.copyFromDomain(extraWorkHistoric);
        if(extraWorkHistoric != null) {
            setServiceDismissalPerYear(extraWorkHistoric.getServiceDismissalPerYear());
            setHolidaysNumberPerYear(extraWorkHistoric.getHolidaysNumberPerYear());
            setTimeForServiceDismissal(extraWorkHistoric.getTimeForServiceDismissal());
            setTimeForHoliday(extraWorkHistoric.getTimeForHoliday());
            setHoursExtraWorkPerYear(extraWorkHistoric.getHoursExtraWorkPerYear());
            setHoursExtraWorkPerDay(extraWorkHistoric.getHoursExtraWorkPerDay());
            setWhen(extraWorkHistoric.getWhen());
        }        
    }   

    public static InfoExtraWorkHistoric newInfoFromDomain(IExtraWorkHistoric extraWorkHistoric) {
        InfoExtraWorkHistoric infoExtraWorkHistoric = null;
        if (extraWorkHistoric != null) {
            infoExtraWorkHistoric = new InfoExtraWorkHistoric();
            infoExtraWorkHistoric.copyFromDomain(extraWorkHistoric);
        }
        return infoExtraWorkHistoric;
    }
}
