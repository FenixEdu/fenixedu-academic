/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class RegistrationWithStateForExecutionYearBean implements Serializable {

    private Registration registration;

    private ExecutionYear executionYear;

    private RegistrationStateType registrationState;

    public RegistrationWithStateForExecutionYearBean(Registration registration, ExecutionYear executionYear) {
	super();
	this.registration = registration;
	this.executionYear = executionYear;
    }

    public RegistrationWithStateForExecutionYearBean(Registration registration, RegistrationStateType registrationState) {
	this.registration = registration;
	this.registrationState = registrationState;
    }

    public RegistrationStateType getActiveStateType() {
	return registrationState != null ? registrationState : getRegistration().getLastRegistrationState(getExecutionYear())
		.getStateType();
    }

    private ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public Registration getRegistration() {
	return registration;
    }

}
