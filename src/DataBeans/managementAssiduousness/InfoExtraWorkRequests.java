/*
 * Created on 29/Jan/2005
 */
package DataBeans.managementAssiduousness;

import java.util.Date;

import DataBeans.InfoCostCenter;
import DataBeans.InfoEmployee;
import DataBeans.InfoObject;
import Dominio.managementAssiduousness.ExtraWorkRequests;
import Dominio.managementAssiduousness.IExtraWorkRequests;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExtraWorkRequests extends InfoObject{
    private InfoEmployee infoEmployee;
    private InfoCostCenter infoCostCenterExtraWork;
    private InfoCostCenter infoCostCenterMoney;
    
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
   
    private InfoEmployee infoWhoEmployee;
    private Date when;
    
    private Boolean forDelete = new Boolean(false);
    
    /**
     * @return Returns the begindate.
     */
    public Date getBeginDate() {
        return beginDate;
    }
    /**
     * @param begindate The begindate to set.
     */
    public void setBeginDate(Date begindate) {
        this.beginDate = begindate;
    }
    /**
     * @return Returns the costCenterMoney.
     */
    public InfoCostCenter getInfoCostCenterMoney() {
        return infoCostCenterMoney;
    }
    /**
     * @param costCenterMoney The costCenterMoney to set.
     */
    public void setInfoCostCenterMoney(InfoCostCenter infoCostCenterMoney) {
        this.infoCostCenterMoney = infoCostCenterMoney;
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
     * @return Returns the infoCostCenter.
     */
    public InfoCostCenter getInfoCostCenterExtraWork() {
        return infoCostCenterExtraWork;
    }
    /**
     * @param infoCostCenter The infoCostCenter to set.
     */
    public void setInfoCostCenterExtraWork(InfoCostCenter infoCostCenter) {
        this.infoCostCenterExtraWork = infoCostCenter;
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
    /**
     * @return Returns the forDelete.
     */
    public Boolean getForDelete() {
        return forDelete;
    }
    /**
     * @param forDelete The forDelete to set.
     */
    public void setForDelete(Boolean forDelete) {
        this.forDelete = forDelete;
    }
    
    public String toString() {
        String string = new String();
        string = string.concat("[");
        string = string.concat(getInfoEmployee().getEmployeeNumber().toString());
        string = string.concat(", ");
        string = string.concat(getBeginDate().toString());
        string = string.concat(", ");
        string = string.concat(getEndDate().toString());
        string = string.concat("]");        
        
        return string;
    }
    public void copyFromDomain(IExtraWorkRequests extraWorkRequests) {
        super.copyFromDomain(extraWorkRequests);
        if(extraWorkRequests != null) {    
            setBeginDate(extraWorkRequests.getBeginDate());    
            setEndDate(extraWorkRequests.getEndDate());
            setOption1(extraWorkRequests.getOption1());
            setOption2(extraWorkRequests.getOption2());
            setOption3(extraWorkRequests.getOption3());
            setOption4(extraWorkRequests.getOption4());
            setOption5(extraWorkRequests.getOption5());
            setOption6(extraWorkRequests.getOption6());
            setOption7(extraWorkRequests.getOption7());
            setOption8(extraWorkRequests.getOption8());
            setOption9(extraWorkRequests.getOption9());
            setOption10(extraWorkRequests.getOption10());
            setOption11(extraWorkRequests.getOption11());
            setOption12(extraWorkRequests.getOption12());
            setWhen(extraWorkRequests.getWhen());
        }
    }
    
    public static InfoExtraWorkRequests newInfoFromDomain(IExtraWorkRequests extraWorkRequests) {
        InfoExtraWorkRequests infoExtraWorkRequests = null;
        if (extraWorkRequests != null) {
            infoExtraWorkRequests = new InfoExtraWorkRequests();
            infoExtraWorkRequests.copyFromDomain(extraWorkRequests);
        }
        return infoExtraWorkRequests;
    }
   
    public void copyToDomain(InfoExtraWorkRequests infoExtraWorkRequests, IExtraWorkRequests extraWorkRequests) {
        super.copyToDomain(infoExtraWorkRequests, extraWorkRequests);
        
        extraWorkRequests.setBeginDate(infoExtraWorkRequests.getBeginDate());
        extraWorkRequests.setEndDate(infoExtraWorkRequests.getEndDate());
        
        extraWorkRequests.setOption1(infoExtraWorkRequests.getOption1());
        extraWorkRequests.setOption2(infoExtraWorkRequests.getOption2());
        extraWorkRequests.setOption3(infoExtraWorkRequests.getOption3());
        extraWorkRequests.setOption4(infoExtraWorkRequests.getOption4());
        extraWorkRequests.setOption5(infoExtraWorkRequests.getOption5());
        extraWorkRequests.setOption6(infoExtraWorkRequests.getOption6());
        extraWorkRequests.setOption7(infoExtraWorkRequests.getOption7());
        extraWorkRequests.setOption8(infoExtraWorkRequests.getOption8());
        extraWorkRequests.setOption9(infoExtraWorkRequests.getOption9());
        extraWorkRequests.setOption10(infoExtraWorkRequests.getOption11());
        extraWorkRequests.setOption11(infoExtraWorkRequests.getOption11());
        extraWorkRequests.setOption12(infoExtraWorkRequests.getOption12());
        
    }
    
    public static IExtraWorkRequests newDomainFromInfo(InfoExtraWorkRequests infoExtraWorkRequests) {
        IExtraWorkRequests extraWorkRequests = null;
        if (infoExtraWorkRequests != null) {
            extraWorkRequests = new ExtraWorkRequests();
            infoExtraWorkRequests.copyToDomain(infoExtraWorkRequests, extraWorkRequests);
        }
        return extraWorkRequests;
    }
}
