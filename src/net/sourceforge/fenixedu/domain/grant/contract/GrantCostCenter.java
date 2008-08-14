/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author pica
 * @author barbosa
 */
public class GrantCostCenter extends GrantCostCenter_Base {

    public GrantCostCenter() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public static GrantCostCenter readGrantCostCenterByNumber(String number) {
	for (GrantPaymentEntity grantPaymentEntity : RootDomainObject.getInstance().getGrantPaymentEntitys()) {
	    if (grantPaymentEntity instanceof GrantCostCenter && grantPaymentEntity.getNumber().equals(number)) {
		return (GrantCostCenter) grantPaymentEntity;
	    }
	}
	return null;
    }
}