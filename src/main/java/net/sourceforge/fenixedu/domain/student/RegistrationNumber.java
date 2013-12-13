package net.sourceforge.fenixedu.domain.student;

import pt.ist.bennu.core.domain.Bennu;

public class RegistrationNumber extends RegistrationNumber_Base {

    public RegistrationNumber(final Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumber() {
        return getNumber() != null;
    }

}
