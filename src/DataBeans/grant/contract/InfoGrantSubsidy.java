/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import java.util.Date;

import DataBeans.InfoObject;
import Dominio.grant.contract.GrantSubsidy;
import Dominio.grant.contract.IGrantSubsidy;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantSubsidy extends InfoObject {

    private static final int activeState = 1;

    private static final int inactiveState = 0;

    private Integer state;

    private Date dateBeginSubsidy;

    private Date dateEndSubsidy;

    private String valueFullName;

    private Double value;

    private Double totalCost;

    private InfoGrantContract infoGrantContract;

    /**
     * @return Returns the infoGrantContract.
     */
    public InfoGrantContract getInfoGrantContract() {
        return infoGrantContract;
    }

    /**
     * @param infoGrantContract
     *            The infoGrantContract to set.
     */
    public void setInfoGrantContract(InfoGrantContract infoGrantContract) {
        this.infoGrantContract = infoGrantContract;
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

    public static Integer getActiveStateValue() {
        return new Integer(activeState);
    }

    public static Integer getInactiveStateValue() {
        return new Integer(inactiveState);
    }

    /**
     * @param GrantSubsidy
     */
    public void copyFromDomain(IGrantSubsidy grantSubsidy) {
        super.copyFromDomain(grantSubsidy);
        if (grantSubsidy != null) {
            setState(grantSubsidy.getState());
            setDateBeginSubsidy(grantSubsidy.getDateBeginSubsidy());
            setDateEndSubsidy(grantSubsidy.getDateEndSubsidy());
            setValueFullName(grantSubsidy.getValueFullName());
            setValue(grantSubsidy.getValue());
            setTotalCost(grantSubsidy.getTotalCost());
        }
    }

    /**
     * @param GrantSubsidy
     * @return
     */
    public static InfoGrantSubsidy newInfoFromDomain(IGrantSubsidy grantSubsidy) {
        InfoGrantSubsidy infoGrantSubsidy = null;
        if (grantSubsidy != null) {
            infoGrantSubsidy = new InfoGrantSubsidy();
            infoGrantSubsidy.copyFromDomain(grantSubsidy);
        }
        return infoGrantSubsidy;
    }

    public void copyToDomain(InfoGrantSubsidy infoGrantSubsidy, IGrantSubsidy grantSubsidy) {
        super.copyToDomain(infoGrantSubsidy, grantSubsidy);

        grantSubsidy.setDateBeginSubsidy(infoGrantSubsidy.getDateBeginSubsidy());
        grantSubsidy.setDateEndSubsidy(infoGrantSubsidy.getDateEndSubsidy());
        grantSubsidy.setState(infoGrantSubsidy.getState());
        grantSubsidy.setTotalCost(infoGrantSubsidy.getTotalCost());
        grantSubsidy.setValue(infoGrantSubsidy.getValue());
        grantSubsidy.setValueFullName(infoGrantSubsidy.getValueFullName());
    }

    public static IGrantSubsidy newDomainFromInfo(InfoGrantSubsidy infoGrantSubsidy) {
        IGrantSubsidy grantSubsidy = null;
        if (infoGrantSubsidy != null) {
            grantSubsidy = new GrantSubsidy();
            infoGrantSubsidy.copyToDomain(infoGrantSubsidy, grantSubsidy);
        }
        return grantSubsidy;
    }

}