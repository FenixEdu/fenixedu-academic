/*
 * Created on 15/Dez/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoCostCenter;
import net.sourceforge.fenixedu.dataTransferObject.InfoEmployee;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IMoneyCostCenter;

/**
 * @author Tânia Pousão
 *
 */
public class InfoMoneyCostCenter  extends InfoObject {
    private InfoCostCenter infoCostCenter;
    
    private Integer year;
    
    private Double initialValue;
    private Double totalValue;
    private Double spentValue;
    
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
     * @return Returns the initialValue.
     */
    public Double getInitialValue() {
        return initialValue;
    }
    /**
     * @param initialValue The initialValue to set.
     */
    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }
    /**
     * @return Returns the spentValue.
     */
    public Double getSpentValue() {
        return spentValue;
    }
    /**
     * @param spentValue The spentValue to set.
     */
    public void setSpentValue(Double spentValue) {
        this.spentValue = spentValue;
    }
    /**
     * @return Returns the totalValue.
     */
    public Double getTotalValue() {
        return totalValue;
    }
    /**
     * @param totalValue The totalValue to set.
     */
    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
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
        
    public String toString() {
        String string = new String();
        string = string.concat("[");
        string = string.concat(getIdInternal().toString());
        string = string.concat(", ");
        string = string.concat(getInitialValue().toString());
        string = string.concat(", ");
        string = string.concat(getTotalValue().toString());
        string = string.concat("]");        
        
        return string;
    }
    public void copyFromDomain(IMoneyCostCenter moneyCostCenter) {
        super.copyFromDomain(moneyCostCenter);
        if(moneyCostCenter != null) {
            setInitialValue(moneyCostCenter.getInitialValue());
            setSpentValue(moneyCostCenter.getSpentValue());
            setTotalValue(moneyCostCenter.getTotalValue());
            setWhen(moneyCostCenter.getWhen());
            setYear(moneyCostCenter.getYear());            
        }
    }
    
    public static InfoMoneyCostCenter newInfoFromDomain(IMoneyCostCenter moneyCostCenter) {
        InfoMoneyCostCenter infoMoneyCostCenter = null;
        if (moneyCostCenter != null) {
            infoMoneyCostCenter = new InfoMoneyCostCenter();
            infoMoneyCostCenter.copyFromDomain(moneyCostCenter);
        }
        return infoMoneyCostCenter;
    }
}

