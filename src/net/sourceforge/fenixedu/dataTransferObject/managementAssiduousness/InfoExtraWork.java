/*
 * Created on 15/Dez/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWork;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWork extends InfoObject {
    private InfoEmployee infoEmployee;

    private InfoCostCenter infoCostCenter;

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

    private Boolean dayPerWeek;

    private Boolean holiday;

    private Boolean remuneration;

    private InfoEmployee infoWhoEmployee;

    private Date when;

    private String observation;

    private Date totalExtraWork;

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
    public void setDiurnalAfterSecondHourAuthorized(Boolean diurnalAfterSecondHourAuthorized) {
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
    public void setDiurnalFirstHourAuthorized(Boolean diurnalFirstHourAuthorized) {
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
    public void setNocturnalAfterSecondHourAuthorized(Boolean nocturnalAfterSecondHourAuthorized) {
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
    public void setNocturnalFirstHourAuthorized(Boolean nocturnalFirstHourAuthorized) {
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
     * @return Returns the observation.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * @param observation The observation to set.
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    public void unAuthorized() {
        setDiurnalAfterSecondHourAuthorized(Boolean.FALSE);
        setDiurnalFirstHourAuthorized(Boolean.FALSE);
        setMealSubsidyAuthorized(Boolean.FALSE);
        setNocturnalAfterSecondHourAuthorized(Boolean.FALSE);
        setNocturnalFirstHourAuthorized(Boolean.FALSE);
        setRestDayAuthorized(Boolean.FALSE);

        setDayPerWeek(Boolean.FALSE);
        setHoliday(Boolean.FALSE);
        setRemuneration(Boolean.FALSE);
    }

    public void copyFromDomain(IExtraWork extraWork) {
        super.copyFromDomain(extraWork);
        if (extraWork != null) {
            setBeginHour(extraWork.getBeginHour());
            setDay(extraWork.getDay());
            setDiurnalAfterSecondHour(extraWork.getDiurnalAfterSecondHour());
            setDiurnalAfterSecondHourAuthorized(extraWork.getDiurnalAfterSecondHourAuthorized());
            setDiurnalFirstHour(extraWork.getDiurnalFirstHour());
            setDiurnalFirstHourAuthorized(extraWork.getDiurnalFirstHourAuthorized());
            setEndHour(extraWork.getEndHour());
            setMealSubsidy(extraWork.getMealSubsidy());
            setMealSubsidyAuthorized(extraWork.getMealSubsidyAuthorized());
            setNocturnalAfterSecondHour(extraWork.getNocturnalAfterSecondHour());
            setNocturnalAfterSecondHourAuthorized(extraWork.getNocturnalAfterSecondHourAuthorized());
            setNocturnalFirstHour(extraWork.getNocturnalFirstHour());
            setNocturnalFirstHourAuthorized(extraWork.getNocturnalFirstHourAuthorized());
            setRestDay(extraWork.getRestDay());
            setRestDayAuthorized(extraWork.getRestDayAuthorized());
            setDayPerWeek(extraWork.getDayPerWeek());
            setHoliday(extraWork.getHoliday());
            setRemuneration(extraWork.getRemuneration());
            setWhen(extraWork.getWhen());
            setWhen(extraWork.getWhen());
            setTotalExtraWork(extraWork.getTotalExtraWork());
            setObservation(extraWork.getObservation());
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

    public void copyToDomain(InfoExtraWork infoExtraWork, IExtraWork extraWork) {
        super.copyToDomain(infoExtraWork, extraWork);
        extraWork.setDay(infoExtraWork.getDay());
        extraWork.setBeginHour(infoExtraWork.getBeginHour());
        extraWork.setEndHour(infoExtraWork.getEndHour());
        extraWork.setMealSubsidy(infoExtraWork.getMealSubsidy());
        extraWork.setMealSubsidyAuthorized(infoExtraWork.getMealSubsidyAuthorized());
        extraWork.setDiurnalFirstHour(infoExtraWork.getDiurnalFirstHour());
        extraWork.setDiurnalFirstHourAuthorized(infoExtraWork.getDiurnalFirstHourAuthorized());
        extraWork.setDiurnalAfterSecondHour(infoExtraWork.getDiurnalAfterSecondHour());
        extraWork.setDiurnalAfterSecondHourAuthorized(infoExtraWork
                .getDiurnalAfterSecondHourAuthorized());
        extraWork.setNocturnalFirstHour(infoExtraWork.getNocturnalFirstHour());
        extraWork.setNocturnalFirstHourAuthorized(infoExtraWork.getNocturnalFirstHourAuthorized());
        extraWork.setNocturnalAfterSecondHour(infoExtraWork.getNocturnalAfterSecondHour());
        extraWork.setNocturnalAfterSecondHourAuthorized(infoExtraWork
                .getNocturnalAfterSecondHourAuthorized());
        extraWork.setRestDay(infoExtraWork.getRestDay());
        extraWork.setRestDayAuthorized(infoExtraWork.getRestDayAuthorized());

        extraWork.setWhen(Calendar.getInstance().getTime());

        extraWork.setObservation(infoExtraWork.getObservation());

        extraWork.setDayPerWeek(infoExtraWork.getDayPerWeek());
        extraWork.setHoliday(infoExtraWork.getHoliday());
        extraWork.setRemuneration(infoExtraWork.getRemuneration());
    }

}
