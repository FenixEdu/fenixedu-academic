package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
	super();
    }

    public EnrolmentCertificateRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, Boolean detailed, ExecutionYear executionYear) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		detailed, executionYear);

    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
	    ExecutionYear executionYear) {

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(detailed, executionYear);
	super.setDetailed(detailed);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(Boolean detailed, ExecutionYear executionYear) {
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
	} else if (!getRegistration().hasSchoolRegistration(executionYear)) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.before.studentCurricularPlan.start");
	}
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.ENROLMENT_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.executionYear");
    }

    @Override
    public void setDetailed(Boolean detailed) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.detailed");
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED
		&& !isFree()) {
	    new CertificateRequestEvent(getAdministrativeOffice(),
		    getEventType(), getRegistration().getPerson(), this);
	}
    }
    
    

    @Override
    public EventType getEventType() {
	return EventType.ENROLMENT_CERTIFICATE_REQUEST;
    }

    public Collection<Enrolment> getEnrolmentsToDisplay() {
	return getRegistration().getEnrolments(getExecutionYear());
    }
    
    @Override
    public Integer getNumberOfUnits() {
	return getEnrolmentsToDisplay().size();
    }

}
