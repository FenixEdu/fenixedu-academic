package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantPart extends GrantPart_Base {

    public GrantPart() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeGrantPaymentEntity();
	removeGrantSubsidy();
	removeResponsibleTeacher();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
