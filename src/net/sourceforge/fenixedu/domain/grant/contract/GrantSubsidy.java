/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class GrantSubsidy extends DomainObject implements IGrantSubsidy {

    private Integer state;

    private Date dateBeginSubsidy;

    private Date dateEndSubsidy;

    private String valueFullName;

    private Double value;

    private Double totalCost;

    private IGrantContract grantContract;

    private Integer keyGrantContract;

    /**
     *  
     */
    public GrantSubsidy() {
        super();
    }

    /**
     * @return Returns the grantContract.
     */
    public IGrantContract getGrantContract() {
        return grantContract;
    }

    /**
     * @param grantContract
     *            The grantContract to set.
     */
    public void setGrantContract(IGrantContract grantContract) {
        this.grantContract = grantContract;
    }

    /**
     * @return Returns the totalCost.
     */
    public Double getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost
     *            The totalCost to set.
     */
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return Returns the value.
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * @return Returns the valueFullName.
     */
    public String getValueFullName() {
        return valueFullName;
    }

    /**
     * @param valueFullName
     *            The valueFullName to set.
     */
    public void setValueFullName(String valueFullName) {
        this.valueFullName = valueFullName;
    }

    /**
     * @return Returns the keyGrantContract.
     */
    public Integer getKeyGrantContract() {
        return keyGrantContract;
    }

    /**
     * @param keyGrantContract
     *            The keyGrantContract to set.
     */
    public void setKeyGrantContract(Integer keyGrantContract) {
        this.keyGrantContract = keyGrantContract;
    }

    /**
     * @return Returns the dateBeginSubsidy.
     */
    public Date getDateBeginSubsidy() {
        return dateBeginSubsidy;
    }

    /**
     * @param dateBeginSubsidy
     *            The dateBeginSubsidy to set.
     */
    public void setDateBeginSubsidy(Date dateBeginSubsidy) {
        this.dateBeginSubsidy = dateBeginSubsidy;
    }

    /**
     * @return Returns the dateEndSubsidy.
     */
    public Date getDateEndSubsidy() {
        return dateEndSubsidy;
    }

    /**
     * @param dateEndSubsidy
     *            The dateEndSubsidy to set.
     */
    public void setDateEndSubsidy(Date dateEndSubsidy) {
        this.dateEndSubsidy = dateEndSubsidy;
    }

    /**
     * @return Returns the state.
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     *            The state to set.
     */
    public void setState(Integer state) {
        this.state = state;
    }
}