package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class DeleteRegistrationActualInfoFactoryExecutor implements FactoryExecutor {

    private DomainReference<Registration> registration;

    public DeleteRegistrationActualInfoFactoryExecutor(final Registration registration) {
	this.registration = new DomainReference<Registration>(registration);
    }

    public Object execute() {
	registration.getObject().deleteActualInfo();
	return registration.getObject();
    }

}
