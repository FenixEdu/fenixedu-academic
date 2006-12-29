package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;

public class RegistrationSelectExecutionYearBean implements Serializable {

    private DomainReference<Registration> registration;

    private DomainReference<ExecutionYear> executionYear;

    public RegistrationSelectExecutionYearBean(Registration registration) {
	setRegistration(registration);
    }

    public Registration getRegistration() {
	return (this.registration != null) ? this.registration.getObject() : null;
    }

    private void setRegistration(Registration registration) {
	this.registration = (registration != null) ? new DomainReference<Registration>(registration)
		: null;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear)
		: null;
    }

}
