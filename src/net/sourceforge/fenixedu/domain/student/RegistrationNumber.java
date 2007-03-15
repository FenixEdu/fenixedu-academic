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
	removeRegistration();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
