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

public class SchoolRegistrationCertificateRequest extends SchoolRegistrationCertificateRequest_Base {

    private SchoolRegistrationCertificateRequest() {
	super();
    }

    public SchoolRegistrationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    ExecutionYear executionYear) {

	this();

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest, executionYear);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    ExecutionYear executionYear) {

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(executionYear);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(ExecutionYear executionYear) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.cannot.be.null");
	} else if (!getStudentCurricularPlan().hasSchoolRegistration(executionYear)) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.executionYear.before.studentCurricularPlan.start");
	}
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();

	result.add(AdministrativeOfficeType.DEGREE);

	return result;
    }

    @Override
    protected void assertProcessingStatePreConditions() throws DomainException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.SCHOOL_REGISTRATION_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.SchoolRegistrationCertificateRequest.cannot.modify.executionYear");
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.CONCLUDED
		&& !isFirstRequestFromExecutionYear()) {
	    new CertificateRequestEvent(getAdministrativeOffice(),
		    getEventType(), getRegistration().getPerson(), this);
	}
    }

    @Override
    public EventType getEventType() {
	return EventType.SCHOOL_REGISTRATION_CERTIFICATE_REQUEST;
    }

}
