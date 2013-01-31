/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.EnrolmentModel;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ManageEnrolmentModelBean implements Serializable {

	private EnrolmentModel enrolmentModel;

	private ExecutionYear executionYear;

	private Registration registration;

	public ManageEnrolmentModelBean(Registration registration) {
		super();
		this.registration = registration;
		setExecutionYear(ExecutionYear.readCurrentExecutionYear());
		setEnrolmentModel(getRegistration().getEnrolmentModelForExecutionYear(getExecutionYear()));
	}

	public EnrolmentModel getEnrolmentModel() {
		return enrolmentModel;
	}

	public void setEnrolmentModel(EnrolmentModel enrolmentModel) {
		this.enrolmentModel = enrolmentModel;
	}

	public ExecutionYear getExecutionYear() {
		return executionYear;
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = executionYear;
	}

	public Registration getRegistration() {
		return registration;
	}

}
