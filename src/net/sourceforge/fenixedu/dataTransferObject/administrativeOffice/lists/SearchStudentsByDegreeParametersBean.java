package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
@SuppressWarnings("serial")
public class SearchStudentsByDegreeParametersBean implements Serializable {

    private DomainReference<Degree> degree;

    private DomainReference<ExecutionYear> executionYear;

    private List<RegistrationAgreement> registrationAgreements = new ArrayList<RegistrationAgreement>();

    private List<RegistrationStateType> registrationStateTypes = new ArrayList<RegistrationStateType>();

    public Degree getDegree() {
	return (this.degree == null) ? null : this.degree.getObject();
    }

    public void setDegree(Degree degree) {
	this.degree = (degree != null) ? new DomainReference<Degree>(degree) : null;
    }

    public ExecutionYear getExecutionYear() {
	return (executionYear == null) ? null : this.executionYear.getObject();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public List<RegistrationAgreement> getRegistrationAgreements() {
	return registrationAgreements;
    }

    public void setRegistrationAgreements(List<RegistrationAgreement> registrationAgreements) {
	this.registrationAgreements = registrationAgreements;
    }

    public List<RegistrationStateType> getRegistrationStateTypes() {
	return registrationStateTypes;
    }

    public void setRegistrationStateTypes(List<RegistrationStateType> registrationStateTypes) {
	this.registrationStateTypes = registrationStateTypes;
    }

}
