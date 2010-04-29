/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.StudentDataByExecutionYear;
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

    public RegistrationWithStateForExecutionYearBean(Registration registration, RegistrationStateType registrationState, ExecutionYear executionYear) {
	this.registration = registration;
	this.registrationState = registrationState;
	this.executionYear = executionYear;
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

    public Student getStudent() {
	return getRegistration().getStudent();
    }

    public String getPersonalDataAuthorization() {
	final StudentDataByExecutionYear info = getStudent().getStudentDataByExecutionYear(getExecutionYear());
	return info != null && info.getPersonalDataAuthorization() != null ? info.getPersonalDataAuthorization().getDescription()
		: "";
    }

}
