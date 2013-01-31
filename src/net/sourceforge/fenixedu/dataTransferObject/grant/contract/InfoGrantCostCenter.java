/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;

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
}
