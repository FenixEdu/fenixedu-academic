package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
	super();
	super.setNumberOfPages(0);
    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	init(registration);
	checkParameters(documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	super.setDocumentPurposeType(documentPurposeType);
	super.setOtherDocumentPurposeTypeDescription(otherDocumentPurposeTypeDescription);
	super.setUrgentRequest(urgentRequest);
    }

    private void checkParameters(DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	if (documentPurposeType == DocumentPurposeType.OTHER
		&& otherDocumentPurposeTypeDescription == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.CertificateRequest.otherDocumentPurposeTypeDescription.cannot.be.null.for.other.purpose.type");
	}

	if (urgentRequest == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.CertificateRequest.urgentRequest.cannot.be.null");
	}
    }

    public boolean isUrgentRequest() {
	return getUrgentRequest().booleanValue();
    }

    public static CertificateRequest create(Registration registration,
	    DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
	    Boolean urgentRequest, Boolean average, Boolean detailed, ExecutionYear executionYear) {

	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_CERTIFICATE:
	    return new SchoolRegistrationCertificateRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest, executionYear);
	case ENROLMENT_CERTIFICATE:
	    return new EnrolmentCertificateRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest, detailed, executionYear);
	case APPROVEMENT_CERTIFICATE:
	    return new ApprovementCertificateRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest);
	case DEGREE_FINALIZATION_CERTIFICATE:
	    return new DegreeFinalizationCertificateRequest(registration, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest, average, detailed);
	}

	return null;
    }

    @Override
    public void setDocumentPurposeType(DocumentPurposeType documentPurposeType) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.documentPurposeType");
    }

    @Override
    public void setOtherDocumentPurposeTypeDescription(String otherDocumentTypeDescription) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.otherDocumentTypeDescription");
    }

    @Override
    public void setUrgentRequest(Boolean urgentRequest) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.urgentRequest");
    }

    public void edit(AcademicServiceRequestSituationType academicServiceRequestSituationType,
	    Employee employee, String justification, Integer numberOfPages) {

	if (isPayed() && !getNumberOfPages().equals(numberOfPages)) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.CertificateRequest.cannot.change.numberOfPages.on.payed.certificates");

	}

	super.edit(academicServiceRequestSituationType, employee, justification);
	super.setNumberOfPages(numberOfPages);
    }

    @Override
    public void setCertificateRequestEvent(CertificateRequestEvent certificateRequestEvent) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.certificateRequestEvent");
    }

    public Integer getNumberOfUnits() {
	return 0;
    }

    private boolean isPayed() {
	return (hasCertificateRequestEvent() && getCertificateRequestEvent().isClosed());
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if ((academicServiceRequestSituationType == AcademicServiceRequestSituationType.CANCELLED || academicServiceRequestSituationType == AcademicServiceRequestSituationType.REJECTED)
		&& hasCertificateRequestEvent()) {
	    getCertificateRequestEvent().cancel(employee);
	}
    }

    protected boolean isFirstRequestFromExecutionYear() {
	return getRegistration().getSucessfullyFinishedDocumentRequestsBy(
		ExecutionYear.readCurrentExecutionYear(), getDocumentRequestType()).isEmpty();
    }

    @Override
    protected void assertProcessingStatePreConditions() throws DomainException {
	if (getRegistration().getLastStudentCurricularPlanExceptPast().hasAnyNotPayedGratuityEvents()) {
	    throw new DomainException("CertificateRequest.registration.has.not.payed.gratuities.for.last.student.curricular.plan");
	}
    }

    @Override
    protected void assertConcludedStatePreConditions() throws DomainException {
	if (getNumberOfPages() == null || getNumberOfPages().intValue() == 0) {
	    throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	}
    }

}
