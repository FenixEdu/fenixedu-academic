/*
 * Created on 13/Fev/2005
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkCompensation;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkCompensation extends InfoObject {
    private InfoEmployee infoEmployee;
    
    private Date beginDate;
    private Date endDate;
    
    private Boolean dayPerWeek;
    private Boolean holiday;
    private Boolean remuneration;
     
    private InfoEmployee infoWhoEmployee;
    private Date when;
    
    /**
     * @return Returns the beginDate.
     */
    public Date getBeginDate() {
        return beginDate;
    }
    /**
     * @param beginDate The beginDate to set.
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
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
   
    public void copyFromDomain(IExtraWorkCompensation extraWorkCompensation) {
        super.copyFromDomain(extraWorkCompensation);
        if(extraWorkCompensation != null) {
            setDayPerWeek(extraWorkCompensation.getDayPerWeek());
            setHoliday(extraWorkCompensation.getHoliday());
            setRemuneration(extraWorkCompensation.getRemuneration());
            setWhen(extraWorkCompensation.getWhen());
        }        
    }
    

    public static InfoExtraWorkCompensation newInfoFromDomain(IExtraWorkCompensation extraWorkCompensation) {
        InfoExtraWorkCompensation infoExtraWorkCompensation = null;
        if (extraWorkCompensation != null) {
            infoExtraWorkCompensation = new InfoExtraWorkCompensation();
            infoExtraWorkCompensation.copyFromDomain(extraWorkCompensation);
        }
        return infoExtraWorkCompensation;
    }
}
