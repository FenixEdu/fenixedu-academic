/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantProject extends InfoGrantPaymentEntity {

    private InfoGrantCostCenter infoGrantCostCenter;

    /**
     * @return Returns the infoGrantCostCenter.
     */
    public InfoGrantCostCenter getInfoGrantCostCenter() {
        return infoGrantCostCenter;
    }

    /**
     * @param infoGrantCostCenter
     *            The infoGrantCostCenter to set.
     */
    public void setInfoGrantCostCenter(InfoGrantCostCenter infoGrantCostCenter) {
        this.infoGrantCostCenter = infoGrantCostCenter;
    }

}
