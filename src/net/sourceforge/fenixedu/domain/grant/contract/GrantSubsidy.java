package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantSubsidy extends GrantSubsidy_Base {

    public GrantSubsidy() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	for (; !getAssociatedGrantParts().isEmpty(); getAssociatedGrantParts().get(0).delete())
	    ;
	removeGrantContract();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
