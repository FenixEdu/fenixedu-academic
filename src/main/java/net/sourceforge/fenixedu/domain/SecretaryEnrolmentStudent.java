package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

public class SecretaryEnrolmentStudent extends SecretaryEnrolmentStudent_Base {

    public SecretaryEnrolmentStudent() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setStudent(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public boolean hasStudent() {
        return getStudent() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasReasonType() {
        return getReasonType() != null;
    }

    @Deprecated
    public boolean hasReasonDescription() {
        return getReasonDescription() != null;
    }

}
