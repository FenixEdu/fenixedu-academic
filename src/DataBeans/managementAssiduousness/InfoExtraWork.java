/*
 * Created on 15/Dez/2004
 */
package DataBeans.managementAssiduousness;

import java.util.Date;

import DataBeans.InfoCostCenter;
import DataBeans.InfoEmployee;
import DataBeans.InfoObject;
import Dominio.managementAssiduousness.IExtraWork;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWork  extends InfoObject  {
    private InfoEmployee infoEmployee;
    private InfoCostCenter infoCostCenter;

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
    
    private InfoEmployee infoWhoEmployee;
    private Date when;
    
    /**
     * @return Returns the costCenter.
     */
    public InfoCostCenter getInfoCostCenter() {
        return infoCostCenter;
    }
    /**
     * @param costCenter The costCenter to set.
     */
    public void setInfoCostCenter(InfoCostCenter costCenter) {
        this.infoCostCenter = costCenter;
    }
    /**
     * @return Returns the employee.
     */
    public InfoEmployee getInfoEmployee() {
        return infoEmployee;
    }
    /**
     * @param employee The employee to set.
     */
    public void setInfoEmployee(InfoEmployee employee) {
        this.infoEmployee = employee;
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
     * @return Returns the whoEmployee.
     */
    public InfoEmployee getInfoWhoEmployee() {
        return infoWhoEmployee;
    }
    /**
     * @param whoEmployee The whoEmployee to set.
     */
    public void setInfoWhoEmployee(InfoEmployee whoEmployee) {
        this.infoWhoEmployee = whoEmployee;
    }
    
    public void copyFromDomain(IExtraWork extraWork) {
        super.copyFromDomain(extraWork);
        if(extraWork != null) {
            setBeginHour(extraWork.getBeginHour());
            setDay(extraWork.getDay());
            setDayPerWeek(extraWork.getDayPerWeek());
            setDiurnalAfterSecondtHour(extraWork.getDiurnalAfterSecondtHour());
            setDiurnalAfterSecondtHourAuthorized(extraWork.getDiurnalAfterSecondtHourAuthorized());
            setDiurnalFirstHour(extraWork.getDiurnalFirstHour());
            setDiurnalFirstHourAuthorized(extraWork.getDiurnalFirstHourAuthorized());
            setEndHour(extraWork.getEndHour());
            setHoliday(extraWork.getHoliday());
            setMealSubsidy(extraWork.getMealSubsidy());
            setMealSubsidyAuthorized(extraWork.getMealSubsidyAuthorized());
            setNocturnalAfterSecondtHour(extraWork.getNocturnalAfterSecondtHour());
            setNocturnalAfterSecondtHourAuthorized(extraWork.getNocturnalAfterSecondtHourAuthorized());
            setNocturnalFirstHour(extraWork.getNocturnalFirstHour());
            setNocturnalFirstHourAuthorized(extraWork.getNocturnalFirstHourAuthorized());       
            setRemuneration(extraWork.getRemuneration());
            setRestDay(extraWork.getRestDay());
            setRestDayAuthorized(extraWork.getRestDayAuthorized());
            setWhen(extraWork.getWhen());
        }        
    }
    

    public static InfoExtraWork newInfoFromDomain(IExtraWork extraWork) {
        InfoExtraWork infoExtraWork = null;
        if (extraWork != null) {
            infoExtraWork = new InfoExtraWork();
            infoExtraWork.copyFromDomain(extraWork);
        }
        return infoExtraWork;
    }
}
