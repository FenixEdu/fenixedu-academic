package net.sourceforge.fenixedu.domain;

public class SecretaryEnrolmentStudent extends SecretaryEnrolmentStudent_Base {

    public SecretaryEnrolmentStudent() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
    public boolean hasRootDomainObject() {
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
