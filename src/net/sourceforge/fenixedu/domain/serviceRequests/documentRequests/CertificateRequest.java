package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public abstract class CertificateRequest extends CertificateRequest_Base {

    protected CertificateRequest() {
	super();
	super.setNumberOfPages(0);
    }

    public CertificateRequest(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice, 
	    DocumentRequestType documentRequestType, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	this();
	init(studentCurricularPlan, administrativeOffice, documentRequestType, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, AdministrativeOffice administrativeOffice, 
	    DocumentRequestType documentRequestType, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest) {

	init(studentCurricularPlan, administrativeOffice, documentRequestType);
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

    public static CertificateRequest create(StudentCurricularPlan studentCurricularPlan, 
	    DocumentRequestType chosenDocumentRequestType,
	    DocumentPurposeType chosenDocumentPurposeType, String otherPurpose, String notes,
	    Boolean urgentRequest, Boolean average, Boolean detailed, ExecutionYear executionYear) {

	AdministrativeOffice administrativeOffice = null;
	final Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	if (employee != null) {
	    administrativeOffice = AdministrativeOffice.readByEmployee(employee);    
	} 
	if (administrativeOffice == null) {
	    administrativeOffice = AdministrativeOffice.getResponsibleAdministrativeOffice(studentCurricularPlan.getDegree());
	}
	
	switch (chosenDocumentRequestType) {
	case SCHOOL_REGISTRATION_CERTIFICATE:
	    return new SchoolRegistrationCertificateRequest(studentCurricularPlan, administrativeOffice,
		    chosenDocumentPurposeType, otherPurpose, urgentRequest, executionYear);
	case ENROLMENT_CERTIFICATE:
	    return new EnrolmentCertificateRequest(studentCurricularPlan, administrativeOffice, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest, detailed, executionYear);
	case APPROVEMENT_CERTIFICATE:
	    return new ApprovementCertificateRequest(studentCurricularPlan, administrativeOffice, chosenDocumentPurposeType,
		    otherPurpose, urgentRequest);
	case DEGREE_FINALIZATION_CERTIFICATE:
	    return new DegreeFinalizationCertificateRequest(studentCurricularPlan, administrativeOffice,
		    chosenDocumentPurposeType, otherPurpose, urgentRequest, average, detailed);
	}

	return null;
    }

    @Override
    public void setDocumentRequestType(DocumentRequestType documentRequestType) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.CertificateRequest.cannot.modify.documentRequestType");
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
    public void setNumberOfPages(Integer numberOfPages) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.enclosing_type.cannot.modify.numberOfPages");
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
	return getStudentCurricularPlan().getSucessfullyFinishedDocumentRequestsBy(
		ExecutionYear.readCurrentExecutionYear(), getDocumentRequestType()).isEmpty();
    }

}
