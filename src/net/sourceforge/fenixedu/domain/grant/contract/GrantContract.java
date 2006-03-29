package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantContract extends GrantContract_Base {

	public GrantContract() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        super.deleteDomainObject();
    }

}
