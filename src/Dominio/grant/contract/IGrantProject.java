/*
 * Created on Jan 21, 2004
 */
package Dominio.grant.contract;

/**
 * @author pica
 * @author barbosa
 */
public interface IGrantProject extends IGrantPaymentEntity {

    public IGrantCostCenter getGrantCostCenter();

    public void setGrantCostCenter(IGrantCostCenter grantCostCenter);
}