package net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.documentRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class DocumentRequestCreateBean implements Serializable {

    private DomainReference<Registration> registration;

    private DocumentRequestType chosenDocumentRequestType;

    private DocumentPurposeType chosenDocumentPurposeType;

    private String otherPurpose;

    private String notes;

    private Boolean urgentRequest;

    private Boolean average;

    private Boolean detailed;

    private DomainReference<ExecutionYear> executionYear;

    private Boolean toBeCreated;

    private Collection<String> warningsToReport;

    public DocumentRequestCreateBean(Registration registration) {
	setRegistration(registration);
    }

    public Registration getRegistration() {
	return (this.registration != null) ? this.registration.getObject() : null;
    }

    private void setRegistration(Registration registration) {
	this.registration = (registration != null) ? new DomainReference<Registration>(registration)
		: null;
    }

    public DocumentRequestType getChosenDocumentRequestType() {
	return chosenDocumentRequestType;
    }

    public void setChosenDocumentRequestType(DocumentRequestType chosenDocumentRequestType) {
	this.chosenDocumentRequestType = chosenDocumentRequestType;
    }

    public DocumentPurposeType getChosenDocumentPurposeType() {
	return chosenDocumentPurposeType;
    }

    public void setChosenDocumentPurposeType(DocumentPurposeType chosenDocumentPurposeType) {
	this.chosenDocumentPurposeType = chosenDocumentPurposeType;
    }

    public String getOtherPurpose() {
	return otherPurpose;
    }

    public void setOtherPurpose(String otherPurpose) {
	this.otherPurpose = otherPurpose;
    }

    public String getNotes() {
	return notes;
    }

    public void setNotes(String notes) {
	this.notes = notes;
    }

    public Boolean getUrgentRequest() {
	return urgentRequest;
    }

    public void setUrgentRequest(Boolean urgentRequest) {
	this.urgentRequest = urgentRequest;
    }

    public Boolean getAverage() {
	return average;
    }

    public void setAverage(Boolean average) {
	this.average = average;
    }

    public Boolean getDetailed() {
	return detailed;
    }

    public void setDetailed(Boolean detailed) {
	this.detailed = detailed;
    }

    public ExecutionYear getExecutionYear() {

	if (chosenDocumentRequestType == DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION
		|| chosenDocumentRequestType == DocumentRequestType.ENROLMENT_DECLARATION) {

	    final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	    if (getRegistration().getActiveStudentCurricularPlan().hasAnyEnrolmentForExecutionYear(
		    currentExecutionYear)) {
		return currentExecutionYear;
	    }
	    throw new DomainException("error.no.enrolments.for.current.year");
	}

	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear)
		: null;
    }

    public Boolean getToBeCreated() {
	return toBeCreated;
    }

    public void setToBeCreated(Boolean toBeCreated) {
	this.toBeCreated = toBeCreated;
    }

    /**
         * This method is only needed to report warnings to the user. While we
         * don't have enough info in our database to decide on what cases the
         * document request should abort (acording to the Administrative Office
         * rules shown in the use cases), warnings must be shown to the user.
         * 
         * @return
         */
    public Collection<String> getWarningsToReport() {
	if (warningsToReport == null) {
	    warningsToReport = new HashSet<String>();

	    if (chosenDocumentRequestType == DocumentRequestType.APPROVEMENT_CERTIFICATE) {
		if (chosenDocumentPurposeType == DocumentPurposeType.PROFESSIONAL) {
		    warningsToReport.add("aprovementType.professionalPurpose.thirdGrade");
		}

		warningsToReport.add("aprovementType.finished.degree");
	    }

	    if (chosenDocumentRequestType == DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE) {
		warningsToReport.add("degreeFinalizationType.withoutDegreeCertificate");
	    }
	}

	return warningsToReport;
    }

    public boolean hasWarningsToReport() {
	if (warningsToReport == null) {
	    getWarningsToReport();
	}
	return !warningsToReport.isEmpty();
    }

    public void setPurpose(DocumentPurposeType chosenDocumentPurposeType, String otherPurpose) {

	if (chosenDocumentPurposeType != null
		&& chosenDocumentPurposeType.equals(DocumentPurposeType.OTHER)
		&& (otherPurpose == null || otherPurpose.length() == 0)) {
	    throw new DomainException("error.other.purpose.required");
	}

	this.chosenDocumentPurposeType = chosenDocumentPurposeType;
	this.otherPurpose = otherPurpose;
    }

}
