package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class RegistrationNumber extends RegistrationNumber_Base {

    public RegistrationNumber(final Registration registration) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setRegistration(registration);
        setNumber(registration.getNumber());
    }

    public void delete() {
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

}
