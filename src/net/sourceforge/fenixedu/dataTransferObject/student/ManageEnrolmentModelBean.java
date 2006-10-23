/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageEnrolmentModelBean implements Serializable {

    private EnrolmentModel enrolmentModel;

    private DomainReference<ExecutionYear> executionYear;
    
    private DomainReference<Registration> registration;

    public ManageEnrolmentModelBean(Registration registration) {
	super();
	this.registration = new DomainReference<Registration>(registration);
	this.executionYear = new DomainReference<ExecutionYear>(ExecutionYear.readCurrentExecutionYear());
	EnrolmentModel enrolmentModelForCurrentExecutionYear = registration
		.getEnrolmentModelForExecutionYear(getExecutionYear());
	this.enrolmentModel = enrolmentModelForCurrentExecutionYear;
    }

    public EnrolmentModel getEnrolmentModel() {
	return enrolmentModel;
    }

    public void setEnrolmentModel(EnrolmentModel enrolmentModel) {
	this.enrolmentModel = enrolmentModel;
    }

    public ExecutionYear getExecutionYear() {
	return executionYear == null ? null : executionYear.getObject();
    }

    public Registration getRegistration() {
        return registration == null ? null : registration.getObject();
    }
    
}
