/*
 * Created on 13/Fev/2005
 */
package DataBeans.managementAssiduousness;

import java.util.Date;

import DataBeans.InfoEmployee;
import DataBeans.InfoObject;
import Dominio.managementAssiduousness.IExtraWorkCompensation;
import Dominio.managementAssiduousness.IExtraWorkHistoric;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkHistoric extends InfoObject {
    private InfoEmployee infoEmployee;
    
    private Integer year;
        
    private Double serviceDismissalPerYear;
    private Double holidaysNumberPerYear;
    private Date hoursExtraWorkPerYear;
    private Date hoursExtraWorkPerDay;
    
    private InfoEmployee infoWhoEmployee;
    private Date when;
    
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
    public void copyFromDomain(IExtraWorkHistoric extraWorkHistoric) {
        super.copyFromDomain(extraWorkHistoric);
        if(extraWorkHistoric != null) {
            setServiceDismissalPerYear(extraWorkHistoric.getServiceDismissalPerYear());
            setHolidaysNumberPerYear(extraWorkHistoric.getHolidaysNumberPerYear());
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
