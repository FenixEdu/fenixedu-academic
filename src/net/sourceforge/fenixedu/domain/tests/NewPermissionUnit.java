package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class NewPermissionUnit extends NewPermissionUnit_Base {

    public NewPermissionUnit() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.removeParty();
        this.removeQuestion();
        this.removeRootDomainObject();

        super.deleteDomainObject();
    }

}
