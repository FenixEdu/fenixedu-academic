package net.sourceforge.fenixedu.domain.tests;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class NewPermissionUnit extends NewPermissionUnit_Base {

    public NewPermissionUnit() {
        super();

        setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
        this.setParty(null);
        this.setQuestion(null);
        this.setRootDomainObject(null);

        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPermission() {
        return getPermission() != null;
    }

    @Deprecated
    public boolean hasParty() {
        return getParty() != null;
    }

    @Deprecated
    public boolean hasQuestion() {
        return getQuestion() != null;
    }

}
