package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantContractMovement extends GrantContractMovement_Base {

    public GrantContractMovement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	removeRootDomainObject();
	removeGrantContract();
	super.deleteDomainObject();
    }

}
