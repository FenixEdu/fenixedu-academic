/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RegistrationWithStateForExecutionYearBean implements Serializable {

    private DomainReference<Registration> registration;

    private DomainReference<ExecutionYear> executionYear;

    public RegistrationWithStateForExecutionYearBean(Registration registration, ExecutionYear executionYear) {
	super();
	this.registration = new DomainReference<Registration>(registration);
	this.executionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public RegistrationStateType getActiveStateType() {
	return getRegistration().getLastRegistrationState(getExecutionYear()).getStateType();
    }

    private ExecutionYear getExecutionYear() {
	return executionYear.getObject();
    }

    public Registration getRegistration() {
	return registration.getObject();
    }

}
