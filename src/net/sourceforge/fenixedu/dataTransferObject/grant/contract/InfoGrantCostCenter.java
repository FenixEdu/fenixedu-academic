/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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
    public void copyFromDomain(GrantCostCenter grantCostCenter) {
	super.copyFromDomain(grantCostCenter);
	if (grantCostCenter != null) {

	    setNumber(grantCostCenter.getNumber());
	    setDesignation(grantCostCenter.getDesignation());
	}
    }

    /**
     * @param GrantCostCenter
     * @return
     */
    public static InfoGrantCostCenter newInfoFromDomain(GrantCostCenter grantCostCenter) {
	InfoGrantCostCenter infoGrantCostCenter = null;
	if (grantCostCenter != null) {
	    infoGrantCostCenter = new InfoGrantCostCenter();
	    infoGrantCostCenter.copyFromDomain(grantCostCenter);
	}
	return infoGrantCostCenter;
    }

    public void copyToDomain(InfoGrantCostCenter infoGrantCostCenter, GrantCostCenter grantCostCenter) {
	super.copyToDomain(infoGrantCostCenter, grantCostCenter);

	grantCostCenter.setDesignation(infoGrantCostCenter.getDesignation());
	grantCostCenter.setNumber(infoGrantCostCenter.getNumber());
    }

}
