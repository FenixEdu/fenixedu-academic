/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

/**
 * @author pica
 * @author barbosa
 */
public class GrantProject extends GrantPaymentEntity implements IGrantProject {

    private IGrantCostCenter grantCostCenter;

    private Integer keyGrantCostCenter;

    public GrantProject() {
    }

    /**
     * @return Returns the grantCostCenter.
     */
    public IGrantCostCenter getGrantCostCenter() {
        return grantCostCenter;
    }

    /**
     * @param grantCostCenter
     *            The grantCostCenter to set.
     */
    public void setGrantCostCenter(IGrantCostCenter grantCostCenter) {
        this.grantCostCenter = grantCostCenter;
    }

    /**
     * @return Returns the keyGrantCostCenter.
     */
    public Integer getKeyGrantCostCenter() {
        return keyGrantCostCenter;
    }

    /**
     * @param keyGrantCostCenter
     *            The keyGrantCostCenter to set.
     */
    public void setKeyGrantCostCenter(Integer keyGrantCostCenter) {
        this.keyGrantCostCenter = keyGrantCostCenter;
    }

}
