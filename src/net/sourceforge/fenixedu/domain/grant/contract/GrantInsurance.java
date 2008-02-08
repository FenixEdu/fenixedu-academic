package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantInsurance extends GrantInsurance_Base {

    public GrantInsurance() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeGrantContract();
	removeGrantPaymentEntity();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
