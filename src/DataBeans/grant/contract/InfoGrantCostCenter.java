/*
 * Created on Jan 21, 2004
 */
package DataBeans.grant.contract;

import Dominio.grant.contract.IGrantCostCenter;

/**
 * @author pica
 * @author barbosa
 */
public class InfoGrantCostCenter extends InfoGrantPaymentEntity {

    public InfoGrantCostCenter() {
    }

    /**
     * @param GrantCostCenter
     */
    public void copyFromDomain(IGrantCostCenter grantCostCenter)
    {
    	if(grantCostCenter != null){
    		setNumber(grantCostCenter.getNumber());
    		setDesignation(grantCostCenter.getDesignation());
    		setOjbConcreteClass(grantCostCenter.getOjbConcreteClass());
    	}
    }

    /**
     * @param GrantCostCenter
     * @return
     */
    public static InfoGrantCostCenter newInfoFromDomain(IGrantCostCenter grantCostCenter)
    {
    	InfoGrantCostCenter infoGrantCostCenter = null;
    	if (grantCostCenter != null)
    	{
    		infoGrantCostCenter = new InfoGrantCostCenter();
    		infoGrantCostCenter.copyFromDomain(grantCostCenter);
    	}
    	return infoGrantCostCenter;
    }
    
}
