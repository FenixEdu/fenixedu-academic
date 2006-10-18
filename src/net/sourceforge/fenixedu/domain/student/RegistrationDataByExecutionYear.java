package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationDataByExecutionYear extends RegistrationDataByExecutionYear_Base {

    public RegistrationDataByExecutionYear() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public RegistrationDataByExecutionYear(Registration registration) {
	this();
	setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	setRegistration(registration);
    }

    public RegistrationDataByExecutionYear(Registration registration, ExecutionYear executionYear) {
	this();
	setExecutionYear(executionYear);
	setRegistration(registration);
    }

}
