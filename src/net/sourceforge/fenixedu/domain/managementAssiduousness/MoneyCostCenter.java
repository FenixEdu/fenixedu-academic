/*
 * Created on 11/Dez/2004
 */
package net.sourceforge.fenixedu.domain.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICostCenter;
import net.sourceforge.fenixedu.domain.IEmployee;

/**
 * @author Tânia Pousão
 *
 */
public class MoneyCostCenter extends DomainObject implements IMoneyCostCenter{
    private ICostCenter costCenter;
    private Integer costCenterKey;
    
    private Integer year;
    
    private Double initialValue = new Double(0);
    private Double totalValue = new Double(0);
    private Double spentValue = new Double(0);
    
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
}

