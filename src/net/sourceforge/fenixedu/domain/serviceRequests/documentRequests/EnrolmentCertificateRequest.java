package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
	super();
    }

    public EnrolmentCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
	    ExecutionYear executionYear) {

	this();

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest, detailed, executionYear);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
	    ExecutionYear executionYear) {

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);

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
	} else if (!getStudentCurricularPlan().hasAnyEnrolmentForExecutionYear(executionYear)) {
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
    public void checkConditions() throws DomainException {
	// TODO Auto-generated method stub
	
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
		&& !isFirstRequestFromExecutionYear()) {
	    new CertificateRequestEvent(getAdministrativeOffice(),
		    EventType.ENROLMENT_CERTIFICATE_REQUEST, getRegistration().getPerson(), this);
	}
    }

}
