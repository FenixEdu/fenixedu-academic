package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CertificateRequestEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

public class DegreeFinalizationCertificateRequest extends DegreeFinalizationCertificateRequest_Base {

    protected DegreeFinalizationCertificateRequest() {
	super();
    }

    public DegreeFinalizationCertificateRequest(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean average,
	    Boolean detailed) {
	this();

	if (!studentCurricularPlan.hasDegreeDiplomaRequest()) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.has.no.degree.diploma.request");
	}

	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest, average, detailed);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    AdministrativeOffice administrativeOffice, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean average,
	    Boolean detailed) {
	init(studentCurricularPlan, administrativeOffice, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(average, detailed);
	super.setAverage(average);
	super.setDetailed(detailed);
    }

    private void checkParameters(Boolean average, Boolean detailed) {
	if (average == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.average.cannot.be.null");
	}
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.detailed.cannot.be.null");
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
	return DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public void setAverage(Boolean average) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.cannot.modify.average");
    }

    @Override
    public void setDetailed(Boolean detailed) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest.cannot.modify.detailed");
    }

    @Override
    protected void internalChangeState(
	    AcademicServiceRequestSituationType academicServiceRequestSituationType, Employee employee) {

	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (academicServiceRequestSituationType == AcademicServiceRequestSituationType.PROCESSING) {
	    new CertificateRequestEvent(getAdministrativeOffice(),
		    EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, getRegistration().getPerson(), this);
	}

    }

}
