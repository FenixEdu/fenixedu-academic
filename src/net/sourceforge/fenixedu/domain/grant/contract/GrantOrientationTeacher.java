package net.sourceforge.fenixedu.domain.grant.contract;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class GrantOrientationTeacher extends GrantOrientationTeacher_Base {

	public GrantOrientationTeacher() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
