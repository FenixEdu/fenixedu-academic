package net.sourceforge.fenixedu.applicationTier.factoryExecutors;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;

public class DeleteRegistrationActualInfoFactoryExecutor implements FactoryExecutor {

    private Registration registration;

    public DeleteRegistrationActualInfoFactoryExecutor(final Registration registration) {
	this.registration = registration;
    }

    public Object execute() {
	registration.deleteActualInfo();
	return registration;
    }

}
